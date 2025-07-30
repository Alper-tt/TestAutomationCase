package pages;

import org.openqa.selenium.*;

public class QueryPage extends BasePage {

    public QueryPage(WebDriver driver) {
        super(driver, "QueryPage");
    }

    public void clickTab(String tabName) {
        By tabLocator = getLocator(tabName);
        clickByLocator(tabLocator);
    }

    public void clickDropdown(String dropdownKey) {
        By dropdown = getLocator(dropdownKey);
        clickByLocator(dropdown);
    }

    public void selectDropdownOption(String optionText) {
        String rawXpath = getLocatorRaw("dropdownOption_Generic");
        String finalXpath = String.format(rawXpath, optionText);
        By optionLocator = By.xpath(finalXpath);
        clickByLocator(optionLocator);
    }

    public void selectStartDate(String date) {
        clickByLocator(getLocator("datepickerButton_BaslangicTarihi"));
        clickByLocator(getLocator("switchToYearButton"));

        String[] parts = date.split("\\.");
        String gun = String.valueOf(Integer.parseInt(parts[0]));
        String ay = parts[1];
        String yil = parts[2];

        String yearXpath = String.format(getLocatorRaw("yearButton_Generic"), yil);
        clickByLocator(By.xpath(yearXpath));

        String ayLabel = getMonthName(ay);
        String monthXpath = String.format(getLocatorRaw("monthButton_Generic"), ayLabel, ayLabel.substring(0, 3));
        clickByLocator(By.xpath(monthXpath));

        String dayXpath = String.format(getLocatorRaw("dayButton_Generic"), gun);
        clickByLocator(By.xpath(dayXpath));
    }

    private String getMonthName(String number) {
        return switch (number) {
            case "01" -> "Ocak";
            case "02" -> "Şubat";
            case "03" -> "Mart";
            case "04" -> "Nisan";
            case "05" -> "Mayıs";
            case "06" -> "Haziran";
            case "07" -> "Temmuz";
            case "08" -> "Ağustos";
            case "09" -> "Eylül";
            case "10" -> "Ekim";
            case "11" -> "Kasım";
            case "12" -> "Aralık";
            default -> throw new IllegalArgumentException("Geçersiz ay: " + number);
        };
    }

    public void clickButton(String buttonLabel) {
        By btnLocator = getLocator(buttonLabel);
        scrollAndClick(btnLocator);
    }
}