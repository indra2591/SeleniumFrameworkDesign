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
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import biz4group.pages.LandingPage;
import io.github.bonigarcia.wdm.WebDriverManager;

public abstract class BaseTest {
	// Constants for driver configuration
	private static final int MAX_RETRIES = 5;
	private static final int IMPLICIT_WAIT_SECONDS = 10;
	private static final int EXPLICIT_WAIT_SECONDS = 15;
	private static final int WINDOW_WIDTH = 1920;
	private static final int WINDOW_HEIGHT = 1080;
	private static final String CONFIG_FILE_PATH = "//src//main//resources/GloablData.properties";
	private static final String BROWSER_PROPERTY = "browser";
	private static final Logger logger = Logger.getLogger(BaseTest.class.getName());
	
	public WebDriver driver;
	public LandingPage landingPage;
	
	public WebDriver driverinit() throws Exception {
		int retries = MAX_RETRIES;
		Exception lastException = null;
		
		while (retries > 0) {
			try {
				return initializeDriver();
			} catch (Exception e) {
				lastException = e;
				retries--;
				if (retries > 0) {
					long delay = (MAX_RETRIES - retries + 1) * 1000; // Increasing delay: 1s, 2s, 3s, 4s, 5s
					logger.warning("Driver initialization failed. Retrying... (" + retries + " attempts left) - waiting " + delay + "ms");
					Thread.sleep(delay);
					System.gc(); // Force garbage collection to free resources
				}
			}
		}
		
		throw lastException != null ? lastException : new Exception("Failed to initialize driver after " + MAX_RETRIES + " attempts");
	}
	
	private WebDriver initializeDriver() throws Exception {
		Properties prop = new Properties();
		String configPath = System.getProperty("user.dir") + CONFIG_FILE_PATH;
		
		// Use try-with-resources to ensure FileInputStream is properly closed
		try (FileInputStream fis = new FileInputStream(configPath)) {
			prop.load(fis);
		} catch (FileNotFoundException e) {
			logger.severe("Properties file not found at: " + configPath);
			throw new IOException("Configuration file not found: " + configPath, e);
		} catch (IOException e) {
			logger.severe("Error reading properties file: " + e.getMessage());
			throw new IOException("Failed to read configuration file", e);
		}
		
		String browserName = System.getProperty(BROWSER_PROPERTY) != null ? System.getProperty(BROWSER_PROPERTY) : prop.getProperty(BROWSER_PROPERTY);
		
		if (browserName.equalsIgnoreCase("chrome") || browserName.equalsIgnoreCase("chromeheadless")) {
			ChromeOptions options = new ChromeOptions();
			WebDriverManager.chromedriver().setup();
			if (browserName.equalsIgnoreCase("chromeheadless")) {
				options.addArguments("--headless=new");
				options.addArguments("--disable-gpu");
				options.addArguments("--start-maximized");
			}
			driver = new ChromeDriver(options);
			driver.manage().window().setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
			 
		} else if (browserName.equalsIgnoreCase("firefox")) {
			System.out.println("Initializing Firefox driver...");
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			options.setAcceptInsecureCerts(true);
			options.addPreference("dom.disable_beforeunload", true);
			options.addPreference("network.cookie.lifetimePolicy", 0);
			options.addPreference("browser.tabs.drawInTitlebar", true);
			options.addPreference("browser.privatebrowsing.autostart", false);
			options.addPreference("extensions.activeThemeID", "firefox-compact-dark@mozilla.org");
			// For Jenkins CI environment, you can uncomment these if running headless:
			// options.addArgument("--headless");
			// options.addArgument("--start-debugger-server");
			System.out.println("Creating Firefox driver with options...");
			driver = new FirefoxDriver(options);
			System.out.println("Firefox driver initialized successfully");
		} else if (browserName.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else {
			System.out.println("Please select the correct browser");
			throw new Exception("Invalid browser: " + browserName);
		}
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
		driver.manage().window().setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
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
			if (driver != null) {
				try {
					driver.quit();
				} catch (Exception e) {
					System.out.println("Error closing browser: " + e.getMessage());
				}
			}
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
	    	String reportsDir = System.getProperty("user.dir") + File.separator + "reports";
	    	File dir = new File(reportsDir);
	    	if (!dir.exists()) {
	    		dir.mkdirs();
	    	}
	    	TakesScreenshot ts = (TakesScreenshot)driver;
	    	File source = ts.getScreenshotAs(OutputType.FILE);
	    	String destPath = reportsDir + File.separator + testCaseName + ".png";
	    	File file = new File(destPath);
	    	FileUtils.copyFile(source, file);
	    	return destPath;
	    }
	
	

}