package newTests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TestFlowTwoRebook {
	private WebDriver driver;
	private String baseUrl;
	private String trainType;// 0--all,1--GaoTie,2--others
	private List<WebElement> myOrdersList;
	private List<WebElement> changeTicketsSearchList;

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
		trainType = "0";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testViewOrders() throws Exception {

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
