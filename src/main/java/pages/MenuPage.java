package pages;

import org.openqa.selenium.By;
import utils.WaitUtils;

public class MenuPage {


    private final By seeAllButton = By.xpath("//*[@id=\"hmenu-content\"]/div[1]/section[3]/ul/li[5]/a[1]/i");

    private final By videoGamesSection = By.xpath("//*[@id=\"hmenu-content\"]/div[1]/section[3]/ul/ul/li[10]/a");

    private final By allVideoGamesLink = By.xpath("//*[@id=\"hmenu-content\"]/div[16]/section/ul/li[1]/a");
    public MenuPage clickSeeAll() {
        WaitUtils.clickElement(seeAllButton);
        return this;
    }

    public MenuPage selectVideoGames() {
        WaitUtils.clickElement(videoGamesSection);
        return this;
    }

    public void clickAllVideoGames() {
        WaitUtils.safeClickWithScrollAndJsFallback(allVideoGamesLink);
    }
}