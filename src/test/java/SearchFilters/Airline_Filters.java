package SearchFilters;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import Utilities.BaseClass;

public class Airline_Filters extends BaseClass {
	WebDriver driver;

	
	/* TestCase 07	: The Airlines filter should be having all values selected by default
	 * Test Case Id	: TC_07
	 * Test Steps	:	1. Given user is on the flight details page and clicks on the filter by button
						2. Airlines filter is displayed to the user
	 * */
	@Test(description = "All airline filters should be selected by default")
	public void testCase07() {
		
		//Driver Instantiation
		driver = getDriver();
		
		//Search for A flight
		searchForAFlight();
		
		//Wait until airline filter options are displayed
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//div[@data-testid='resultPage-AIRLINESFilter-content']")));
		
		// Check if all options are selected.
		List<WebElement> airlineOptions = driver
				.findElements(By.xpath("//div[@data-testid='resultPage-AIRLINESFilter-content']//ul//li//input"));
		
		for (WebElement option : airlineOptions) {
			Assert.assertTrue(option.getText()+" is not selected by default. Please review",option.isSelected());
		}
	}

	/* TestCase 08	: When user selects any one airline, flight details of only this selected airline should be displayed
	 * TestCase Id	: TC_08
	 * Test Steps	: 	1. Given user is on the flight details page and clicks on the filter by button
						2. Under the airlines filter, user clicks on clear all button
						3. User selects any one airline from the available option
						4. User clicks on done button
	 */
	@Test(description = "When user selects any one airline, flight details of only this selected airline should be displayed")
	public void testCase08() {
		
		//Click on 'Clear all' button.
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Clear all']"))).click();
		
		//Select any one of the available Airlines.
		List<WebElement> airlineOptions = driver.findElements(By.xpath("//label[contains(@for,'airlines')]"));
		for (WebElement option : airlineOptions) {
			if (option.getText().equals("Finnair")) {
				option.click();
				break;
			}
		}
		
		//Click on Done button
		wait.until(ExpectedConditions
				.visibilityOf(driver.findElement(By.xpath("//button[@data-testid='filtersForm-applyFilters-button']"))))
				.click();

		jse.executeScript("window.scrollBy(0,-250)", "");

		// Check if displayed results match the filtered flight
		List<WebElement> airlineNames = driver.findElements(By.xpath("//div[@class='css-akpcxl e16bcfpx4']"));
		for (WebElement name : airlineNames) {
			Assert.assertTrue("Incorrect flight details are shown. Please review.", name.getText().equals("Finnair"));
			}
	}

	/* TestCase 09	: When user selects none of the available flights, no results should be displayed to the user
	 * TestCase Id	: TC_09
	 * Test Steps	: 	1. Given user is on the flight details page and clicks on the filter by button
						2. Under the airlines filter, user clicks on clear all button
						3. User clicks on done button
	 */
	@Test(description = "When user selects none of the available flights, no results should be displayed to the user")
	public void testCase09() {
		
		//Click on the Filter By button
		driver.findElement(By.xpath("//span[text()='Filter by']/parent::button")).click();
		
		//Click on Clear All button
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Clear all']"))).click();
		
		//Click on 'Done' button
		wait.until(ExpectedConditions
				.visibilityOf(driver.findElement(By.xpath("//button[@data-testid='filtersForm-applyFilters-button']"))))
				.click();

		jse.executeScript("window.scrollBy(0,-250)", "");
		
		//Check if any results are displayed when no flights are selected
		List<WebElement> airlineNames = driver
				.findElements(By.xpath("//*[contains(@data-testid,'resultPage-resultTrip')]"));
		
		Assert.assertEquals("Airline filter did not work. Search results are available although no airline is selected.",0,airlineNames.size());
	}

	/* TestCase 10	: To verify the 'select all' and 'clear all' buttons of Airlines filter are working correctly
	 * TestCase Id	: TC_10
	 * Test Steps	: 	1. Given user is on the flight details page and clicks on the filter by button
						2. Under Airlines filter, user clicks on clear all button
						3. User clicks on select all button
	 */
	@Test(description="To verify the 'select all' and 'clear all' buttons of Airlines filter are working correctly")
	public void testCase10() {
		//Click on 'Filter by' button
		driver.findElement(By.xpath("//span[text()='Filter by']/parent::button")).click();

		//Click on 'Clear All' button
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Clear all']"))).click();

		//Check if all options are deselected
		List<WebElement> airlineOptions1 = driver
				.findElements(By.xpath("//div[@data-testid='resultPage-AIRLINESFilter-content']//ul//li//input"));
		for (WebElement option : airlineOptions1) {
			if (option.isSelected()) {
				System.out.println(option.getText() + " is selected.");
			}
		}
		//Now, click on the 'Select all' button
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Select all']"))).click();
		
		//Check if all options are selected.
		List<WebElement> airlineOptions2 = driver
				.findElements(By.xpath("//div[@data-testid='resultPage-AIRLINESFilter-content']//ul//li//input"));
		for (WebElement option : airlineOptions2) {
			Assert.assertTrue(option.isSelected());
		}
	}
}
