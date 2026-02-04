package biz4group.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
			cartHeader.click();
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
		

}
