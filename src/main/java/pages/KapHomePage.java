package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class KapHomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public KapHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickMenu(String menuText) {
        By menuButton = By.xpath("//button[.//span[text()='" + menuText + "']]");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(menuButton));
        element.click();
    }

    public void clickSubMenu(String subMenuText) {
        By subMenuLink = By.xpath("//a[contains(@href, '/tr/') and contains(text(), '" + subMenuText + "')]");
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(subMenuLink));
        element.click();
    }
}
