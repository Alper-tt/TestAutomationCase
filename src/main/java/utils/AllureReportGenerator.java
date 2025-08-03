package utils;

public class AllureReportGenerator {

    public static void generateReport() {
        try {
            Process process = new ProcessBuilder("allure", "generate", "reports/xml-report", "--clean", "-o", "allure-report")
                    .start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Allure raporu başarıyla oluşturuldu.");
            } else {
                System.err.println("Allure raporu oluşturulamadı.");
            }

            // new ProcessBuilder("allure", "open", "allure-report").start();

        } catch (Exception e) {
            System.err.println("Allure raporu oluşturulurken hata oluştu:");
            e.printStackTrace();
        }
    }
}
