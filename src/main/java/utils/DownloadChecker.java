package utils;

import java.io.File;

public class DownloadChecker {

    public static boolean waitForFileDownload(String filePath, int timeoutInSeconds) throws InterruptedException {
        File file = new File(filePath);
        int waitedSeconds = 0;

        while (waitedSeconds < timeoutInSeconds) {
            if (file.exists()) {
                return true;
            }
            Thread.sleep(1000);
            waitedSeconds++;
        }
        System.err.println("Dosya indirme zaman aşımına uğradı: " + filePath);
        return false;
    }
}
