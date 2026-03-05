package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import utils.DriverFactory;
import utils.JavaScriptActions;
import utils.WaitUtils;
import java.util.List;

public class LanguagePage {

    private final By pageBody = By.tagName("body");
    private final By languageMenuTrigger = By.id("icp-nav-flyout");
    private final By changeLanguageLink = By.xpath(
            "//a[contains(@href,'customer-preferences/edit') or contains(@href,'preferences') or contains(@href,'lang')]"
    );
    private final By englishRadio = By.xpath(
            "//input[@type='radio' and (contains(@value,'en_') or contains(@value,'en-') or @value='en_AE')] | " +
            "//label[contains(.,'English') and contains(.,'EN')]//input[@type='radio'] | " +
            "//span[contains(normalize-space(),'English') and contains(normalize-space(),'EN')]/ancestor::label//input[@type='radio']"
    );
    private final By englishOptionLabel = By.xpath(
            "//label[contains(normalize-space(),'English') and contains(normalize-space(),'EN')] | " +
            "//span[contains(normalize-space(),'English - EN') or contains(normalize-space(),'English EN') or contains(normalize-space(),'English')]/ancestor::*[self::label or self::span or self::div][1]"
    );
    private final By saveLanguageButton = By.xpath(
            "//input[@type='submit' and contains(@value,'Save')] | " +
            "//button[contains(normalize-space(),'Save')] | " +
            "//a[contains(normalize-space(),'Save')] | " +
            "//span[contains(normalize-space(),'Save')]/ancestor::*[self::button or self::a or contains(@class,'a-button')][1]"
    );
    private final By saveLanguageTextNode = By.xpath(
            "//span[contains(normalize-space(),'Save Changes') or contains(normalize-space(),'Save')]"
    );
    private final By languageHeaderIndicator = By.cssSelector("#icp-nav-flyout, #icp-nav-flyout-2");
    private final String directLanguageSettingsUrl =
            "https://www.amazon.eg/customer-preferences/edit?ie=UTF8&preferencesReturnUrl=%2F&ref_=topnav_lang";

    public void openLanguageSettings() {
        try {
            WaitUtils.safeClickWithScrollAndJsFallback(languageMenuTrigger);
            if (!DriverFactory.getDriver().findElements(changeLanguageLink).isEmpty()) {
                WaitUtils.safeClickWithScrollAndJsFallback(changeLanguageLink);
            } else {
                DriverFactory.getDriver().get(directLanguageSettingsUrl);
            }
        } catch (Exception ignored) {
            DriverFactory.getDriver().get(directLanguageSettingsUrl);
        }
        WaitUtils.waitForElementVisible(pageBody);
    }

    public boolean selectEnglishAndSave() {
        boolean selected = clickEnglishOption();
        if (!selected) {
            System.out.println("Language debug -> English option not found/clickable.");
            return false;
        }
        if (!isEnglishRadioSelected()) {
            System.out.println("Language debug -> English radio still not selected after click.");
            return false;
        }

        boolean saved = clickSaveButton();
        if (!saved) {
            System.out.println("Language debug -> Save language button not found/clickable.");
            return false;
        }

        WaitUtils.waitForElementVisible(pageBody);
        return waitForLanguageApply();
    }

    private boolean clickEnglishOption() {
        if (clickFirst(englishOptionLabel)) {
            return true;
        }
        return clickFirst(englishRadio);
    }

    private boolean clickSaveButton() {
        if (clickFirstVisibleEnabled(saveLanguageButton)) {
            return true;
        }
        if (clickFirst(saveLanguageTextNode)) {
            return true;
        }
        try {
            ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                    "var forms=document.querySelectorAll('form');" +
                    "if(forms.length>0){forms[0].submit(); return true;} return false;");
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean isEnglishRadioSelected() {
        List<WebElement> radios = DriverFactory.getDriver().findElements(englishRadio);
        if (radios.isEmpty()) {
            return false;
        }
        for (WebElement radio : radios) {
            try {
                if (radio.isSelected()) {
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    private boolean clickFirst(By locator) {
        List<WebElement> elements = DriverFactory.getDriver().findElements(locator);
        if (elements.isEmpty()) {
            return false;
        }
        WebElement target = elements.get(0);
        return clickElement(target);
    }

    private boolean clickFirstVisibleEnabled(By locator) {
        List<WebElement> elements = DriverFactory.getDriver().findElements(locator);
        if (elements.isEmpty()) {
            return false;
        }
        for (WebElement element : elements) {
            try {
                if (!element.isDisplayed() || !element.isEnabled()) {
                    continue;
                }
                if (clickElement(element)) {
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    private boolean clickElement(WebElement target) {
        try {
            JavaScriptActions.scrollIntoViewCenter(target);
            target.click();
            return true;
        } catch (Exception ex) {
            try {
                JavaScriptActions.click(target);
                return true;
            } catch (Exception jsEx) {
                return false;
            }
        }
    }

    private boolean waitForLanguageApply() {
        try {
            WaitUtils.getCustomWait(10).until(d -> {
                String currentUrl = d.getCurrentUrl();
                if (currentUrl != null && currentUrl.contains("language=en")) {
                    return true;
                }
                if (currentUrl != null && !currentUrl.contains("customer-preferences/edit")) {
                    List<WebElement> indicators = d.findElements(languageHeaderIndicator);
                    if (!indicators.isEmpty()) {
                        String text = indicators.get(0).getText();
                        return text != null && text.toLowerCase().contains("en");
                    }
                }
                return false;
            });
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
