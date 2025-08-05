package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
    /**
     * Test projesi boyunca merkezi loglama yapılmasını sağlayan yardımcı sınıftır.
     * Apache Log4j2 kullanılarak oluşturulan KapTestLogger logger nesnesini içerir.
     * Test adımları, hata mesajları ve özel olaylar bu logger üzerinden kayıt altına alınabilir.
     * Proje genelinde statik erişimle kullanılması amaçlanmıştır.
     */
    public static Logger logger = LogManager.getLogger("KapTestLogger");
}
