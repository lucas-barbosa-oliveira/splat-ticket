package newTests;

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

public class Main {
	
//	TestServiceLogin1.class, 
//	TestServiceLogin2.class, 
//	TestServiceLogin3.class, 
//	TestServiceLogin4.class

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		List<ITestResult> results = runTest(TestServiceLogin.class);
		
		for (ITestResult result : results) {
			System.out.println(result.toString());
			if(result.getStatus() == result.FAILURE){
				System.out.println(result.getThrowable().toString());
			}
		}
		
	}
	
	private static List<ITestResult> runTest(Class test) {
		List<ITestResult> results = new ArrayList<ITestResult>();
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		//, TestFlowTwoPay.class
		testng.setTestClasses(new Class[] { test });
		testng.addListener(tla);
		testng.run();
		
		if(!tla.getPassedTests().isEmpty())
			results = tla.getPassedTests();
		else results = tla.getFailedTests();
		
		return results;
	}
	
//	private void runTest(Class cls, List<Integer> list, List<Integer> expected) {
//		TestNG m_testNg = new TestNG();   
//		m_testNg.setTestClasses(new Class[] {cls});
//		m_testNg.run();
//		Assert.assertEquals(list, expected);
//		
//		}

}
