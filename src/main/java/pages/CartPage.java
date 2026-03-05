package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverFactory;
import utils.WaitUtils;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CartPage {

    private final By cartButton = By.id("nav-cart");
    private final By pageBody = By.tagName("body");
    private final By cartItems = By.cssSelector("div.sc-list-item");
    private final By cartItemTitle = By.cssSelector("span.a-truncate-cut, span.sc-product-title, .sc-grid-item-product-title");
    private final By deleteItemButtons = By.cssSelector("input[value='Delete'], input[name*='delete'], .sc-action-delete input");
    private final By proceedToCheckoutButton = By.name("proceedToRetailCheckout");

    public CartPage openCart() {
        WaitUtils.safeClickWithScrollAndJsFallback(cartButton);
        WaitUtils.waitForElementVisible(pageBody);
        return this;
    }

    public boolean verifyAllExpectedItemsExist(List<String> expectedTitles) {
        if (expectedTitles == null || expectedTitles.isEmpty()) {
            return false;
        }

        List<String> cartTitles = DriverFactory.getDriver().findElements(cartItems).stream()
                .map(item -> {
                    try {
                        return item.findElement(cartItemTitle).getText();
                    } catch (Exception e) {
                        return item.getText();
                    }
                })
                .map(text -> text == null ? "" : text.trim().toLowerCase(Locale.ROOT))
                .filter(text -> !text.isBlank())
                .collect(Collectors.toList());

        if (cartTitles.isEmpty()) {
            return false;
        }

        for (String expectedTitle : expectedTitles) {
            if (expectedTitle == null || expectedTitle.isBlank()) {
                continue;
            }
            String normalizedExpected = expectedTitle.trim().toLowerCase(Locale.ROOT);
            boolean exists = cartTitles.stream().anyMatch(cartTitle ->
                    cartTitle.contains(normalizedExpected) || normalizedExpected.contains(cartTitle));
            if (!exists) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAtLeastItemCount(int expectedCount) {
        if (expectedCount <= 0) {
            return false;
        }
        int actualCount = DriverFactory.getDriver().findElements(cartItems).size();
        return actualCount >= expectedCount;
    }

    public CartPage clearCartIfNotEmpty() {
        openCart();
        int safetyCounter = 0;
        while (safetyCounter < 30) {
            List<WebElement> buttons = DriverFactory.getDriver().findElements(deleteItemButtons);
            if (buttons.isEmpty()) {
                break;
            }

            int beforeCount = buttons.size();
            WebElement deleteButton = buttons.get(0);
            try {
                deleteButton.click();
            } catch (Exception clickError) {
                try {
                    ((JavascriptExecutor) DriverFactory.getDriver())
                            .executeScript("arguments[0].click();", deleteButton);
                } catch (Exception ignored) {
                    // If one delete action fails, continue and retry with fresh DOM.
                }
            }

            try {
                WaitUtils.shortWait()
                        .until(d -> d.findElements(deleteItemButtons).size() < beforeCount);
            } catch (Exception ignored) {
                // Continue; cart DOM can refresh asynchronously.
            }

            safetyCounter++;
        }
        return this;
    }

    public CheckoutPage proceedToCheckout() {
        WaitUtils.safeClickWithScrollAndJsFallback(proceedToCheckoutButton);
        WaitUtils.waitForElementVisible(pageBody);
        return new CheckoutPage();
    }
}
