package utils;

public class AllureReportGenerator {

    /**
     * Gauge test çalıştırmalarından sonra Allure raporunu oluşturur.
     * Bu metod `allure generate` komutunu işletim sistemi üzerinden çalıştırarak:
     *   `reports/xml-report` dizinindeki XML test sonuçlarını okur,
     *   `allure-report` dizinine temiz bir şekilde HTML raporu oluşturur.
     * Eğer süreç başarılı tamamlanırsa log'a bilgi yazılır, aksi halde hata log'lanır.
     */
    public static void generateReport() {
        try {
            LogUtil.logger.info("Allure raporu oluşturuluyor...");
            Process process = new ProcessBuilder("allure", "generate", "reports/xml-report", "--clean", "-o", "allure-report")
                    .start();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                LogUtil.logger.info("Allure raporu başarıyla oluşturuldu.");
            } else {
                LogUtil.logger.error("Allure raporu oluşturulamadı");
            }

            // Allure raporunu otomatik açmak istersek:
            // new ProcessBuilder("allure", "open", "allure-report").start();

        } catch (Exception e) {
            LogUtil.logger.error("Allure raporu oluşturulurken hata oluştu.", e);
        }
    }

}