package pages;

import org.openqa.selenium.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NotificationDetailsPage extends BasePage{

    public NotificationDetailsPage(WebDriver driver) {
        super(driver);
    }
    public Map<String, String> extractWebData() {
        Map<String, String> result = new LinkedHashMap<>();

        List<WebElement> rows = driver.findElements(By.cssSelector("tr.data-input-row"));

        for (WebElement row : rows) {
            try {
                WebElement questionElement = row.findElement(By.cssSelector(".taxonomy-field-title .gwt-Label"));
                WebElement valueElement = row.findElement(By.cssSelector(".taxonomy-context-value .gwt-Label"));

                String question = questionElement.getText().trim();
                String value = valueElement.getText().trim();

                result.put(question, value);
            } catch (NoSuchElementException e) {
                // Bazı satırlarda boş olabilir
            }
        }

        try {
            WebElement explanation = driver.findElement(By.cssSelector(".text-block-value"));
            result.put("Açıklama", explanation.getText().trim());
        } catch (NoSuchElementException ignored) {}

        return result;
    }
}
