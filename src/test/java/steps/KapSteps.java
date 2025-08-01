package steps;

import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.KapHomePage;
import pages.NotificationDetailsPage;
import pages.ResultsPage;
import pages.QueryPage;
import utils.DriverFactory;
import utils.DownloadChecker;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class KapSteps {

    WebDriver driver = DriverFactory.getDriver();
    KapHomePage homePage = new KapHomePage(driver);
    QueryPage queryPage = new QueryPage(driver);
    ResultsPage resultsPage = new ResultsPage(driver);
    NotificationDetailsPage notificationDetailsPage = new NotificationDetailsPage(driver);

    @BeforeScenario
    public void before() {
        DriverFactory.clearDownloadDir();
    }

    @Step("<url> adresine gidilir")
    public void navigateToUrl(String url) {
        driver.get(url);
    }

    @Step("Sayfanın URL'si <expectedUrl> olmalı")
    public void checkCurrentUrl(String expectedUrl) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean urlMatches = wait.until(ExpectedConditions.urlToBe(expectedUrl));
        assertThat(urlMatches)
                .isTrue();
    }

    @Step("<menuKey> menüsüne tıklanır")
    public void clickMenu(String menuKey) {
        homePage.clickMenu(menuKey);
    }

    @Step("<submenuKey> seçilir")
    public void clickSubMenu(String submenuKey) {
        homePage.clickSubMenu(submenuKey);
    }

    @Step("<tab> sekmesi seçilir")
    public void selectTab(String tabName) {
        queryPage.clickTab(tabName);
    }

    @Step("<dropdown> açılır menüsü tıklanır")
    public void openDropdown(String dropdownKey) {
        queryPage.clickDropdown(dropdownKey);
    }

    @Step("<group> grubu açılır menüden seçilir")
    public void selectDropdownOption(String optionText) {
        queryPage.selectDropdownOption(optionText);
    }

    @Step("Tarih olarak <date> girilir")
    public void enterDateAndClickButton(String date) {
        queryPage.selectStartDate(date);
    }

    @Step("<button> butonuna tıklanır")
    public void clickSearchButton(String buttonLabel) {
        queryPage.clickButton(buttonLabel);
    }

    @Step("<text> içeren ilk satıra tıklanır")
    public void clickFirstRowContainingText(String text) {
        resultsPage.clickFirstRowContaining(text);
        resultsPage.switchToNewTab();
    }

    @Step("Sayfanın URL'si <text> içermeli")
    public void urlShouldStartWith(String pathPrefix) {
        String currentUrl = driver.getCurrentUrl();
        assertThat(currentUrl)
                .as("URL beklenen <pathPrefix> ile başlamıyor")
                .contains(pathPrefix);
    }

    @Step("<format> dosyası indirilir")
    public void clickExcelDownloadButton(String format) {
        notificationDetailsPage.clickDownloadButton(format);
    }

    @Step("<format> dosyası <expectedFileName> olarak indirilmiş olmalı")
    public void checkExcelDownloaded(String format, String expectedFileName) throws InterruptedException {
        String filePath = DriverFactory.getDownloadDir() + "/" + expectedFileName;
        boolean isDownloaded = DownloadChecker.waitForFileDownload(filePath, 10); //Proje ihtiyacına göre ScheduledExecutorService kullanılabilir
        assertThat(isDownloaded)
                .as("Dosya belirtilen sürede indirilemedi: %s", expectedFileName)
                .isTrue();
        driver.quit();
    }

}
