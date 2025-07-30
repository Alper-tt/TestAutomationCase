package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import utils.ElementLocatorReader;

import java.time.Duration;

public class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    private final ElementLocatorReader locatorReader;


    public BasePage(WebDriver driver, String pageName) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.locatorReader = new ElementLocatorReader(pageName + ".json");
    }

    protected By getLocator(String key) {
        return locatorReader.get(key);
    }

    public void clickByLocator(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void clickByKey(String key) {
        By locator = getLocator(key);
        clickByLocator(locator);
    }

    protected String getLocatorRaw(String key) {
        return locatorReader.getRaw(key);
    }

    public void clickWithJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void scrollAndClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        scrollAndClick(element);
    }

    public void scrollAndClick(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e) {
            clickWithJS(element);
        }
    }
}
