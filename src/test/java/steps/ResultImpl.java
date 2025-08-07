package steps;

import com.thoughtworks.gauge.Step;
import org.openqa.selenium.WebDriver;
import pages.ResultsPage;

import static utils.DriverFactory.getDriver;

public class ResultImpl {
    WebDriver driver = getDriver();
    ResultsPage resultsPage = new ResultsPage(driver);

    @Step("<text> içeren ilk satıra tıklanır")
    public void clickFirstRowContainingText(String text) {
        resultsPage.clickFirstRowContaining(text);
        resultsPage.switchToNewTab();
    }
}