package pages;

import config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverFactory;
import utils.WaitUtils;
import java.util.List;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CheckoutPage {

    private final By pageBody = By.tagName("body");
    private final By shipToAddressButton = By.cssSelector(
            "input[name='shipToThisAddress'], input[data-testid='Address_selectShipToThisAddress']"
    );
    private final By useAddressButtonByText = By.xpath(
            "//input[contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'use this address')] | " +
            "//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'use this address')]"
    );
    private final By addressRequiredMarkers = By.xpath(
            "//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add a delivery address') " +
            "or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'enter shipping address')]"
    );
    private final By zeroAddressPanel = By.cssSelector(
            "#checkout-delivery-address-panel.expanded, #shipping-address-selection-panel-card-id"
    );
    private final By addNewAddressModalTrigger = By.cssSelector(
            "span[data-action='checkout-view-modal'][data-checkout-view-modal*='ADD_NEW_SHIPPING_ADDRESS']"
    );
    private final By addNewAddressButton = By.xpath(
            "//a[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add a new address')] | " +
            "//a[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add a new delivery address')] | " +
            "//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add a new address')] | " +
            "//input[contains(@value,'Add a new address')]"
    );
    private final By addNewAddressExactSlotButton = By.cssSelector(
            "a[data-csa-c-slot-id='add-new-address-non-mobile-tango-sasp-zero-address']"
    );
    private final By addNewAddressButtonFallback = By.cssSelector(
            "[id*='add-new-address'], [data-testid*='add-new-address'], a[href*='address'], button[name*='address']"
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
    private final By addressCityAreaInput = By.cssSelector(
            "input[name='address-ui-widgets-enterAddressCityArea'], input[name='address-ui-widgets-enterAddressCity'], input[name='cityArea']"
    );
    private final By addressCitySelect = By.cssSelector(
            "select[name='address-ui-widgets-enterAddressCity'], select[name='address-ui-widgets-enterAddressStateOrRegion']"
    );
    private final By addressCityInput = By.cssSelector(
            "input[name='address-ui-widgets-enterAddressCity'], input[name='address-ui-widgets-enterAddressStateOrRegion']"
    );
    private final By addressGovernorateSelect = By.cssSelector(
            "select[name='address-ui-widgets-enterAddressStateOrRegion'], select[name='address-ui-widgets-enterAddressGovernorate']"
    );
    private final By addressGovernorateInput = By.cssSelector(
            "input[name='address-ui-widgets-enterAddressStateOrRegion'], input[name='address-ui-widgets-enterAddressGovernorate']"
    );
    private final By useAddressButton = By.xpath(
            "//input[contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'use this address')] | " +
            "//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'use this address')] | " +
            "//input[contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'save address')]"
    );
    private final By useAddressPrimarySubmitButton = By.cssSelector(
            "#checkout-primary-continue-button-id input, " +
            "#checkout-primary-continue-button-id button, " +
            "input[name*='saveAddress'], input[name*='saveOriginalOrSuggestedAddress'], " +
            "input[id*='use-this-address'], button[id*='use-this-address'], " +
            ".a-modal-scroller input.a-button-input[type='submit'], .a-modal-scroller button[type='submit']"
    );
    private final By addressFormContainer = By.cssSelector(
            "form[action*='address'], #address-ui-widgets-enterAddressFormContainer, .a-modal-scroller"
    );
    private final By addAddressModalTitle = By.xpath(
            "//*[contains(normalize-space(),'Add an address')]"
    );
    private final By addressSuggestionConfirmButton = By.xpath(
            "//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'use this address')] | " +
            "//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'continue')]"
    );
    private final By districtInputFallbackLocator = By.cssSelector(
            "input[id*='District'], input[id*='district'], input[name*='District'], input[name*='district'], " +
            "input[aria-label*='District'], input[placeholder*='District'], input[aria-labelledby*='district']"
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
        String cityArea = ConfigReader.getProperty("amazon.address.cityArea");
        String district = ConfigReader.getProperty("amazon.address.district");
        String area = ConfigReader.getProperty("amazon.address.area");
        String governorate = ConfigReader.getProperty("amazon.address.governorate");
        String building = ConfigReader.getProperty("amazon.address.building");
        return addDeliveryAddressIfRequired(fullName, phone, line1, city, cityArea, district, area, governorate, building);
    }

    public boolean selectAddressIfPrompted() {
        if (!DriverFactory.getDriver().findElements(shipToAddressButton).isEmpty()) {
            WaitUtils.safeClickWithScrollAndJsFallback(shipToAddressButton);
            WaitUtils.waitForElementVisible(pageBody);
            return true;
        }

        if (!DriverFactory.getDriver().findElements(useAddressButtonByText).isEmpty()) {
            WaitUtils.safeClickWithScrollAndJsFallback(useAddressButtonByText);
            WaitUtils.waitForElementVisible(pageBody);
            return true;
        }

        if (isAddressStillRequired()) {
            System.out.println("Step 8 debug -> Checkout is still requesting delivery address before payment methods.");
            return false;
        }
        return true;
    }

    private boolean addDeliveryAddressIfRequired(String fullName, String phone, String line1,
                                                 String city, String cityArea, String district, String area,
                                                 String governorate, String building) {
        if (!isAddressStillRequired()) {
            return true;
        }

        if (isBlank(fullName) || isBlank(phone) || isBlank(line1) || isBlank(city)) {
            System.out.println("Step 8 debug -> Address is required but config fields are missing.");
            return false;
        }

        if (!hasAddressFormInputsVisible()) {
            clickAddNewAddressIfPresent();
            waitForAddressFormToOpen();
        }

        if (!hasAddressFormInputsVisible()) {
            logAddressActionCandidates();
            System.out.println("Step 8 debug -> Address form did not open after clicking add-new-address.");
            return false;
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
        setInputIfPresent(addressCityAreaInput, isBlank(cityArea) ? city : cityArea);
        selectOrTypeCity(city);
        selectOrTypeGovernorate(governorate);
        if (!fillDistrictAfterCitySelection(district, area)) {
            System.out.println("Step 8 debug -> Could not fill required district field.");
            return false;
        }

        if (!submitAddressForm()) {
            System.out.println("Step 8 debug -> Could not locate 'Use this address' submit button.");
            return false;
        }

        resolveAddressConfirmationIfPresent();
        boolean stillRequired = isAddressStillRequired();
        if (stillRequired) {
            System.out.println("Step 8 debug -> Address form submitted but checkout still requests address.");
            return false;
        }

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

    private void selectOrTypeGovernorate(String governorate) {
        if (isBlank(governorate)) {
            return;
        }
        if (!DriverFactory.getDriver().findElements(addressGovernorateSelect).isEmpty()) {
            try {
                Select select = new Select(DriverFactory.getDriver().findElement(addressGovernorateSelect));
                select.selectByVisibleText(governorate);
                return;
            } catch (Exception ignored) {
                // fall through to type/input fallback
            }
        }
        setInputIfPresent(addressGovernorateInput, governorate);
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

    private void clickAddNewAddressIfPresent() {
        if (clickFirstVisible(addNewAddressModalTrigger)) {
            return;
        }
        if (clickFirstVisible(addNewAddressExactSlotButton)) {
            return;
        }
        if (clickFirstVisible(addNewAddressButton)) {
            return;
        }
        clickFirstVisible(addNewAddressButtonFallback);
    }

    private void waitForAddressFormToOpen() {
        try {
            new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(20))
                    .until(d -> !d.findElements(addressFullNameInput).isEmpty()
                            || !d.findElements(addressPhoneInput).isEmpty()
                            || !d.findElements(addressLine1Input).isEmpty()
                            || !d.findElements(addAddressModalTitle).isEmpty());
        } catch (Exception ignored) {
            // If form doesn't appear, caller will fail with explicit debug reason.
        }
    }

    private boolean hasAddressFormInputsVisible() {
        return hasAnyDisplayed(addressFullNameInput)
                || hasAnyDisplayed(addressPhoneInput)
                || hasAnyDisplayed(addressLine1Input);
    }

    private boolean isAddressStillRequired() {
        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        if (currentUrl != null) {
            String lowered = currentUrl.toLowerCase();
            if (lowered.contains("/pay?") || lowered.contains("/pay/") || lowered.contains("pipeline-type=chewbacca")) {
                return false;
            }
        }

        if (hasAnyDisplayed(zeroAddressPanel)
                || hasAnyDisplayed(addNewAddressExactSlotButton)
                || hasAnyDisplayed(addNewAddressModalTrigger)) {
            return true;
        }
        if (hasAnyDisplayed(addressFormContainer) && hasAddressFormInputsVisible()) {
            return true;
        }
        List<WebElement> markers = DriverFactory.getDriver().findElements(addressRequiredMarkers);
        for (WebElement marker : markers) {
            try {
                if (marker.isDisplayed()) {
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    private boolean hasAnyDisplayed(By locator) {
        List<WebElement> elements = DriverFactory.getDriver().findElements(locator);
        for (WebElement element : elements) {
            try {
                if (element.isDisplayed()) {
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    private void resolveAddressConfirmationIfPresent() {
        if (!DriverFactory.getDriver().findElements(addressSuggestionConfirmButton).isEmpty()) {
            try {
                WaitUtils.safeClickWithScrollAndJsFallback(addressSuggestionConfirmButton);
                WaitUtils.waitForElementVisible(pageBody);
            } catch (Exception ignored) {
            }
        }
    }

    private boolean clickFirstVisible(By locator) {
        List<WebElement> candidates = DriverFactory.getDriver().findElements(locator);
        if (candidates.isEmpty()) {
            return false;
        }
        for (WebElement target : candidates) {
            try {
                ((JavascriptExecutor) DriverFactory.getDriver())
                        .executeScript("arguments[0].scrollIntoView({block:'center'});", target);
                if (target.isDisplayed() && target.isEnabled()) {
                    target.click();
                } else {
                    ((JavascriptExecutor) DriverFactory.getDriver())
                            .executeScript("arguments[0].click();", target);
                }
                WaitUtils.waitForElementVisible(pageBody);
                return true;
            } catch (Exception clickFailure) {
                try {
                    ((JavascriptExecutor) DriverFactory.getDriver())
                            .executeScript("arguments[0].click();", target);
                    WaitUtils.waitForElementVisible(pageBody);
                    return true;
                } catch (Exception jsFailure) {
                    // try next matching element
                }
            }
        }
        return false;
    }

    private void logAddressActionCandidates() {
        List<WebElement> nodes = DriverFactory.getDriver().findElements(
                By.xpath("//a|//button|//input[@type='button' or @type='submit']")
        );
        List<String> texts = nodes.stream()
                .map(e -> {
                    String t = e.getText();
                    if (t == null || t.isBlank()) {
                        t = e.getAttribute("value");
                    }
                    return t == null ? "" : t.trim();
                })
                .filter(t -> !t.isBlank())
                .distinct()
                .limit(15)
                .collect(Collectors.toList());
        System.out.println("Step 8 debug -> Address action candidates on page: " + texts);
    }

    private boolean fillDistrictAfterCitySelection(String district, String area) {
        String resolvedDistrict = resolveDistrictValue(district, area);
        if (isBlank(resolvedDistrict)) {
            return false;
        }
        List<String> candidates = new ArrayList<>();
        candidates.add(resolvedDistrict);

        By districtInputLocator = By.cssSelector(
                "input[name='address-ui-widgets-enterAddressDistrictOrCounty'], " +
                "input[name='address-ui-widgets-enterAddressNeighborhood'], " +
                "input[name='address-ui-widgets-enterAddressCityArea'], " +
                "input[name*='district'], input[name*='neighborhood'], input[name*='cityArea']"
        );
        WebElement districtField = resolveDistrictInputField(districtInputLocator);
        if (districtField == null) {
            return false;
        }

        boolean filled = false;
        for (String candidate : candidates) {
            if (isBlank(candidate)) {
                continue;
            }
            if (typeDistrictAndSelectSuggestion(districtField, candidate)) {
                filled = true;
                break;
            }
            if (setDistrictWithJsFallback(districtField, candidate)) {
                filled = true;
                break;
            }
        }
        return filled && (isDistrictFieldFilled(districtField) || isDistrictFilled());
    }

    private boolean isDistrictFilled() {
        By districtByName = By.cssSelector(
                "input[name='address-ui-widgets-enterAddressDistrictOrCounty'], " +
                "input[name='address-ui-widgets-enterAddressNeighborhood'], " +
                "input[name='address-ui-widgets-enterAddressCityArea'], " +
                "input[name*='district'], input[name*='neighborhood'], input[name*='cityArea']"
        );
        List<WebElement> fields = DriverFactory.getDriver().findElements(districtByName);
        for (WebElement field : fields) {
            try {
                String value = field.getAttribute("value");
                if (value != null && !value.trim().isEmpty()) {
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    private String resolveDistrictValue(String district, String area) {
        String value = isBlank(district) ? area : district;
        if (isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    private WebElement resolveDistrictInputField(By districtInputLocator) {
        List<WebElement> byName = DriverFactory.getDriver().findElements(districtInputLocator);
        for (WebElement candidate : byName) {
            try {
                if (candidate.isDisplayed() && candidate.isEnabled()) {
                    return candidate;
                }
            } catch (Exception ignored) {
            }
        }

        List<WebElement> byFallback = DriverFactory.getDriver().findElements(districtInputFallbackLocator);
        for (WebElement candidate : byFallback) {
            try {
                if (candidate.isDisplayed() && candidate.isEnabled()) {
                    return candidate;
                }
            } catch (Exception ignored) {
            }
        }

        List<By> byLabelLocators = Arrays.asList(
                By.xpath("//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'district')]/following::input[1]"),
                By.xpath("//label[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'district')]/following::input[1]")
        );
        for (By locator : byLabelLocators) {
            List<WebElement> byLabel = DriverFactory.getDriver().findElements(locator);
            for (WebElement candidate : byLabel) {
                try {
                    if (candidate.isDisplayed() && candidate.isEnabled()) {
                        return candidate;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    private boolean typeDistrictAndSelectSuggestion(WebElement districtField, String value) {
        try {
            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", districtField);
            districtField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            districtField.sendKeys(Keys.DELETE);
            districtField.sendKeys(value);
            dispatchInputEvents(districtField);
            waitForDistrictSuggestionsAndPickFirst(districtField);
            dispatchInputEvents(districtField);
            districtField.sendKeys(Keys.TAB);
            dispatchInputEvents(districtField);
            return isDistrictFieldFilled(districtField);
        } catch (Exception ignored) {
            return false;
        }
    }

    private void waitForDistrictSuggestionsAndPickFirst(WebElement districtField) {
        By suggestionOptions = By.cssSelector(
                "ul[role='listbox'] li, [role='option'], .a-popover [data-value], .autocomplete-results-container li"
        );
        try {
            new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(3))
                    .until(d -> !d.findElements(suggestionOptions).isEmpty());
            List<WebElement> options = DriverFactory.getDriver().findElements(suggestionOptions);
            if (!options.isEmpty()) {
                try {
                    options.get(0).click();
                    return;
                } catch (Exception ignored) {
                    // fallback to keyboard selection
                }
            }
        } catch (Exception ignored) {
            // no suggestion list shown; continue with keyboard fallback
        }
        districtField.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
    }

    private void dispatchInputEvents(WebElement field) {
        try {
            ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                    "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                    "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));" +
                    "arguments[0].dispatchEvent(new Event('blur', {bubbles:true}));", field);
        } catch (Exception ignored) {
        }
    }

    private boolean submitAddressForm() {
        if (clickFirstVisible(useAddressButton)) {
            return true;
        }
        if (clickFirstVisible(useAddressButtonByText)) {
            return true;
        }
        if (clickFirstVisible(useAddressPrimarySubmitButton)) {
            return true;
        }

        try {
            Object clicked = ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                    "var selectors=[" +
                            "'#checkout-primary-continue-button-id input'," +
                            "'#checkout-primary-continue-button-id button'," +
                            "'.a-modal-scroller input.a-button-input[type=\"submit\"]'," +
                            "'.a-modal-scroller button[type=\"submit\"]'," +
                            "'form[action*=\"address\"] input[type=\"submit\"]'," +
                            "'form[action*=\"address\"] button[type=\"submit\"]'];" +
                            "for (var i=0;i<selectors.length;i++){" +
                            "  var nodes=document.querySelectorAll(selectors[i]);" +
                            "  for (var j=0;j<nodes.length;j++){" +
                            "    var el=nodes[j];" +
                            "    var rect=el.getBoundingClientRect();" +
                            "    if(rect.width>0 && rect.height>0){" +
                            "      el.click();" +
                            "      return true;" +
                            "    }" +
                            "  }" +
                            "}" +
                            "var forms=document.querySelectorAll('form[action*=\"address\"], form');" +
                            "for (var k=0;k<forms.length;k++){" +
                            "  try{ forms[k].requestSubmit ? forms[k].requestSubmit() : forms[k].submit(); return true; }catch(e){}" +
                            "}" +
                            "return false;");
            WaitUtils.waitForElementVisible(pageBody);
            return Boolean.TRUE.equals(clicked);
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean setDistrictWithJsFallback(WebElement field, String value) {
        try {
            ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                    "arguments[0].value = arguments[1];" +
                    "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                    "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));" +
                    "arguments[0].dispatchEvent(new Event('blur', {bubbles:true}));",
                    field, value);
            field.sendKeys(Keys.TAB);
            return isDistrictFieldFilled(field);
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean isDistrictFieldFilled(WebElement field) {
        try {
            String value = field.getAttribute("value");
            if (value != null && !value.trim().isEmpty()) {
                return true;
            }
            String text = field.getText();
            return text != null && !text.trim().isEmpty();
        } catch (Exception ignored) {
            return false;
        }
    }

}
