package pages;

import org.openqa.selenium.*;

public class NotificationDetailsPage extends BasePage{

    public NotificationDetailsPage(WebDriver driver) {
        super(driver,"NotificationDetailsPage");
    }

    public void clickDownloadButton(String format) {
        String rawXpath = getLocatorRaw("btn_File_Download_Generic");
        String finalXpath = String.format(rawXpath, format.toLowerCase());
        By downloadBtn = By.xpath(finalXpath);
        scrollAndClick(downloadBtn);
    }
}
