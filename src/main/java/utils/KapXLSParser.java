package utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class KapXLSParser {

    /**
     * Verilen XLS dosyasını parse ederek, içeriğindeki soru-cevap verilerini ve açıklamayı çıkarır.
     * Yapı şu şekilde işlenir:
     * Her bir tr.data-input-row satırında:
     *     .taxonomy-field-title .gwt-Label} → Soru (key)
     *     .taxonomy-context-value .gwt-Label} → Değer (value)
     *   Eğer varsa div.text-block-value elementi → "Açıklama" key’iyle eklenir.
     * Eksik veya hatalı satırlar atlanır ve loglanır.
     *
     * @param file Parse edilecek XLS dosyası
     * @return Soru → Cevap eşleşmelerini içeren Map<String, String> veri yapısı
     */
    public static Map<String, String> parseXLS(File file) throws IOException {
        LogUtil.logger.info("XLS dosyası parse ediliyor: {}", file.getAbsolutePath());

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

            //Set<String> hedefAlanlar = Set.of("Gönderim Tarihi", "Bildirim Tipi");
            Set<String> hedefAlanlar = Set.of("Gönderim Tarihi");

            Elements infoDivs = doc.select("div");

            for (Element div : infoDivs) {
                String text = div.text().trim();

                for (String hedef : hedefAlanlar) {
                    if (text.startsWith(hedef + ":")) {
                        String value = text.substring((hedef + ":").length()).trim();
                        result.put(hedef, value);
                        LogUtil.logger.debug("Seçili alan bulundu (HTML): {} -> {}", hedef, value);
                    }
                }
            }

            LogUtil.logger.info("XLS parse işlemi tamamlandı. Toplam {} veri alındı.", result.size());

        } catch (IOException e) {
            LogUtil.logger.error("XLS dosyası okunurken hata oluştu: {}", e.getMessage());
            throw e;
        }
        return result;
    }


}
