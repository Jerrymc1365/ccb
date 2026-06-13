package org.infpancakefactory.ccb.core.util.filesystem;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class Fs {
    public static boolean replaceDir(String from, String to) {
        try {
            copyDir(new File(System.getProperty("user.dir") + from), new File(System.getProperty("user.dir") + to));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyDir(File src, File dst) throws IOException {
        if (!src.exists()) return;

        if (!dst.exists()) dst.mkdirs();

        for (File f : src.listFiles()) {
            File d = new File(dst, f.getName());

            if (f.isDirectory()) {
                copyDir(f, d);
            } else {
                copy(f, d);
            }
        }
    }

    public static boolean copyAndReplaceFile(String src, String dst) {
        try {
            copy(
                    new File(System.getProperty("user.dir") + src),
                    new File(System.getProperty("user.dir") + dst)
            );
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeFiles(String src) {
        try {
            return Files.deleteIfExists(new File(System.getProperty("user.dir") + src).toPath());
        } catch (Throwable t) {
            //t.printStackTrace();
            try {
                Files.walkFileTree(new File(System.getProperty("user.dir") + src).toPath(), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }

    private static void copy(File src, File dst) throws IOException {
        dst.getParentFile().mkdirs();

        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {

            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        }
    }

    public static boolean isFileExists(String f) {
        return new File(System.getProperty("user.dir") + f).exists();
    }

    public static boolean dirCopy(String path, String dest) {
        return replaceDir(path, dest);
    }
}
