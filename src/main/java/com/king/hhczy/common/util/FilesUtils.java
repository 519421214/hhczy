package com.king.hhczy.common.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

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
}
