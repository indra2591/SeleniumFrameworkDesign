package biz4group.pages;

import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import AbstractComponent.AbstractComponent;

/**
 * LandingPage represents the login page of the application.
 * Handles user authentication and error message validation.
 */
public class LandingPage extends AbstractComponent {
	private static final Logger logger = Logger.getLogger(LandingPage.class.getName());
	private WebDriver driver;
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
	
	
	/**
	 * Login with provided email and password credentials.
	 * @param emailID the email address to login with
	 * @param passwordText the password to use
	 * @return Productdetails page after successful login
	 */
	public Productdetails login(String emailID, String passwordText) {
		try {
			logger.info("Logging in with email: " + emailID);
			email.sendKeys(emailID);
			password.sendKeys(passwordText);
			submit.click();
			logger.info("Login submit clicked successfully");
			Productdetails productDetails = new Productdetails(driver);
			return productDetails;
		} catch (Exception e) {
			logger.severe("Login failed: " + e.getMessage());
			throw new RuntimeException("Failed to login: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Navigate to the landing page URL.
	 */
	public void goTo() {
		String applicationUrl = "https://rahulshettyacademy.com/client/";
		logger.info("Navigating to: " + applicationUrl);
		driver.get(applicationUrl);
	}
	
	
	/**
	 * Get error message displayed on login failure.
	 * @return error message text
	 */
	public String getErrorMessage() {
		try {
			logger.info("Retrieving error message");
			waitElementToastToAppear(errorMessage);
			String error = errorMessage.getText();
			logger.warning("Error message retrieved: " + error);
			return error;
		} catch (Exception e) {
			logger.severe("Failed to retrieve error message: " + e.getMessage());
			throw new RuntimeException("Error message not found: " + e.getMessage(), e);
		}
	}
}
