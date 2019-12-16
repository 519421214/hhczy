package com.king.hhczy.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * @author ningjinxiang
 * date 20190918
 */
@Slf4j
public class FilesUtils {
    /**
     * 遍历获取整个文件夹（包括子文件夹）的文件路径(不遍历文件夹)
     * @param path
     * @return
     */
    public static List<Path> getAllFilesPaths(String path) {
        return getAllFilesPaths(Paths.get(path));
    }
    public static List<Path> getAllFilesPaths(Path path) {
        List<Path> result = new LinkedList<Path>();
        try {
            Files.walkFileTree(path, new FindJavaVisitor(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取文件夹的所有子文件个数
     * @param path
     * @return
     */
    public static Integer getDirFilesSize(String path) {
        return getDirFilesSize(Paths.get(path));
    }
    /**
     * 获取文件创建、最后修改、最后访问时间戳Map
     * @param path
     * @return
     */
    public static Map<String, Long> getFileTime(String path) {
        BasicFileAttributeView fileAttributeView = Files.getFileAttributeView(Paths.get(path), BasicFileAttributeView.class,
                LinkOption.NOFOLLOW_LINKS);
        Map<String, Long> result = new HashMap<>();
        try {
            BasicFileAttributes basicFileAttributes = fileAttributeView.readAttributes();
            result.put("creationTime",basicFileAttributes.creationTime().toMillis());
            result.put("lastModifiedTime",basicFileAttributes.lastModifiedTime().toMillis());
            result.put("lastAccessTime",basicFileAttributes.lastAccessTime().toMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 指定获取文件创建、最后修改、最后访问时间戳
     * @param path
     * @return
     */
    public static Long getFileTime(String path,FilesAttribute filesAttribute) {
        return getFileTime(Paths.get(path),filesAttribute);
    }
    public static Long getFileTime(Path path,FilesAttribute filesAttribute) {
        BasicFileAttributeView fileAttributeView = Files.getFileAttributeView(path, BasicFileAttributeView.class,
                LinkOption.NOFOLLOW_LINKS);
        try {
            BasicFileAttributes basicFileAttributes = fileAttributeView.readAttributes();
            if (filesAttribute== FilesAttribute.CREATION_TIME) {
                return basicFileAttributes.creationTime().toMillis();
            }else if (filesAttribute== FilesAttribute.LAST_ACCESS_TIME){
                return basicFileAttributes.lastAccessTime().toMillis();
            }else if (filesAttribute== FilesAttribute.LAST_MODIFIED_TIME){
                return basicFileAttributes.lastModifiedTime().toMillis();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Integer getDirFilesSize(Path path) {
        Map<String, Integer> count = new HashMap<>();
        count.put("count", 0);
        try {
            Files.walkFileTree(path, new CountJavaVisitor(count));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count.get("count");
    }

    /**
     * 并行拷贝目录下所以子文件
     * @param sourceStr 原目录
     * @param targetRoot 目标根文件夹
     * @param deleteSource true：剪切 false:复制
     * @throws IOException
     */
    public static void copyAllParallel(Path sourceStr,String sourceRoot,String targetRoot,boolean deleteSource) {
        List<Path> allFilesPaths = getAllFilesPaths(sourceStr);
        allFilesPaths.parallelStream().forEach(x->{
            try {
                //备份路径创建
                Path bakPath = Paths.get(x.getParent().toString().replace(sourceRoot, targetRoot));
                Files.createDirectories(bakPath);
                if (deleteSource) {
                    Files.move(x, bakPath);
                }else {
                    //执行复制
                    Files.copy(x, bakPath,StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    /**
     * 拷贝目录下所以子文件
     * @param sourceStr 原目录
     * @param targetStr 目标文件夹
     * @param deleteSource true：剪切 false:复制
     * @throws IOException
     */
    public static void copyAll(String sourceStr,String targetStr,boolean deleteSource) throws IOException {
        copyAll(Paths.get(sourceStr), targetStr,deleteSource);
    }
    public static void copyAll(Path source,String targetStr,boolean deleteSource) throws IOException {
        Files.walkFileTree(source, new CopyFindJavaVisitor(source,targetStr,deleteSource));
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

        if (log.isInfoEnabled()) {
            log.info("下载文件: {}, 文件大小: {}", fileRealPath, contentLength);
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


    private static class FindJavaVisitor extends SimpleFileVisitor<Path> {

        private List<Path> result;
        public FindJavaVisitor(List<Path> result) {
            this.result = result;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            result.add(file);
            return FileVisitResult.CONTINUE;
        }

    }
    private static class CountJavaVisitor extends SimpleFileVisitor<Path> {

        private Map<String,Integer> count;
        public CountJavaVisitor(Map count) {
            this.count = count;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            count.put("count",count.get("count")+1);
            return FileVisitResult.CONTINUE;
        }

    }
    private static class CopyFindJavaVisitor extends SimpleFileVisitor<Path> {
        private Path source;
        private Path target;
        private boolean deleteSource;

        public CopyFindJavaVisitor(Path source, String target, boolean deleteSource) {
            this.source = source;
            this.target = Paths.get(target);
            this.deleteSource = deleteSource;
        }

        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            // 在目标文件夹中创建dir对应的子文件夹
            Path subDir;
            if (dir.compareTo(source) == 0) {
                subDir = target;
            } else {
                subDir = target.resolve(dir.subpath(source.getNameCount(), dir.getNameCount()));
            }

            Files.createDirectories(subDir);
            return FileVisitResult.CONTINUE;
        }
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (deleteSource) {
                Files.move(file, target.resolve(file.subpath(source.getNameCount(), file.getNameCount())));
            }else {
                Files.copy(file, target.resolve(file.subpath(source.getNameCount(), file.getNameCount())),StandardCopyOption.REPLACE_EXISTING);
            }
            return FileVisitResult.CONTINUE;
        }

    }
    private final static Set<String> imageSuffixs = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            ".bmp", ".dib", ".gif", ".jfif", ".jpe", ".jpeg", ".jpg", ".png", ".tif", ".tiff", ".ico")));

    public enum FilesAttribute{
        CREATION_TIME,
        LAST_MODIFIED_TIME,
        LAST_ACCESS_TIME;
    }
}
