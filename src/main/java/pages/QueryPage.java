package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class QueryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public QueryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private By dropdownToggle = By.cssSelector(".custom-select-toggle");

    private By datepickerButton = By.cssSelector(".MuiIconButton-root[aria-label^='Choose date']");
    private By switchToYearButton = By.cssSelector("button[aria-label*='calendar view']");

    public void clickTab(String tabName) {
        By tabLocator = By.xpath("//button[normalize-space()='" + tabName + "']");
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(tabLocator));
        tab.click();
    }

    public void selectFromDropdownByVisibleText(String dropdownText, String optionText) {
        List<WebElement> dropdowns = driver.findElements(dropdownToggle);
        boolean found = false;

        for (WebElement dropdown : dropdowns) {
            if (dropdown.getText().trim().contains(dropdownText)) {
                wait.until(ExpectedConditions.elementToBeClickable(dropdown)).click();
                found = true;
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("Dropdown with text '" + dropdownText + "' not found.");
        }

        By optionLocator = By.xpath("//div[contains(@class,'select-option') and text()='" + optionText + "']");
        wait.until(ExpectedConditions.elementToBeClickable(optionLocator)).click();
    }

    public void setDate(String targetDate) {
        openDatePicker();

        wait.until(ExpectedConditions.elementToBeClickable(switchToYearButton)).click();

        String[] parts = targetDate.split("\\.");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];

        By dynamicYear = By.xpath("//button[text()='" + year + "']");
        wait.until(ExpectedConditions.elementToBeClickable(dynamicYear)).click();

        String monthLabel = getMonthName(month);
        By dynamicMonth = By.xpath("//button[contains(@aria-label, '" + monthLabel + "') or contains(text(), '" + monthLabel.substring(0, 3) + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(dynamicMonth)).click();

        selectDay(Integer.parseInt(day));
    }

    private void openDatePicker() {
        wait.until(ExpectedConditions.elementToBeClickable(datepickerButton)).click();
    }

    private void selectDay(int day) {
        String xpath = "//button[normalize-space()='" + day + "' and not(@disabled)]";
        WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        dayElement.click();
    }

    private String getMonthName(String monthNumber) {
        return switch (monthNumber) {
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
            default -> throw new IllegalArgumentException("Geçersiz ay: " + monthNumber);
        };
    }

    public void clickButton(String buttonLabel) {
        By btnLocator = By.xpath("//button[contains(text(),'" + buttonLabel + "')]");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnLocator));

        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
            btn.click();
        } catch (Exception e) {
            System.out.println("Normal click başarısız, JS ile tıklanıyor.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }
}
