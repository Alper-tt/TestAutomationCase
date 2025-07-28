package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class ResultsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private By loadingIndicator = By.cssSelector(".progress-indicator");
    private By allRows = By.cssSelector("tr.cursor-pointer");

    public ResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitUntilResultsLoaded() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingIndicator));
    }

    public void clickFirstRowContaining(String text) {
        waitUntilResultsLoaded();

        List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(allRows));

        for (WebElement row : rows) {
            if (row.getText().toUpperCase().contains(text.toUpperCase())) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", row);

                try {
                    WebElement clickableCell = row.findElement(By.cssSelector("td[name='notification-navigation']"));
                    wait.until(ExpectedConditions.elementToBeClickable(clickableCell)).click();
                } catch (Exception e) {
                    WebElement clickableCell = row.findElement(By.cssSelector("td[name='notification-navigation']"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickableCell);
                }
                return;
            }
        }

        throw new NotFoundException("'" + text + "' içeren satır bulunamadı.");
    }

    public void switchToNewTab() {
        String originalWindow = driver.getWindowHandle();

        wait.until(driver -> driver.getWindowHandles().size() > 1);

        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }
}
