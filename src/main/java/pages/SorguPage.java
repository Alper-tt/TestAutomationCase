package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SorguPage {

    WebDriver driver;
    WebDriverWait wait;

    public SorguPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 1. Şirket Bildirimi tab
    private By sirketBildirimiTab = By.xpath("//button[text()='Şirket Bildirimi']");

    // 2. Dropdown açan element
    private By dropdownToggle = By.cssSelector(".custom-select-toggle");

    // 3. Dropdown içinden “İşlem Gören Şirketler” seçimi
    private By islemGorenOption = By.xpath("//div[contains(@class, 'select-option') and text()='İşlem Gören Şirketler']");

    // 4. Tarih alanı (readonly input)
    private By dateInput = By.cssSelector("input[placeholder='Tarih...']");

    // 5. Ara butonu
    private By searchButton = By.xpath("//button[normalize-space()='Ara']");

    public void clickSirketBildirimiTab() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(sirketBildirimiTab));
        element.click();
        System.out.println("clickSirketBildirimiTab tıklandı");
    }

    public void selectFromDropdownByVisibleText(String dropdownText, String optionText) {
        List<WebElement> dropdowns = driver.findElements(By.cssSelector(".custom-select-toggle"));
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
        System.out.println("Seçilen opsiyon: " + optionText);
    }


    private By datepickerButton = By.cssSelector(".MuiIconButton-root[aria-label^='Choose date']");
    private By switchToYearButton = By.cssSelector("button[aria-label*='calendar view']");
    private By year2025 = By.xpath("//button[text()='2025']");
    private By juneMonth = By.xpath("//button[contains(@aria-label, 'Haziran') or contains(text(), 'Haz')]");

    public void setDate(String targetDate) {
        // Step 1: Takvimi aç
        openDatePicker();

        // Step 2: Ay/yıl seçim görünümüne geç
        wait.until(ExpectedConditions.elementToBeClickable(switchToYearButton)).click();

        // Step 3: Yılı seç
        wait.until(ExpectedConditions.elementToBeClickable(year2025)).click();

        // Step 4: Ayı seç
        wait.until(ExpectedConditions.elementToBeClickable(juneMonth)).click();

        // Step 5: Günü seç (1)
        selectDay(1);
    }

    private void openDatePicker() {
        wait.until(ExpectedConditions.elementToBeClickable(datepickerButton)).click();
    }

    private void selectDay(int day) {
        String xpath = "//button[normalize-space()='" + day + "' and not(@disabled)]";
        WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        dayElement.click();
    }

    public void clickSearchButton() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        btn.click();
        System.out.println("Ara butonuna tıklandı.");
    }

}
