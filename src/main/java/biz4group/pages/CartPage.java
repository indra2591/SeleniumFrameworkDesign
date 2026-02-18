package biz4group.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {
	WebDriver driver;
	public CartPage(WebDriver driver) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	 @FindBy(xpath = "//button[@routerlink='/dashboard/cart']")
	 WebElement cartHeader;
	
	@FindBy(css=".cart h3")
	List<WebElement> cartProducts;
	
	@FindBy(css="li[class='totalRow'] button[type='button']")
	WebElement checkoutItem;
	
	
	
		public void goToCartPage() {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
			WebElement cartBtn = wait.until(
			    ExpectedConditions.elementToBeClickable(
			        By.xpath("//button[@routerlink='/dashboard/cart']")
			    )
			);
			
			// Scroll to element and click using JavaScript for Jenkins compatibility
			try {
				org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
				js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", cartBtn);
				Thread.sleep(300);
				js.executeScript("arguments[0].click();", cartBtn);
			} catch (Exception e) {
				// Fallback to regular click if JS click fails
				cartBtn.click();
			}
			
			waitForOverlaysToDisappear(driver);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ignored) {
			}
		}
		
		public Boolean verifyProductDisplay(String productName)
		{
			Boolean value= cartProducts.stream().anyMatch(cartProducts1 -> cartProducts1.getText().equalsIgnoreCase(productName));
			 return value;
		}
		
		public CheckOutPage goToCheckout() {
			checkoutItem.click();
			CheckOutPage checkoutPage = new CheckOutPage(driver);
			return checkoutPage;

		}
		
		private void waitForOverlaysToDisappear(WebDriver driver) {
		    WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
		    try { wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ng-animating"))); } catch (Exception ignored) {}
		    try { wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ngx-spinner-overlay"))); } catch (Exception ignored) {}
		    try { Thread.sleep(300); } catch (InterruptedException ignored) {}
		}

}