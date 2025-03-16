package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("WebDriver Instance Is Created!");
	}

	// Method To Find Element
	public WebElement find(By by) {
		return driver.findElement(by);
	}

	// Method To Click Element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeClickAble(by);
			applyBorder(by, "green");
			find(by).click();
			ExtentManager.logStep("Clicked An Element: " + elementDescription);
			logger.info("Clicked An Element-->" + elementDescription);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable To Click The Element: ",
					elementDescription + "Unable To Click");
			logger.error("Unable To Click The Element: " + e.getMessage());
		}
	}

	// Method To Enter Text
	public void setValue(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			find(by).clear();
			find(by).sendKeys(value);
			logger.info("Value Entered On " + getElementDescription(by) + "---> " + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable To Enter The Value: " + e.getMessage());
		}
	}

	// Wait For Element To Be ClickAble
	private void waitForElementToBeClickAble(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Element Is Not Clickable: " + e.getMessage());
		}
	}

	// Wait For Element To Be Visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element Is Not Visible: " + e.getMessage());
		}
	}

	// Method To Get Text
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Get Text From: " + getElementDescription(by));
			return find(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable To Get Text : " + e.getMessage());
			return "";
		}
	}

	// Method To Compare Two Values -- change return type
	public boolean compareValue(By by, String expectedValue) {
		try {
			waitForElementToBeVisible(by);
			String actualValue = find(by).getText();
			if (expectedValue.equals(actualValue)) {
				applyBorder(by, "green");
				logger.info("Text Are Matching: " + actualValue + " Equals " + expectedValue);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text",
						"Text Verified Successfully! " + actualValue + " Equals " + expectedValue);
				return true;
			} else
				applyBorder(by, "red");
			logger.error("Text Are Not Matching: " + actualValue + " Not Equals " + expectedValue);
			ExtentManager.logFailure(BaseClass.getDriver(), "Texts Are Not Matching",
					"Text Comparison Failed! " + actualValue + " Not Equals " + expectedValue);
			return false;
		} catch (Exception e) {
			logger.error("Unable To Comapare Texts: " + e.getMessage());
		}
		return false;
	}

	/*
	 * Method To Check Element Is Displayed public boolean isDisplayed(By by) { try
	 * { waitForElementToBeVisible(by); boolean isDisplayed =
	 * find(by).isDisplayed(); if (isDisplayed) {
	 * System.out.println("Element Is Displayed"); return isDisplayed; } else {
	 * System.out.println("Element Is Not Displayed"); return isDisplayed; } } catch
	 * (Exception e) {
	 * System.out.println("Element Is Not Displayed:"+e.getMessage()); return false;
	 * } }
	 */

	// Simplified Check Element Visibility Method
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Element Is Displayed: " + getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element Is Displayed ",
					"Element Is Displayed " + getElementDescription(by));
			return find(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Element Is Not Displayed:" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element Is Not Displayed: ",
					"Element Is Not Displayed" + getElementDescription(by));
			return false;
		}
	}

	// Scroll To Element Using JavaScript
	public void scrollToElement(By by) {
		try {
			applyBorder(by, "green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", find(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable To Locate The Element Using JavaScript:" + e.getMessage());
		}
	}

	// Wait For page To Load
	public void waitForPageToLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page Loaded Successfully.");
		} catch (Exception e) {
			logger.error(
					"Page Did Not Loaded Successfully. " + timeOutInSec + " Seconds. Exception: " + e.getMessage());
		}
	}

	// Method To Get The Description Of Element

	public String getElementDescription(By by) {
		// Check For Null Driver Or Locator
		if (driver == null)
			return "Driver Is Null";
		if (by == null)
			return "Locator Is Null";

		try {
			// Find The Element Using The Locator
			WebElement element = find(by);

			// Get Element Attributes
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeHolder = element.getDomAttribute("placeholder");

			// Return Description Based On Element Attribute
			if (isNotEmpty(name)) {
				return "Element With Name: " + name;
			} else if (isNotEmpty(id)) {
				return "Element With Id: " + id;
			} else if (isNotEmpty(text)) {
				return "Element With Text: " + truncate(text, 20);
			} else if (isNotEmpty(className)) {
				return "Element With ClassName: " + className;
			} else if (isNotEmpty(placeHolder)) {
				return "Element With PlaceHolder: " + placeHolder;
			} else {
				// Get XPath of the Element using JavaScript
				JavascriptExecutor js = (JavascriptExecutor) ((WebDriver) driver);
				String xpath = (String) js.executeScript("function getXPath(el) {"
						+ "   if (el.id !== '') return '//*[@id=\"' + el.id + '\"]';"
						+ "   if (el === document.body) return '/html/' + el.tagName.toLowerCase();"
						+ "   var ix = 0, siblings = el.parentNode.childNodes;"
						+ "   for (var i = 0; i < siblings.length; i++) {" + "       var sibling = siblings[i];"
						+ "       if (sibling === el) return getXPath(el.parentNode) + '/' + el.tagName.toLowerCase() + '[' + (ix + 1) + ']';"
						+ "       if (sibling.nodeType === 1 && sibling.tagName === el.tagName) ix++;" + "   }"
						+ "   return '';" + "}" + "return getXPath(arguments[0]);", element);

				return "Element With XPath: " + xpath;
			}
		} catch (Exception e) {
			logger.error("Unable To Describe The Element " + e.getMessage());
		}
		return "Unable To Describe The Element";

	}

	// Utility Method To Check A String Is Not Null Or Empty
	public boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utility Method To Truncate String
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, 20) + "...";
	}

	// Utility Method To Border An Element
	public void applyBorder(By by, String color) {
		try {
			WebElement element = find(by);
			// Apply Border
			String script = "arguments[0].style.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("Applied Border With Color " + color + "To Element " + getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Failed To Apply The Border To An Element: " + getElementDescription(by), e.getMessage());
		}
	}
}
