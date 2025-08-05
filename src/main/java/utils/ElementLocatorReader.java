package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.openqa.selenium.By;

import java.io.InputStream;
import java.util.*;

public class ElementLocatorReader {
    private final Map<String, By> locatorMap = new HashMap<>();
    private final Map<String, String> rawValueMap = new HashMap<>();

    /**
     * Belirtilen JSON dosyalarındaki locator tanımlarını okuyarak bir map'e yükler.
     *
     * @param jsonFileNames elements klasörü içindeki JSON dosya adları
     */
    public ElementLocatorReader(String... jsonFileNames) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            for (String jsonFileName : jsonFileNames) {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("elements/" + jsonFileName);
                if (inputStream == null) {
                    throw new RuntimeException("JSON dosyası bulunamadı: " + jsonFileName);
                }

                List<Map<String, String>> elements = mapper.readValue(inputStream, new TypeReference<>() {});
                for (Map<String, String> item : elements) {
                    String key = item.get("key");
                    String value = item.get("value");
                    String type = item.get("type");

                    By by = switch (type.toLowerCase()) {
                        case "css" -> By.cssSelector(value);
                        case "xpath" -> By.xpath(value);
                        case "id" -> By.id(value);
                        default -> throw new IllegalArgumentException("Desteklenmeyen locator tipi: " + type);
                    };

                    if (locatorMap.containsKey(key)) {
                        LogUtil.logger.info("Aynı key birden fazla JSON dosyasında bulundu: {}", key);
                    }

                    locatorMap.put(key, by);
                    rawValueMap.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Element JSON dosyaları okunurken hata oluştu.", e);
        }
    }

    /**
     * Verilen key'e karşılık gelen By locator nesnesini döner.
     * Bu metot test sınıflarının veya yardımcı sınıfların dinamik locator'lara erişmesini sağlar.
     *
     * @param key JSON dosyalarında tanımlı olan locator anahtarı
     * @return By türünde Selenium locator nesnesi
     */
    public By get(String key) {
        if (!locatorMap.containsKey(key)) {
            throw new IllegalArgumentException("Element key bulunamadı: " + key);
        }
        return locatorMap.get(key);
    }

    /**
     * Verilen key'e karşılık gelen locator string'ini (raw value) döner.
     * Bu metot özellikle String formatlı XPath/CSS ile işlem yapmak gerektiğinde kullanılır.
     *
     * @param key JSON dosyalarında tanımlı olan locator anahtarı
     * @return Locator'ın ham (raw) string karşılığı
     */
    public String getRaw(String key) {
        if (!rawValueMap.containsKey(key)) {
            throw new IllegalArgumentException("Elementin raw (string) değeri bulunamadı: " + key);
        }
        return rawValueMap.get(key);
    }
}
