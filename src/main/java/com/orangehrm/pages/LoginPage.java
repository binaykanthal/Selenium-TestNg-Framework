package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class LoginPage {

	private ActionDriver actionDriver;

	// Define Locators
	private final By userNameField = By.xpath("//input[@name='username']");
	private final By passwordField = By.xpath("//input[@name='password']");
	private final By loginButton = By.xpath("//button[text()=' Login ']");
	private final By errorMessage = By.xpath("//*[text()='Invalid credentials']");

	// Initialize ActionDriver by passing WebDriver Instance
	/*public LoginPage(WebDriver driver) {
		this.actionDriver = new ActionDriver(driver);
	}
	*/
	
	public LoginPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	// Method To Perform Login
	public void login(String userName, String password) {
		ExtentManager.logStep("Navigating To Login Page Entering UserName And Password");
		actionDriver.setValue(userNameField, userName);
		actionDriver.setValue(passwordField, password);
		actionDriver.click(loginButton);
	}

	// Method To Check If ErroMessage Is DisPlayed
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}

	// Method To Get The ErrorMessage
	public String getErrorMessage() {
		return actionDriver.getText(errorMessage);
	}

	// Verify If Error Message
	public boolean verifyErrorMessage(String expectedMessage) {
		return actionDriver.compareValue(errorMessage, expectedMessage);
	}

}
