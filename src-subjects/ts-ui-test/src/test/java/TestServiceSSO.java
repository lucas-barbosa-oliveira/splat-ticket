
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

//import static org.junit.Assert.assertEquals;


public class TestServiceSSO {
    private WebDriver driver;
    private String baseUrl;
    @BeforeClass
    public void setUp() throws Exception {
    	System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
    	baseUrl = "http://127.0.0.1/";
    	driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    @Test
    public void testSSOAccount() throws Exception {
        driver.get(baseUrl + "/");
        driver.findElement(By.id("refresh_account_button")).click();
        driver.findElement(By.id("refresh_login_account_button")).click();
    }
    @AfterClass
    public void tearDown() throws Exception {
        driver.quit();
    }
}

