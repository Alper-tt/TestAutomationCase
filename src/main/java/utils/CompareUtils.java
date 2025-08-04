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

public class CompareUtils {
    WebDriver driver = DriverFactory.getDriver();

    public void compareHtmlWithWeb(String relativePath) throws Exception {
        LogUtil.logger.info("HTML ve web verileri karşılaştırılıyor. HTML dosyası: {}", relativePath);
        File htmlPath = new File(System.getProperty("user.dir"), relativePath);


        Map<String, String> htmlData = KapHtmlParser.parseHtml(htmlPath);
        Map<String, String> webData = extractWebData();

        for (String key : htmlData.keySet()) {
            if (webData.containsKey(key)) {
                String expected = htmlData.get(key);
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
            if (!htmlData.containsKey(key)) {
                LogUtil.logger.warn("HTML'de '{}' alanı bulunmuyor.", key);
                throw new Exception("HTML'de bulunmayan alan: " + key);
            }
        }

        LogUtil.logger.info("HTML ve web karşılaştırması başarıyla tamamlandı.");
    }

    public Map<String, String> extractWebData() {
        LogUtil.logger.info("Web sayfasından veri çekme işlemi başlatıldı.");
        Map<String, String> result = new LinkedHashMap<>();

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

}
