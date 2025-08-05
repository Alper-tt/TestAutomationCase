package steps;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import org.openqa.selenium.WebDriver;
import pages.QueryPage;

import static utils.DriverFactory.getDriver;

public class QueryImpl {

    WebDriver driver = getDriver();
    QueryPage queryPage = new QueryPage(driver);

    @Step("Sirket grubu menüsünden <param> seç")
    public void clickCompanyGroup(String param) {
        queryPage.clickCompanyGroup(param);
    }

    @Step("Tablodaki veriyi tarih alanina gir <date>")
    public void enterDateAndClickButton(Table date) {
        queryPage.selectStartDate(date);
    }

    @Step("<button> elementine JS ile tıklanır")
    public void JsClickButton(String buttonLabel) {
        queryPage.clickWithJS(buttonLabel);
    }

}
