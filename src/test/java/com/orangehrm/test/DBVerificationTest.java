package com.orangehrm.test;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass{

	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setUpPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	
	@Test(dataProvider ="emplVerification", dataProviderClass = DataProviders.class)
	public void verifyEmployeeNameFromDB(String emp_id, String emp_name) {
		SoftAssert softAssert = getSoftAssert();
		ExtentManager.logStep("Logging With Admin Credential.");
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		ExtentManager.logStep("Click On PIM Tab.");
		homePage.clickOnPIMTab();
		ExtentManager.logStep("Search For Employee");
		staticWait(2);
		homePage.employeeSearch(emp_name);
		
		ExtentManager.logStep("Get Employee Name From DB");
		String employee_id=emp_id;
		Map<String,String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);
		String empFirstName = employeeDetails.get("firstName");
		String empMiddleName = employeeDetails.get("middleName");
		String empLastName = employeeDetails.get("lastName");
		
		String empFirstAndMiddleName = (empFirstName+" "+empMiddleName).trim();
		ExtentManager.logStep("Verify The Employee First And Middle Name");
		softAssert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(empFirstAndMiddleName),
				"First And Middle Name Are Not Matching");
		ExtentManager.logStep("Verify The Employee Last Name");
		softAssert.assertTrue(homePage.verifyEmployeeLastName(empLastName),
				"Last Name Are Not Matching");
		ExtentManager.logStep("DataBase Validation Completed");
		softAssert.assertAll();
	}
}
