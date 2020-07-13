package utils;

import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

public class CheckAlert {
	
	public static void occurrs(WebDriver driver) { 
	    try { 
	        driver.switchTo().alert().accept(); 
	    } 
	    catch (NoAlertPresentException Ex) { 
	    }    
	} 	
}
