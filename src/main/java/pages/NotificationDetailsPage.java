package pages;

import org.openqa.selenium.*;
import utils.LogUtil;

public class NotificationDetailsPage extends BasePage {

    public NotificationDetailsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Verilen dosya formatına göre dosya indirme butonuna tıklar.
     * JSON'da tanımlı "BTN_FILE_DOWNLOAD_GENERIC" adlı raw XPath içine
     * parametre yerleştirilerek butonun locatörü oluşturulur ve butona tıklanır.
     *
     * @param param İndirilecek dosyanın formatını belirten parametre
     */
    public void clickFileFormat(String param) {
        try {
            LogUtil.logger.info("'{}' parametresi ile dosya indirme butonuna tıklanıyor...", param);
            String rawXpath = getLocatorRaw("BTN_FILE_DOWNLOAD_GENERIC");
            String finalXpath = String.format(rawXpath, param);
            By locator = By.xpath(finalXpath);
            clickByLocator(locator);
            LogUtil.logger.info("Dosya indirme butonuna başarıyla tıklandı.");
        } catch (Exception e) {
            LogUtil.logger.error("'{}' parametresi ile dosya indirme butonuna tıklanırken hata oluştu.", param, e);
            throw new RuntimeException("Dosya indirme tıklama hatası: " + param, e);
        }
    }

}
