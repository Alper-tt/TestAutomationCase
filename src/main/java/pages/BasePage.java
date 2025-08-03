package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import utils.ElementLocatorReader;

import java.time.Duration;

public class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    private static final ElementLocatorReader locatorReader = new ElementLocatorReader(
            "KapHomePage.json",
            "QueryPage.json",
            "ResultsPage.json",
            "NotificationDetailsPage.json"
    );

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected By getLocator(String key) {
        return locatorReader.get(key);
    }

    public void clickByLocator(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void clickByKey(String key) {
        try {
            By locator = locatorReader.get(key);
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            throw new RuntimeException("Element tıklanamadı: " + key, e);
        }
    }

    public void clickDynamicElement(String rawKey, String param) {
        String rawXpath = getLocatorRaw(rawKey);
        String finalXpath = String.format(rawXpath, param);
        By locator = By.xpath(finalXpath);
        clickByLocator(locator);
    }

    protected String getLocatorRaw(String key) {
        return locatorReader.getRaw(key);
    }

    public void clickWithJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void scrollAndClick(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            Thread.sleep(1000);
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e) {
            clickWithJS(element);
            System.out.println("Element tıklanamadı: ");
        }
    }

    public void scrollAndClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        scrollAndClick(element);
    }

    public void scrollAndClick(String key) {
        By locator = getLocator(key);
        scrollAndClick(locator);
    }

}
