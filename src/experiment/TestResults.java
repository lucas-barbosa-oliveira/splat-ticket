package experiment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.runner.notification.Failure;

import splat.SPLat.Result;
import util.Operation;

public class TestResults implements Serializable{

	String testname;
	List<SPLatResult> results;
	long total_time = 0;
	int num_failures = 0;
	List<Class> bugs = new ArrayList<Class>();
	long ttff = 0;// time to find 1st failure
	int ctff = 0;// confs to find 1st failure

	public TestResults(String tname) {
		this.testname = tname;
		this.results = new ArrayList<SPLatResult>();
	}

	public void add(SPLatResult r) {
		this.results.add(r);
	}

	public List<SPLatResult> getTestResults() {
		return this.results;
	}

	public String getTestname() {
		return testname;
	}

	public List<SPLatResult> getResults() {
		return results;
	}

	public long getTotal_time() {
		return total_time;
	}
	
	public void setTotalTime(long totalTime) {
		this.total_time = totalTime;
	}

	public int getNum_confs() {
		return results.size();
	}

	public int getNum_failures() {
		return num_failures;
	}

	public List<Class> getBugs() {
		return bugs;
	}

	public long getTtff() {
		return ttff;
	}

	public int getCtff() {
		return ctff;
	}
	
	/**
	 * Calculating reachable configurations from this test result.
	 */
	public Set<String> calculateReachableConfs(){
		Set<String> reachableConfs = new TreeSet<String>();
		for (SPLatResult res : results) {
			String conf = res.getConfiguration().toString();
			reachableConfs.add(conf);
		}
		return reachableConfs;
	}

	public void calculateStats() {
		boolean firstFailure = false;
		for (SPLatResult res : results) {
			Result r = res.getTestResult();
			total_time += r.getExecTime();
//			Failure f = r.getFailure();
//			Throwable throwable = f == null ? null : f.getException();
			Throwable throwable = r.getFailure();
			
			if (!firstFailure) {
				ttff += r.getExecTime();
				ctff++;
			}
			
			if (throwable != null) {
				Class bug = throwable.getClass();
				if(bug.getName().contains("RuntimeException"))//"NOT SAMPLE"
					continue;
				if (!bugs.contains(bug))
					bugs.add(bug);
				num_failures++;
				
				firstFailure = true;
			}
		}
		if (!firstFailure) {
			ttff = 0;
			ctff = 0;
		}
	}

	public String print() {
		calculateStats();
		int numConfs = results.size();
		int numBugs = bugs.size();
		String out = 
				Operation.truncateDecimal((double)total_time/1000000, 2) + " & "
				+ numConfs + " & "
				+ num_failures + " & "
				+ numBugs + " & "
				+ Operation.truncateDecimal((double)ttff/1000000, 2) + " & "
				+ ctff;
		return out;
	}

}
