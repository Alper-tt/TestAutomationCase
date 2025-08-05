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
    }

    @BeforeScenario
    public void beforeScenario() throws Exception {
        LogUtil.logger.info("Scenario başlatılıyor...");
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
        DriverFactory.quitDriver();
    }

    @AfterSuite
    public void afterSuite() {
        LogUtil.logger.info("Test suite tamamlandı...");
        AllureReportGenerator.generateReport();
    }
}