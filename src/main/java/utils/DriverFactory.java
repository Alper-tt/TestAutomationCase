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

    /**
     * Singleton WebDriver örneğini oluşturur ve döner.
     * İlk çağrıldığında:
     * İndirme klasörü temizlenir.
     * Chrome tarayıcı için indirme ayarları yapılandırılır.
     * Yeni bir ChromeDriver örneği başlatılır.
     * Sonraki çağrılarda var olan driver örneği döndürülür.
     *
     * @return WebDriverq
     */
    public static WebDriver getDriver() {
        if (driver == null) {
            LogUtil.logger.info("Yeni WebDriver örneği oluşturuluyor...");
            LogUtil.logger.info("Indirme klasörü temizleniyor: {}", downloadDir);
            createDownloadDir();

            Map<String, Object> prefs = new HashMap<>();
            prefs.put("download.default_directory", downloadDir);
            prefs.put("download.prompt_for_download", false);

            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", prefs);

            driver = new ChromeDriver(options);
            LogUtil.logger.info("WebDriver başarıyla başlatıldı.");
        }
        return driver;
    }

    /**
     * Tarayıcı oturumu kapatılır ve kaynaklar serbest bırakılır.
     */
    public static void quitDriver() {
        if (driver != null) {
            LogUtil.logger.info("WebDriver kapatılıyor...");
            driver.quit();
            driver = null;
            LogUtil.logger.info("WebDriver null olarak sıfırlandı.");
        }
    }

    /**
     * İndirme klasörünün yolunu döner.
     *
     * @return Kullanılan indirme klasörünün dizin yolu
     */
    public static String getDownloadDir() {
        return downloadDir;
    }

    /**
     * İndirme klasörü yoksa oluşturur.
     * İndirme klasörü downloadDir üzerinden belirlenir.
     * Klasör zaten varsa işlem yapılmaz.
     */
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

    /**
     * İndirme klasöründeki tüm dosyaları siler.
     * Testler sırasında indirilen dosyaların birikmesini önlemek ve
     * doğrulama işlemleri öncesinde temiz bir ortam sağlamak için kullanılır.
     */
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
