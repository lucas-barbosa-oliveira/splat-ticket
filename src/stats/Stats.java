package stats;

import java.util.ArrayList;
import java.util.List;

import experiment.TestResults;

public class Stats {
	
	List<TestResults> results;
	
	public Stats(){
		results = new ArrayList<TestResults>();
	}
	
	public void add(TestResults result){
		this.results.add(result);
	}
	
	public void addAll(List<TestResults> results){
		this.results.addAll(results);
	}
	
	public List<TestResults> getTestResults(){
		return this.results;
	}
	
	public String toString(){
		String out = "Test\tTime\t#Confs\t#F\t#Bugs\tTTFF\tCTFF \n";
		int counter = 0;
		for (TestResults t_res : results) {
			out += "T" + ++counter + "\t" + t_res.print();
		}
		return out;
	}

}
