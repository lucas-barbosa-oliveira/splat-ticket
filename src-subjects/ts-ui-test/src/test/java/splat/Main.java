package splat;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import backtracker.Backtracker;
import newTests.TestServiceLogin;
import newTests.TestServiceLogin1;
import sampling.Sampling;
import sampling.Sampling.Mode;
import trie.TrieST;

public class Main {

	public static void main(String[] args) throws Exception {
//		PrintStream log = new PrintStream(new File("results_trainticket.txt"));
		String dataPath = (new java.io.File("./../..")).getCanonicalPath().toString() + "/data/";
		String resourcesPath = (new java.io.File("./")).getCanonicalPath() + "/src/main/resources/services.txt";

		/***** SPLat *****/
		Sampling.mode = Mode.SPLAT;
		args = new String[] { "--services-path", resourcesPath, "--logfile", dataPath + "trainticket_splat.txt", "--shouldsample",
				"false", "--samplerate", "1", "--validate", "false" };
		
//		--services-path deve ser um arquivo por teste, que pode ser o mesmo e vai sendo sobre-escrito a cada execução
		
		Map<Class,String> tests = new HashMap<Class,String>();
		tests.put(TestServiceLogin.class, "testSignIn");
		tests.put(TestServiceLogin1.class, "testSignIn");

		SPLat spLat = new SPLat(TrainTicketVariables.getSINGLETON());
		spLat.run(args, new Backtracker(true, true, new TrieST<Boolean>()), tests);

	}

}
