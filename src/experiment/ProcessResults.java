package experiment;

import java.io.PrintStream;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Processes results of SPLat executions for each mode: SPLAT, RANDOM,
 * ONE_ENABLED, ONE_DISABLED, MOST_ENABLED_DISABLED, ...
 * 
 * @author sabrinasouto
 *
 */
public class ProcessResults {

	PrintStream log;
	List<TestResults> resultsSplat, resultsOneEnabled, resultsOneDisabled, resultsRandom1, resultsRandom2,resultsRandom3,
	resultsRandom4, resultsRandom5, resultsRandom6, resultsRandom7, resultsRandom8, resultsRandom9, resultsRandom10,
	resultsMostEnabledDisabled, resultsCombination, resultsPairwise;
	double[] totalSplat, totalOneEnabled, totalOneDisabled, totalRandom1, totalRandom2, totalRandom3, totalRandom4, totalRandom5, 
	totalRandom6, totalRandom7, totalRandom8, totalRandom9, totalRandom10, totalMostEnabledDisabled, totalCombination, totalPairwise;

	public ProcessResults(PrintStream logFile, List<TestResults> resultsSplat, 
			List<TestResults> resultsOneEnabled, List<TestResults> resultsOneDisabled, 
			List<TestResults> resultsRandom1, List<TestResults> resultsRandom2, List<TestResults> resultsRandom3, 
			List<TestResults> resultsRandom4, List<TestResults> resultsRandom5, List<TestResults> resultsRandom6, 
			List<TestResults> resultsRandom7, List<TestResults> resultsRandom8, List<TestResults> resultsRandom9, 
			List<TestResults> resultsRandom10, List<TestResults> resultsMostEnabledDisabled, 
			List<TestResults> resultsCombination, List<TestResults> resultsPairwise) {
		this.log = logFile;
		this.resultsSplat = resultsSplat;
		this.resultsOneEnabled = resultsOneEnabled;
		this.resultsOneDisabled = resultsOneDisabled;
		this.resultsRandom1 = resultsRandom1;
		this.resultsRandom2 = resultsRandom2;
		this.resultsRandom3 = resultsRandom3;
		this.resultsRandom4 = resultsRandom4;
		this.resultsRandom5 = resultsRandom5;
		this.resultsRandom6 = resultsRandom6;
		this.resultsRandom7 = resultsRandom7;
		this.resultsRandom8 = resultsRandom8;
		this.resultsRandom9 = resultsRandom9;
		this.resultsRandom10 = resultsRandom10;
		this.resultsMostEnabledDisabled = resultsMostEnabledDisabled;
		this.resultsCombination = resultsCombination;
		this.resultsPairwise = resultsPairwise;
		this.totalSplat = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalOneEnabled = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalOneDisabled = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalRandom1 = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalRandom2 = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalRandom3 = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalRandom4 = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalRandom5 = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalRandom6 = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalRandom7 = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalRandom8 = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalRandom9 = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalRandom10 = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalMostEnabledDisabled = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalCombination = new double[] { 0, 0, 0, 0, 0, 0 };
		this.totalPairwise = new double[] { 0, 0, 0, 0, 0, 0 };
	}
	
	/**
	 * Calculates date for plot %bugs x %configurations, where each mode is a
	 * percentage of splat mode (100%).
	 */
	public void calculateDataPlot(String subjectName) {
		if (resultsSplat != null) {
//			double totalConfs = calculateReachableConfs();
			double totalConfs = totalSplat[1];
			double totalBugs = totalSplat[3];
			String data = "gpl splat 100 100\n";
			data += subjectName + " oneEnabled " + (totalOneEnabled[3] / totalBugs) * 100 + " "
					+ (totalOneEnabled[1] / totalConfs) * 100 + "\n";
			data += subjectName + " oneDisabled " + (totalOneDisabled[3] / totalBugs) * 100 + " "
					+ (totalOneDisabled[1] / totalConfs) * 100 + "\n";
			
			data += subjectName + " random1 " + (totalRandom1[3] / totalBugs) * 100 + " "
					+ (totalRandom1[1] / totalConfs) * 100 + "\n";
			data += subjectName + " random2 " + (totalRandom2[3] / totalBugs) * 100 + " "
					+ (totalRandom2[1] / totalConfs) * 100 + "\n";
			data += subjectName + " random3 " + (totalRandom3[3] / totalBugs) * 100 + " "
					+ (totalRandom3[1] / totalConfs) * 100 + "\n";
			data += subjectName + " random4 " + (totalRandom4[3] / totalBugs) * 100 + " "
					+ (totalRandom4[1] / totalConfs) * 100 + "\n";
			data += subjectName + " random5 " + (totalRandom5[3] / totalBugs) * 100 + " "
					+ (totalRandom5[1] / totalConfs) * 100 + "\n";
			data += subjectName + " random6 " + (totalRandom6[3] / totalBugs) * 100 + " "
					+ (totalRandom6[1] / totalConfs) * 100 + "\n";
			data += subjectName + " random7 " + (totalRandom7[3] / totalBugs) * 100 + " "
					+ (totalRandom7[1] / totalConfs) * 100 + "\n";
			data += subjectName + " random8 " + (totalRandom8[3] / totalBugs) * 100 + " "
					+ (totalRandom8[1] / totalConfs) * 100 + "\n";
			data += subjectName + " random9 " + (totalRandom9[3] / totalBugs) * 100 + " "
					+ (totalRandom9[1] / totalConfs) * 100 + "\n";
			data += subjectName + " random10 " + (totalRandom10[3] / totalBugs) * 100 + " "
					+ (totalRandom10[1] / totalConfs) * 100 + "\n";
			
			data += subjectName + " mostEnabledDisabled " + (totalMostEnabledDisabled[3] / totalBugs) * 100 + " "
					+ (totalMostEnabledDisabled[1] / totalConfs) * 100 + "\n";
			data += subjectName + " combination " + (totalCombination[3] / totalBugs) * 100 + " "
					+ (totalCombination[1] / totalConfs) * 100 + "\n";
			data += subjectName + " pairwise " + (totalPairwise[3] / totalBugs) * 100 + " "
					+ (totalPairwise[1] / totalConfs) * 100 + "\n";
			log.printf(data);
		}
	}
	
	public void calculateAverage(){
		double avgSample, avgNumFailures;
		double totalSampleRandom = 0;
		double totalNumFailuresRandom = 0;
		String data = "\nAVG_NUM_FAILURES AVG_SAMPLE TECHNIQUE\n";
		
		if(resultsMostEnabledDisabled != null) {
			avgSample = totalMostEnabledDisabled[1]/resultsMostEnabledDisabled.size();
			avgNumFailures = totalMostEnabledDisabled[3]/resultsMostEnabledDisabled.size();
			data += String.format("%.2f %.2f most-enabled-disabled\n", avgNumFailures, avgSample);
		}
		
		if(resultsOneEnabled != null){
			avgSample = totalOneEnabled[1]/resultsOneEnabled.size();
			avgNumFailures = totalOneEnabled[3]/resultsOneEnabled.size();
			data += String.format("%.2f %.2f one-enabled\n", avgNumFailures, avgSample);
		} 
		
		if(resultsOneDisabled != null) {
			avgSample = totalOneDisabled[1]/resultsOneDisabled.size();
			avgNumFailures = totalOneDisabled[3]/resultsOneDisabled.size();
			data += String.format("%.2f %.2f one-disabled\n", avgNumFailures, avgSample);
		}
		
		if(resultsRandom1 != null) {
			avgSample = totalRandom1[1]/resultsRandom1.size();
			avgNumFailures = totalRandom1[3]/resultsRandom1.size();
			data += String.format("%.2f %.2f random1\n", avgNumFailures, avgSample);
			totalNumFailuresRandom += avgNumFailures;
			totalSampleRandom += avgSample;
		}
		
		if(resultsRandom2 != null) {
			avgSample = totalRandom2[1]/resultsRandom2.size();
			avgNumFailures = totalRandom2[3]/resultsRandom2.size();
			data += String.format("%.2f %.2f random2\n", avgNumFailures, avgSample);
			totalNumFailuresRandom += avgNumFailures;
			totalSampleRandom += avgSample;
		}
		
		if(resultsRandom3 != null) {
			avgSample = totalRandom3[1]/resultsRandom3.size();
			avgNumFailures = totalRandom3[3]/resultsRandom3.size();
			data += String.format("%.2f %.2f random4\n", avgNumFailures, avgSample);
			totalNumFailuresRandom += avgNumFailures;
			totalSampleRandom += avgSample;
		}
//		
//		if(resultsRandom4 != null) {
//			avgSample = totalRandom4[1]/resultsRandom4.size();
//			avgNumFailures = totalRandom4[3]/resultsRandom4.size();
////			data += String.format("%.2f %.2f random4\n", avgNumFailures, avgSample);
//			totalNumFailuresRandom += avgNumFailures;
//			totalSampleRandom += avgSample;
//		}
//		
//		if(resultsRandom5 != null) {
//			avgSample = totalRandom5[1]/resultsRandom5.size();
//			avgNumFailures = totalRandom5[3]/resultsRandom5.size();
////			data += String.format("%.2f %.2f random5\n", avgNumFailures, avgSample);
//			totalNumFailuresRandom += avgNumFailures;
//			totalSampleRandom += avgSample;
//		}
//		
//		if(resultsRandom6 != null) {
//			avgSample = totalRandom6[1]/resultsRandom6.size();
//			avgNumFailures = totalRandom6[3]/resultsRandom6.size();
////			data += String.format("%.2f %.2f random6\n", avgNumFailures, avgSample);
//			totalNumFailuresRandom += avgNumFailures;
//			totalSampleRandom += avgSample;
//		}
//		
//		if(resultsRandom7 != null) {
//			avgSample = totalRandom7[1]/resultsRandom7.size();
//			avgNumFailures = totalRandom7[3]/resultsRandom7.size();
////			data += String.format("%.2f %.2f random7\n", avgNumFailures, avgSample);
//			totalNumFailuresRandom += avgNumFailures;
//			totalSampleRandom += avgSample;
//		}
//		
//		if(resultsRandom8 != null) {
//			avgSample = totalRandom8[1]/resultsRandom8.size();
//			avgNumFailures = totalRandom8[3]/resultsRandom8.size();
////			data += String.format("%.2f %.2f random8\n", avgNumFailures, avgSample);
//			totalNumFailuresRandom += avgNumFailures;
//			totalSampleRandom += avgSample;
//		}
//		
//		if(resultsRandom9 != null) {
//			avgSample = totalRandom9[1]/resultsRandom9.size();
//			avgNumFailures = totalRandom9[3]/resultsRandom9.size();
////			data += String.format("%.2f %.2f random9\n", avgNumFailures, avgSample);
//		}
//		
//		if(resultsRandom10 != null) {
//			avgSample = totalRandom10[1]/resultsRandom10.size();
//			avgNumFailures = totalRandom10[3]/resultsRandom10.size();
////			data += String.format("%.2f %.2f random10\n", avgNumFailures, avgSample);
//			totalNumFailuresRandom += avgNumFailures;
//			totalSampleRandom += avgSample;
//		}
//		
		data += String.format("%.2f %.2f random\n", totalNumFailuresRandom/3, totalSampleRandom/3);
		
		if(resultsPairwise != null) {
			avgSample = totalPairwise[1]/resultsPairwise.size();
			avgNumFailures = totalPairwise[3]/resultsPairwise.size();
			data += String.format("%.2f %.2f pairwise\n", avgNumFailures, avgSample);
		} 
		
		if (resultsSplat != null) {
			avgSample = totalSplat[1]/resultsSplat.size();
			avgNumFailures = totalSplat[3]/resultsSplat.size();
			data += String.format("%.2f %.2f splat\n", avgNumFailures, avgSample);
		}
		
		log.printf(data);
	}

	/**
	 * Calculates all results.
	 */
	public void calculateResults() {
		log.printf(
				"Test($t_i$)\tTime\t#Confs\t#F\t#Bugs\tTTFF\tCTFF\tTime\tCR\t#FR\t#MBugs\tTTFF\tCTFF\tTime\tCR\t#FR\t#MBugs\tTTFF\tCTFF \n");
		
		int iterations = 0;
		if(resultsSplat != null)
			iterations = resultsSplat.size();
		else if(resultsOneEnabled != null)
			iterations = resultsOneEnabled.size();
		else if(resultsOneDisabled != null)
			iterations = resultsOneDisabled.size();
		
		else if(resultsRandom1 != null)
			iterations = resultsRandom1.size();
		else if(resultsRandom2 != null)
			iterations = resultsRandom2.size();
		else if(resultsRandom3 != null)
			iterations = resultsRandom3.size();
		else if(resultsRandom4 != null)
			iterations = resultsRandom4.size();
		else if(resultsRandom5 != null)
			iterations = resultsRandom5.size();
		else if(resultsRandom6 != null)
			iterations = resultsRandom6.size();
		else if(resultsRandom7 != null)
			iterations = resultsRandom7.size();
		else if(resultsRandom8 != null)
			iterations = resultsRandom8.size();
		else if(resultsRandom9 != null)
			iterations = resultsRandom9.size();
		else if(resultsRandom10 != null)
			iterations = resultsRandom10.size();
		
		else if(resultsMostEnabledDisabled != null)
			iterations = resultsMostEnabledDisabled.size();
		else if(resultsCombination != null)
			iterations = resultsCombination.size();
		else if(resultsPairwise != null)
			iterations = resultsPairwise.size();
		
		for (int i = 0; i < iterations; i++)
			calculateResults(i);
	}

	/**
	 * Calculates total for each execution mode.
	 */
	public void calculateTotal() {
		String result = "\\textbf{TOTAL} & \n";

		if (resultsSplat != null) {
			for (int i = 0; i < totalSplat.length; i++)
				result += totalSplat[i] + " & ";
			result += "\n";
		} //else log.printf(" - & - & - & - & - & - & ");

		if (resultsOneEnabled != null) {
			for (int i = 0; i < totalOneEnabled.length; i++)
				result += totalOneEnabled[i] + " & ";
			result += "\n";
		} //else log.printf(" - & - & - & - & - & - & ");

		if (resultsOneDisabled != null) {
			for (int i = 0; i < totalOneDisabled.length; i++)
				result += totalOneDisabled[i] + " & ";
			result += "\n";
		} //else log.printf(" - & - & - & - & - & - & ");

		
		if (resultsRandom1 != null) {
			for (int i = 0; i < totalRandom1.length; i++)
				result += totalRandom1[i] + " & ";
			result += "\n";
		}// else log.printf(" - & - & - & - & - & - & ");
		if (resultsRandom2 != null) {
			for (int i = 0; i < totalRandom2.length; i++)
				result += totalRandom2[i] + " & ";
			result += "\n";
		}// else log.printf(" - & - & - & - & - & - & ");
		if (resultsRandom3 != null) {
			for (int i = 0; i < totalRandom3.length; i++)
				result += totalRandom3[i] + " & ";
			result += "\n";
		}// else log.printf(" - & - & - & - & - & - & ");
		if (resultsRandom4 != null) {
			for (int i = 0; i < totalRandom4.length; i++)
				result += totalRandom4[i] + " & ";
			result += "\n";
		}// else log.printf(" - & - & - & - & - & - & ");
		if (resultsRandom5 != null) {
			for (int i = 0; i < totalRandom5.length; i++)
				result += totalRandom5[i] + " & ";
			result += "\n";
		}// else log.printf(" - & - & - & - & - & - & ");
		if (resultsRandom6 != null) {
			for (int i = 0; i < totalRandom6.length; i++)
				result += totalRandom6[i] + " & ";
			result += "\n";
		}// else log.printf(" - & - & - & - & - & - & ");
		if (resultsRandom7 != null) {
			for (int i = 0; i < totalRandom7.length; i++)
				result += totalRandom7[i] + " & ";
			result += "\n";
		}// else log.printf(" - & - & - & - & - & - & ");
		if (resultsRandom8 != null) {
			for (int i = 0; i < totalRandom8.length; i++)
				result += totalRandom8[i] + " & ";
			result += "\n";
		}// else log.printf(" - & - & - & - & - & - & ");
		if (resultsRandom9 != null) {
			for (int i = 0; i < totalRandom9.length; i++)
				result += totalRandom9[i] + " & ";
			result += "\n";
		}// else log.printf(" - & - & - & - & - & - & ");
		if (resultsRandom10 != null) {
			for (int i = 0; i < totalRandom10.length; i++)
				result += totalRandom10[i] + " & ";
			result += "\n";
		}// else log.printf(" - & - & - & - & - & - & ");
		
		

		if (resultsMostEnabledDisabled != null) {
			for (int i = 0; i < totalMostEnabledDisabled.length; i++)
				result += totalMostEnabledDisabled[i] + " & ";
			result += "\n";
		} //else log.printf(" - & - & - & - & - & - & ");

		if (resultsCombination != null) {
			for (int i = 0; i < totalCombination.length; i++)
				result += totalCombination[i] + " & ";
			result += "\n";
		} //else log.printf(" - & - & - & - & - & - & ");
		
		if (resultsPairwise != null) {
			for (int i = 0; i < totalPairwise.length - 1; i++)
				result += totalPairwise[i] + " & ";
			result += totalPairwise[totalPairwise.length - 1] + " \\\\ \\hline \n";
		} //else log.printf(" - & - & - & - & - & - & ");
		
		log.printf(result);
	}

	/**
	 * Calculates only the different configurations reached by all tests,
	 * resulting in a set.
	 */
	public int calculateReachableConfs() {
		Set<String> reachableConfs = new TreeSet<String>();

		if (resultsSplat != null) {
			for (TestResults tr : this.resultsSplat)
				reachableConfs.addAll(tr.calculateReachableConfs());

			log.printf("REACHABLE CONFS: " + reachableConfs.size() + "\n");

			for (String c : reachableConfs)
				log.printf(c + "\n");
		}
		
		if (resultsMostEnabledDisabled != null) {
			reachableConfs.clear();
			for (TestResults tr : this.resultsMostEnabledDisabled)
				reachableConfs.addAll(tr.calculateReachableConfs());

			log.printf("REACHABLE CONFS: " + reachableConfs.size() + "\n");

			for (String c : reachableConfs)
				log.printf(c + "\n");
		}
		return reachableConfs.size();
	}

	/**
	 * Calculates results for each execution mode.
	 */
	private void calculateResults(int i) {
		
		log.printf((i + 1) + " & ");

		/***** SPLat *****/
		if (resultsSplat != null) {
			TestResults t_res_splat = resultsSplat.get(i);// $t_i$
			String parcialSPlat = t_res_splat.print();
			log.printf(parcialSPlat + " & ");
			calculateTotal(parcialSPlat, totalSplat);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");

		/***** one-enabled *****/
		if (resultsOneEnabled != null) {
			TestResults t_res_splatOneEnabled = resultsOneEnabled.get(i);
			String parcialOneEnabled = t_res_splatOneEnabled.print();
			log.printf(parcialOneEnabled + " & ");
			calculateTotal(parcialOneEnabled, totalOneEnabled);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");

		/***** one-disabled *****/
		if (resultsOneDisabled != null) {
			TestResults t_res_splatOneDisabled = resultsOneDisabled.get(i);
			String parcialOneDisabled = t_res_splatOneDisabled.print();
			log.printf(parcialOneDisabled + " & ");
			calculateTotal(parcialOneDisabled, totalOneDisabled);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");

		
		/***** random *****/
		if (resultsRandom1 != null) {
			TestResults t_res_splatRandom1 = resultsRandom1.get(i);
			String parcialRandom1 = t_res_splatRandom1.print();
			log.printf(parcialRandom1 + " & ");
			calculateTotal(parcialRandom1, totalRandom1);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");
		/***** random *****/
		if (resultsRandom2 != null) {
			TestResults t_res_splatRandom2 = resultsRandom2.get(i);
			String parcialRandom2 = t_res_splatRandom2.print();
			log.printf(parcialRandom2 + " & ");
			calculateTotal(parcialRandom2, totalRandom2);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");
		/***** random *****/
		if (resultsRandom3 != null) {
			TestResults t_res_splatRandom3 = resultsRandom3.get(i);
			String parcialRandom3 = t_res_splatRandom3.print();
			log.printf(parcialRandom3 + " & ");
			calculateTotal(parcialRandom3, totalRandom3);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");
		/***** random *****/
		if (resultsRandom4 != null) {
			TestResults t_res_splatRandom4 = resultsRandom4.get(i);
			String parcialRandom4 = t_res_splatRandom4.print();
			log.printf(parcialRandom4 + " & ");
			calculateTotal(parcialRandom4, totalRandom4);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");
		/***** random *****/
		if (resultsRandom5 != null) {
			TestResults t_res_splatRandom5 = resultsRandom5.get(i);
			String parcialRandom5 = t_res_splatRandom5.print();
			log.printf(parcialRandom5 + " & ");
			calculateTotal(parcialRandom5, totalRandom5);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");
		/***** random *****/
		if (resultsRandom6 != null) {
			TestResults t_res_splatRandom6 = resultsRandom6.get(i);
			String parcialRandom6 = t_res_splatRandom6.print();
			log.printf(parcialRandom6 + " & ");
			calculateTotal(parcialRandom6, totalRandom6);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");
		/***** random *****/
		if (resultsRandom7 != null) {
			TestResults t_res_splatRandom7 = resultsRandom7.get(i);
			String parcialRandom7 = t_res_splatRandom7.print();
			log.printf(parcialRandom7 + " & ");
			calculateTotal(parcialRandom7, totalRandom7);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");
		/***** random *****/
		if (resultsRandom8 != null) {
			TestResults t_res_splatRandom8 = resultsRandom8.get(i);
			String parcialRandom8 = t_res_splatRandom8.print();
			log.printf(parcialRandom8 + " & ");
			calculateTotal(parcialRandom8, totalRandom8);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");
		/***** random *****/
		if (resultsRandom9 != null) {
			TestResults t_res_splatRandom9 = resultsRandom9.get(i);
			String parcialRandom9 = t_res_splatRandom9.print();
			log.printf(parcialRandom9 + " & ");
			calculateTotal(parcialRandom9, totalRandom9);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");
		/***** random *****/
		if (resultsRandom10 != null) {
			TestResults t_res_splatRandom10 = resultsRandom10.get(i);
			String parcialRandom10 = t_res_splatRandom10.print();
			log.printf(parcialRandom10 + " & ");
			calculateTotal(parcialRandom10, totalRandom10);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");

		
		
		/***** most-enabled-disabled *****/
		if (resultsMostEnabledDisabled != null) {
			TestResults t_res_splatMostEnabledDisabled = resultsMostEnabledDisabled.get(i);
			String parcialMostEnabledDisabled = t_res_splatMostEnabledDisabled.print();
			log.printf(parcialMostEnabledDisabled + " & ");
			calculateTotal(parcialMostEnabledDisabled, totalMostEnabledDisabled);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");

		/***** combination *****/
		if (resultsCombination != null) {
			TestResults t_res_splatCombination = resultsCombination.get(i);
			String parcialCombination = t_res_splatCombination.print();
			log.printf(parcialCombination);
			calculateTotal(parcialCombination, totalCombination);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");
		
		/***** pairwise *****/
		if (resultsPairwise != null) {
			TestResults t_res_splatPairwise = resultsPairwise.get(i);
			String parcialPairwise = t_res_splatPairwise.print();
			log.printf(parcialPairwise);
			calculateTotal(parcialPairwise, totalPairwise);
		}// else log.printf((i + 1) + " & - & - & - & - & - & - & ");
		
		
		log.printf(" \\\\ \\hline \n");
	}
	
	/**
	 * Accumulates the total of each metric in array total: time,
	 * configurations, failing configurations, bugs, ttff (time to find the
	 * first failure), and ctff (configurations to find the first failure).
	 */
	public void calculateTotal(String parcial, double[] total) {
		String[] tokens = parcial.split(" & ");
		for (int i = 0; i < tokens.length; i++) {
			String tok = tokens[i];
			double currentValue = Double.parseDouble(tok);
			total[i] += currentValue;
		}
	}
	
	/**
	 * Calculates statistics for each execution mode. TODO: update for new modes
	 * (most-enabled-disables, ...)
	 */
	public void calculateStats() {
		String result = "\\textbf{STATS} & - & - & - & - & - & - & ";
		result += calculateStatsTotal(totalSplat, totalOneEnabled) + " & ";
		result += calculateStatsTotal(totalSplat, totalOneDisabled) + " & ";
//		result += calculateStatsTotal(totalSplat, totalRandom) + " \\\\ \\hline \n";
		log.printf(result);
	}
	
	/**
	 * Calculates statistics for each metric: time, configurations, failing
	 * configurations, bugs, ttff (time to find the first failure), and ctff
	 * (configurations to find the first failure).
	 */
	public String calculateStatsTotal(double[] totalSplat, double[] totalSampling) {
		String result = "";

		double timeSplat = totalSplat[0];
		double confsSplat = totalSplat[1];
		double confsFailSplat = totalSplat[2];
		double bugsSplat = totalSplat[3];
		double ttffSplat = totalSplat[4];
		double ctffSplat = totalSplat[5];

		double timeSampling = totalSampling[0];
		double confsSampling = totalSampling[1];
		double confsFailSampling = totalSampling[2];
		double bugsSampling = totalSampling[3];
		double ttffSampling = totalSampling[4];
		double ctffSampling = totalSampling[5];

		double diff = timeSplat - timeSampling;
		double time_reduction = (timeSampling < 1.0) ? 0.99 : diff / timeSplat;

		double confs_reduction = ((confsSplat == 0) || (confsSplat - confsSampling == 0)) ? 0
				: ((double) (confsSplat - confsSampling) / (double) confsSplat);
		double failures_reduction = (confsFailSplat == 0 || (confsFailSplat - confsFailSampling == 0)) ? 0
				: ((double) (confsFailSplat - confsFailSampling) / (double) confsFailSplat);
		double missed_bugs = ((bugsSplat == 0)) ? 0 : ((double) (bugsSplat - bugsSampling) / (double) bugsSplat);

		double ttff_reduction = 0;
		if (confsFailSampling >= 1) {
			if (ttffSplat - ttffSampling == 0)
				ttff_reduction = 1;
			else if (ttffSplat == 0)
				ttffSplat = 1;
			if (ttffSampling == 0)
				ttffSampling = 1;
			ttff_reduction = (ttffSplat - ttffSampling) / (ttffSplat);
		}
		double ctff_reduction = (confsFailSampling >= 1)
				? ((double) ctffSplat - (double) ctffSampling) / (double) ctffSplat : (double) 0;

		result += (String.format("%.2f", time_reduction * 100)).replace(",", ".") + " & "
				+ (String.format("%.2f", confs_reduction * 100)).replace(",", ".") + " & "
				+ (String.format("%.2f", failures_reduction * 100)).replace(",", ".") + " & "
				+ (String.format("%.2f", missed_bugs * 100)).replace(",", ".") + " & "
				+ (String.format("%.2f", ttff_reduction * 100)).replace(",", ".") + " & "
				+ (String.format("%.2f", ctff_reduction * 100)).replace(",", ".");

		return result;
	}

}
