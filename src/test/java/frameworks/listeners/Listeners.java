package frameworks.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import frameworks.base.BaseTest;
import reports.ExtentReportDemo;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;

public class Listeners extends BaseTest implements ITestListener  {
	ExtentTest test;
	ExtentReports extent = ExtentReportDemo.config();
	ThreadLocal <ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
	@Override
	public void onTestStart(ITestResult result) {
		
		test = extent.createTest(result.getMethod().getMethodName());
		extentTest.set(test);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		
		test.log(Status.PASS, "Test Passed");
		
	}
	
	@Override
	public void onTestFailure(ITestResult result) {
		test.log(Status.FAIL, "Test Failed");
	    extentTest.get().fail(result.getThrowable());
	    WebDriver driver = null;
	    try {
	        // Try to get driver from the test instance or its superclass
	        try {
	            driver = (WebDriver) result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());
	        } catch (NoSuchFieldException e) {
	            // Try superclass if not found
	            driver = (WebDriver) result.getInstance().getClass().getSuperclass().getDeclaredField("driver").get(result.getInstance());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    String filePath = null;
	    if (driver != null) {
	        try {
	            filePath = getScreenshot(result.getMethod().getMethodName(), driver);
	            test.addScreenCaptureFromPath(filePath, result.getMethod().getMethodName());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    } else {
	        test.log(Status.WARNING, "WebDriver was null, screenshot not captured.");
	    }
	}
	

	@Override
	public void onTestSkipped(ITestResult result) {}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

	@Override
	public void onStart(ITestContext context) {}

	@Override
	public void onFinish(ITestContext context) {
		extent.flush();
	}

}
