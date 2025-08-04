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
    KapHomePage homePage = new KapHomePage(driver);

    @Step("Dosya indirme dizinini temizle")
    public void clearDownloadDir() {
        DriverFactory.clearDownloadDir();
    }

    @Step("Tarayıcı boyutu <width>x<height> olarak ayarlanır")
    public void resizeBrowser(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    @Step("<url> adresine gidilir")
    public void navigateToUrl(String url) {
        //String url = utils.readproperties.readProperty("url");
        driver.get(url);
    }

    @Step("Sayfanın URL'si <expectedUrl> olmalı")
    public void checkCurrentUrl(String expectedUrl) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean urlMatches = wait.until(ExpectedConditions.urlToBe(expectedUrl));
        assertThat(urlMatches)
                .isTrue();
    }

    @Step("<button> elementine tıklanır")
    public void clickButton(String buttonLabel) {
        homePage.clickByKey(buttonLabel);
    }
}
