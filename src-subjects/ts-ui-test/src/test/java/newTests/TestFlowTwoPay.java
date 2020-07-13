package newTests;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestFlowTwoPay {
	private WebDriver driver;
	private String baseUrl;
	private List<WebElement> myOrdersList;

	public static void login(WebDriver driver, String username, String password) {
		driver.findElement(By.id("flow_one_page")).click();
		driver.findElement(By.id("flow_preserve_login_email")).clear();
		driver.findElement(By.id("flow_preserve_login_email")).sendKeys(username);
		driver.findElement(By.id("flow_preserve_login_password")).clear();
		driver.findElement(By.id("flow_preserve_login_password")).sendKeys(password);
		driver.findElement(By.id("flow_preserve_login_button")).click();
	}

	@BeforeClass
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
		baseUrl = "http://127.0.0.1/";
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	// Test Flow Preserve Step 1: - Login
	public void testLogin() throws Exception {
		driver.get(baseUrl + "/");

		// define username and password
		String username = "fdse_microservices@163.com";
		String password = "DefaultPassword";

		// call function login
		login(driver, username, password);
		Thread.sleep(1000);

		// get login status
		String statusLogin = driver.findElement(By.id("flow_preserve_login_msg")).getText();
		if ("".equals(statusLogin))
			System.out.println("Failed to Login! Status is Null!");
		else if (statusLogin.startsWith("Success"))
			System.out.println("Success to Login! Status:" + statusLogin);
		else
			System.out.println("Failed to Login! Status:" + statusLogin);
		Assert.assertEquals(statusLogin.startsWith("Success"), true);
		driver.findElement(By.id("flow_two_page")).click();
	}

	@Test(dependsOnMethods = { "testLogin" })
	public void testViewOrders() throws Exception {
		driver.findElement(By.id("flow_two_page")).click();
		driver.findElement(By.id("refresh_my_order_list_button")).click();
		Thread.sleep(1000);
		// gain my oeders
		myOrdersList = driver.findElements(By.xpath("//div[@id='my_orders_result']/div"));
		if (myOrdersList.size() > 0) {
			System.out.printf("Success to show my orders list，the list size is:%d%n", myOrdersList.size());
		} else
			System.out.println("Failed to show my orders list，the list size is 0 or No orders in this user!");
		Assert.assertEquals(myOrdersList.size() > 0, true);
	}

	@AfterClass
	public void tearDown() throws Exception {
		driver.quit();
	}
}
