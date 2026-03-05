package uiTests;

import base.BaseUITest;
import config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.HomePage;
import pages.LoginPage;
import pages.MenuPage;
import pages.ProductsPage;
import utils.DriverFactory;
import java.util.List;
import java.time.Duration;

public class AmazonScenarioTest extends BaseUITest {

    @Test(description = "Task Step 1: Open Amazon and Login successfully")
    public void testAmazonLogin() {


        String phone = ConfigReader.getProperty("amazon.phone");
        String password = ConfigReader.getProperty("amazon.password");


        HomePage homePage = new HomePage();
        LoginPage loginPage = homePage.clickLogin();
        loginPage.loginToAmazon(phone, password);
        boolean languageSet = homePage.ensureEnglishLanguage();
        Assert.assertTrue(languageSet, "Failed to switch Amazon UI language to English.");
        System.out.println("✅ Login steps executed successfully!");
    }

    @Test(description = "Task Steps 1-3: Login and Navigate to Video Games")
    public void testAmazonScenario() {
        String phone = ConfigReader.getProperty("amazon.phone");
        String password = ConfigReader.getProperty("amazon.password");

        HomePage homePage = new HomePage();
        MenuPage menuPage = new MenuPage();
        ProductsPage productsPage = new ProductsPage();
        CartPage cartPage = new CartPage();

        homePage.clickLogin().loginToAmazon(phone, password);
        boolean languageSet = homePage.ensureEnglishLanguage();
        Assert.assertTrue(languageSet, "Failed to switch Amazon UI language to English.");
        cartPage.clearCartIfNotEmpty();
        driver.get(ConfigReader.getProperty("ui.amazon.url"));
        homePage.openAllMenu();
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("hmenu-content")));
        menuPage.clickSeeAll().selectVideoGames().clickAllVideoGames();
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(d ->
                d.getCurrentUrl().contains("node=18022560031")
                        || !d.findElements(By.id("s-refinements")).isEmpty()
        );
        System.out.println("Now at URL: " + driver.getCurrentUrl());
        System.out.println("✅ Navigated to All Video Games successfully!");


        productsPage.applyFreeShipping()
                .applyNewCondition()
                .sortHighToLow();
        System.out.println("✅ Filters (Free Shipping, New) and Sorting (High to Low) applied successfully!");

        List<String> addedTitles = productsPage.addFirstProductBelowPriceAndStop(15000, 50);
        int addedItems = addedTitles.size();
        System.out.println("✅ Step 6 completed. Items added below 15k EGP: " + addedItems);

        Assert.assertTrue(addedItems > 0,
                "Step 6 failed: no products below 15k EGP were added to cart.");

        cartPage.openCart();
        boolean cartCountValid = cartPage.hasAtLeastItemCount(addedItems);
        Assert.assertTrue(cartCountValid,
                "Step 7 failed: cart item count is less than the number of added products.");
        System.out.println("✅ Step 7 completed. All added products were verified in cart.");

        CheckoutPage checkoutPage = cartPage.proceedToCheckout().waitForCheckoutLoad();
        boolean addressAdded = checkoutPage.addDeliveryAddressIfRequiredFromConfig();
        Assert.assertTrue(addressAdded, "Step 8 failed: delivery address is required and could not be added.");
        boolean addressHandled = checkoutPage.selectAddressIfPrompted();
        Assert.assertTrue(addressHandled, "Step 8 failed: address selection step could not be completed.");

        String checkoutUrl = driver.getCurrentUrl();
        Assert.assertTrue(checkoutUrl.contains("/checkout/"),
                "Step 8 failed: did not remain on checkout flow after address handling.");

        System.out.println("✅ Address added and reached checkout successfully.");
        System.out.println("✅ Test ended by design at checkout payment page.");
        DriverFactory.quitDriver();
        return;
    }
}