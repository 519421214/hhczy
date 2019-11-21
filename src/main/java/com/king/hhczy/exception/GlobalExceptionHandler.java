package com.king.hhczy.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.king.hhczy.common.result.BaseResultCode;
import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.common.util.JsonUtils;
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
			HttpMessageNotReadableException hmnre = (HttpMessageNotReadableException) e;
			respBody.result(BaseResultCode.NON_PARAMTER.fillArgs(hmnre.getMessage()));
		}//HttpMessageNotReadableException 当空参时抛出，统一归json格式错误处理java.net.ConnectException: Connection refused: connect
		else if(e.getCause() instanceof JsonParseException || e.getCause() instanceof JsonMappingException){
			respBody.result(BaseResultCode.NON_PARAMTER_JSON_FORMAT);
		}
		//请求参数绑定到对象成功后，但校验失败的时异常，此时不会进入WebLogAspect
		else if (e instanceof MethodArgumentNotValidException) {
			respBody.setCode(BaseResultCode.NON_PARAMTER_VALID_ERROR.getCode());
			MethodArgumentNotValidException argNotValidEx = (MethodArgumentNotValidException) e;
			Method method = argNotValidEx.getParameter().getMethod();

			//参数校验失败时，可直接获取各种校验注解上的提示信息并可直接拿到入参target从而得到requestId
			BindingResult bindingResult = argNotValidEx.getBindingResult();
			respBody.setMsg(bindingResult.getFieldError().getDefaultMessage());
		} else if (e instanceof BindException) {
			respBody.setCode(BaseResultCode.NON_PARAMTER_VALID_ERROR.getCode());
			BindingResult bindingResult = ((BindException) e).getBindingResult();
			respBody.setMsg(bindingResult.getFieldError().getDefaultMessage());
		}
		//数据库操作异常（sql有误等）
		/*else if (e instanceof DataAccessException) {
			respBody.setCode(ErrorCodeConstant.DATAACCESS_ERROR);
			respBody.setMsg("数据处理异常, 请稍后再操作");
		}*/
		//请求头、请求类型错误
		else if (e instanceof HttpRequestMethodNotSupportedException) {
			respBody.result(BaseResultCode.INVALID_ACCESS_NOT_SUPPORTED_ERROR);
		}
		//远程接口请求超时
		else if (e.getCause() instanceof SocketTimeoutException) {
			respBody.result(BaseResultCode.SOCKET_ERROR_CONNET_OUT);
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
			respBody.result(BaseResultCode.UNKNOWN_ERROR.fillArgs(e.getMessage()));
			//调试用 todo
			e.printStackTrace();
		}
		Long startTime = (Long) request.getAttribute("startTime");
		if (startTime == null) {
			startTime = System.currentTimeMillis();
		}
		requestMap.put("costTime", System.currentTimeMillis() - startTime);
		requestMap.put("responseBody", respBody);
		log.error("response : {} ", JsonUtils.objectToString(requestMap,true));
		return respBody;
	}
}
