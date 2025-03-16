package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

public class TestListener implements ITestListener,IAnnotationTransformer{

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

	//Trigger When Test Start
	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		//Starts Logging In Extent Report
		ExtentManager.startTest(testName);
		ExtentManager.logStep("🟢 Test Started:"+testName);
	}

	//Trigger When Test Succeeds
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed SuccessFully ", "Test End: " + testName + " - ✅ Test Passed");
		}else {
			ExtentManager.logStepAPI("Test End:  "+ testName +"  - ✅ Test Passed");
		}
		
	}

	//Trigger When Test Fails
	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String errorMessage = result.getThrowable().getMessage();
		ExtentManager.logStep(errorMessage);
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed ", "Test End: " + testName + " - ❌ Test Failed");
		}else {
			ExtentManager.logFailureAPI("Test End: " + testName + " - ❌ Test Failed");
		}
		
	}

	//Trigger When Test Skipped
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("Test Skipped "+testName);
	}

	//Trigger When Suite Start
	@Override
	public void onStart(ITestContext context) {
		//Initialize Extent Report
		ExtentManager.getReporter();
	}

	// Trigger When Suite End
	@Override
	public void onFinish(ITestContext context) {
		// Flush The Report
		ExtentManager.endTest();
	}

	
}
