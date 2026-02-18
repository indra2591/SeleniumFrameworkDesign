package biz4group.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import AbstractComponent.AbstractComponent;

public class Productdetails extends AbstractComponent {
	WebDriver driver;
	public Productdetails(WebDriver driver)
	{
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	//WebElement email1 = driver.findElement(By.id("userEmail"));
	//WebElement password1 = driver.findElement(By.id("userPassword"));
	//WebElement submit1 = driver.findElement(By.id("login"));
	@FindBy(css = ".mb-3")
	List<WebElement> products;
	
	By productsBy = By.cssSelector(".mb-3");
	
	@FindBy(css ="button:last-of-type")
			WebElement addToCartButton;
	
			//@FindBy(css = ".ng-animating")
			//WebElement elementDisappear;
	
	By toastMessage = By.cssSelector("#toast-container");
	By elementDisappear = By.cssSelector(".ng-animating");
	
	

	public List<WebElement> getProductlist()
	{
		waitElementToAppear(productsBy);
		return products;
	}
    
	public WebElement getProductByName(String productName)
	{
		WebElement prod = 	products.stream().filter(product->product.findElement(By.cssSelector("b")).getText().equals(productName)).findFirst().orElse(null);
		return prod;
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

	public CartPage addProductToCart(String productName) {
	    WebElement prod = getProductByName(productName);
	    WebElement addToCartBtn = prod.findElement(By.cssSelector("button:last-of-type"));
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].scrollIntoView(true);", addToCartBtn);
	    WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
	    wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));
	    waitForOverlaysToDisappear(driver);
	    addToCartBtn.click();
	    
		waitElementToAppear(toastMessage);
		waitElementToDisappear(elementDisappear);
		CartPage cartPage = new CartPage(driver);
		return cartPage;
	}

	public WebElement getAddToCartButton() {
		return addToCartButton;
	}


}