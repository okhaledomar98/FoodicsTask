package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WaitUtils {

    private static WebDriverWait getWait() {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(15));
    }

    public static void safeClickWithScrollAndJsFallback(By locator) {
        WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript("arguments[0].scrollIntoView({block:'center'});", element);

        try {
            getWait().until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (ElementClickInterceptedException | TimeoutException e) {
            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript("arguments[0].click();", element);
        }
    }

    public static void clickElement(By locator) {
        WebElement element = getWait().until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    public static WebElement waitForElementVisible(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void typeText(By locator, String text) {
        getWait().until(ExpectedConditions.visibilityOfElementLocated(locator)).clear();
        DriverFactory.getDriver().findElement(locator).sendKeys(text);
    }

    public static String getElementText(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    public static void waitForElementToDisappear(By locator) {
        getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}