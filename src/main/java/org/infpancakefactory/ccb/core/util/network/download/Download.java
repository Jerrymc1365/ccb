package org.infpancakefactory.ccb.core.util.network.download;

import org.infpancakefactory.ccb.core.CommonEntrypoint;
import org.slf4j.MarkerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Download {
    public static class DownloadResult {
        private DownloadResult() {

        }
        public static DownloadResult from(boolean b, String dir, String fileName , String link) {
            var dl = new DownloadResult();
            dl.b = b;
            dl.link = link;
            dl.fileName = fileName;
            dl.dir = dir;
            return dl;
        }

        boolean b;
        String dir;
        String fileName;
        String link;

        public boolean ifFailedPrint() {
            if (!b)
                CommonEntrypoint.LOG.error(MarkerFactory.getMarker("Download"), "Download Failed -> as {} | from link {}", dir + fileName , link);
            return b;
        }
    }

    public static class DownloadInput {
        private DownloadInput() {

        }
        public static DownloadInput from(String dir, String fileName, String link) {
            var dl = new DownloadInput();
            dl.dir = dir;
            dl.fileName = fileName;
            dl.link = link;
            return dl;
        }

        String dir;
        String fileName;
        String link;
    }

    public static class DownloadInputList {
        private DownloadInputList() {

        }
        public static DownloadInputList from(List<DownloadInput> list) {
            var dl = new DownloadInputList();
            dl.dirAndFileNameAndLink = list;
            return dl;
        }
        public static DownloadInputList of(DownloadInput... list) {
            var dl = new DownloadInputList();
            dl.dirAndFileNameAndLink = List.of(list);
            return dl;
        }

        List<DownloadInput> dirAndFileNameAndLink;
    }

    public static DownloadResult downloadToPath(DownloadInputList dirs) {
        for (var a : dirs.dirAndFileNameAndLink){
            try {
                Files.createDirectory(Paths.get(System.getProperty("user.dir") + a.dir));
            } catch (IOException ignored) {

            } finally {
                try (
                        InputStream in = new URL(a.link).openStream();
                        FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + a.dir + a.fileName);
                ) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    return DownloadResult.from(false, a.dir, a.fileName, a.link);
                }
            }

        }
        return DownloadResult.from(true, "downloadToPath from List", "downloadToPath from List", "downloadToPath from List");
    }

    public static DownloadResult downloadToPath(String dir, String fileName, String downloadLink) {
        try {
            Files.createDirectory(Paths.get(System.getProperty("user.dir") + dir));
        } catch (IOException ignored) {}
        try (InputStream in = new URL(downloadLink).openStream();
             FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + dir + fileName);
        ) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return DownloadResult.from(true, dir, fileName, downloadLink);
        } catch (IOException e) {
            return DownloadResult.from(false, dir, fileName, downloadLink);
        }
    }
}
