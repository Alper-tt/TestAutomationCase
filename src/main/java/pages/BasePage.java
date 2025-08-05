package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import utils.DriverFactory;
import utils.ElementLocatorReader;
import utils.LogUtil;

import java.time.Duration;

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
     * Verilen anahtara karşılık gelen locator'ı JSON dosyasından alır.
     *
     * @param key JSON dosyasında tanımlı locator anahtarı
     * @return Anahtara karşılık gelen By nesnesi
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

    /**
     * Verilen anahtara karşılık gelen raw (ham) locator string'ini JSON dosyasından alır.
     *
     * @param key JSON dosyasında tanımlı locator anahtarı
     * @return Anahtara karşılık gelen locator'ın string hali
     */
    protected String getLocatorRaw(String key) {
        try {
            LogUtil.logger.info("'{}' anahtarına karşılık gelen raw locator alınıyor.", key);
            return locatorReader.getRaw(key);
        } catch (Exception e) {
            LogUtil.logger.error("'{}' anahtarına ait raw locator alınamadı.", key, e);
            throw new RuntimeException("Raw locator alınamadı: " + key, e);
        }
    }

    /**
     * Verilen locator üzerinden element bulunur ve tıklanır.
     *
     * @param locator Tıklanacak elementin By nesnesi
     */
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

    /**
     * JSON dosyasındaki key'e karşılık gelen element bulunur ve tıklanır.
     *
     * @param key JSON dosyasında tanımlı locator anahtarı
     */
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

    /**
     * WebElement'e JavaScript kullanılarak tıklanır.
     * Bu yöntem, normal Selenium tıklamasının başarısız olduğu durumlar için kullanılır.
     *
     * @param element Tıklanacak WebElement nesnesi
     */
    public void clickWithJS(WebElement element) {
        try {
            LogUtil.logger.info("JS ile element tıklanıyor: {}", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            LogUtil.logger.error("JS ile tıklama başarısız oldu. Element: {}", element, e);
            throw new RuntimeException("JS tıklama hatası", e);
        }
    }

    public void clickWithJS(String buttonLabel) {
        WebElement element = driver.findElement(getLocator(buttonLabel));
        try {
            LogUtil.logger.info("JS ile element tıklanıyor: {}", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            LogUtil.logger.error("JS ile tıklama başarısız oldu. Element: {}", element, e);
            throw new RuntimeException("JS tıklama hatası", e);
        }
    }

    /**
     * Verilen WebElement önce görünür alana scroll edilir, ardından tıklanır.
     * Eğer tıklama başarısız olursa, JS ile tıklama denenir.
     *
     * @param element Scroll edilip tıklanacak WebElement
     */
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

    /**
     * Verilen locator ile element beklenir, scroll edilerek görünür hale getirilir ve tıklanır.
     *
     * @param locator Scroll edilip tıklanacak elementin By nesnesi
     */
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

    /**
     * JSON dosyasındaki anahtara karşılık gelen locator üzerinden scroll ve tıklama işlemi yapılır.
     *
     * @param key JSON dosyasında tanımlı locator anahtarı
     */
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

    public void setBrowserSizeByDevice(String device) {
        Dimension dimension = switch (device.toLowerCase()) {
            case "mobil" -> new Dimension(390, 844);
            case "masaüstü" -> new Dimension(1920, 1080);
            case "tablet" -> new Dimension(768, 1024);
            default -> throw new IllegalArgumentException("Bilinmeyen cihaz tipi: " + device);
        };

        WebDriver driver = DriverFactory.getDriver();
        driver.manage().window().setSize(dimension);
        LogUtil.logger.info("{} cihaz tipi için tarayıcı boyutu ayarlandı: {}", device, dimension);
    }
}
