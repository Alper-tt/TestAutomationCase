package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class NotificationDetailsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By excelDownloadButton = By.cssSelector("a[href*='/export/excel/']");

    public NotificationDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void downloadExcel() {
        WebElement downloadLink = wait.until(ExpectedConditions.elementToBeClickable(excelDownloadButton));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", downloadLink);

        try {
            downloadLink.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", downloadLink);
        }
    }
}
