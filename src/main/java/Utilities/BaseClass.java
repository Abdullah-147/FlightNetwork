package Utilities;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;

public class BaseClass {

	public String url = "https://www.flightnetwork.com/";
	public String source = "London";
	public String destination = "Frankfurt";
	public String tripType = "One-way";
	public WebDriverWait wait;
	public static WebDriver driver;
	public JavascriptExecutor jse;
	public static ExtentReports reports;
	public String browserName="chrome";
	@BeforeSuite(alwaysRun = true)
	public void setup() {
		reports = new ExtentReports("./ExtentReport.html");
		reports.loadConfig(new File(System.getProperty("user.dir") + "/extent_customization_configs.xml"));
		try {
			FileUtils.forceDelete(new File(System.getProperty("user.dir") + "/ScreenCaptures"));
		} catch (Exception e) {
		}
		;
		File screenshotFolder = new File(System.getProperty("user.dir") + "/ScreenCaptures");
		if (!screenshotFolder.exists()) {
			screenshotFolder.mkdir();
		}
		if (browserName.equals("chrome"))
			driver = new ChromeDriver();
		else if (browserName.equals("firefox"))
			driver = new FirefoxDriver();
		else driver=new EdgeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void searchForAFlight() {
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		jse = (JavascriptExecutor) driver;
		driver.get(url);
		
		//Check if browser landed on correct page.
		Assert.assertTrue(driver.getCurrentUrl().equals(url));
		
		// Decline cookies
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//button[@data-testid='cookieBanner-closeButton']"))).click();

		// Click on one way button
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//span[text()='" + tripType + "']"))))
				.click();

		// Enter From details
		driver.findElement(By.id("searchForm-singleBound-origin-input")).sendKeys(source);

		// wait till dropdown loads
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//span[text()='" + source + " (All Airports)']"))).click();

		// Enter To details
		driver.findElement(By.id("searchForm-singleBound-destination-input")).sendKeys(destination);

		// wait till dropdown loads
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//span[text()='" + destination + " (All Airports)']"))).click();

		// Click on departure date
		driver.findElement(By.id("singleBound.departureDate")).click();

		// select a date
		// Thread.sleep(3000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='29']/ancestor::button")))
				.click();

		// click on search flights
		jse.executeScript("window.scrollBy(0,-200)", "");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Search flights']/parent::button")))
				.click();
		
		//Wait till results page is loaded.
		wait.until(ExpectedConditions.urlContains("result"));
		
		// click on filter by button
		WebElement filterBy = driver.findElement(By.xpath("//span[text()='Filter by']/parent::button"));
		jse.executeScript("window.scrollBy(0,-200)", "");
		filterBy.click();
	}

	@AfterSuite(alwaysRun = true)
	public void tearDown() {
		driver.quit();
	}

	public String screenCapture(String testName, String status) throws IOException {
		File srcfile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File destFile = new File(System.getProperty("user.dir") + "/ScreenCaptures/" + testName
				+ System.currentTimeMillis() + status + ".png");
		FileUtils.copyFile(srcfile, destFile);
		return destFile.getAbsolutePath();
	}

}
