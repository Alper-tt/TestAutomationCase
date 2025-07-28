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
            createDownloadDir();

            Map<String, Object> prefs = new HashMap<>();
            prefs.put("download.default_directory", downloadDir);
            prefs.put("download.prompt_for_download", false);
            prefs.put("safebrowsing.enabled", true);

            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", prefs);

            driver = new ChromeDriver(options);
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public static String getDownloadDir() {
        return downloadDir;
    }

    public static void createDownloadDir() {
        File dir = new File(downloadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void clearDownloadDir() {
        File dir = new File(downloadDir);
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                file.delete();
            }
        }
    }
}
