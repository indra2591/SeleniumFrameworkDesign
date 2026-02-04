package biz4group.tests;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import biz4group.pages.CartPage;
import biz4group.pages.Productdetails;
import frameworks.base.BaseTest;
import frameworks.listeners.Retry;

public class ErrorValidation extends BaseTest {
	  @Test(groups = {"ErrorHandling"})
	    public void submitOrder() throws Exception {
		// TODO Auto-generated method stub
		String productName = "ZARA COAT 3";
		String countryName = "India";
		Productdetails productDetails = landingPage.login("hukowuhu@yopmail.com", "Test@1234");
		Assert.assertEquals(landingPage.getErrorMessage(), "Incorrect email or password.");
		
             }
	  
	  @Test	(retryAnalyzer = Retry.class)
	  public void productDisplayError() throws Exception {
			// TODO Auto-generated method stub
			String productName = "ZARA COAT 3";
			String countryName = "India";
			Productdetails productDetails = landingPage.login("suwuqi@yopmail.com", "Test@123");
			List<WebElement> products = productDetails.getProductlist();
			CartPage cartPage = productDetails.addProductToCart(productName);
			cartPage.goToCartPage();
			Boolean match = cartPage.verifyProductDisplay("ZARA COAT 33");
			Assert.assertFalse(match);
	  }
	  
	  }
	  
