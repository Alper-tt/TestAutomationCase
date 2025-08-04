package utils;

import java.io.File;

public class DownloadChecker {

    public static boolean waitForFileDownload(String filePath, int timeoutInSeconds) throws InterruptedException {
        File file = new File(filePath);
        int waitedSeconds = 0;

        LogUtil.logger.info("Dosya indirme bekleniyor: {} (Zaman aşımı: {} saniye)", filePath, timeoutInSeconds);

        while (waitedSeconds < timeoutInSeconds) {
            if (file.exists()) {
                LogUtil.logger.info("Dosya başarıyla indirildi: {}", filePath);
                return true;
            }
            Thread.sleep(1000);
            waitedSeconds++;
        }

        LogUtil.logger.error("Dosya indirme zaman aşımına uğradı: {}", filePath);
        return false;
    }

}
