package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import utils.DriverFactory;
import utils.WaitUtils;

public class HomePage {




    private final By loginButtonPrimary = By.id("nav-link-accountList");
    private final By loginButtonFallback = By.cssSelector("a#nav-link-accountList, a[data-nav-role='signin']");
    private final String directSignInUrl = "https://www.amazon.eg/ap/signin";


    private final By allMenuButton = By.id("nav-hamburger-menu");



    public LoginPage clickLogin() {
        WaitUtils.waitForElementVisible(By.tagName("body"));
        try {
            WaitUtils.safeClickWithScrollAndJsFallback(loginButtonPrimary);
            return new LoginPage();
        } catch (TimeoutException ignored) {
            // Try fallback selector before direct signin URL.
        }

        try {
            WaitUtils.safeClickWithScrollAndJsFallback(loginButtonFallback);
            return new LoginPage();
        } catch (TimeoutException ignored) {
            DriverFactory.getDriver().get(directSignInUrl);
            WaitUtils.waitForElementVisible(By.tagName("body"));
        }

        return new LoginPage();
    }


    public void openAllMenu() {
        WaitUtils.clickElement(allMenuButton);
    }
}