package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;

public class KapHomePage extends BasePage {
    public KapHomePage(WebDriver driver) {
        super(driver, "KapHomePage");
    }

    public void clickMenu(String menuKey) {
        By menuLocator = getLocator(menuKey);
        clickByLocator(menuLocator);
    }

    public void clickSubMenu(String submenuKey) {
        By subMenuLocator = getLocator(submenuKey);
        clickByLocator(subMenuLocator);
    }
}
