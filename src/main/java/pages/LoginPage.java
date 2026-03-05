package pages;

import org.openqa.selenium.By;
import utils.WaitUtils;

public class LoginPage {


    private final By phoneOrEmailInput = By.id("ap_email_login");
    private final By continueButton = By.xpath("//span[@id='continue']//input");


    private final By passwordInput = By.id("ap_password");
    private final By signInSubmitButton = By.id("signInSubmit");





    public LoginPage enterPhoneNumber(String phone) {
        WaitUtils.typeText(phoneOrEmailInput, phone);
        WaitUtils.clickElement(continueButton);
        return this;
    }


    public void enterPasswordAndSubmit(String password) {
        WaitUtils.typeText(passwordInput, password);
        WaitUtils.clickElement(signInSubmitButton);
    }


    public void loginToAmazon(String phone, String password) {
        enterPhoneNumber(phone);
        enterPasswordAndSubmit(password);
    }
}