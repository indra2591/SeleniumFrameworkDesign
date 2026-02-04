package biz4group.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

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
	
	public void placeHolder() {
		placeOrderButton.click();
		String orderMessageText = orderMessage.getText();
		
		
	}
	
	

}
