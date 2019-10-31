package com.king.hhczy.common.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * @author ningjinxiang
 * date 20190918
 */
public class FilesUtils {
	/**
	 * 遍历获取整个文件夹（包括子文件夹）的文件路径(不遍历文件夹)
	 * @param path
	 * @return
	 */
    public static List<Path> getAllFilesPaths(String path) {
        Path startingDir = Paths.get(path);
        List<Path> result = new LinkedList<Path>();
		try {
			Files.walkFileTree(startingDir, new FindJavaVisitor(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
    }

    private static class FindJavaVisitor extends SimpleFileVisitor<Path> {
        private List<Path> result;

        public FindJavaVisitor(List<Path> result) {
            this.result = result;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            result.add(file.getFileName());
            return FileVisitResult.CONTINUE;
        }
    }
    public static ResponseEntity<Resource> downloadFile(Path fileRealPath) {
        String filePath = fileRealPath.getFileName().toString();
        Resource resource = null;
        try {
            resource = new UrlResource(fileRealPath.toUri());
        } catch (MalformedURLException e) {
//            throw new BossFileNotFoundException(e, "({})文件不存在", filePath);
        }

        if (!resource.exists()) {
//            throw new BossFileNotFoundException("({})文件不存在", filePath);
        }

        int lastIndexOfDot = filePath.lastIndexOf(".");
        String fileSuffix = null;
        if (lastIndexOfDot != -1) {
            fileSuffix = filePath.substring(lastIndexOfDot).toLowerCase();
        }

        long contentLength = 0;
        try {
            contentLength = resource.contentLength();
        } catch (IOException e) {/*ignore*/}

        if (Log.isInfoEnabled()) {
            Log.info("下载文件: {}, 文件大小: {}", fileRealPath, contentLength);
        }

        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok().contentLength(contentLength);
        String dispositionValue;
        if (imageSuffixs.contains(fileSuffix)) {
            //图片可直接显示
            bodyBuilder.contentType(MediaType.IMAGE_JPEG);
            dispositionValue = "inline; ";
        } else if (".pdf".equals(fileSuffix)) {
            //PDF可直接显示
            bodyBuilder.contentType(MediaType.APPLICATION_PDF);
            dispositionValue = "inline; ";
        } else {
            //下载文件，强制弹出下载
            dispositionValue = "attachment; ";
        }
        dispositionValue += "filename=\"" + resource.getFilename() + "\"";
        bodyBuilder.header(HttpHeaders.CONTENT_DISPOSITION, dispositionValue);
        return bodyBuilder.body(resource);
    }
    private final static Set<String> imageSuffixs = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            ".bmp", ".dib", ".gif", ".jfif", ".jpe", ".jpeg", ".jpg", ".png", ".tif", ".tiff", ".ico")));
}
