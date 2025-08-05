package pages;

import com.thoughtworks.gauge.Table;
import org.openqa.selenium.*;
import utils.LogUtil;

public class QueryPage extends BasePage {

    public QueryPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Açılır menüden şirket grubunu seçer.
     * "DROPDOWN_MENU_GENERIC" raw XPath'ine parametre yerleştirilerek locator oluşturulur
     * ve bu locatöre tıklanır.
     *
     * @param param Açılır menüde seçilecek şirket grubu adı
     */
    public void clickCompanyGroup(String param) {
        try {
            LogUtil.logger.info("'{}' parametresi ile şirket grubu seçiliyor...", param);
            String rawXpath = getLocatorRaw("DROPDOWN_MENU_GENERIC");
            String finalXpath = String.format(rawXpath, param);
            By locator = By.xpath(finalXpath);
            clickByLocator(locator);
            LogUtil.logger.info("Şirket grubu başarıyla seçildi: {}", param);
        } catch (Exception e) {
            LogUtil.logger.error("Şirket grubu seçilirken hata oluştu: {}", param, e);
            throw new RuntimeException("Şirket grubu seçim hatası: " + param, e);
        }
    }


    /**
     * Gauge tablosundan gelen tarih verisini kullanarak başlangıç tarihi seçer.
     * Tarih seçim işlemi şu adımları içerir:
     * Başlangıç tarihi alanına tıklanır.
     * Yıl görünümüne geçiş yapılır.
     * Yıl, ay ve gün ayrı ayrı seçilir.
     *
     * @param date Gauge üzerinden gelen tablo yapısında, ilk hücresinde "gg.aa.yyyy" formatında tarih bilgisi içeren tablo
     */
    public void selectStartDate(Table date) {
        try {
            LogUtil.logger.info("Tarih seçme işlemi başlatıldı.");
            scrollAndClick("DATE_PICKER_BASLANGIC_TARIHI");
            clickByKey("BTN_SWITCH_TO_YEAR_VIEW");

            String[] parts = date.getColumnValues(0).get(0).split("\\.");
            String gun = String.valueOf(Integer.parseInt(parts[0]));
            String ay = parts[1];
            String yil = parts[2];

            LogUtil.logger.debug("Seçilecek tarih: Gün='{}', Ay='{}', Yıl='{}'", gun, ay, yil);

            String yearXpath = String.format(getLocatorRaw("BTN_SELECT_YEAR"), yil);
            clickByLocator(By.xpath(yearXpath));
            LogUtil.logger.info("Yıl seçildi: {}", yil);

            String ayLabel = getMonthName(ay);
            String monthXpath = String.format(getLocatorRaw("BTN_SELECT_MONTH"), ayLabel, ayLabel.substring(0, 3));
            clickByLocator(By.xpath(monthXpath));
            LogUtil.logger.info("Ay seçildi: {} ({})", ayLabel, ay);

            String dayXpath = String.format(getLocatorRaw("BTN_SELECT_DAY"), gun);
            clickByLocator(By.xpath(dayXpath));
            LogUtil.logger.info("Gün seçildi: {}", gun);

            LogUtil.logger.info("Tarih seçme işlemi tamamlandı.");

        } catch (Exception e) {
            LogUtil.logger.error("Tarih seçim işlemi sırasında hata oluştu.", e);
            throw new RuntimeException("Tarih seçim hatası", e);
        }
    }

    /**
     * Ay numarasına karşılık gelen Türkçe ay adını döner.
     *
     * @param number Ay numarası ("01" ile "12" arasında olmalı)
     * @return Türkçe ay adı (örneğin: "01" için "Ocak")
     */
    private String getMonthName(String number) {
        return switch (number) {
            case "01" -> "Ocak";
            case "02" -> "Şubat";
            case "03" -> "Mart";
            case "04" -> "Nisan";
            case "05" -> "Mayıs";
            case "06" -> "Haziran";
            case "07" -> "Temmuz";
            case "08" -> "Ağustos";
            case "09" -> "Eylül";
            case "10" -> "Ekim";
            case "11" -> "Kasım";
            case "12" -> "Aralık";
            default -> {
                LogUtil.logger.error("Geçersiz ay numarası alındı: {}", number);
                throw new IllegalArgumentException("Geçersiz ay: " + number);
            }
        };
    }
}