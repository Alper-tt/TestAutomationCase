package hooks;

import com.thoughtworks.gauge.*;
import org.openqa.selenium.WebDriver;
import utils.*;


public class ExecutionHooks {
    protected static WebDriver driver;
    protected static ElementLocatorReader locatorReader;

    @BeforeSuite
    public void beforeSuite() {
        LogUtil.logger.info("Test suite başlatılıyor...");
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

    @BeforeScenario
    public void beforeScenario() {
        LogUtil.logger.info("Scenario başlatılıyor...");
    }

    @BeforeSpec
    public void beforeSpec() {
        LogUtil.logger.info("Spec başlatılıyor...");
    }

    @AfterStep
    public void afterStep() {
        ScreenshotUtils.takeScreenshotAfterEachStep();
    }

    @AfterScenario
    public void afterScenario() {
        LogUtil.logger.info("Senaryo tamamlandı...");
    }

    @AfterSuite
    public void afterSuite() {
        LogUtil.logger.info("Test suite tamamlandı...");
        AllureReportGenerator.generateReport();
        DriverFactory.quitDriver();
    }
}