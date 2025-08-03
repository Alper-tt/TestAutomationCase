package steps;

import com.thoughtworks.gauge.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import tests.BaseTest;
import utils.*;

import java.io.File;
import java.time.Duration;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static utils.DriverFactory.getDriver;

public class KapSteps extends BaseTest {

    WebDriver driver = getDriver();
    BasePage basePage = new BasePage(driver);
    KapHomePage homePage = new KapHomePage(driver);
    QueryPage queryPage = new QueryPage(driver);
    ResultsPage resultsPage = new ResultsPage(driver);

    @Step("Tarayıcı boyutu <width>x<height> olarak ayarlanır")
    public void resizeBrowser(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }
    @Step("<url> adresine gidilir")
    public void navigateToUrl(String url) {
        driver.get(url);
        // web sitesine yönlendirir
    }

    @Step("Sayfanın URL'si <expectedUrl> olmalı")
    public void checkCurrentUrl(String expectedUrl) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean urlMatches = wait.until(ExpectedConditions.urlToBe(expectedUrl));
        assertThat(urlMatches)
                .isTrue();
    }

    @Step("<button> elementine tıklanır")
    public void clickButton(String buttonLabel) {
        homePage.clickByKey(buttonLabel);
    }

    @Step("<locator> locatöründen <param> parametresiyle seçim yapılır")
    public void clickDynamicElement(String locator, String param) {
        basePage.clickDynamicElement(locator,param);
    }


    @Step("Tarih olarak <date> girilir")
    public void enterDateAndClickButton(String date) {
        queryPage.selectStartDate(date);
    }

    @Step("<button> elementine JS ile tıklanır")
    public void JsClickButton(String buttonLabel) {
        queryPage.scrollAndClick(buttonLabel);
    }

    @Step("<text> içeren ilk satıra tıklanır")
    public void clickFirstRowContainingText(String text) {
        resultsPage.clickFirstRowContaining(text);
        resultsPage.switchToNewTab();
    }

    @Step("Sayfanın URL'si <text> içermeli")
    public void urlShouldStartWith(String pathPrefix) {
        String currentUrl = driver.getCurrentUrl();
        assertThat(currentUrl)
                .as("URL beklenen <pathPrefix> ile başlamıyor")
                .contains(pathPrefix);
    }

    @Step("<format> dosyası <expectedFileName> olarak indirilmiş olmalı")
    public void checkExcelDownloaded(String format, String expectedFileName) throws InterruptedException {
        String filePath = DriverFactory.getDownloadDir() + "/" + expectedFileName;
        boolean isDownloaded = DownloadChecker.waitForFileDownload(filePath, 10); //Proje ihtiyacına göre ScheduledExecutorService kullanılabilir
        assertThat(isDownloaded)
                .as("Dosya belirtilen sürede indirilemedi: %s", expectedFileName)
                .isTrue();
    }

    private String normalize(String text) {
        if (text == null) return "";
        return text
                .replace("\u00A0", " ")               // non-breaking space (&nbsp;)
                .replaceAll("[\\n\\r\\t]", " ")       // newline, tab, carriage return
                .replaceAll("\\s+", " ")              // multiple spaces to single space
                .replaceAll("(?i)[ ]+(\\p{Punct})", "$1") // boşluk+noktalama → sadece noktalama
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    @Step("XLS dosyasındaki veriler ile web sayfasındaki veriler karşılaştırılır <htmlPath>")
    public void compareHtmlWithWeb(String htmlPath) throws Exception {

        Map<String, String> htmlData = KapHtmlParser.parseHtml(new File(htmlPath));
        Map<String, String> webData = new NotificationDetailsPage(driver).extractWebData();

        for (String key : htmlData.keySet()) {
            if (webData.containsKey(key)) {
                String expected = htmlData.get(key);
                String actual = webData.get(key);

                if (key.equalsIgnoreCase("Açıklama")) {
                    String normExpected = normalize(expected);
                    String normActual = normalize(actual);

                    boolean matches = normActual.contains(normExpected) || normExpected.contains(normActual);

                    assertThat(matches)
                            .as("Alan uyuşmazlığı: " + key + "\nBeklenen:\n" + normExpected + "\nGerçek:\n" + normActual)
                            .isTrue();
                }else {
                    assertThat(actual)
                            .as("Alan uyuşmazlığı: " + key)
                            .contains(expected);
                }

            } else {
                System.out.println("❌ Web sayfasında bulunamayan alan: " + key);
                throw new Exception("HTML'de bulunmayan alan: " + key);
            }
        }

        for (String key : webData.keySet()) {
            if (!htmlData.containsKey(key)) {
                System.out.println("⚠️ HTML'de bulunmayan alan: " + key);
                throw new Exception("HTML'de bulunmayan alan: " + key);
            }
        }
    }

}
