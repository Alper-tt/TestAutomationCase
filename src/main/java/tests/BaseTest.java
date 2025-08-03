package tests;

import com.thoughtworks.gauge.*;
import org.openqa.selenium.WebDriver;
import utils.*;

public class BaseTest {
    protected static WebDriver driver;
    protected static ElementLocatorReader locatorReader;

    @BeforeScenario
    public void beforeScenario() throws Exception {
        DriverFactory.clearDownloadDir();
        driver = DriverFactory.getDriver();

        if (locatorReader == null) {
            locatorReader = new ElementLocatorReader(
                    "KapHomePage.json",
                    "QueryPage.json",
                    "ResultsPage.json",
                    "NotificationDetailsPage.json"
            );
        }
    }

    @AfterScenario
    public void afterSpec() {
        if (driver != null) {
        }
        AllureReportGenerator.generateReport();
        DriverFactory.quitDriver();
    }
}
