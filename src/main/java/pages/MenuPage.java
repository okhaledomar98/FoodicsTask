package pages;

import org.openqa.selenium.By;
import utils.DriverFactory;
import utils.WaitUtils;

public class MenuPage {

    private final By seeAllButton = By.xpath(
            "//a[@aria-label='See All Categories' or contains(normalize-space(.),'See All')]"
    );
    private final By videoGamesSectionPrimary = By.xpath(
            "//a[contains(@data-menu-id,'22') or contains(@href,'video-games') or contains(normalize-space(.),'Video Games')]"
    );
    private final By videoGamesSectionFallback = By.xpath(
            "//a[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'video games')]"
    );
    private final By allVideoGamesLink = By.xpath(
            "//a[contains(@href,'node=18022560031') or contains(normalize-space(.),'All Video Games')]"
    );
    private final String directAllVideoGamesUrl =
            "https://www.amazon.eg/-/en/gp/browse.html?node=18022560031&ref_=nav_em_vg_all_0_2_16_2";

    public MenuPage clickSeeAll() {
        WaitUtils.clickElement(seeAllButton);
        return this;
    }

    public MenuPage selectVideoGames() {
        try {
            WaitUtils.clickElement(videoGamesSectionPrimary);
            return this;
        } catch (RuntimeException ignored) {
            // Try broader fallback.
        }
        try {
            WaitUtils.clickElement(videoGamesSectionFallback);
            return this;
        } catch (RuntimeException ignored) {
            // Fall through to direct URL in clickAllVideoGames.
        }
        return this;
    }

    public void clickAllVideoGames() {
        try {
            WaitUtils.safeClickWithScrollAndJsFallback(allVideoGamesLink);
        } catch (RuntimeException ex) {
            DriverFactory.getDriver().get(directAllVideoGamesUrl);
            WaitUtils.waitForElementVisible(By.tagName("body"));
        }
    }
}