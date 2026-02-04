package biz4group.tests;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class StandAlonTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WebDriverManager.chromedriver().setup();
		ChromeDriver driver = new ChromeDriver();
		String productName = "ZARA COAT 3";
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
		driver.get("https://rahulshettyacademy.com/client/");
		driver.findElement(By.id("userEmail")).sendKeys("hukowuhu@yopmail.com");
		driver.findElement(By.id("userPassword")).sendKeys("Test@123");
		driver.findElement(By.id("login")).click();
	   List<WebElement> products =  driver.findElements(By.cssSelector(".mb-3"));
	   WebElement prod = 	products.stream().filter(product->product.findElement(By.cssSelector("b")).getText().equals(productName)).findFirst().orElse(null);
	   prod.findElement(By.cssSelector(".card-body button:last-of-type")).click();
	   WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(5));
	   wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container")));
	   wait.until(ExpectedConditions.invisibilityOfAllElements(driver.findElements(By.cssSelector(".ng-animating"))));
	   driver.findElement(By.cssSelector("[routerlink='/dashboard/cart']")).click();
	   List<WebElement> cartProducts = driver.findElements(By.cssSelector(".cart h3"));
	  Boolean value= cartProducts.stream().anyMatch(cartProducts1 -> cartProducts1.getText().equalsIgnoreCase(productName));
	  Assert.assertTrue(value);
	  driver.findElement(By.cssSelector("li[class='totalRow'] button[type='button']")).click();
	  Actions a = new Actions(driver);
	  a.sendKeys(driver.findElement(By.xpath("//input[@placeholder='Select Country']")), "India").build().perform();
	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ta-results")));
	  driver.findElement(By.xpath("//section//button[2]")).click();
	  driver.findElement(By.xpath("//a[text()='Place Order ']")).click();
	  String orderMessage= driver.findElement(By.cssSelector(".hero-primary")).getText();
	  Assert.assertTrue(orderMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER."));
	  driver.close();
	  
	  
	}

}
