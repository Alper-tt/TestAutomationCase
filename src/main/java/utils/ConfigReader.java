package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("env/default/user.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("user.properties dosyası yüklenemedi!");
        }
    }
    /**
     * Verilen anahtar kelimeye karşılık gelen property değerini döner.
     *
     * @param key user.properties dosyasındaki yapılandırma anahtarı
     * @return Anahtara karşılık gelen değer. Anahtar bulunamazsa null döner.
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
