package Utilities;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Listeners extends BaseClass implements ITestListener {

	static Logger logger = LogManager.getLogger(Listeners.class);
	ExtentTest test;

	@Override
	public void onStart(ITestContext c) {
		logger.info("Test Suite Begins at " + java.time.LocalDateTime.now());
	}

	@Override
	public void onTestStart(ITestResult result) {
		test = reports.startTest(result.getName());
		logger.info("Test Case "+result.getName()+" started at " + java.time.LocalDateTime.now());
	}

	@Override
	public void onTestFailure(ITestResult result) {
		try {
			test.log(LogStatus.FAIL, test.addScreenCapture(screenCapture(BaseClass.driver, result.getName(), "FAIL"))
					+ result.getName() + " failed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.error(result.getName() + "- This test case failed!");
		reports.endTest(test);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		try {
			test.log(LogStatus.PASS, test.addScreenCapture(screenCapture(BaseClass.driver, result.getName(), "PASS"))
					+ result.getName() + " passed.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info(result.getName() + "- This test case passed!");
		reports.endTest(test);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		test.log(LogStatus.SKIP, result.getName() + " skipped.");
		reports.endTest(test);
		logger.warn(result.getName() + "- This test case was skipped.");
	}
	
	@Override
	public void onFinish(ITestContext c) {
		reports.flush();
		logger.info("Test Suite Ends at " + java.time.LocalDateTime.now());
	}

	public String screenCapture(WebDriver driver, String testName, String status) throws IOException {
		File srcfile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File destFile = new File("./ScreenCaptures/" + testName +"_"+ System.currentTimeMillis() + "_"+status + ".png");
		FileUtils.copyFile(srcfile, destFile);
		return destFile.getAbsolutePath();
	}
}
