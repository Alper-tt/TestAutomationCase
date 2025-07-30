package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.openqa.selenium.By;

import java.io.InputStream;
import java.util.*;

public class ElementLocatorReader {
    private final Map<String, By> locatorMap = new HashMap<>();
    private final Map<String, String> rawValueMap = new HashMap<>();

    public ElementLocatorReader(String jsonFileName) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("elements/" + jsonFileName);
            if (inputStream == null) {
                throw new RuntimeException("JSON dosyası bulunamadı: " + jsonFileName);
            }

            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, String>> elements = mapper.readValue(inputStream, new TypeReference<>() {});

            for (Map<String, String> item : elements) {
                String key = item.get("key");
                String value = item.get("value");
                String type = item.get("type");

                By by = switch (type.toLowerCase()) {
                    case "css" -> By.cssSelector(value);
                    case "xpath" -> By.xpath(value);
                    case "id" -> By.id(value);
                    case "name" -> By.name(value);
                    default -> throw new IllegalArgumentException("Desteklenmeyen locator tipi: " + type);
                };

                locatorMap.put(key, by);
                rawValueMap.put(key, value); // yeni eklendi
            }
        } catch (Exception e) {
            throw new RuntimeException("Element JSON dosyası okunurken hata oluştu: " + jsonFileName, e);
        }
    }

    public By get(String key) {
        if (!locatorMap.containsKey(key)) {
            throw new IllegalArgumentException("Element key bulunamadı: " + key);
        }
        return locatorMap.get(key);
    }

    public String getRaw(String key) {
        if (!rawValueMap.containsKey(key)) {
            throw new IllegalArgumentException("Elementin raw (string) değeri bulunamadı: " + key);
        }
        return rawValueMap.get(key);
    }
}
