package AbstractComponent;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import biz4group.pages.OrderHistoryPage;

public class AbstractComponent {
	
	WebDriver driver;
	
	public AbstractComponent(WebDriver driver) {
		this.driver = driver;

	}

	@FindBy(css ="[routerlink*='myorders']")
	WebElement orderLink;
	
	public void waitElementToAppear(By FindBy)
	{
		WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
		   wait.until(ExpectedConditions.visibilityOfElementLocated(FindBy));
	}
	
	public void waitElementToDisappear(By FindBy)
    {
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfAllElements(driver.findElements(FindBy)));
    }
	
	public void waitElementtoastToAppear(WebElement FindBy)
	{
		WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
		   wait.until(ExpectedConditions.visibilityOfAllElements(FindBy));
	}
	
	public OrderHistoryPage goToOrderHistory() {
		orderLink.click();
		OrderHistoryPage orderHistoryPage = new OrderHistoryPage(driver);
		return orderHistoryPage;
	}

}


