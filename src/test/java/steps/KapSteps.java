package steps;

import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.WebDriver;
import pages.KapHomePage;
import pages.NotificationDetailsPage;
import pages.ResultsPage;
import pages.QueryPage;
import utils.DriverFactory;
import utils.DownloadChecker;

import static org.assertj.core.api.Assertions.assertThat;

public class KapSteps {

    WebDriver driver = DriverFactory.getDriver();
    KapHomePage homePage = new KapHomePage(driver);
    QueryPage sorguPage = new QueryPage(driver);

    @BeforeScenario
    public void before() {
        DriverFactory.clearDownloadDir();
    }

    @Step("<url> adresine gidilir")
    public void navigateToUrl(String url) {
        driver.get(url);
    }

    @Step("<menu> menüsüne tıklanır ve <submenu> seçilir")
    public void clickMenuAndSubmenu(String menu, String submenu) {
        homePage.clickMenu(menu);
        homePage.clickSubMenu(submenu);
    }

    @Step("<tab> sekmesi seçilir")
    public void selectTab(String tabName) {
        sorguPage.clickTab(tabName);
    }

    @Step("<dropdown> açılır menüsünden <option> seçilir")
    public void selectFromDropdown(String dropdownLabel, String option) {
        sorguPage.selectFromDropdownByVisibleText(dropdownLabel, option);
    }

    @Step("Tarih olarak <date> girilir ve <button> butonuna tıklanır")
    public void enterDateAndClickButton(String date, String buttonLabel) {
        sorguPage.setDate(date);
        sorguPage.clickButton(buttonLabel);
    }

    @Step("<metin> içeren ilk satıra tıklanır")
    public void clickFirstRowContainingText(String text) {
        ResultsPage sonucPage = new ResultsPage(driver);
        sonucPage.clickFirstRowContaining(text);
        sonucPage.switchToNewTab();
    }

    @Step("Excel dosyası indirilir")
    public void downloadExcel() {
        NotificationDetailsPage notificationDetailsPage = new NotificationDetailsPage(driver);
        notificationDetailsPage.downloadExcel();
        System.out.println("Excel dosyası indirilir....");
    }

    @Step("Excel dosyası <dosyaAdı> olarak indirilmiş olmalı")
    public void checkExcelDownloaded(String dosyaAdı) throws InterruptedException {
        String filePath = DriverFactory.getDownloadDir() + "/" + dosyaAdı;
        boolean isDownloaded = DownloadChecker.waitForFileDownload(filePath, 10); //Proje ihtiyacına göre ScheduledExecutorService kullanılabilir
        assertThat(isDownloaded)
                .as("Dosya belirtilen sürede indirilemedi: %s", dosyaAdı)
                .isTrue();
        driver.quit();
    }

}
