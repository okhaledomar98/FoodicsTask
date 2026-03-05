package pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.DriverFactory;
import utils.JavaScriptActions;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddressFormComponent {
    private final By pageBody = By.tagName("body");
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
    private final By useAddressButtonByText = By.xpath(
            "//input[contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'use this address')] | " +
                    "//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'use this address')]"
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

    public boolean hasFormInputsVisible() {
        return hasAnyDisplayed(addressFullNameInput)
                || hasAnyDisplayed(addressPhoneInput)
                || hasAnyDisplayed(addressLine1Input);
    }

    public boolean isFormContainerVisible() {
        return hasAnyDisplayed(addressFormContainer);
    }

    public void waitForFormToOpen() {
        try {
            WaitUtils.longWait().until(d -> !d.findElements(addressFullNameInput).isEmpty()
                    || !d.findElements(addressPhoneInput).isEmpty()
                    || !d.findElements(addressLine1Input).isEmpty()
                    || !d.findElements(addAddressModalTitle).isEmpty());
        } catch (Exception ignored) {
        }
    }

    public boolean fillAndSubmit(AddressData data) {
        if (isBlank(data.fullName) || isBlank(data.phone) || isBlank(data.line1) || isBlank(data.city)) {
            System.out.println("Step 8 debug -> Address is required but config fields are missing.");
            return false;
        }

        if (!setInputIfPresent(addressFullNameInput, data.fullName)) {
            System.out.println("Step 8 debug -> Could not locate full-name field in address form.");
            return false;
        }
        if (!setInputIfPresent(addressPhoneInput, data.phone)) {
            System.out.println("Step 8 debug -> Could not locate phone field in address form.");
            return false;
        }
        if (!setInputIfPresent(addressLine1Input, data.line1)) {
            System.out.println("Step 8 debug -> Could not locate address-line field in address form.");
            return false;
        }

        setInputIfPresent(addressLine2Input, data.building);
        setInputIfPresent(addressAreaInput, data.area);
        setInputIfPresent(addressCityAreaInput, isBlank(data.cityArea) ? data.city : data.cityArea);
        selectOrTypeCity(data.city);
        selectOrTypeGovernorate(data.governorate);
        if (!fillDistrictAfterCitySelection(data.district, data.area)) {
            System.out.println("Step 8 debug -> Could not fill required district field.");
            return false;
        }

        if (!submitAddressForm()) {
            System.out.println("Step 8 debug -> Could not locate 'Use this address' submit button.");
            return false;
        }
        return true;
    }

    public void resolveAddressConfirmationIfPresent() {
        if (!DriverFactory.getDriver().findElements(addressSuggestionConfirmButton).isEmpty()) {
            try {
                WaitUtils.safeClickWithScrollAndJsFallback(addressSuggestionConfirmButton);
                WaitUtils.waitForElementVisible(pageBody);
            } catch (Exception ignored) {
            }
        }
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
            JavaScriptActions.scrollIntoViewCenter(districtField);
            districtField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            districtField.sendKeys(Keys.DELETE);
            districtField.sendKeys(value);
            JavaScriptActions.dispatchInputChangeBlur(districtField);
            waitForDistrictSuggestionsAndPickFirst(districtField);
            JavaScriptActions.dispatchInputChangeBlur(districtField);
            districtField.sendKeys(Keys.TAB);
            JavaScriptActions.dispatchInputChangeBlur(districtField);
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
            WaitUtils.shortWait().until(d -> !d.findElements(suggestionOptions).isEmpty());
            List<WebElement> options = DriverFactory.getDriver().findElements(suggestionOptions);
            if (!options.isEmpty()) {
                try {
                    options.get(0).click();
                    return;
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        districtField.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
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
            boolean clicked = JavaScriptActions.clickFirstVisibleBySelectors(
                    "#checkout-primary-continue-button-id input",
                    "#checkout-primary-continue-button-id button",
                    ".a-modal-scroller input.a-button-input[type='submit']",
                    ".a-modal-scroller button[type='submit']",
                    "form[action*='address'] input[type='submit']",
                    "form[action*='address'] button[type='submit']"
            );
            if (!clicked) {
                clicked = JavaScriptActions.submitFirstMatchingForm("form[action*='address'], form");
            }
            WaitUtils.waitForElementVisible(pageBody);
            return clicked;
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean setDistrictWithJsFallback(WebElement field, String value) {
        try {
            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript("arguments[0].value = arguments[1];", field, value);
            JavaScriptActions.dispatchInputChangeBlur(field);
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

    private boolean clickFirstVisible(By locator) {
        List<WebElement> candidates = DriverFactory.getDriver().findElements(locator);
        if (candidates.isEmpty()) {
            return false;
        }
        for (WebElement target : candidates) {
            try {
                JavaScriptActions.scrollIntoViewCenter(target);
                if (target.isDisplayed() && target.isEnabled()) {
                    target.click();
                } else {
                    JavaScriptActions.click(target);
                }
                WaitUtils.waitForElementVisible(pageBody);
                return true;
            } catch (Exception clickFailure) {
                try {
                    JavaScriptActions.click(target);
                    WaitUtils.waitForElementVisible(pageBody);
                    return true;
                } catch (Exception ignored) {
                }
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

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static class AddressData {
        public final String fullName;
        public final String phone;
        public final String line1;
        public final String city;
        public final String cityArea;
        public final String district;
        public final String area;
        public final String governorate;
        public final String building;

        public AddressData(String fullName, String phone, String line1, String city, String cityArea,
                           String district, String area, String governorate, String building) {
            this.fullName = fullName;
            this.phone = phone;
            this.line1 = line1;
            this.city = city;
            this.cityArea = cityArea;
            this.district = district;
            this.area = area;
            this.governorate = governorate;
            this.building = building;
        }
    }
}
