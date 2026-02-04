package biz4group.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import AbstractComponent.AbstractComponent;

public class LandingPage extends AbstractComponent {
	WebDriver driver;
	// Constructor to initialize the WebDriver instance
	public LandingPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	//WebElement email1 = driver.findElement(By.id("userEmail"));
	//WebElement password1 = driver.findElement(By.id("userPassword"));
	//WebElement submit1 = driver.findElement(By.id("login"));
	@FindBy(id = "userEmail")
	WebElement email;
	
	@FindBy(id = "userPassword")
	WebElement password;
	
	@FindBy(id= "login")
	WebElement submit;
	
	@FindBy(css="[class*='flyInOut']")
	WebElement errorMessage;
	
	
	public Productdetails login(String emailID, String passwordText)
	{
		email.sendKeys(emailID);
		password.sendKeys(passwordText);
		submit.click();
		Productdetails productDetails = new Productdetails(driver);
		return productDetails;
	}
	
	public void goTo()
	{
		driver.get("https://rahulshettyacademy.com/client/");
		
		
	}
	
	public String getErrorMessage()
	{
   
		waitElementtoastToAppear(errorMessage);
		String error = errorMessage.getText();
		System.out.println(error);
		return error;
		
	}
}
