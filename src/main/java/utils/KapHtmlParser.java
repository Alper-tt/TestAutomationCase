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
        Document doc = Jsoup.parse(file, "UTF-8");
        Map<String, String> result = new LinkedHashMap<>();

        Elements rows = doc.select("tr.data-input-row");
        for (Element row : rows) {
            Elements questionDivs = row.select(".taxonomy-field-title .gwt-Label");
            Elements valueDivs = row.select(".taxonomy-context-value .gwt-Label");

            if (!questionDivs.isEmpty() && !valueDivs.isEmpty()) {
                String question = questionDivs.first().text().trim();
                String value = valueDivs.first().text().trim();
                result.put(question, value);
            }
        }

        Element explanation = doc.selectFirst("div.text-block-value");
        if (explanation != null) {
            result.put("Açıklama", explanation.text().trim());
        }

        return result;
    }
}
