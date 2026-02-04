package biz4group.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
	
	public CartPage addProductToCart(String productName)
	{
		WebElement prod=getProductByName(productName);
		prod.findElement(By.cssSelector("button:last-of-type")).click();
		waitElementToAppear(toastMessage);
		waitElementToDisappear(elementDisappear);
		CartPage cartPage = new CartPage(driver);
		return cartPage;
	}


}


