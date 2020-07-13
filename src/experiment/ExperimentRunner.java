package experiment;

import java.util.ArrayList;
import java.util.List;

import backtracker.Backtracker;
import splat.SPLat;
import splat.Variables;
import stats.Stats;
import trie.TrieST;

public class ExperimentRunner {
	
	public static List<TestResults> runExp(String[] input, Variables var) throws Exception {
		List<TestResults> resultsSplat = new ArrayList<TestResults>();
		SPLat splat = new SPLat(var);
		TrieST<Boolean> generalTrie = new TrieST<Boolean>();
		Stats stats = splat.run(input, new Backtracker(true, true, generalTrie), null);
		resultsSplat = stats.getTestResults();
		return resultsSplat;
	}


}
