package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import utils.DriverFactory;
import utils.WaitUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.time.Duration;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductsPage {

    private final By filtersPanel = By.id("s-refinements");
    private final By freeShippingText = By.xpath(
            "//*[@id='s-refinements']//a[.//*[contains(.,'Free Shipping') or contains(.,'FREE Delivery') or contains(.,'\u0634\u062d\u0646 \u0645\u062c\u0627\u0646\u064a')]]"
    );
    private final By freeShippingParam = By.cssSelector(
            "a[href*='p_76'], a[href*='free_shipping'], a[href*='shipping_option']"
    );
    private final By newConditionText = By.xpath(
            "//*[@id='s-refinements']//a[.//*[normalize-space()='New' or normalize-space()='\u062c\u062f\u064a\u062f']]"
    );
    private final By newConditionParam = By.cssSelector(
            "a[href*='p_n_condition-type']"
    );

    private final By sortSelect = By.id("s-result-sort-select");
    private final By sortDropdownTrigger = By.cssSelector("span[data-action='a-dropdown-button']");
    private final By sortHighToLowOption = By.xpath(
            "//a[contains(normalize-space(),'Price: High to Low') or contains(normalize-space(),'High to Low') or contains(normalize-space(),'\u0627\u0644\u0633\u0639\u0631: \u0645\u0646 \u0627\u0644\u0623\u0639\u0644\u0649 \u0625\u0644\u0649 \u0627\u0644\u0623\u0642\u0644')]"
    );
    private final By resultsContainer = By.cssSelector("div.s-main-slot");
    private final By pageBody = By.tagName("body");
    private final By productCardsPrimary = By.cssSelector("[data-component-type='s-search-result'][data-asin]:not([data-asin=''])");
    private final By productCardsFallbackA = By.cssSelector("div.s-result-item[data-asin]:not([data-asin=''])");
    private final By productCardsFallbackB = By.cssSelector("[cel_widget_id^='MAIN-SEARCH_RESULTS-'][data-asin]:not([data-asin=''])");
    private final By noResultsMarker = By.cssSelector(".s-no-results-section, .a-section.a-spacing-top-large .a-color-state");
    private final By productTitleInCard = By.cssSelector("h2 a span");
    private final By productLinkInCard = By.cssSelector("h2 a");
    private final By productAnyPdpLink = By.cssSelector("a[href*='/dp/'], a[href*='/gp/product/']");
    private final By productPriceInCard = By.cssSelector(".a-price .a-offscreen");
    private final By productPriceWhole = By.cssSelector(".a-price .a-price-whole");
    private final By productPriceFraction = By.cssSelector(".a-price .a-price-fraction");
    private final By productInlineAddButton = By.cssSelector("button[name='submit.addToCart'], input[name='submit.addToCart'], [aria-label*='Add to Cart'], [aria-label*='\u0625\u0636\u0627\u0641\u0629 \u0625\u0644\u0649 \u0639\u0631\u0628\u0629 \u0627\u0644\u062a\u0633\u0648\u0642']");
    private final By nextPageButton = By.cssSelector("a.s-pagination-next:not(.s-pagination-disabled)");
    private final By addToCartButtonPrimary = By.id("add-to-cart-button");
    private final By addToCartButtonAlt = By.name("submit.add-to-cart");
    private final By addToCartButtonDeal = By.cssSelector("input#add-to-cart-button, input[name='submit.addToCart']");
    private final By closeAddedOverlay = By.cssSelector("a[data-action='a-popover-close'], button[aria-label='Close'], .a-popover-close");
    private final By noThanksProtectionById = By.cssSelector(
            "#attachSiNoCoverage, input[name='submit.addCoverageDecline'], button[id*='NoCoverage']"
    );
    private final By noThanksProtectionByText = By.xpath(
            "//button[contains(normalize-space(),'No thanks') or contains(normalize-space(),'No Thanks') " +
            "or contains(normalize-space(),'\u0644\u0627 \u0634\u0643\u0631\u0627') or contains(normalize-space(),'\u0644\u0627 \u0634\u0643\u0631\u0627\u064b')] | " +
            "//span[contains(normalize-space(),'No thanks') or contains(normalize-space(),'\u0644\u0627 \u0634\u0643\u0631\u0627') or contains(normalize-space(),'\u0644\u0627 \u0634\u0643\u0631\u0627\u064b')]"
    );
    private final By cartCountBadge = By.id("nav-cart-count");
    private final By codMessageMarkers = By.xpath(
            "//*[contains(translate(normalize-space(.), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'CASH ON DELIVERY')] " +
            "or //*[contains(normalize-space(.), '\u0627\u0644\u062f\u0641\u0639 \u0639\u0646\u062f \u0627\u0644\u0627\u0633\u062a\u0644\u0627\u0645')] " +
            "or //*[contains(normalize-space(.), '\u0627\u0644\u062f\u0641\u0639 \u0643\u0627\u0634')]"
    );
    private final List<String> addedProductTitles = new ArrayList<>();

    private boolean isPresent(By locator) {
        return !DriverFactory.getDriver().findElements(locator).isEmpty();
    }

    private void ensureSearchLayoutForFilters() {
        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        boolean isSearchLayout = currentUrl != null && currentUrl.contains("/s?");

        if (!isSearchLayout || !isPresent(filtersPanel) || !isPresent(resultsContainer)) {
            DriverFactory.getDriver().get("https://www.amazon.eg/s?k=video+games&rh=n%3A18022560031");
        }
        WaitUtils.waitForElementVisible(pageBody);
    }

    private void clickFirstAvailable(By... candidates) {
        for (By locator : candidates) {
            if (!isPresent(locator)) {
                continue;
            }
            try {
                WaitUtils.safeClickWithScrollAndJsFallback(locator);
                return;
            } catch (RuntimeException ignored) {
                // Try next candidate if current locator is present but not interactable.
            }
        }
        throw new RuntimeException("Could not find a clickable locator for the requested filter.");
    }

    public ProductsPage applyFreeShipping() {
        ensureSearchLayoutForFilters();
        clickFirstAvailable(freeShippingText, freeShippingParam);
        return this;
    }

    public ProductsPage applyNewCondition() {
        clickFirstAvailable(newConditionText, newConditionParam);
        return this;
    }

    public ProductsPage sortHighToLow() {
        ensureSearchLayoutForFilters();
        if (isPresent(sortSelect)) {
            WaitUtils.waitForElementVisible(sortSelect);
            new Select(DriverFactory.getDriver().findElement(sortSelect)).selectByValue("price-desc-rank");
            WaitUtils.waitForElementVisible(resultsContainer);
            return this;
        }

        clickFirstAvailable(sortDropdownTrigger);
        clickFirstAvailable(sortHighToLowOption);
        WaitUtils.waitForElementVisible(resultsContainer);
        return this;
    }

    public List<String> addProductsBelowPriceAndReturnTitles(double maxPriceEgp, int maxPages) {
        ensureSearchLayoutForFilters();
        addedProductTitles.clear();
        int eligibleCount = 0;
        int addedCount = 0;

        for (int page = 1; page <= maxPages; page++) {
            WaitUtils.waitForElementVisible(pageBody);
            WaitUtils.waitForElementVisible(resultsContainer);
            List<WebElement> cards = getSearchResultCards();
            if (cards.isEmpty()) {
                boolean noResults = isPresent(noResultsMarker);
                System.out.println("Step 6 page " + page + " -> no cards found. noResults=" + noResults + ", url=" + DriverFactory.getDriver().getCurrentUrl());
            }
            int eligibleOnCurrentPage = 0;
            int pricedOnCurrentPage = 0;
            int addedOnCurrentPage = 0;

            int inspectedDebugCount = 0;
            for (WebElement card : cards) {
                ProductCandidate candidate = readCandidate(card, maxPriceEgp);
                if (candidate == null) {
                    if (inspectedDebugCount < 3) {
                        String cardText = card.getText();
                        String snippet = (cardText == null) ? "" : cardText.replace("\n", " ");
                        if (snippet.length() > 90) {
                            snippet = snippet.substring(0, 90) + "...";
                        }
                        System.out.println("Step 6 page " + page + " -> skipped card sample: " + snippet);
                        inspectedDebugCount++;
                    }
                    continue;
                }
                pricedOnCurrentPage++;
                eligibleCount++;
                eligibleOnCurrentPage++;

                if (tryAddFromListing(card, candidate.title)) {
                    addedCount++;
                    addedOnCurrentPage++;
                    continue;
                }

                if (candidate.url != null && !candidate.url.isBlank()) {
                    if (addFromProductPage(candidate.url, candidate.title)) {
                        addedCount++;
                        addedOnCurrentPage++;
                    }
                }
            }
            System.out.println("Step 6 page " + page + " -> cards: " + cards.size()
                    + ", priced: " + pricedOnCurrentPage
                    + ", below15k: " + eligibleOnCurrentPage
                    + ", added: " + addedOnCurrentPage);

            // Requirement: if no product on current page is below 15k, go next page.
            if (!goToNextPage()) {
                break;
            }
            if (eligibleOnCurrentPage > 0) {
                // still continue to next page to keep coverage high; requirement is satisfied either way
                WaitUtils.waitForElementVisible(resultsContainer);
            }
        }

        System.out.println("Step 6 debug -> eligible: " + eligibleCount + ", added: " + addedCount);
        return new ArrayList<>(addedProductTitles);
    }

    public List<String> addAllProductsBelowPriceAndReturnTitles(double maxPriceEgp) {
        return addProductsBelowPriceAndReturnTitles(maxPriceEgp, 50);
    }

    public List<String> addFirstProductBelowPriceAndStop(double maxPriceEgp, int maxPages) {
        ensureSearchLayoutForFilters();
        addedProductTitles.clear();

        for (int page = 1; page <= maxPages; page++) {
            WaitUtils.waitForElementVisible(pageBody);
            WaitUtils.waitForElementVisible(resultsContainer);
            List<WebElement> initialCards = getSearchResultCards();
            System.out.println("Step 6 (stop-on-first) page " + page + " -> cards: " + initialCards.size());

            int index = 0;
            while (true) {
                List<WebElement> liveCards = getSearchResultCards();
                if (index >= liveCards.size()) {
                    break;
                }

                WebElement card = liveCards.get(index);
                ProductCandidate candidate;
                try {
                    candidate = readCandidate(card, maxPriceEgp);
                } catch (StaleElementReferenceException ex) {
                    // Retry once using freshly fetched elements.
                    liveCards = getSearchResultCards();
                    if (index >= liveCards.size()) {
                        index++;
                        continue;
                    }
                    try {
                        candidate = readCandidate(liveCards.get(index), maxPriceEgp);
                    } catch (StaleElementReferenceException ignored) {
                        index++;
                        continue;
                    }
                }
                if (candidate == null) {
                    index++;
                    continue;
                }

                if (candidate.url != null && !candidate.url.isBlank()
                        && addFromProductPageInNewTabAndClose(candidate.url, candidate.title)) {
                    System.out.println("Step 6 -> Added first eligible product after opening and closing PDP: " + candidate.title);
                    return new ArrayList<>(addedProductTitles);
                }
                index++;
            }

            if (!goToNextPage()) {
                break;
            }
        }

        return new ArrayList<>(addedProductTitles);
    }

    public int getCartCount() {
        if (!isPresent(cartCountBadge)) {
            return 0;
        }
        String raw = DriverFactory.getDriver().findElement(cartCountBadge).getText();
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private ProductCandidate readCandidate(WebElement card, double maxPriceEgp) {
        Double price = extractPriceFromCard(card);
        if (price == null || price <= 0 || price >= maxPriceEgp) {
            return null;
        }

        String url = extractProductUrl(card);
        if (url == null || url.isBlank()) {
            return null;
        }

        String title = extractProductTitle(card, url);
        if (title == null || title.isBlank()) {
            return null;
        }

        return new ProductCandidate(url, title);
    }

    private Double extractPriceFromCard(WebElement card) {
        try {
            String offscreen = card.findElement(productPriceInCard).getText();
            double parsed = parsePrice(offscreen);
            if (parsed > 0) {
                return parsed;
            }
        } catch (NoSuchElementException ignored) {
        }

        try {
            String whole = card.findElement(productPriceWhole).getText();
            String fraction = "";
            List<WebElement> fractionElements = card.findElements(productPriceFraction);
            if (!fractionElements.isEmpty()) {
                fraction = fractionElements.get(0).getText();
            }
            String combined = whole + (fraction.isBlank() ? "" : "." + fraction);
            double parsed = parsePrice(combined);
            if (parsed > 0) {
                return parsed;
            }
        } catch (NoSuchElementException ignored) {
        }

        String cardText = card.getText();
        if (cardText != null && !cardText.isBlank()) {
            double parsed = parsePrice(cardText);
            if (parsed > 0) {
                return parsed;
            }
        }
        return null;
    }

    private String extractProductUrl(WebElement card) {
        try {
            WebElement link = card.findElement(productAnyPdpLink);
            String href = link.getAttribute("href");
            if (href != null && !href.isBlank()) {
                return href;
            }
        } catch (NoSuchElementException ignored) {
        }

        try {
            WebElement link = card.findElement(productLinkInCard);
            String href = link.getAttribute("href");
            if (href != null && !href.isBlank()) {
                return href;
            }
        } catch (NoSuchElementException ignored) {
        }

        return null;
    }

    private String extractProductTitle(WebElement card, String productUrl) {
        try {
            String title = card.findElement(productTitleInCard).getText();
            if (isLikelyMeaningfulTitle(title)) {
                return title.trim();
            }
        } catch (NoSuchElementException ignored) {
        }

        try {
            WebElement link = card.findElement(productAnyPdpLink);
            String title = link.getText();
            if (isLikelyMeaningfulTitle(title)) {
                return title.trim();
            }
        } catch (NoSuchElementException ignored) {
        }

        String text = card.getText();
        if (text != null && !text.isBlank()) {
            String[] lines = text.split("\\R");
            for (String line : lines) {
                String candidate = line == null ? "" : line.trim();
                if (isLikelyMeaningfulTitle(candidate)) {
                    return candidate;
                }
            }
        }

        return productUrl;
    }

    private List<WebElement> getSearchResultCards() {
        List<WebElement> cards = DriverFactory.getDriver().findElements(productCardsPrimary);
        if (!cards.isEmpty()) {
            return cards;
        }
        cards = DriverFactory.getDriver().findElements(productCardsFallbackA);
        if (!cards.isEmpty()) {
            return cards;
        }
        return DriverFactory.getDriver().findElements(productCardsFallbackB);
    }

    private boolean tryAddFromListing(WebElement card, String title) {
        try {
            List<WebElement> buttons = card.findElements(productInlineAddButton);
            if (buttons.isEmpty()) {
                return false;
            }
            int cartBefore = getCartCount();
            buttons.get(0).click();
            closeTransientOverlaysIfAny();
            if (!waitForCartCountIncrease(cartBefore)) {
                return false;
            }
            addedProductTitles.add(title);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private boolean addFromProductPage(String productUrl, String title) {
        String returnUrl = DriverFactory.getDriver().getCurrentUrl();
        DriverFactory.getDriver().get(productUrl);
        WaitUtils.waitForElementVisible(pageBody);
        try {
            if (!isLikelyCodEligibleOnPdp()) {
                DriverFactory.getDriver().get(returnUrl);
                return false;
            }
            int cartBefore = getCartCount();
            if (isPresent(addToCartButtonPrimary)) {
                WaitUtils.safeClickWithScrollAndJsFallback(addToCartButtonPrimary);
                closeTransientOverlaysIfAny();
                if (!waitForCartCountIncrease(cartBefore)) {
                    DriverFactory.getDriver().get(returnUrl);
                    return false;
                }
                addedProductTitles.add(title);
                DriverFactory.getDriver().get(returnUrl);
                return true;
            }
            if (isPresent(addToCartButtonAlt)) {
                WaitUtils.safeClickWithScrollAndJsFallback(addToCartButtonAlt);
                closeTransientOverlaysIfAny();
                if (!waitForCartCountIncrease(cartBefore)) {
                    DriverFactory.getDriver().get(returnUrl);
                    return false;
                }
                addedProductTitles.add(title);
                DriverFactory.getDriver().get(returnUrl);
                return true;
            }
            if (isPresent(addToCartButtonDeal)) {
                WaitUtils.safeClickWithScrollAndJsFallback(addToCartButtonDeal);
                closeTransientOverlaysIfAny();
                if (!waitForCartCountIncrease(cartBefore)) {
                    DriverFactory.getDriver().get(returnUrl);
                    return false;
                }
                addedProductTitles.add(title);
                DriverFactory.getDriver().get(returnUrl);
                return true;
            }
            DriverFactory.getDriver().get(returnUrl);
            return false;
        } catch (RuntimeException ex) {
            DriverFactory.getDriver().get(returnUrl);
            return false;
        }
    }

    private boolean addFromProductPageInNewTabAndClose(String productUrl, String title) {
        WebDriver driver = DriverFactory.getDriver();
        String listingWindow = driver.getWindowHandle();
        Set<String> beforeHandles = driver.getWindowHandles();

        try {
            ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", productUrl);
            String productWindow = waitForNewWindowHandle(beforeHandles);
            if (productWindow == null) {
                return false;
            }
            driver.switchTo().window(productWindow);
            WaitUtils.waitForElementVisible(pageBody);

            int cartBefore = getCartCount();
            if (isPresent(addToCartButtonPrimary)) {
                WaitUtils.safeClickWithScrollAndJsFallback(addToCartButtonPrimary);
            } else if (isPresent(addToCartButtonAlt)) {
                WaitUtils.safeClickWithScrollAndJsFallback(addToCartButtonAlt);
            } else if (isPresent(addToCartButtonDeal)) {
                WaitUtils.safeClickWithScrollAndJsFallback(addToCartButtonDeal);
            } else {
                driver.close();
                driver.switchTo().window(listingWindow);
                return false;
            }

            closeTransientOverlaysIfAny();
            boolean added = waitForCartCountIncrease(cartBefore);
            if (added) {
                addedProductTitles.add(title);
            }

            driver.close();
            driver.switchTo().window(listingWindow);
            return added;
        } catch (RuntimeException ex) {
            try {
                if (!driver.getWindowHandle().equals(listingWindow)) {
                    driver.close();
                }
            } catch (Exception ignored) {
            }
            driver.switchTo().window(listingWindow);
            return false;
        }
    }

    private String waitForNewWindowHandle(Set<String> existingHandles) {
        try {
            new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10))
                    .until(d -> d.getWindowHandles().size() > existingHandles.size());
            for (String handle : DriverFactory.getDriver().getWindowHandles()) {
                if (!existingHandles.contains(handle)) {
                    return handle;
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean isLikelyCodEligibleOnPdp() {
        if (isPresent(codMessageMarkers)) {
            return true;
        }
        String pageText = DriverFactory.getDriver().findElement(pageBody).getText();
        if (pageText == null || pageText.isBlank()) {
            return false;
        }
        String normalized = pageText.toLowerCase(Locale.ROOT);
        return normalized.contains("cash on delivery")
                || normalized.contains("\u0627\u0644\u062f\u0641\u0639 \u0639\u0646\u062f \u0627\u0644\u0627\u0633\u062a\u0644\u0627\u0645")
                || normalized.contains("\u0627\u0644\u062f\u0641\u0639 \u0643\u0627\u0634");
    }

    private boolean isLikelyMeaningfulTitle(String rawTitle) {
        if (rawTitle == null) {
            return false;
        }
        String title = rawTitle.trim();
        if (title.length() < 8) {
            return false;
        }
        String lowered = title.toLowerCase(Locale.ROOT);
        if (lowered.contains("egp")
                || lowered.contains("free delivery")
                || lowered.contains("free shipping")
                || lowered.contains("prime")
                || lowered.contains("stars")
                || lowered.contains("rating")
                || lowered.contains("deliver")
                || lowered.contains("viewed")
                || lowered.contains("previously")
                || lowered.contains("recently")
                || lowered.contains("\u0634\u062d\u0646 \u0645\u062c\u0627\u0646\u064a")
                || lowered.contains("\u062a\u0648\u0635\u064a\u0644")
                || lowered.contains("\u0646\u062c\u0648\u0645")
                || lowered.contains("\u062a\u0645 \u0639\u0631\u0636\u0647\u0627 \u0633\u0627\u0628\u0642\u064b\u0627")
                || lowered.contains("\u0639\u0631\u0636\u0647\u0627 \u0633\u0627\u0628\u0642\u0627")) {
            return false;
        }
        return !title.matches("^[\\d\\s.,-]+$");
    }

    private boolean goToNextPage() {
        if (!isPresent(nextPageButton)) {
            return false;
        }
        try {
            WaitUtils.safeClickWithScrollAndJsFallback(nextPageButton);
            WaitUtils.waitForElementVisible(resultsContainer);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private double parsePrice(String rawPriceText) {
        if (rawPriceText == null || rawPriceText.isBlank()) {
            return -1;
        }

        String normalized = normalizeArabicDigits(rawPriceText)
                .replace("\u066B", ".")
                .replace("\u066C", ",");

        Matcher matcher = Pattern.compile("(\\d{1,3}(?:,\\d{3})*(?:\\.\\d{1,2})?|\\d+(?:\\.\\d{1,2})?)")
                .matcher(normalized);
        if (!matcher.find()) {
            return -1;
        }

        String firstAmount = matcher.group(1).replace(",", "");
        try {
            return Double.parseDouble(firstAmount);
        } catch (NumberFormatException ex) {
            return -1;
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

    private void closeTransientOverlaysIfAny() {
        // Handle protection plan drawer that appears after add-to-cart.
        clickIfPresent(noThanksProtectionById);
        clickIfPresent(noThanksProtectionByText);

        clickIfPresent(closeAddedOverlay);
    }

    private boolean waitForCartCountIncrease(int beforeCount) {
        try {
            new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(8))
                    .until(d -> getCartCount() > beforeCount);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void clickIfPresent(By locator) {
        try {
            new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(3))
                    .until(d -> !d.findElements(locator).isEmpty());
            WaitUtils.safeClickWithScrollAndJsFallback(locator);
        } catch (Exception ignored) {
            // Optional popup/overlay is not present in all add-to-cart flows.
        }
    }

    private static class ProductCandidate {
        private final String url;
        private final String title;

        private ProductCandidate(String url, String title) {
            this.url = url;
            this.title = title;
        }
    }

}