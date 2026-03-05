package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import utils.DriverFactory;
import utils.WaitUtils;
import java.util.List;
import java.util.Locale;

public class HomePage {




    private final By loginButtonPrimary = By.id("nav-link-accountList");
    private final By loginButtonFallback = By.cssSelector("a#nav-link-accountList, a[data-nav-role='signin']");
    private final String directSignInUrl = "https://www.amazon.eg/ap/signin";
    private final String amazonHomeUrl = "https://www.amazon.eg/";


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

    public boolean ensureEnglishLanguage() {
        WaitUtils.waitForElementVisible(By.tagName("body"));
        if (isEnglishUi()) {
            return true;
        }

        LanguagePage languagePage = new LanguagePage();
        languagePage.openLanguageSettings();
        boolean saved = languagePage.selectEnglishAndSave();
        if (saved) {
            DriverFactory.getDriver().get(amazonHomeUrl);
            WaitUtils.waitForElementVisible(By.tagName("body"));
        }

        if (isEnglishUi()) {
            return true;
        }

        // Last fallback with explicit language query.
        DriverFactory.getDriver().get(amazonHomeUrl + "?language=en_AE");
        WaitUtils.waitForElementVisible(By.tagName("body"));
        return isEnglishUi();
    }

    private boolean isEnglishUi() {
        try {
            Object lang = ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript("return document.documentElement.lang || '';");
            String langValue = (lang == null) ? "" : lang.toString().toLowerCase();
            if (langValue.startsWith("en")) {
                return true;
            }
        } catch (Exception ignored) {
        }
        try {
            List<WebElement> navParts = DriverFactory.getDriver().findElements(By.cssSelector("#icp-nav-flyout, #icp-nav-flyout-2"));
            String navText = navParts.isEmpty() ? "" : navParts.get(0).getText();
            if (navText != null) {
                String lowered = navText.toLowerCase(Locale.ROOT);
                if (lowered.contains("en")) {
                    return true;
                }
                if (lowered.contains("ar")) {
                    return false;
                }
            }
            String bodyText = DriverFactory.getDriver().findElement(By.tagName("body")).getText();
            if (bodyText == null || bodyText.isBlank()) {
                return false;
            }
            return bodyText.contains("English") || bodyText.contains("Your Account") || bodyText.contains("Deliver to");
        } catch (Exception ignored) {
            return false;
        }
    }
}