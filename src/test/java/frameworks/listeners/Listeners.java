package frameworks.listeners;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import frameworks.base.BaseTest;
import reports.ExtentReportDemo;

/**
 * TestNG Listener for test execution tracking and Extent Report generation.
 * Captures test results, failures with screenshots, and generates HTML reports.
 */
public class Listeners extends BaseTest implements ITestListener {
	private static final Logger logger = Logger.getLogger(Listeners.class.getName());
	
	private ExtentTest test;
	private final ExtentReports extent = ExtentReportDemo.config();
	private final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
	
	/**
	 * Called when a test starts execution.
	 * Creates a new test entry in the Extent Report.
	 */
	@Override
	public void onTestStart(ITestResult result) {
		try {
			logger.info("Test Started: " + result.getMethod().getMethodName());
			test = extent.createTest(result.getMethod().getMethodName());
			extentTest.set(test);
		} catch (Exception e) {
			logger.severe("Error during onTestStart: " + e.getMessage());
			throw new RuntimeException("Failed to start test reporting", e);
		}
	}

	/**
	 * Called when a test passes successfully.
	 * Logs test pass status in Extent Report.
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		try {
			logger.info("Test Passed: " + result.getMethod().getMethodName());
			if (extentTest.get() != null) {
				extentTest.get().log(Status.PASS, "Test Passed Successfully");
			}
		} catch (Exception e) {
			logger.severe("Error logging test pass: " + e.getMessage());
		}
	}
	
	/**
	 * Called when a test fails.
	 * Logs failure details and captures screenshot in Extent Report.
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		try {
			logger.severe("Test Failed: " + result.getMethod().getMethodName());
			
			if (extentTest.get() != null) {
				extentTest.get().log(Status.FAIL, "Test Failed");
				extentTest.get().fail(result.getThrowable());
			}
			
			// Attempt to capture screenshot on failure
			WebDriver driver = getDriverFromTestInstance(result);
			if (driver != null) {
				captureScreenshotOnFailure(result, driver);
			} else {
				logger.warning("WebDriver instance not found - screenshot not captured");
				if (extentTest.get() != null) {
					extentTest.get().log(Status.WARNING, "WebDriver was null, screenshot not captured.");
				}
			}
		} catch (Exception e) {
			logger.severe("Error during test failure handling: " + e.getMessage());
		}
	}
	
	/**
	 * Attempts to retrieve WebDriver from test instance using reflection.
	 * Searches in test class and its superclass.
	 * 
	 * @param result The test result containing test instance
	 * @return WebDriver instance if found, null otherwise
	 */
	private WebDriver getDriverFromTestInstance(ITestResult result) {
		try {
			// Try to get driver from the test class
			try {
				return (WebDriver) result.getTestClass().getRealClass()
						.getDeclaredField("driver")
						.get(result.getInstance());
			} catch (NoSuchFieldException e) {
				logger.info("Driver field not found in test class, checking superclass");
				// Try superclass if not found in test class
				return (WebDriver) result.getInstance().getClass()
						.getSuperclass()
						.getDeclaredField("driver")
						.get(result.getInstance());
			}
		} catch (NoSuchFieldException e) {
			logger.warning("Driver field not found in test class or superclass");
			return null;
		} catch (IllegalAccessException e) {
			logger.warning("Cannot access driver field due to access restrictions: " + e.getMessage());
			return null;
		} catch (Exception e) {
			logger.warning("Error retrieving WebDriver instance: " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Captures screenshot on test failure and adds to Extent Report.
	 * 
	 * @param result The test result
	 * @param driver The WebDriver instance
	 */
	private void captureScreenshotOnFailure(ITestResult result, WebDriver driver) {
		try {
			String testMethodName = result.getMethod().getMethodName();
			String filePath = getScreenshot(testMethodName, driver);
			
			if (filePath != null && !filePath.isEmpty()) {
				logger.info("Screenshot captured at: " + filePath);
				if (extentTest.get() != null) {
					extentTest.get().addScreenCaptureFromPath(filePath, testMethodName);
				}
			} else {
				logger.warning("Screenshot file path is null or empty");
			}
		} catch (IOException e) {
			logger.severe("Failed to capture screenshot: " + e.getMessage());
			if (extentTest.get() != null) {
				extentTest.get().log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
			}
		} catch (Exception e) {
			logger.severe("Unexpected error during screenshot capture: " + e.getMessage());
		}
	}

	/**
	 * Called when a test is skipped.
	 */
	@Override
	public void onTestSkipped(ITestResult result) {
		try {
			logger.info("Test Skipped: " + result.getMethod().getMethodName());
			if (extentTest.get() != null) {
				extentTest.get().log(Status.SKIP, "Test Skipped");
			}
		} catch (Exception e) {
			logger.severe("Error logging skipped test: " + e.getMessage());
		}
	}

	/**
	 * Called when a test fails but within success percentage.
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		logger.info("Test failed but within success percentage: " + result.getMethod().getMethodName());
	}

	/**
	 * Called when test suite starts.
	 */
	@Override
	public void onStart(ITestContext context) {
		logger.info("Test Suite Started: " + context.getName());
	}

	/**
	 * Called when test suite finishes.
	 * Flushes Extent Report and cleans up ThreadLocal.
	 */
	@Override
	public void onFinish(ITestContext context) {
		try {
			logger.info("Test Suite Finished: " + context.getName());
			if (extent != null) {
				extent.flush();
				logger.info("Extent Report flushed successfully");
			}
		} catch (Exception e) {
			logger.severe("Error flushing Extent Report: " + e.getMessage());
		} finally {
			// Clean up ThreadLocal to prevent memory leaks
			extentTest.remove();
		}
	}
}
