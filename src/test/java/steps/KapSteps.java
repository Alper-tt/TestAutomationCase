package steps;

import com.thoughtworks.gauge.Step;
import org.openqa.selenium.WebDriver;
import pages.KapHomePage;
import pages.SorguPage;
import utils.DriverFactory;

public class KapSteps {

    WebDriver driver = DriverFactory.getDriver();
    KapHomePage homePage = new KapHomePage(driver);
    SorguPage sorguPage = new SorguPage(driver);


    @Step("Kap anasayfasına gidilir")
    public void goToKapHomePage() {
        driver.get("https://www.kap.org.tr/tr");
        System.out.println("Kap anasayfasına gidilir");
    }

    @Step("Bildirim Sorguları menüsüne tıklanır ve Detaylı Sorgulama seçilir")
    public void clickBildirimSorgulariAndDetayli() {
        homePage.clickBildirimSorgulari();
        homePage.clickDetayliSorgulama();
        System.out.println("Bildirim Sorguları menüsüne tıklanır ve Detaylı Sorgulama seçilir");
    }

    @Step("Şirket Bildirimi seçilir, İşlem Gören Şirketler seçilir, tarih olarak <date> girilir ve Ara butonuna tıklanır")
    public void filtrele(String date) {
        sorguPage.clickSirketBildirimiTab();
        sorguPage.selectFromDropdownByVisibleText("İşlem Gören Şirketler", "İşlem Gören Şirketler");
        sorguPage.setDate(date);
        sorguPage.clickSearchButton();
        System.out.println("Şirket Bildirimi seçilir, İşlem Gören Şirketler seçilir, tarih olarak <date> girilir ve Ara butonuna tıklanır");
    }
}
