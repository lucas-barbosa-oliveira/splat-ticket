package newTests;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestServiceLogin {
	
	@BeforeClass
	public void setUp() throws Exception {
			
	}

	@Test
	public void testSignIn() {
		Assert.assertTrue(true);
	}

	@AfterClass
	public void tearDown() throws Exception {
		
	}
}
