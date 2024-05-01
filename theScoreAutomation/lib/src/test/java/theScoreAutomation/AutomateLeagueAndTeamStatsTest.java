package theScoreAutomation;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.Point;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

public class AutomateLeagueAndTeamStatsTest {

    private AppiumDriver driver;
    private String expectedActivity = "com.fivemobile.thescore.ui.MainActivity";

	@BeforeMethod
    public void setUp() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("appium:deviceName", "Android Emulator");
        desiredCapabilities.setCapability("automationName", "UiAutomator2");
        // Replace with path to your app
       // desiredCapabilities.setCapability("app", "/path/to/your/app.apk");
		desiredCapabilities.setCapability("appPackage", "com.fivemobile.thescore");
		desiredCapabilities.setCapability("appActivity", "com.fivemobile.thescore.ui.MainActivity");
		desiredCapabilities.setCapability("autoGrantPermissions", true); // Accept all permissions
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), desiredCapabilities);
    }

    @Test
    public void testSearchFCBarcelonaAndNavigateToTeamStats() throws InterruptedException {
    	// Check if the app is in background
    	// Assuming you have an element identifier unique to the desired activity (e.g., id, xpath)  
    	
    	By locatorOfButtonBy = By.id("com.fivemobile.thescore:id/action_button_text");
    	findElementWithTimeout(locatorOfButtonBy, 10);
    	
        clickElementWithTimeout(driver.findElement(AppiumBy.id("com.fivemobile.thescore:id/action_button_text")), 5);
        clickElementWithTimeout(driver.findElement(AppiumBy.id("com.fivemobile.thescore:id/action_button_text")), 5);

        // Search for FC Barcelona
        clickElementWithTimeout(driver.findElement(AppiumBy.id("com.fivemobile.thescore:id/search_bar_placeholder")), 5);
        sendKeysToElement(driver.findElement(AppiumBy.id("com.fivemobile.thescore:id/search_src_text")), "FC Barcelona", 2);
        
        // add as favorite
        clickElementWithTimeout(driver.findElement(AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.fivemobile.thescore:id/txt_name\" and @text=\"FC Barcelona\"]")), 5);

        // press continue
        clickElementWithTimeout(driver.findElement(AppiumBy.id("com.fivemobile.thescore:id/btn_primary")), 5);
        
        // press done
        clickElementWithTimeout(driver.findElement(AppiumBy.id("com.fivemobile.thescore:id/btn_primary")), 5);
        
        // press may be later
        Thread.sleep(1000);
        By locatorMayBeLaterButton = By.xpath("//android.widget.TextView[@resource-id=\"com.fivemobile.thescore:id/btn_secondary\"]");
        WebElement mayBeLaterButtonElement = findElementWithTimeout(locatorMayBeLaterButton, 5);
        clickElementWithTimeout(mayBeLaterButtonElement, 5);
        
        // touch anywhere to dismiss the popup 
        Thread.sleep(5000);
        final PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        var tapPoint = new Point(552, 265);
        var tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ofMillis(0),
            PointerInput.Origin.viewport(), tapPoint.x, tapPoint.y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(finger, Duration.ofMillis(50)));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tap));
        
        // search FC Barcelona team and open its team stats
        By searchBarTextViewLocator = By.id("com.fivemobile.thescore:id/search_bar_text_view");
        WebElement searchBarTextViewElement = findElementWithTimeout(searchBarTextViewLocator, 5);
        clickElementWithTimeout(searchBarTextViewElement, 5);
        
        By searchSrcTextLocator = By.id("com.fivemobile.thescore:id/search_src_text");
        WebElement searchSrcTextElement = findElementWithTimeout(searchSrcTextLocator, 5);
        sendKeysToElement(searchSrcTextElement, "FC Barcelona", 2);

        clickElementWithTimeout(driver.findElement(AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.fivemobile.thescore:id/txt_name\" and @text=\"FC Barcelona\"]")), 5);

        // Navigate to Team Stats
        By locatorTeamStat = By.xpath("//android.widget.TextView[@text=\"TEAM STATS\"]");
        WebElement teamStatsElement = findElementWithTimeout(locatorTeamStat, 5);
        
        clickElementWithTimeout(teamStatsElement, 5);
        
        Thread.sleep(2000);
        assert(correctStatPageIsVisible());
        
        // Navigate back to search
        driver.findElement(AppiumBy.accessibilityId("Navigate up")).click();
        
        // Navigate back to main page
        Thread.sleep(1000);
        driver.findElement(AppiumBy.accessibilityId("Navigate up")).click();  
        
        Thread.sleep(3000);
        assert(correctHomePageIsVisible());
    }

	@AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private boolean correctStatPageIsVisible() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    	By secondaryHeaderElementLocator = By.id("com.fivemobile.thescore:id/header_secondary_text");
        boolean isRankTextVisible = wait.until(ExpectedConditions.presenceOfElementLocated(secondaryHeaderElementLocator))
          .getText().contains("(RANK)");
        By headerTextElementLocator = By.id("com.fivemobile.thescore:id/header_text");
        boolean isSTATSTextVisible = wait.until(ExpectedConditions.presenceOfElementLocated(headerTextElementLocator))
          .getText().contains("STATS (LIGA)");
        if (isRankTextVisible && isSTATSTextVisible) {
          return true;
        }
        return false;
	}
    
    private boolean correctHomePageIsVisible() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    	By textIconLocator = By.xpath("//android.widget.TextView[@resource-id=\"com.fivemobile.thescore:id/navigation_bar_item_small_label_view\" and @text=\"News\"]");

        boolean isTextIconVisible = wait.until(ExpectedConditions.presenceOfElementLocated(textIconLocator))
          .getText().contains("News");
        
        By scoresIconLocator = By.xpath("//android.widget.TextView[@resource-id=\"com.fivemobile.thescore:id/navigation_bar_item_small_label_view\" and @text=\"Scores\"]");

        boolean isScoresIconVisible = wait.until(ExpectedConditions.presenceOfElementLocated(scoresIconLocator))
          .getText().contains("Scores");

        if (isTextIconVisible && isScoresIconVisible) {
          return true;
        }
        return false;
	}
    
    private void clickElementWithTimeout(WebElement element, int timeoutInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)).until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    private WebElement findElementWithTimeout(By locator, int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    private void sendKeysToElement(WebElement element, String text, int timeoutInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)).until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
    }
}