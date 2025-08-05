package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    /**
     * Her adım sonunda ekran görüntüsü alır ve zaman damgası ile birlikte "screenshots/" dizinine kaydeder.
     * Eğer WebDriver nesnesi TakesScreenshot arayüzünü destekliyorsa:
     * Ekran görüntüsü alınır
     * Klasör mevcut değilse oluşturulur
     * Dosya ismi şu formatla kaydedilir: step_yyyyMMdd_HHmmss_SSS.png
     * Aksi takdirde, log’a driver’ın screenshot desteklemediği bilgisi yazılır.
     */
    public static void takeScreenshotAfterEachStep() {
        WebDriver driver = DriverFactory.getDriver();

        if (driver instanceof TakesScreenshot) {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String screenshotDir = "screenshots/";
            String screenshotPath = screenshotDir + "step_" + timestamp + ".png";

            try {
                Files.createDirectories(Paths.get(screenshotDir));
                Files.copy(screenshot.toPath(), Paths.get(screenshotPath));
                LogUtil.logger.info("Ekran görüntüsü kaydedildi: {}", screenshotPath);
            } catch (IOException e) {
                LogUtil.logger.error("Ekran görüntüsü kaydedilemedi: {}", e.getMessage());
            }
        } else {
            LogUtil.logger.warn("Driver nesnesi screenshot desteklemiyor.");
        }
    }
}
