package frameworks.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import biz4group.pages.LandingPage;
import io.github.bonigarcia.wdm.WebDriverManager;

public abstract class BaseTest {
	public WebDriver driver;
	public LandingPage landingPage;
	
	public WebDriver driverinit() throws Exception {
		
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream( System.getProperty("user.dir") + "//src//main//resources/GloablData.properties");
		prop.load(fis);
		String browserName = System.getProperty("browser")!=null ? System.getProperty("browser") : prop.getProperty("browser");
		//prop.getProperty("browser");
		if(browserName.equalsIgnoreCase("chrome"))
		{
		
		WebDriverManager.chromedriver().setup();
		 driver = new ChromeDriver();
		
		}
		else if(browserName.equalsIgnoreCase("firefox"))
        {
            WebDriverManager.firefoxdriver().setup();
             driver = new FirefoxDriver();	
        }
		else if(browserName.equalsIgnoreCase("edge"))
		{
			WebDriverManager.edgedriver().setup();
			 driver = new EdgeDriver();
		}
		
		else {
			System.out.println("Please select the correct browser");
		}
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		return driver;
	    }
		
		@BeforeMethod(alwaysRun = true)
		public  LandingPage openApplication() throws Exception {
        
			driver = driverinit();
			 landingPage = new LandingPage(driver);
			landingPage.goTo();
			return landingPage;
			
        }
		
		@AfterMethod(alwaysRun = true)
		public void closeBrowser() {
			driver.close();
		}
		
		public List<HashMap<String,String>> getJasonToMap() throws IOException
		{
			//Read json to string
			String dataSet = FileUtils.readFileToString(new File(System.getProperty("user.dir")+"\\src\\test\\resources\\PurchaseOrder.json"), StandardCharsets.UTF_8);
			
			//String to map Jackson databind
			ObjectMapper mapper = new ObjectMapper();
			//map to class
			List<HashMap<String,String>> data = mapper.readValue(dataSet, new TypeReference<List<HashMap<String,String>>>(){});
			System.out.println(data);
			return data;
			
		}
		
		public String getScreenshot(String testCaseName, WebDriver driver) throws IOException
	    {
	    	TakesScreenshot ts = (TakesScreenshot)driver;
	    	File source = ts.getScreenshotAs(OutputType.FILE);
	    	File file = new File(System.getProperty("user.dir") + "//reports//" + testCaseName + ".png");
	    	FileUtils.copyFile(source, file);
	    	return System.getProperty("user.dir") + "//reports//" + testCaseName + ".png";
	    }
	
	

}
