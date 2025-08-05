package steps;

import com.thoughtworks.gauge.Step;
import org.openqa.selenium.WebDriver;
import pages.NotificationDetailsPage;
import utils.CompareWebWithXlsUtils;
import utils.DownloadChecker;
import utils.DriverFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static utils.DriverFactory.getDriver;

public class NotificationDetailsImpl {

    WebDriver driver = getDriver();
    NotificationDetailsPage notificationDetailsPage = new NotificationDetailsPage(driver);
    CompareWebWithXlsUtils compareWebWithXlsUtils = new CompareWebWithXlsUtils();

    @Step("Sayfanın URL'si <text> içermeli")
    public void urlShouldContain(String prefix) {
        String currentUrl = driver.getCurrentUrl();
        assertThat(currentUrl)
                .as("URL beklenen <prefix> içermiyor")
                .contains(prefix);
    }

    @Step("İndirilecek dosya formatı <param> seçilir")
    public void clickFileFormat(String param) {
        notificationDetailsPage.clickFileFormat(param);
    }

    @Step("<format> dosyası <expectedFileName> olarak indirilmiş olmalı")
    public void checkExcelDownloaded(String format, String expectedFileName) throws InterruptedException {
        String filePath = DriverFactory.getDownloadDir() + "/" + expectedFileName;
        boolean isDownloaded = DownloadChecker.waitForFileDownload(filePath, 10); //Proje ihtiyacına göre ScheduledExecutorService kullanılabilir
        assertThat(isDownloaded)
                .as("Dosya belirtilen sürede indirilemedi: %s", expectedFileName)
                .isTrue();
    }

    @Step("XLS dosyasındaki veriler ile web sayfasındaki veriler karşılaştırılır <xlsPath>")
    public void comparexlsWithWeb(String xlsPath) throws Exception {
        compareWebWithXlsUtils.compareXlsWithWeb(xlsPath);
    }
}
