package steps;

import com.thoughtworks.gauge.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import utils.*;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class KapImpl {

    WebDriver driver = DriverFactory.getDriver();
    BasePage basePage = new BasePage(driver);

    @Step("Dosya indirme dizinini temizle")
    public void clearDownloadDir() {
        DriverFactory.clearDownloadDir();
    }

    @Step("<device> cihaz tipi için tarayici boyutu ayarla")
    public void setBrowserSizeByDevice(String device) {
        basePage.setBrowserSizeByDevice(device);
    }

    @Step("Kullanıcı <urlKey> sayfasina git")
    public void navigateToUrl(String urlKey) {
        String url = ConfigReader.getProperty(urlKey);
        driver.get(url);
    }

    @Step("Sayfanın URL'si <expectedUrl> olsun")
    public void checkCurrentUrl(String expectedUrl) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean urlMatches = wait.until(ExpectedConditions.urlToBe(ConfigReader.getProperty(expectedUrl)));
        assertThat(urlMatches)
                .as("URL uyuşmazlığı: Beklenen URL: %s, Gerçek URL: %s", expectedUrl, driver.getCurrentUrl())
                .isTrue();
    }

    @Step("<button> elementine tıkla")
    public void clickButton(String buttonLabel) {
        basePage.clickByKey(buttonLabel);
    }
}
