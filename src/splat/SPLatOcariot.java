package splat;

import backtracker.Backtracker;
import backtracker.BacktrackerJS;
import sampling.Sampling;
import sampling.Sampling.Mode;
import trie.TrieST;

public class SPLatOcariot {

	public static void main(String[] args) throws Exception {
		String dataPath = (new java.io.File("./")).getCanonicalPath().toString() + "/data/";
		String resourcesPath = dataPath + "services.txt";
		String pathTestCases = dataPath + "test-cases.txt";

		if (args.length != 1) {
			System.out.println("Invalid number of parameters or path to the unspecified system-test project.");
			System.exit(1);
		}

		String testProjectPath = args[0];

		/***** SPLat *****/
		Sampling.mode = Mode.SPLAT;
		args = new String[] { "--services-path", resourcesPath, "--logfile", dataPath + "ocariot.txt", "--shouldsample",
				"false", "--samplerate", "1", "--validate", "false" };

		SPLatJS spLat = new SPLatJS(OcariotVariables.getSINGLETON());
		spLat.run(args, new BacktrackerJS(true, true, new TrieST<Boolean>()), pathTestCases, testProjectPath);
	}

}
