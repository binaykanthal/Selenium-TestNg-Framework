package com.orangehrm.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyTest2 extends BaseClass {

	@Test
	public void dummyTest() {
		//ExtentManager.startTest("Dummy2 Test"); -TestListener
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verifying The Titel");
		assert title.equals("OrangeHRM") : "Test Failed: Title Is Not Matching";

		System.out.println("Test passed: Title Is Matching --" + title);
		//ExtentManager.logSkip("This Test Case Is Skipped");
		//throw new SkipException("Skipping The Test As Part Of Testing");
		
	}
}
