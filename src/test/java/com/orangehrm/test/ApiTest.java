package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;

import io.restassured.response.Response;

public class ApiTest {

	@Test
	public void verifyGetUserApi() {
		String endPoint ="https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("Api EndPoint: "+ endPoint);
		
		ExtentManager.logStep("Sending GET Request Of The Api");
		Response response = ApiUtility.sendGetRequest(endPoint);
		
		ExtentManager.logStep("Validating Api Response Status Code");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);
		
		if(isStatusCodeValid) {
			ExtentManager.logStepAPI("Status Code Validation Passed.");
		}
		else {
			ExtentManager.logFailureAPI("Status Code Validation Failed!");
		}
		Assert.assertTrue(isStatusCodeValid,"Status Code Is Not as Expected.");
		
		ExtentManager.logStep("Validating Response Body For UserName ");
		String userName = ApiUtility.getJsonValue(response, "username");
		boolean isUserNamerValid = "Bret".equals(userName);
		
		if(isUserNamerValid) {
			ExtentManager.logStepAPI("UserName Validation Passed.");
		}
		else {
			ExtentManager.logFailureAPI("UserName Validation Failed!");
		}
		Assert.assertTrue(isUserNamerValid,"UserName Do Not Match");
		
		ExtentManager.logStep("Validating Response Body For Email ");
		String email = ApiUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Sincere@april.biz".equals(email);
		
		if(isEmailValid) {
			ExtentManager.logStepAPI("Email Validation Passed.");
		}
		else {
			ExtentManager.logFailureAPI("Email Validation Failed!");
		}
		Assert.assertTrue(isEmailValid,"Email Do Not Match");
		
	}
}
