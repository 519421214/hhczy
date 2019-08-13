package com.king.hhczy.exception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.king.hhczy.base.constant.ErrorCodeConstant;
import com.king.hhczy.common.result.ReqtBody;
import com.king.hhczy.common.result.RespBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 统一异常处理
 * @author ningjinxiang
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
	/**
	 * 出现异常时，定义在@ControllerAdvice中的@ExceptionHandler就开始发挥作用了
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value=Exception.class)
	public RespBody exceptionHandler(HttpServletRequest request, Exception e){
		RespBody respBody = new RespBody();
		respBody.setRequestId((String) request.getAttribute("requestId"));
		Map<String,Object> requestMap = (Map<String, Object>) request.getAttribute("requestMap");
		if (requestMap==null) {
			requestMap = new LinkedHashMap<>();
		}
		//请求信息读取及绑定到对象时的异常，此时不会进入WebLogAspect
		if (e instanceof HttpMessageNotReadableException) {
			respBody.setCode(ErrorCodeConstant.PARAMS_ERROR);
			HttpMessageNotReadableException hmnre = (HttpMessageNotReadableException) e;
			respBody.setMsg("请求入参解析失败，请参考解析异常检查您的请求再重试 : "
					+ hmnre.getMessage());
		}//HttpMessageNotReadableException 当空参时抛出，统一归json格式错误处理java.net.ConnectException: Connection refused: connect
		else if(e.getCause() instanceof JsonParseException || e.getCause() instanceof JsonMappingException){
			respBody.setCode(ErrorCodeConstant.PARAMS_ERROR);
			respBody.setMsg("入参JSON格式错误");
		}
		//请求参数绑定到对象成功后，但校验失败的时异常，此时不会进入WebLogAspect
		else if (e instanceof MethodArgumentNotValidException) {
			respBody.setCode(ErrorCodeConstant.PARAMS_VALID_ERROR);
			MethodArgumentNotValidException argNotValidEx = (MethodArgumentNotValidException) e;
			Method method = argNotValidEx.getParameter().getMethod();

			//参数校验失败时，可直接获取各种校验注解上的提示信息并可直接拿到入参target从而得到requestId
			BindingResult bindingResult = argNotValidEx.getBindingResult();
			handleArgNotValidException(request, respBody, requestMap, method, bindingResult);
		} else if (e instanceof BindException) {
			respBody.setCode(ErrorCodeConstant.PARAMS_VALID_ERROR);
			BindingResult bindingResult = ((BindException) e).getBindingResult();
			handleArgNotValidException(request, respBody, requestMap, null, bindingResult);
		}
		//数据库操作异常（sql有误等）
		/*else if (e instanceof DataAccessException) {
			respBody.setCode(ErrorCodeConstant.DATAACCESS_ERROR);
			respBody.setMsg("数据处理异常, 请稍后再操作");
		}*/
		//请求头、请求类型错误
		else if (e instanceof HttpRequestMethodNotSupportedException) {
			respBody.setCode(ErrorCodeConstant.NOT_SUPPORTED_ERROR);
			respBody.setMsg("请正确使用GET/POST请求类型");
		}
		//远程接口请求超时
		else if (e.getCause() instanceof SocketTimeoutException) {
			respBody.setCode(ErrorCodeConstant.GOVIDEO_CONNET_OUT_ERROR);
			respBody.setMsg("govideo请求超时");
		}
		//远程接口请求超时
		else if (e.getCause() instanceof ConnectException) {
			respBody.setCode(ErrorCodeConstant.GOVIDEO_CONNET_OUT_ERROR);
			respBody.setMsg("govideo请求异常，请检查CMS服务是否在线");
		}
		//自定义异常
		else if (e instanceof CustomizedException) {
			CustomizedException ex = (CustomizedException) e;
			if (StringUtils.isEmpty(respBody.getRequestId())) {
				respBody.setRequestId(ex.getRequestId());
			}
			respBody.setCode(ex.getCode());
			respBody.setMsg(ex.getMsg());
		}
		//其他异常(服务器异常)
		else {
			respBody.setCode(ErrorCodeConstant.SYSTEM_ERROR);
			respBody.setMsg(e.getMessage());
			//调试用 todo
			e.printStackTrace();
		}
		Long startTime = (Long) request.getAttribute("startTime");
		if (startTime == null) {
			startTime = System.currentTimeMillis();
		}
		requestMap.put("costTime", System.currentTimeMillis() - startTime);
		requestMap.put("responseBody", respBody);
		log.error("response : {} ", JSONObject.toJSONString(requestMap, SerializerFeature.WriteMapNullValue));
		return respBody;
	}

	private void handleArgNotValidException(HttpServletRequest req, RespBody respBody,
											 Map<String, Object> requestMap, Method method,
											 BindingResult bindingResult) {
		String url = req.getRequestURL().toString();
		String remoteAddr = req.getRemoteAddr();
		String httpMethod = req.getMethod();

		requestMap.put("remoteAddr", remoteAddr);
		requestMap.put("url", url);
		requestMap.put("httpMethod", httpMethod);
		if (method != null) {
			requestMap.put("classMethod", method.getDeclaringClass().getName() + "." + method.getName());
		}

		Object target = bindingResult.getTarget();
		if (target instanceof ReqtBody) {
			respBody.setRequestId(((ReqtBody) target).getRequestId());
		}
		requestMap.put("requestBody", target);

		respBody.setMsg(bindingResult.getFieldError().getDefaultMessage());
	}

}
