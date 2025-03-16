package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	// Initialize Extent Report
	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			extent.attachReporter(spark);
			// Adding System Information
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
		}
		return extent;
	}

	// Start The Test
	public synchronized static ExtentTest startTest(String name) {
		ExtentTest extentTest = getReporter().createTest(name);
		test.set(extentTest);
		return extentTest;
	}

	// End The Test
	public synchronized static void endTest() {
		getReporter().flush();
	}

	// Get current Thread's Test
	public synchronized static ExtentTest getTest() {
		return test.get();
	}

	// Method To Get The Name Of Current Test
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		if (currentTest != null) {
			return currentTest.getModel().getName();
		} else {
			return "No Test Is Currently Active For The Test";
		}
	}

	// Method To Log Step
	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}

	// Method To Validate With Screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenshotMessage) {
		getTest().pass(logMessage);
		// Screenshot Method
		attachScreenshot(driver, screenshotMessage);
	}

	// Method To Validate API
		public static void logStepAPI(String logMessage) {
			getTest().pass(logMessage);
		}
		
	// Method To Validate API Failure 
	public static void logFailureAPI(String logMessage) {
		String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
		getTest().fail(colorMessage);
	}
	
	// Method To Validate Failure With Screenshot
		public static void logFailure(WebDriver driver, String logMessage, String screenshotMessage) {
			String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
			getTest().fail(colorMessage);
			// Screenshot Method
			attachScreenshot(driver, screenshotMessage);
		}

	// Method To Skip
	public static void logSkip(String logMessage) {
		String colorMessage = String.format("<span style='color:yellow;'>%s</span>", logMessage);
		getTest().skip(colorMessage);
	}

	// Take Screenshot With Date And Time
	public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		// Format Date And Time
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		// Saving The Screenshot To A File
		String destPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/" + screenshotName + "_"
				+ timeStamp + ".png";
		File dest = new File(destPath);
		try {
			FileUtils.copyFile(src, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Convert Screenshot To Base64 For Embedding In The Report
		String base64Format = convertToBase64(src);
		return base64Format;
	}

	// Convert Screenshot To Base64
	public static String convertToBase64(File screenshot) {
		String base64 = "";
		// Read File Content InTo Byte Array
		byte[] fileContent;
		try {
			fileContent = FileUtils.readFileToByteArray(screenshot);
			// Convert The Byte To Base64 String
			base64 = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base64;
	}

	// Attach Screenshot To Report
	public synchronized static void attachScreenshot(WebDriver driver, String message) {
		try {
			String screenshotBase64 = takeScreenshot(driver, getTestName());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder
					.createScreenCaptureFromBase64String(screenshotBase64).build());
		} catch (Exception e) {
			getTest().fail("Failed To Attach The Screenshot");
			e.printStackTrace();
		}
	}

	// Register WebDriver For Current Thread
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}
}
