package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CompareWebWithXlsUtils {
    WebDriver driver = DriverFactory.getDriver();

    /**
     * XLS dosyasındaki veriler ile web sayfasından çekilen verileri karşılaştırır.
     * XLS verisi KapXLSParser.parseXLS() ile okunur, web verisi extractWebData() ile elde edilir.
     * Karşılaştırma her bir alan (key) bazında yapılır.
     * Eğer alan adı "Açıklama" ise, içerikler normalize edilerek esnek karşılaştırma yapılır.
     * Diğer alanlarda doğrudan actual.contains(expected) kontrolü yapılır.
     * Eksik veya fazla alanlar hata olarak raporlanır.
     *
     * @param relativePath Proje kök dizinine göre XLS dosyasının yolu
     */
    public void compareXlsWithWeb(String relativePath) throws Exception {
        LogUtil.logger.info("XLS ve web verileri karşılaştırılıyor. XLS dosyası: {}", relativePath);
        File xlsPath = new File(System.getProperty("user.dir"), relativePath);


        Map<String, String> xlsData = KapXLSParser.parseXLS(xlsPath);
        Map<String, String> webData = extractWebData();

        for (String key : xlsData.keySet()) {
            if (webData.containsKey(key)) {
                String expected = xlsData.get(key);
                String actual = webData.get(key);

                if (key.equalsIgnoreCase("Açıklama")) {
                    String normExpected = normalize(expected);
                    String normActual = normalize(actual);

                    boolean matches = normActual.contains(normExpected) || normExpected.contains(normActual);
                    LogUtil.logger.info("Açıklama alanı karşılaştırılıyor.\nBeklenen: {}\nGerçek: {}", normExpected, normActual);

                    assertThat(matches)
                            .as("Alan uyuşmazlığı: {}\nBeklenen:\n{}\nGerçek:\n{}", key, normExpected, normActual)
                            .isTrue();
                } else {

                    if (key.equalsIgnoreCase("Gönderim Tarihi")) {
                        expected = normalizeDateTime(expected);
                        actual = normalizeDateTime(actual);
                    }

                    LogUtil.logger.info("'{}' alanı karşılaştırılıyor. Beklenen: '{}', Gerçek: '{}'", key, expected, actual);
                    assertThat(actual)
                            .as("Alan uyuşmazlığı: " + key)
                            .contains(expected);
                }
            } else {
                LogUtil.logger.error("Web sayfasında '{}' alanı bulunamadı.", key);
                throw new Exception("Web sayfasında bulunamayan alan: " + key);
            }
        }

        for (String key : webData.keySet()) {
            if (!xlsData.containsKey(key)) {
                LogUtil.logger.warn("XLS'te '{}' alanı bulunmuyor.", key);
                throw new Exception("XLS'te bulunmayan alan: " + key);
            }
        }

        LogUtil.logger.info("XLS ve web karşılaştırması başarıyla tamamlandı.");
    }

    /**
     * Web sayfasındaki soru-cevap (etiket-değer) yapısında olan verileri çeker.
     * Sayfa üzerindeki tüm tr.data-input-row satırları taranır.
     * Her satırdan ".taxonomy-field-title .gwt-Label" ve ".taxonomy-context-value .gwt-Label"
     * elementleri bulunup key-value eşlemesi oluşturulur.
     * Ek olarak ".text-block-value" CSS selector'ü ile "Açıklama" alanı da varsa çekilip eklenir.
     * Eksik satırlar ya da alanlar atlanır ve loglanır.
     *
     * @return Web sayfasındaki verileri Map<String, String> yapısında döner
     */
    public Map<String, String> extractWebData() {
        LogUtil.logger.info("Web sayfasından veri çekme işlemi başlatıldı.");
        Map<String, String> result = new LinkedHashMap<>();

        try {
            WebElement labelTarih = driver.findElement(By.xpath("//*[text()='Gönderim Tarihi']/following-sibling::*"));
            String tarihText = labelTarih.getText().trim();
            result.put("Gönderim Tarihi", tarihText);
            LogUtil.logger.debug("Gönderim Tarihi bulundu (Web): {}", tarihText);
        } catch (NoSuchElementException e) {
            LogUtil.logger.warn("Gönderim Tarihi alanı bulunamadı.");
        }

//        DKB tipli bildirimler XLS dosyasinda DUY olarak işleniyor
//        ÖDA tipli bildirimler XLS dosyasinda ODA olarak işleniyor
//        try {
//            WebElement labelTip = driver.findElement(By.xpath("//*[text()='Bildirim Tipi']/following-sibling::*"));
//            String tipText = labelTip.getText().trim();
//            result.put("Bildirim Tipi", tipText);
//            LogUtil.logger.debug("Bildirim Tipi bulundu (Web): {}", tipText);
//        } catch (NoSuchElementException e) {
//            LogUtil.logger.warn("Bildirim Tipi alanı bulunamadı.");
//        }

        try {
            List<WebElement> rows = driver.findElements(By.cssSelector("tr.data-input-row"));
            LogUtil.logger.info("{} satır bulundu.", rows.size());

            for (WebElement row : rows) {
                try {
                    WebElement questionElement = row.findElement(By.cssSelector(".taxonomy-field-title .gwt-Label"));
                    WebElement valueElement = row.findElement(By.cssSelector(".taxonomy-context-value .gwt-Label"));

                    String question = questionElement.getText().trim();
                    String value = valueElement.getText().trim();

                    LogUtil.logger.debug("Veri bulundu - '{}' : '{}'", question, value);
                    result.put(question, value);
                } catch (NoSuchElementException e) {
                    LogUtil.logger.warn("Bir satırda beklenen element bulunamadı, atlanıyor.", e);
                }
            }

            try {
                WebElement explanation = driver.findElement(By.cssSelector(".text-block-value"));
                String explanationText = explanation.getText().trim();
                result.put("Açıklama", explanationText);
                LogUtil.logger.debug("Açıklama eklendi: {}", explanationText);
            } catch (NoSuchElementException e) {
                LogUtil.logger.warn("Açıklama alanı bulunamadı, atlanıyor.", e);
            }

        } catch (Exception e) {
            LogUtil.logger.error("Web verisi çekilirken beklenmeyen bir hata oluştu.", e);
            throw new RuntimeException("Web verisi çekme hatası", e);
        }

        LogUtil.logger.info("Web verisi başarıyla çekildi. Toplam {} alan alındı.", result.size());
        return result;
    }

    /**
     * Verilen metni karşılaştırma için normalize eder.
     * Aşağıdaki işlemleri gerçekleştirir:
     * Non-breaking space karakterlerini normal boşluklara çevirir
     * Satır sonu, tab gibi karakterleri boşlukla değiştirir
     * Gereksiz birden fazla boşluğu teke indirger
     * Boşluk + noktalama kombinasyonlarını sadeleştirir (örnek: " ." → ".")
     * Trim ve lowercase dönüşümü uygular
     *
     * @param text Normalize edilecek metin
     */
    private String normalize(String text) {
        if (text == null) return "";
        return text
                .replace("\u00A0", " ")
                .replaceAll("[\\n\\r\\t]", " ")
                .replaceAll("\\s+", " ")
                .replaceAll("(?i)[ ]+(\\p{Punct})", "$1")
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    /**
     * Tarih ve saat bilgisini normalize eder.
     * Eğer verilen metin "05.08.202512:18:34" gibi arada boşluk olmayan bir formattaysa,
     * otomatik olarak "05.08.2025 12:18:34" biçimine dönüştürür.
     * Diğer durumlarda metni olduğu gibi döner.
     * Bu metod, XLS dosyasından gelen ve formatı bozuk olan tarih-saat değerlerini
     * web sayfasındaki değerlerle karşılaştırılabilir hale getirmek için kullanılır.
     * @param raw Normalize edilecek tarih-saat metni
     * @return Boşlukla ayrılmış normalize edilmiş tarih-saat metni
     */

    public String normalizeDateTime(String raw) {
        if (raw.length() == 18 && !raw.contains(" ")) {
            return raw.substring(0, 10) + " " + raw.substring(10);
        }
        return raw;
    }

}