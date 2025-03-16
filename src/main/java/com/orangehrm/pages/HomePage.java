package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class HomePage {

	private ActionDriver actionDriver;

	// Define Locators
	private final By adminTab = By.xpath("//span[text()='Admin']");
	private final By userIdButton = By.xpath("//*[@class='oxd-userdropdown-name']");
	private final By logoutButton = By.xpath("//a[text()='Logout']");
	private final By orangeHRMLogo = By.xpath("//img[contains(@src,'orangehrm-logo')]");

	private final By pimTab = By.xpath("//span[text()='PIM']");
	private final By employeeSearch = By
			.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div//input");
	private final By searchButton = By.xpath("//button[@type='submit']");
	private final By employeeFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
	private final By employeeLastName = By.xpath("//div[@class='oxd-table-card']/div/div[4]");

	// Initialize ActionDriver by passing WebDriver Instance
	/*
	 * public HomePage(WebDriver driver) { this.actionDriver = new
	 * ActionDriver(driver); }
	 */

	public HomePage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	// Method To Verify If Admin Tab Is Visible
	public boolean isAdminTabVisible() {
		ExtentManager.logStep("Verifying Admin Tab Is Visible");
		return actionDriver.isDisplayed(adminTab);
	}

	// Method To Verify OrangeHRMLogp
	public boolean isLogoVisible() {
		ExtentManager.logStep("Verifying Logo Is Visible");
		return actionDriver.isDisplayed(orangeHRMLogo);
	}

	// Method To Perform Logout
	public void logout() {
		actionDriver.click(userIdButton);
		actionDriver.click(logoutButton);
		ExtentManager.logStep("Logout SuccessFully!");
	}

	// Method To Navigate PIM Tab
	public void clickOnPIMTab() {
		actionDriver.click(pimTab);
	}

	public void employeeSearch(String value) {
		actionDriver.setValue(employeeSearch, value);
		actionDriver.click(searchButton);
		actionDriver.scrollToElement(employeeFirstAndMiddleName);
	}

	public boolean verifyEmployeeFirstAndMiddleName(String employeeFirstAndMiddleNameDB) {
		return actionDriver.compareValue(employeeFirstAndMiddleName, employeeFirstAndMiddleNameDB);
	}

	public boolean verifyEmployeeLastName(String employeeLastNameDB) {
		return actionDriver.compareValue(employeeLastName, employeeLastNameDB);
	}
}
