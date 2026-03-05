package base;

import config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.DriverFactory;

import java.time.Duration;

public class BaseUITest {

    protected WebDriver driver;

    @BeforeMethod(description = "Initialize Browser using Factory and navigate to Amazon")
    public void setupBrowser() {


        String browserName = ConfigReader.getProperty("browser");


        driver = DriverFactory.initializeDriver(browserName);


        int waitTime = Integer.parseInt(ConfigReader.getProperty("ui.implicit.wait"));
        int effectiveImplicitWait = Math.min(Math.max(waitTime, 0), 3);
        String url = ConfigReader.getProperty("ui.amazon.url");


        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(effectiveImplicitWait));


        driver.get(url);
        System.out.println("🌐 Navigated successfully to: " + url);
    }

    @AfterMethod(description = "Close Browser after the test finishes")
    public void teardown() {

        DriverFactory.quitDriver();
        System.out.println("🛑 Browser closed successfully.");
    }
}