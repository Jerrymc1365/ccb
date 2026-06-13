package org.infpancakefactory.ccb.core.util.zip;

import java.io.*;
import java.nio.file.Paths;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zip {
    public static boolean unzip(String zipFilePath, String destDirectory) {
        zipFilePath = System.getProperty("user.dir") + zipFilePath;
        destDirectory = System.getProperty("user.dir") + destDirectory;

        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();

            while (entry != null) {
                // 防止路径遍历攻击
                String entryName = entry.getName();
                File file = new File(destDir, entryName);

                // 验证解压路径在目标目录内
                String destDirPath = destDir.getCanonicalPath();
                String destFilePath = file.getCanonicalPath();

                if (!destFilePath.startsWith(destDirPath + File.separator)) {
                    throw new IOException("Inviald Zip File: " + entryName);
                }

                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    // 创建父目录
                    file.getParentFile().mkdirs();

                    try (BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(file))
                    ) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipIn.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                    }
                }

                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void zipped(String sourceDir, String zipFile) {
        sourceDir = Paths.get(System.getProperty("user.dir") + sourceDir).toFile().getAbsolutePath();
        zipFile =  Paths.get(System.getProperty("user.dir") + zipFile).toFile().getAbsolutePath();
        File folder = new File(sourceDir);
        if (!folder.exists() || !folder.isDirectory()) {
            new IOException("invalid " + zipFile).printStackTrace();
        }

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            zos.setMethod(ZipOutputStream.STORED);
            addFilesToZip(folder, "", zos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addFilesToZip(File folder, String basePath, ZipOutputStream zos)
            throws IOException {
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            String entryName = basePath + file.getName();

            if (file.isDirectory()) {
                addFilesToZip(file, entryName + "/", zos);
            } else {
                byte[] data = readFile(file);

                ZipEntry entry = new ZipEntry(entryName);
                entry.setMethod(ZipEntry.STORED);
                entry.setSize(data.length);
                entry.setCompressedSize(data.length);

                CRC32 crc = new CRC32();
                crc.update(data);
                entry.setCrc(crc.getValue());

                zos.putNextEntry(entry);
                zos.write(data);
                zos.closeEntry();
            }
        }
    }

    private static byte[] readFile(File file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
        }
        return baos.toByteArray();
    }
}
