package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class ResultsPage extends BasePage {

    public ResultsPage(WebDriver driver) {
        super(driver, "ResultsPage");
    }

    public void waitUntilResultsLoaded() {
        By loading = getLocator("loadingIndicator");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loading));
    }

    public void clickFirstRowContaining(String text) {
        waitUntilResultsLoaded();

        String rawXpath = getLocatorRaw("resultRow_Generic");
        String finalXpath = String.format(rawXpath, text);
        By rowLocator = By.xpath(finalXpath);

        List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(rowLocator));

        for (WebElement row : rows) {
                WebElement cell = row.findElement(getLocator("clickableCellInRow"));
                scrollAndClickWebElement(cell);
            return;
        }

        throw new NotFoundException("'" + text + "' içeren satır bulunamadı.");
    }


    public void switchToNewTab() {
        String originalWindow = driver.getWindowHandle();
        wait.until(driver -> driver.getWindowHandles().size() > 1);

        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }
}
