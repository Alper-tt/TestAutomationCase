package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import utils.ElementLocatorReader;
import utils.LogUtil;

import java.time.Duration;

// concept dosyaları oluştur +
// step dosyalarını sayfa sayfa ayır ****** +
// element isimlerini 'BTN,LBL_NAME' olarak yaz ****** +
// her stepten sonra ekran görüntüsü aldır + (rapor içine eklenebiliyorsa ekle)
// hooks yap** hooksa +
// driver hookta ayağa kalkacak +
// driver tipi çözünürlük pom.xml propertiesden gelmeli (before suite içerisinde çek driverı) + (tekrar denenecek)
// log4j ekle logger.info("Getting locator for key: ", key); +
// log dosyası ekle +
// url gibi bilgileri user properties ekle
// tarih kısmını javascript executor ile value değiştirmeye çalış. chatgpt sor
// docker ekleyip kubernetes entegre etmeye çalış selenium grid dockerdan kaldır.

// olarak tüm methodların başına ekle
/**
 * Ekranda bulunması gereken elementin locatorunu döndürür.
 *
 * @param
 * @return
 */

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

    /**
     * Ekranda bulunması gereken elementin locatorunu döndürür.
     *
     * @param key
     * @return
     */
    protected By getLocator(String key) {
        try {
            LogUtil.logger.info("'{}' anahtarına karşılık gelen locator alınıyor.", key);
            return locatorReader.get(key);
        } catch (Exception e) {
            LogUtil.logger.error("'{}' anahtarına ait locator alınamadı.", key, e);
            throw new RuntimeException("Locator alınamadı: " + key, e);
        }
    }

    protected String getLocatorRaw(String key) {
        try {
            LogUtil.logger.info("'{}' anahtarına karşılık gelen raw locator alınıyor.", key);
            return locatorReader.getRaw(key);
        } catch (Exception e) {
            LogUtil.logger.error("'{}' anahtarına ait raw locator alınamadı.", key, e);
            throw new RuntimeException("Raw locator alınamadı: " + key, e);
        }
    }

    public void clickByLocator(By locator) {
        try {
            LogUtil.logger.info("Locator ile tıklama işlemi başlatılıyor: {}", locator);
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
            LogUtil.logger.info("Tıklama işlemi başarıyla tamamlandı.");
        } catch (Exception e) {
            LogUtil.logger.error("Tıklama işlemi başarısız oldu. Locator: {}", locator, e);
            throw new RuntimeException("Tıklama hatası: " + locator, e);
        }
    }

    public void clickByKey(String key) {
        try {
            LogUtil.logger.info("'{}' anahtarına karşılık gelen element tıklanıyor.", key);
            By locator = locatorReader.get(key);
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
            LogUtil.logger.info("Tıklama işlemi başarıyla tamamlandı.");
        } catch (Exception e) {
            LogUtil.logger.error("Element tıklanamadı: {}", key, e);
            throw new RuntimeException("Tıklama hatası: " + key, e);
        }
    }

    public void clickWithJS(WebElement element) {
        try {
            LogUtil.logger.info("JS ile element tıklanıyor: {}", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            LogUtil.logger.error("JS ile tıklama başarısız oldu. Element: {}", element, e);
            throw new RuntimeException("JS tıklama hatası", e);
        }
    }

    public void scrollAndClick(WebElement element) {
        try {
            LogUtil.logger.info("Element scroll ediliyor ve tıklanıyor: {}", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            Thread.sleep(1000);
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            LogUtil.logger.info("Scroll ve tıklama işlemi başarıyla tamamlandı.");
        } catch (Exception e) {
            LogUtil.logger.warn("Element tıklanamadı, JS ile tıklama deneniyor. Element: {}", element, e);
            clickWithJS(element);
        }
    }

    public void scrollAndClick(By locator) {
        try {
            LogUtil.logger.info("Locator'a scroll ediliyor ve tıklanıyor: {}", locator);
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            scrollAndClick(element);
        } catch (Exception e) {
            LogUtil.logger.error("Scroll ve tıklama işlemi başarısız oldu. Locator: {}", locator, e);
            throw new RuntimeException("Scroll + tıklama hatası: " + locator, e);
        }
    }

    public void scrollAndClick(String key) {
        try {
            LogUtil.logger.info("'{}' anahtarına karşılık gelen element scroll ediliyor ve tıklanıyor.", key);
            By locator = getLocator(key);
            scrollAndClick(locator);
        } catch (Exception e) {
            LogUtil.logger.error("Scroll ve tıklama işlemi başarısız oldu. Anahtar: {}", key, e);
            throw new RuntimeException("Scroll + tıklama hatası: " + key, e);
        }
    }
}
