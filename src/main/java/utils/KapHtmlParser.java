package utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class KapHtmlParser {

    public static Map<String, String> parseHtml(File file) throws IOException {
        LogUtil.logger.info("HTML dosyası parse ediliyor: {}", file.getAbsolutePath());

        Map<String, String> result = new LinkedHashMap<>();

        try {
            Document doc = Jsoup.parse(file, "UTF-8");

            Elements rows = doc.select("tr.data-input-row");
            LogUtil.logger.info("Toplam {} satır bulundu.", rows.size());

            for (Element row : rows) {
                Elements questionDivs = row.select(".taxonomy-field-title .gwt-Label");
                Elements valueDivs = row.select(".taxonomy-context-value .gwt-Label");

                if (!questionDivs.isEmpty() && !valueDivs.isEmpty()) {
                    String question = questionDivs.first().text().trim();
                    String value = valueDivs.first().text().trim();
                    result.put(question, value);
                    LogUtil.logger.debug("Soru: '{}', Değer: '{}'", question, value);
                } else {
                    LogUtil.logger.warn("Satırda eksik bilgi bulundu. Satır atlandı.");
                }
            }

            Element explanation = doc.selectFirst("div.text-block-value");
            if (explanation != null) {
                String text = explanation.text().trim();
                result.put("Açıklama", text);
                LogUtil.logger.debug("Açıklama bulundu: {}", text);
            } else {
                LogUtil.logger.warn("Açıklama alanı bulunamadı.");
            }

            LogUtil.logger.info("HTML parse işlemi tamamlandı. Toplam {} veri alındı.", result.size());

        } catch (IOException e) {
            LogUtil.logger.error("HTML dosyası okunurken hata oluştu: {}", e.getMessage());
            throw e;
        }

        return result;
    }


}
