package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    private static WebDriver driver;
    private static final String downloadDir = System.getProperty("user.dir") + "/target/downloads";

    public static WebDriver getDriver() {
        if (driver == null) {
            LogUtil.logger.info("Yeni WebDriver örneği oluşturuluyor...");
            LogUtil.logger.info("Indirme klasörü temizleniyor: {}", downloadDir);
            createDownloadDir();

            Map<String, Object> prefs = new HashMap<>();
            prefs.put("download.default_directory", downloadDir);
            prefs.put("download.prompt_for_download", false);
            prefs.put("safebrowsing.enabled", true);

            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", prefs);

            driver = new ChromeDriver(options);
            LogUtil.logger.info("WebDriver başarıyla başlatıldı.");
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            LogUtil.logger.info("WebDriver kapatılıyor...");
            driver.quit();
            driver = null;
            LogUtil.logger.info("WebDriver null olarak sıfırlandı.");
        }
    }

    public static String getDownloadDir() {
        return downloadDir;
    }

    public static void createDownloadDir() {
        File dir = new File(downloadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                LogUtil.logger.info("İndirme klasörü oluşturuldu: {}", downloadDir);
            } else {
                LogUtil.logger.warn("İndirme klasörü oluşturulamadı: {}", downloadDir);
            }
        } else {
            LogUtil.logger.info("İndirme klasörü zaten mevcut: {}", downloadDir);
        }
    }

    public static void clearDownloadDir() {
        File dir = new File(downloadDir);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    boolean deleted = file.delete();
                    if (deleted) {
                        LogUtil.logger.info("Dosya silindi: {}", file.getName());
                    } else {
                        LogUtil.logger.warn("Dosya silinemedi: {}", file.getName());
                    }
                }
            }
        }
    }
}
