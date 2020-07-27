package experiment;

import java.util.List;

import org.testng.ITestResult;

public interface IResult {
	String toString();

	List<ITestResult> getTestResults();

	Throwable getFailure();

	void setExecTime(long time);

	long getExecTime();
}
