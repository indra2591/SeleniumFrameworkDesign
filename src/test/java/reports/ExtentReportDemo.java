package reports;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportDemo {

	public static ExtentReports config() {
		String reportsDir = System.getProperty("user.dir") + File.separator + "reports";
		File dir = new File(reportsDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String path = reportsDir + File.separator + "index.html";
		ExtentSparkReporter reporter = new ExtentSparkReporter(path);
		reporter.config().setReportName("Automation Test Results");
		reporter.config().setDocumentTitle("Test Results");

		ExtentReports extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Tester", "Indra");
		return extent;
	}
}
