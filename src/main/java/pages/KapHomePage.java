package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class KapHomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor
    public KapHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Bildirim Sorguları dropdown butonu
    private By bildirimSorgulariButton = By.xpath("//button[.//span[text()='Bildirim Sorguları']]");

    // Detaylı Sorgulama linki
    private By detayliSorgulamaLink = By.xpath("//a[@href='/tr/bildirim-sorgu']");

    // Dropdown'ı aç
    public void clickBildirimSorgulari() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(bildirimSorgulariButton));
        element.click();
    }

    // Detaylı Sorgulama linkine tıkla
    public void clickDetayliSorgulama() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(detayliSorgulamaLink));
        element.click();
    }
}
