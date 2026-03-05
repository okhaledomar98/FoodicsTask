package pages;

import config.ConfigReader;
import pages.components.AddressFormComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.DriverFactory;
import utils.JavaScriptActions;
import utils.WaitUtils;
import java.util.List;
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
    private final AddressFormComponent addressForm = new AddressFormComponent();

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

        if (!addressForm.hasFormInputsVisible()) {
            clickAddNewAddressIfPresent();
            waitForAddressFormToOpen();
        }

        if (!addressForm.hasFormInputsVisible()) {
            logAddressActionCandidates();
            System.out.println("Step 8 debug -> Address form did not open after clicking add-new-address.");
            return false;
        }

        boolean submitted = addressForm.fillAndSubmit(new AddressFormComponent.AddressData(
                fullName, phone, line1, city, cityArea, district, area, governorate, building
        ));
        if (!submitted) {
            return false;
        }

        addressForm.resolveAddressConfirmationIfPresent();
        boolean stillRequired = isAddressStillRequired();
        if (stillRequired) {
            System.out.println("Step 8 debug -> Address form submitted but checkout still requests address.");
            return false;
        }

        return true;
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
        addressForm.waitForFormToOpen();
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
        if (addressForm.isFormContainerVisible() && addressForm.hasFormInputsVisible()) {
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

}
