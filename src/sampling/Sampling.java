package sampling;

import java.util.Random;

import splat.SPLat;
import splat.Variables;

public class Sampling {

	public enum Mode {
		SPLAT, RANDOM, ONE_ENABLED, ONE_DISABLED, MOST_ENABLED_DISABLED, COMBINATION, PAIRWISE
	};

	public static Mode mode = Mode.SPLAT;

	public static void main(String[] args) {
//		Random rr = new Random(11111);
//		int counter = 0;
//		for(int i = 0; i < 160; i++){
//			int n = (int) Math.round(rr.nextGaussian() * 5) + 50;
//			if(n <= 50) counter++;
//			System.out.println(n);
//		}
//		System.out.println("--->" + counter);
	}

	public static boolean pairwise(Variables vars) {
		return SPLat.pw.check(vars);
	}

	/**
	 * Implements the algorithm one-enabled, where only one option is enabled.
	 */
	public static boolean oneEnabled(Variables vars) {
		boolean shouldSample = false;
		int currNumVars = vars.getNumDefinedVars();
		if (currNumVars >= 2) {
			String[] variables = vars.getArrayFeatures();
			int oneEnabledCounter = 0;
			for (int i = 0; i < variables.length; i++) {
				if (i == 0) continue;
				String var = variables[i]; //ignore the 1st var, it is fixed
				if (var.equals("1"))
					oneEnabledCounter++;
			}
			if (oneEnabledCounter == 1)
				shouldSample = true;
		}
		return shouldSample;
	}

	/**
	 * Implements the algorithm one-disabled, where only one option is enabled.
	 */
	public static boolean oneDisabled(Variables vars) {
		boolean shouldSample = false;
		int currNumVars = vars.getNumDefinedVars();
		if (currNumVars >= 2) {
			String[] variables = vars.getArrayFeatures();
			int oneDisabledCounter = 0;
			for (int i = 0; i < variables.length; i++) {
				if (i == 0) continue; //ignore the 1st var, it is fixed
				String var = variables[i];
				if (var.equals("0"))
					oneDisabledCounter++;
			}
			// if (oneDisabledCounter == 1)
			// shouldSample = true;
			if (oneDisabledCounter < 2)
				shouldSample = true;
		}
		return shouldSample;
	}

	public static boolean random(Random r) {
//		int n = r.nextInt(10);
		int n = (int) Math.round(r.nextGaussian() * 5) + 50;
		System.out.println(n);
		if (n >= 50)
			return true; // skip
		return false;
	}
	
//	int n = (int) Math.round(rr.nextGaussian() * 5) + 50;
//	if(n <= 50) counter++;
	
	public static boolean mostEnabledDisabled(Variables vars){
		boolean shouldSample = false;
		int currNumVars = vars.getNumDefinedVars();
		if (currNumVars >= 2) {
			String[] variables = vars.getArrayFeatures();
			int disabledCounter = 0;
			int enabledCounter = 0;
			for (int i = 0; i < variables.length; i++) {
				if (i == 0) continue; //ignore the 1st var, it is fixed
				String var = variables[i];
				if (var.equals("0"))
					disabledCounter++;
				if (var.equals("1"))
					enabledCounter++;
			}
			//conf only contains 0
			if((disabledCounter > 0) && (enabledCounter <= 0))
				return true;
			
			//conf only contains 1
			if((disabledCounter <= 0) && (enabledCounter > 0))
				return true;
			
			//conf contains 1 and 0
			if((disabledCounter >= 1) && (enabledCounter >= 1))
				return false;
		}
		return shouldSample;
	}
			

}

//import splat.Variables;
//
//public interface Sampling {
//	
//	public boolean check(Variables vars);
//
//}
