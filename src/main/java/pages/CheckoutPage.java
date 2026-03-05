package pages;

import config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverFactory;
import utils.WaitUtils;
import java.util.List;
import java.util.Locale;
import java.time.Duration;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckoutPage {

    private final By pageBody = By.tagName("body");
    private final By shipToAddressButton = By.cssSelector(
            "input[name='shipToThisAddress'], input[data-testid='Address_selectShipToThisAddress']"
    );
    private final By useAddressButtonByText = By.xpath(
            "//input[contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'use this address')] | " +
            "//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'use this address')] | " +
            "//span[contains(normalize-space(.),'\u0627\u0633\u062a\u062e\u062f\u0645 \u0647\u0630\u0627 \u0627\u0644\u0639\u0646\u0648\u0627\u0646')]"
    );
    private final By addressRequiredMarkers = By.xpath(
            "//*[contains(normalize-space(.),'\u0625\u0636\u0627\u0641\u0629 \u0639\u0646\u0648\u0627\u0646 \u0627\u0644\u062a\u0648\u0635\u064a\u0644') " +
            "or contains(normalize-space(.),'\u0623\u062f\u062e\u0644 \u0639\u0646\u0648\u0627\u0646 \u0627\u0644\u062a\u0648\u0635\u064a\u0644') " +
            "or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add a delivery address') " +
            "or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'enter shipping address')]"
    );
    private final By useThisPaymentMethodButton = By.cssSelector(
            "input[name='ppw-widgetEvent:SetPaymentPlanSelectContinueEvent'], input[name='continue-top']"
    );
    private final By cashOnDeliveryOption = By.xpath(
            "//input[contains(translate(@value,'cod','COD'),'COD') or contains(translate(@id,'cod','COD'),'COD') or " +
            "contains(translate(@name,'cod','COD'),'COD') or contains(translate(@aria-label,'cod','COD'),'COD')] | " +
            "//label[contains(.,'Cash on Delivery') or contains(.,'\u0627\u0644\u062f\u0641\u0639 \u0639\u0646\u062f \u0627\u0644\u0627\u0633\u062a\u0644\u0627\u0645') or contains(.,'\u0627\u0644\u062f\u0641\u0639 \u0643\u0627\u0634')] | " +
            "//span[contains(normalize-space(),'Cash on Delivery') or contains(normalize-space(),'\u0627\u0644\u062f\u0641\u0639 \u0639\u0646\u062f \u0627\u0644\u0627\u0633\u062a\u0644\u0627\u0645') or contains(normalize-space(),'\u0627\u0644\u062f\u0641\u0639 \u0643\u0627\u0634')]"
    );
    private final By paymentMethodsBlocks = By.xpath(
            "//*[@id='payment-methods-container']//*[self::label or self::span or self::div]"
    );
    private final By paymentSectionContainer = By.cssSelector(
            "#payment-methods-container, #existing-payment-methods, form[name='payselect-form']"
    );
    private final By addNewAddressButton = By.xpath(
            "//a[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add a new address')] | " +
            "//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add a new address')] | " +
            "//span[contains(normalize-space(.),'\u0625\u0636\u0627\u0641\u0629 \u0639\u0646\u0648\u0627\u0646 \u062c\u062f\u064a\u062f')]"
    );
    private final By addressFullNameInput = By.cssSelector(
            "input[name='address-ui-widgets-enterAddressFullName'], input[name='address-ui-widgets-enterAddressFullNameOrPhoneNumber']"
    );
    private final By addressPhoneInput = By.cssSelector(
            "input[name='address-ui-widgets-enterAddressPhoneNumber'], input[name='address-ui-widgets-enterAddressPhoneNumberOrEmail']"
    );
    private final By addressLine1Input = By.cssSelector(
            "input[name='address-ui-widgets-enterAddressLine1'], input[name='address-ui-widgets-enterAddressLineOne']"
    );
    private final By addressLine2Input = By.cssSelector(
            "input[name='address-ui-widgets-enterAddressLine2'], input[name='address-ui-widgets-enterAddressBuildingNumberOrName']"
    );
    private final By addressAreaInput = By.cssSelector(
            "input[name='address-ui-widgets-enterAddressDistrictOrCounty'], input[name='address-ui-widgets-enterAddressNeighborhood']"
    );
    private final By addressCitySelect = By.cssSelector(
            "select[name='address-ui-widgets-enterAddressCity'], select[name='address-ui-widgets-enterAddressStateOrRegion']"
    );
    private final By addressCityInput = By.cssSelector(
            "input[name='address-ui-widgets-enterAddressCity'], input[name='address-ui-widgets-enterAddressStateOrRegion']"
    );
    private final By useAddressButton = By.xpath(
            "//input[contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'use this address')] | " +
            "//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'use this address')] | " +
            "//span[contains(normalize-space(.),'\u0627\u0633\u062a\u062e\u062f\u0645 \u0647\u0630\u0627 \u0627\u0644\u0639\u0646\u0648\u0627\u0646')]"
    );

    public CheckoutPage waitForCheckoutLoad() {
        WaitUtils.waitForElementVisible(pageBody);
        return this;
    }

    public boolean addDeliveryAddressIfRequiredFromConfig() {
        String fullName = ConfigReader.getProperty("amazon.address.fullName");
        String phone = ConfigReader.getProperty("amazon.address.phone");
        String line1 = ConfigReader.getProperty("amazon.address.line1");
        String city = ConfigReader.getProperty("amazon.address.city");
        String area = ConfigReader.getProperty("amazon.address.area");
        String building = ConfigReader.getProperty("amazon.address.building");
        return addDeliveryAddressIfRequired(fullName, phone, line1, city, area, building);
    }

    public boolean selectAddressIfPrompted() {
        if (!DriverFactory.getDriver().findElements(shipToAddressButton).isEmpty()) {
            WaitUtils.safeClickWithScrollAndJsFallback(shipToAddressButton);
            WaitUtils.waitForElementVisible(pageBody);
            waitForPaymentMethodsToLoad();
            return true;
        }

        if (!DriverFactory.getDriver().findElements(useAddressButtonByText).isEmpty()) {
            WaitUtils.safeClickWithScrollAndJsFallback(useAddressButtonByText);
            WaitUtils.waitForElementVisible(pageBody);
            waitForPaymentMethodsToLoad();
            return true;
        }

        if (!DriverFactory.getDriver().findElements(addressRequiredMarkers).isEmpty()) {
            System.out.println("Step 8 debug -> Checkout is still requesting delivery address before payment methods.");
            return false;
        }
        return true;
    }

    public boolean selectCashOnDeliveryIfAvailable() {
        waitForPaymentMethodsToLoad();
        if (!DriverFactory.getDriver().findElements(addressRequiredMarkers).isEmpty()) {
            System.out.println("Step 8 debug -> COD check skipped because address is still required.");
            return false;
        }
        List<WebElement> cashOptions = DriverFactory.getDriver().findElements(cashOnDeliveryOption);
        if (cashOptions.isEmpty()) {
            logAvailablePaymentMethods();
            return false;
        }
        try {
            cashOptions.get(0).click();
        } catch (Exception e) {
            WaitUtils.safeClickWithScrollAndJsFallback(cashOnDeliveryOption);
        }

        if (!DriverFactory.getDriver().findElements(useThisPaymentMethodButton).isEmpty()) {
            WaitUtils.safeClickWithScrollAndJsFallback(useThisPaymentMethodButton);
            WaitUtils.waitForElementVisible(pageBody);
        }
        return true;
    }

    private void waitForPaymentMethodsToLoad() {
        try {
            new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(20)).until(d -> {
                boolean containerVisible = !d.findElements(paymentSectionContainer).isEmpty();
                boolean methodNodesVisible = !d.findElements(paymentMethodsBlocks).isEmpty();
                boolean codVisible = !d.findElements(cashOnDeliveryOption).isEmpty();
                return codVisible || (containerVisible && methodNodesVisible);
            });
        } catch (Exception ignored) {
            // Fall through and attempt detection anyway; caller handles missing COD option.
        }
    }

    private void logAvailablePaymentMethods() {
        List<WebElement> nodes = DriverFactory.getDriver().findElements(paymentMethodsBlocks);
        List<String> texts = nodes.stream()
                .map(WebElement::getText)
                .filter(t -> t != null && !t.isBlank())
                .map(String::trim)
                .distinct()
                .limit(12)
                .collect(Collectors.toList());
        boolean addressStillRequired = !DriverFactory.getDriver().findElements(addressRequiredMarkers).isEmpty();
        System.out.println("Step 8 debug -> COD not found. addressRequired=" + addressStillRequired
                + ", payment text snippets: " + texts);
    }

    private boolean addDeliveryAddressIfRequired(String fullName, String phone, String line1,
                                                 String city, String area, String building) {
        if (DriverFactory.getDriver().findElements(addressRequiredMarkers).isEmpty()) {
            return true;
        }

        if (isBlank(fullName) || isBlank(phone) || isBlank(line1) || isBlank(city)) {
            System.out.println("Step 8 debug -> Address is required but config fields are missing.");
            return false;
        }

        if (!DriverFactory.getDriver().findElements(addNewAddressButton).isEmpty()) {
            WaitUtils.safeClickWithScrollAndJsFallback(addNewAddressButton);
            WaitUtils.waitForElementVisible(pageBody);
        }

        if (!setInputIfPresent(addressFullNameInput, fullName)) {
            System.out.println("Step 8 debug -> Could not locate full-name field in address form.");
            return false;
        }
        if (!setInputIfPresent(addressPhoneInput, phone)) {
            System.out.println("Step 8 debug -> Could not locate phone field in address form.");
            return false;
        }
        if (!setInputIfPresent(addressLine1Input, line1)) {
            System.out.println("Step 8 debug -> Could not locate address-line field in address form.");
            return false;
        }
        setInputIfPresent(addressLine2Input, building);
        setInputIfPresent(addressAreaInput, area);
        selectOrTypeCity(city);

        if (!DriverFactory.getDriver().findElements(useAddressButton).isEmpty()) {
            WaitUtils.safeClickWithScrollAndJsFallback(useAddressButton);
            WaitUtils.waitForElementVisible(pageBody);
        } else if (!DriverFactory.getDriver().findElements(useAddressButtonByText).isEmpty()) {
            WaitUtils.safeClickWithScrollAndJsFallback(useAddressButtonByText);
            WaitUtils.waitForElementVisible(pageBody);
        } else {
            System.out.println("Step 8 debug -> Could not locate 'Use this address' submit button.");
            return false;
        }

        boolean stillRequired = !DriverFactory.getDriver().findElements(addressRequiredMarkers).isEmpty();
        if (stillRequired) {
            System.out.println("Step 8 debug -> Address form submitted but checkout still requests address.");
            return false;
        }

        waitForPaymentMethodsToLoad();
        return true;
    }

    private void selectOrTypeCity(String city) {
        if (isBlank(city)) {
            return;
        }
        if (!DriverFactory.getDriver().findElements(addressCitySelect).isEmpty()) {
            try {
                Select select = new Select(DriverFactory.getDriver().findElement(addressCitySelect));
                select.selectByVisibleText(city);
                return;
            } catch (Exception ignored) {
                // fall through to type/input fallback
            }
        }
        setInputIfPresent(addressCityInput, city);
    }

    private boolean setInputIfPresent(By locator, String value) {
        if (isBlank(value) || DriverFactory.getDriver().findElements(locator).isEmpty()) {
            return false;
        }
        try {
            WebElement field = DriverFactory.getDriver().findElement(locator);
            field.clear();
            field.sendKeys(value);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public boolean isOrderTotalConsistentWithShipping() {
        Double subtotal = readAmountByLabel(
                "subtotal", "items:", "\u0627\u0644\u0645\u062c\u0645\u0648\u0639 \u0627\u0644\u0641\u0631\u0639\u064a", "\u0625\u062c\u0645\u0627\u0644\u064a \u0627\u0644\u0633\u0644\u0639");
        Double shipping = readAmountByLabel(
                "shipping", "delivery", "\u0627\u0644\u0634\u062d\u0646", "\u0631\u0633\u0648\u0645 \u0627\u0644\u0634\u062d\u0646");
        Double orderTotal = readAmountByLabel(
                "order total", "total before tax", "\u0627\u0644\u0625\u062c\u0645\u0627\u0644\u064a", "\u0625\u062c\u0645\u0627\u0644\u064a \u0627\u0644\u0637\u0644\u0628");

        if (subtotal == null || orderTotal == null) {
            return false;
        }

        double shippingValue = (shipping == null) ? 0.0 : shipping;
        double expected = subtotal + shippingValue;
        return Math.abs(orderTotal - expected) <= 1.0;
    }

    private Double readAmountByLabel(String... labels) {
        String pageText = DriverFactory.getDriver().findElement(pageBody).getText().toLowerCase(Locale.ROOT);
        for (String label : labels) {
            String lowered = label.toLowerCase(Locale.ROOT);
            int idx = pageText.indexOf(lowered);
            if (idx < 0) {
                continue;
            }
            int end = Math.min(pageText.length(), idx + 220);
            String snippet = pageText.substring(idx, end);
            Double amount = extractFirstAmount(snippet);
            if (amount != null) {
                return amount;
            }
        }

        try {
            WebElement value = DriverFactory.getDriver().findElement(
                    By.xpath("//*[contains(@class,'grand-total-price') or contains(@class,'a-color-price')]"));
            return extractFirstAmount(value.getText());
        } catch (NoSuchElementException ignored) {
            return null;
        }
    }

    private Double extractFirstAmount(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        String normalized = normalizeArabicDigits(text).replace(",", "");
        Matcher matcher = Pattern.compile("(\\d+(?:\\.\\d{1,2})?)").matcher(normalized);
        if (!matcher.find()) {
            return null;
        }
        try {
            return Double.parseDouble(matcher.group(1));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String normalizeArabicDigits(String value) {
        return value
                .replace('\u0660', '0')
                .replace('\u0661', '1')
                .replace('\u0662', '2')
                .replace('\u0663', '3')
                .replace('\u0664', '4')
                .replace('\u0665', '5')
                .replace('\u0666', '6')
                .replace('\u0667', '7')
                .replace('\u0668', '8')
                .replace('\u0669', '9');
    }
}
