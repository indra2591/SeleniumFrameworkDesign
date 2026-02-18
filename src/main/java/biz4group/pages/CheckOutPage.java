package biz4group.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckOutPage {
	WebDriver driver;
	public CheckOutPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		
	}
	
	// driver.findElement(By.xpath("//section//button[2]")).click();
	  //driver.findElement(By.xpath("//a[text()='Place Order ']")).click();
	  //String orderMessage= driver.findElement(By.cssSelector(".hero-primary")).getText();
	  //Assert.assertTrue(orderMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER."));
	 // driver.close();
	
	@FindBy(xpath="//input[@placeholder='Select Country']")
	WebElement countryName;
	
	@FindBy(xpath="//section//button[2]")
	WebElement selectCountryButton;
	
	@FindBy(xpath="//a[text()='Place Order ']")
	WebElement placeOrderButton;
	
	@FindBy(css=".hero-primary")
	WebElement orderMessage;
	
	public void selectCountry(String country)
	{
		Actions a = new Actions(driver);
		a.sendKeys(countryName, country).build().perform(); 
		selectCountryButton.click();
	}
	
	private void waitForOverlaysToDisappear(WebDriver driver) {
	    WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
	    try {
	        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ng-animating")));
	    } catch (Exception ignored) {}
	    try {
	        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ngx-spinner-overlay")));
	    } catch (Exception ignored) {}
	    try {
	        Thread.sleep(300); // Small buffer for UI to settle
	    } catch (InterruptedException ignored) {}
	}
	
	public void placeHolder() {
	    // Scroll element into view and wait for clickable
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].scrollIntoView(true);", placeOrderButton);
	    WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
	    wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
	    waitForOverlaysToDisappear(driver);
	    placeOrderButton.click();
	    String orderMessageText = orderMessage.getText();
		
		
	}
	
	

}