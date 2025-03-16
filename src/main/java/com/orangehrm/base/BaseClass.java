package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	// protected static WebDriver driver;
	// private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}
	
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	// Load Configuration File
	@BeforeSuite
	public void loadConfig() throws IOException {
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties File Loaded ");

		// Start The ExtentReport
		//ExtentManager.getReporter(); -- This Implemented IN TestListener
	}


	/**
     * Setting up driver before every method
     *
     * The browser name (chrome, firefox, edge)
     */
	@BeforeMethod
	public synchronized void setUp() {
		logger.info("Setting Up WebDriver For:" + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2);

		logger.info("WebDriver Initialized And Browser Maximized ");
		logger.trace("This Is A Trace Message");
		logger.error("This Is A Error Message");
		logger.warn("This Is A Warn Message");
		logger.debug("This Is A Debug Message");
		logger.fatal("This Is A Fatal Message");

		/*
		 * //Initialize The ActionDriver Once if(actionDriver==null) { actionDriver =
		 * new ActionDriver(driver);
		 * logger.info("ActionDriver Instance Is Created."+Thread.currentThread().getId(
		 * )); }
		 */

		// Initialize ActionDriver For Current Thread
		actionDriver.set(new ActionDriver(driver.get()));
		logger.info("ActionDriver Initialized For Thread: " + Thread.currentThread().getId());

	}

	// Quit WebDriver Instance
	@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable To Quit The Browser :" + e.getMessage());
			}
		}
		logger.info("Webdriver Instance Is Closed");
		// driver = null;
		// actionDriver = null;
		driver.remove();
		actionDriver.remove();
		//ExtentManager.endTest(); -- TestListener
		
	}

	// Initialize WebDriver
	private synchronized void launchBrowser() {
		String browser = prop.getProperty("browser");
		if (browser.equalsIgnoreCase("chrome")) {
			
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless"); // Run Chrome in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU for headless mode
			//options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments
			
			// driver = new ChromeDriver();
			driver.set(new ChromeDriver(options));
			ExtentManager.registerDriver(getDriver());// New Changes As per Thread
			logger.info("ChromeDriver Instance Is Created");
		} else if (browser.equalsIgnoreCase("firefox")) {
			// driver = new FirefoxDriver();
			
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless"); // Run Firefox in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU rendering (useful for headless mode)
			options.addArguments("--width=1920"); // Set browser width
			options.addArguments("--height=1080"); // Set browser height
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD environments
			options.addArguments("--disable-dev-shm-usage"); // Prevent crashes in low-resource environments
			
			driver.set(new FirefoxDriver(options));
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver Instance Is Created");
		} else if (browser.equalsIgnoreCase("edge")) {
			// driver = new EdgeDriver();
			
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless"); // Run Edge in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU acceleration
			options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable pop-up notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD
			options.addArguments("--disable-dev-shm-usage"); // Prevent resource-limited crashes
			
			driver.set(new EdgeDriver(options));
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver Instance Is Created");
		} else {
			throw new IllegalArgumentException("Browser Not Supported:" + browser);
		}
	}

	// Implicit Wait, Maximize The Driver, Navigate to URL
	private void configureBrowser() {
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		getDriver().manage().window().maximize();
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			logger.error("Failed To Navigate To The URL :" + e.getMessage());
		}
	}

	// Static Wait
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

	/*
	 * // Driver Getter Method public WebDriver getDriver() { return driver; }
	 * 
	 * // Driver Setter Method public void setDriver(ThreadLocal<WebDriver> driver)
	 * { this.driver = driver; }
	 */

	// Getter Method For Driver
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			logger.error("WebDriver Is Not Initialized");
			throw new IllegalStateException("WebDriver Is Not Initialized");
		}
		return driver.get();
	}

	// Getter Method For ActionDriver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			logger.error("ActionDriver Is Not Initialized");
			throw new IllegalStateException("ActionDriver Is Not Initialized");
		}
		return actionDriver.get();
	}

	// Properties Getter Method
	public static Properties getProp() {
		return prop;
	}
}
