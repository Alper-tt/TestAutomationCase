package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.LogUtil;

import java.util.List;

public class ResultsPage extends BasePage {

    public ResultsPage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilResultsLoaded() {
        try {
            LogUtil.logger.info("Yüklenme göstergesinin kaybolması bekleniyor...");
            By loading = getLocator("PROGRESS_INDICATOR");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loading));
            LogUtil.logger.info("Yüklenme göstergesi kayboldu, sonuçlar yüklendi.");
        } catch (Exception e) {
            LogUtil.logger.error("Sonuçların yüklenmesi beklenirken hata oluştu.", e);
            throw new RuntimeException("Sonuçların yüklenmesi sırasında hata oluştu.", e);
        }
    }


    public void clickFirstRowContaining(String text) {
        try {
            LogUtil.logger.info("'{}' metnini içeren ilk satıra tıklanıyor...", text);
            waitUntilResultsLoaded();

            String rawXpath = getLocatorRaw("ROW_CONTAINING_TEXT");
            String finalXpath = String.format(rawXpath, text);
            By rowLocator = By.xpath(finalXpath);

            List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(rowLocator));

            for (WebElement row : rows) {
                WebElement cell = row.findElement(getLocator("CLICKABLE_CELL_IN_ROW"));
                clickWithJS(cell);
                LogUtil.logger.info("Satır bulundu ve tıklandı: {}", text);
                return;
            }

            LogUtil.logger.warn("'{}' içeren herhangi bir satır bulunamadı!", text);
            throw new NotFoundException("'" + text + "' içeren satır bulunamadı.");

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            LogUtil.logger.error("'{}' içeren satır tıklanırken hata oluştu.", text, e);
            throw new RuntimeException("Satır tıklanamadı: " + text, e);
        }
    }



    public void switchToNewTab() {
        try {
            LogUtil.logger.info("Yeni sekmeye geçiş yapılıyor...");
            String originalWindow = driver.getWindowHandle();
            wait.until(driver -> driver.getWindowHandles().size() > 1);

            for (String handle : driver.getWindowHandles()) {
                if (!handle.equals(originalWindow)) {
                    driver.switchTo().window(handle);
                    LogUtil.logger.info("Yeni sekmeye geçildi.");
                    return;
                }
            }

        } catch (Exception e) {
            LogUtil.logger.error("Yeni sekmeye geçilirken hata oluştu.", e);
            throw new RuntimeException("Yeni sekmeye geçiş başarısız.", e);
        }
    }

}
