package utils;

import java.io.File;

public class DownloadChecker {

    /**
     * Belirtilen dosya yolunda, dosyanın indirildiğini doğrulamak için bekleme yapar.
     * Her saniye kontrol ederek dosyanın varlığını denetler.
     * Dosya belirtilen zaman aşımı süresi içinde oluşursa true, aksi takdirde false döner.
     *
     * @param filePath İndirilen dosyanın dosya yolu target/downloads/Bildirimler.xls
     * @param timeoutInSeconds Beklenecek maksimum süre
     * @return Dosya belirtilen süre içerisinde başarıyla indirilmişse true, aksi halde false
     */
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
