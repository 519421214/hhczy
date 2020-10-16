package com.king.hhczy.controller;

import com.king.hhczy.common.util.FilesUtils;
import com.king.hhczy.common.util.MyFtpClient;
import com.king.hhczy.entity.model.UploadModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;

/**
 *
 * 文件下载、图片展示等接口
 * 知识：
 * RestController:方法都以json格式输出，不用再转
 */
@RestController
public class FileController {
    @Autowired
    private MyFtpClient myFtpClient;
    @Value("${ftp.absolute.addr}")
    private String absoluteAddr;

    /**
     * FTP形式读取图片（）
     *
     * @param workspace
     * @param projectName
     * @param module
     * @param fileName
     * @param response
     */
    @GetMapping("/image/{workspace}/{projectName}/{module}/{fileName:.+}")
    public void showImage(@PathVariable String workspace,
                          @PathVariable String projectName,
                          @PathVariable String module,
                          @PathVariable String fileName,
                          HttpServletResponse response) {
        try {
            String imagePath = Paths.get(workspace, projectName, module, fileName).toString();
            response.setContentType("image/jpeg"); //设置图片显示
            myFtpClient.download(imagePath, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 本地方式上传文件/图片
     *
     * @param model
     * @return
     */
    @PostMapping("/file-upload")
    public boolean upload(@RequestBody UploadModel model) {
        String parentFilePath = Paths.get(absoluteAddr, model.getFilePath()).toString();
        return FilesUtils.uploadFile(parentFilePath, model.getFileName(), model.getFileBuf());
    }

    /**
     * 本地方式读取文件/图片
     *
     * @param workspace
     * @param projectName
     * @param module
     * @param fileName
     * @param response
     * @return
     */
    @GetMapping("/file/{workspace}/{projectName}/{module}/{fileName:.+}")
    public ResponseEntity<Resource> showOrDown(@PathVariable String workspace,
                                               @PathVariable String projectName,
                                               @PathVariable String module,
                                               @PathVariable String fileName,
                                               HttpServletResponse response) {
        //允许跨域访问静态资源
        response.addHeader("Access-Control-Allow-Origin", "*");
        String filePath = Paths.get(workspace, projectName, module, fileName).toString();
        return FilesUtils.downloadFile(Paths.get(absoluteAddr + filePath));
    }
}