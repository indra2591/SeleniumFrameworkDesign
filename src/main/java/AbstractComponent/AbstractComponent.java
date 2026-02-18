package AbstractComponent;

import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import biz4group.pages.OrderHistoryPage;

/**
 * Base component class providing common wait and interaction utilities
 * for Page Object Model elements.
 */
public class AbstractComponent {
	
	private static final Logger logger = Logger.getLogger(AbstractComponent.class.getName());
	private static final int EXPLICIT_WAIT_SECONDS = 15;
	private static final int ELEMENT_WAIT_SECONDS = 10;
	
	WebDriver driver;
	
	public AbstractComponent(WebDriver driver) {
		this.driver = driver;
	}

	@FindBy(css ="[routerlink*='myorders']")
	WebElement orderLink;
	
	/**
	 * Waits for an element to appear on the page.
	 * @param locator The By locator for the element
	 */
	public void waitElementToAppear(By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(ELEMENT_WAIT_SECONDS));
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		} catch (TimeoutException e) {
			logger.warning("Element did not appear within " + ELEMENT_WAIT_SECONDS + " seconds: " + locator);
			throw e;
		}
	}
	
	/**
	 * Waits for an element to disappear from the page.
	 * @param locator The By locator for the element
	 */
	public void waitElementToDisappear(By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(ELEMENT_WAIT_SECONDS));
			wait.until(ExpectedConditions.invisibilityOfAllElements(driver.findElements(locator)));
		} catch (TimeoutException e) {
			logger.warning("Element did not disappear within " + ELEMENT_WAIT_SECONDS + " seconds: " + locator);
			throw e;
		}
	}
	
	/**
	 * Waits for a WebElement to be visible.
	 * @param element The WebElement to wait for
	 */
	public void waitElementToastToAppear(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(ELEMENT_WAIT_SECONDS));
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (TimeoutException e) {
			logger.warning("Element toast did not appear within " + ELEMENT_WAIT_SECONDS + " seconds");
			throw e;
		}
	}
	
	/**
	 * Navigate to order history page with proper wait handling
	 * @return OrderHistoryPage instance
	 */
	public OrderHistoryPage goToOrderHistory() {
		// Wait for spinner overlay to disappear before clicking
		waitForSpinnerToDisappear();
		
		// Use JavaScript click for reliability
		try {
			org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", orderLink);
			// Wait for element to be clickable instead of sleeping
			WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(ELEMENT_WAIT_SECONDS));
			wait.until(ExpectedConditions.elementToBeClickable(orderLink));
			js.executeScript("arguments[0].click();", orderLink);
		} catch (Exception e) {
			logger.warning("JavaScript click failed, falling back to regular click: " + e.getMessage());
			// Fallback to regular click if JS click fails
			orderLink.click();
		}
		
		OrderHistoryPage orderHistoryPage = new OrderHistoryPage(driver);
		return orderHistoryPage;
	}
	
	/**
	 * Wait for spinner overlays to disappear
	 */
	private void waitForSpinnerToDisappear() {
		WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(EXPLICIT_WAIT_SECONDS));
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ngx-spinner-overlay")));
		} catch (TimeoutException e) {
			logger.warning("ngx-spinner-overlay did not disappear within " + EXPLICIT_WAIT_SECONDS + " seconds");
		}
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ng-animating")));
		} catch (TimeoutException e) {
			logger.warning("ng-animating element did not disappear within " + EXPLICIT_WAIT_SECONDS + " seconds");
		}
	}

}


