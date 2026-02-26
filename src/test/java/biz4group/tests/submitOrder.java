package biz4group.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import biz4group.pages.CartPage;
import biz4group.pages.CheckOutPage;
import biz4group.pages.OrderHistoryPage;
import biz4group.pages.Productdetails;
import frameworks.base.BaseTest;

public class submitOrder extends BaseTest {
	String productName = "ZARA COAT 3";
	    @Test(dataProvider = "getData", groups = {"purchase"})
	    public void submitOrder(HashMap <String,String> data) throws Exception {
		// TODO Auto-generated method stub
		
		String countryName = "India";
		Productdetails productDetails = landingPage.login(data.get("email"), data.get("password"));
		List<WebElement> products = productDetails.getProductlist();
		CartPage cartPage = productDetails.addProductToCart(data.get("productName"));
		cartPage.goToCartPage();
		Boolean match = cartPage.verifyProductDisplay(data.get("productName"));
		Assert.assertTrue(match);
		CheckOutPage checkoutPage=cartPage.goToCheckout();
		checkoutPage.selectCountry(countryName);
		checkoutPage.placeHolder();
		}
	    
	    @Test(dependsOnMethods = {"submitOrder"})
	    
	    public void orderHistory()
	    {
	    	Productdetails productDetails = landingPage.login("hukowuhu@yopmail.com", "Test@123");
	        OrderHistoryPage orderHistoryPage = landingPage.goToOrderHistory();
	        Boolean value = orderHistoryPage.verifyOrderDisplay(productName);
	        Assert.assertTrue(value);
	    }
	    
	    @DataProvider
		public Object[][] getData() throws IOException {
            // HashMap<String,String> map = new HashMap<String,String>();
            // map.put("email", "hukowuhu@yopmail.com");
            // map.put("password", "Test@123");
            // map.put("productName", "ZARA COAT 3");
            // HashMap<String,String> map1 = new HashMap<String,String>();
            // map1.put("email", "dasadykege@yopmail.com");
            // map1.put("password", "Test@123");
            // map1.put("productName", "ADIDAS ORIGINAL");
	    	List<HashMap<String, String>> map = getJasonToMap();
	    	List<HashMap<String, String>> map1 = getJasonToMap();
	    	Object[][] data =new Object[][] {{map.get(0)},{map1.get(1)}};
	    	return data;
		}
	    
	    

}