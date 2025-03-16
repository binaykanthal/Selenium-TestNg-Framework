package com.orangehrm.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyTest extends BaseClass {

	@Test
	public void dummyTest() {
		//ExtentManager.startTest("Dummy Test"); -- TestListener
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verifying The Titel");
		assert title.equals("OrangeHRM") : "Test Failed: Title Is Not Matching";

		System.out.println("Test passed: Title Is Matching --" + title);
		ExtentManager.logStep("Validation Successful");
	}
}
