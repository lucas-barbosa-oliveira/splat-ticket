package splat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.testng.IClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import backtracker.Backtracker;
import configuration.Configuration;
import entry.CacheEntry;
import entry.FeatureVar;
import experiment.SPLatResult;
import experiment.TestResults;
import sampling.Pairwise;
import sampling.Sampling;
import sampling.Sampling.Mode;
import stats.Stats;
import util.Config;
import util.Config.ExecutionMode;

public class SPLatJS {

	public static Backtracker bt = new Backtracker(true, true /* SPLar */, null); // Stack
	public static PrintStream out;
	public static String servicesPath;
	public static CommandLine cmd;
	public static boolean validate;
	public static boolean shouldSample;
	public List<CacheEntry> cache;// NEW
	String confDefault;
	Stats stats;

	private Variables vars;

	public SPLatJS(Variables vars) {
		this.vars = vars;
		this.confDefault = vars.toString();
		this.stats = new Stats();
	}

	public static Pairwise pw = null;

	private String getNotifiedService() {
		String servicesName = new String();
		final Iterator it = vars.getState().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			FeatureVar fvar = (FeatureVar) pair.getKey();
			if (pair.getValue().equals("1")) {
				servicesName = servicesName.concat(fvar.getName() + " ");
			}
		}
		return servicesName;
	}

	private void execDocker(String testProjectPath, String services) {

		final String generateDockerFile = "cd $(pwd)/src-subjects/ocariot && ./generateDockerFile.sh " + testProjectPath
				+ "/docker-compose.yml " + services;
		final String execDockerFile = "docker-compose -f " + testProjectPath + "/docker-generated.yml --env-file "
				+ testProjectPath + "/.env up -d --remove-orphans";

		try {
			Process process = new ProcessBuilder(new String[] { "bash", "-c", generateDockerFile })
					.redirectErrorStream(true).start();

			BufferedReader commandBr = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line = null;
			while ((line = commandBr.readLine()) != null) {
				System.out.println(line);
			}

			process = new ProcessBuilder(new String[] { "bash", "-c", execDockerFile }).redirectErrorStream(true)
					.start();

			commandBr = new BufferedReader(new InputStreamReader(process.getInputStream()));

			System.out.println("Executing Docker file...");
			process.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method is called from a driver Pure SPLat
	 * 
	 * @param bt
	 * @throws Exception
	 */
	// public List<String> run(String[] args, Backtracker st) throws Exception {
	public Stats run(String[] args, Backtracker st, String testsPath, String testProjectPath) throws Exception {
		List<String> traces = new ArrayList<String>();
		Config.mode = ExecutionMode.SPLAT;
		try {
			cmd = Config.loadOptions(args);
			out = new PrintStream(new File(cmd.getOptionValue("logfile")));
			servicesPath = cmd.getOptionValue("services-path");
			loadParams();
			printHeader();

			File file = new File(testsPath);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String testCase = null;
			while ((testCase = br.readLine()) != null) {
				String testCaseName = testCase.split("-")[0];
				String testGroup = testCase.split("-")[1].replace(" ", "|");
				if (testGroup.indexOf("|") == 0)
					testGroup = testGroup.substring(1);
				// run splat for each test
				if (Sampling.mode == Mode.PAIRWISE) {
					pw = new Pairwise(vars);
				}
				System.out.println("***CONFIGURING ENVIRONMENT - STARTING ALL SERVICES FOR THE TEST CASE: " + testCaseName + "***");
				execDocker(testProjectPath, "all");
				
				TestResults t_res = new TestResults(testCaseName);
				vars.setValidate(validate);
				printTestHeader(testCaseName);
				bt = new Backtracker(st.pureSPLat, st.isFirstRun(), st.getGeneralTrie());
				// Running first to discover services touched
				
				System.out.println("Running test case to discover the services touched...");
				runJavaScriptTest(testGroup, testProjectPath, servicesPath);
				int round = 0;
				do {
					System.out.println("\nROUND NUMBER " + round);
					vars.restore();
					boolean sample = true;
					if (SPLatJS.shouldSample && Sampling.mode == Mode.RANDOM) {
						if (!bt.isFirstRun())
							sample = Sampling.random(random);
					}

					// execute or not
					if (sample) {
						Result r = runJavaScriptTest(testGroup, testProjectPath, "");
						vars.notifyServicesLoaded(servicesPath);
						System.out.println("Services to next round: " + getNotifiedService());
						execDocker(testProjectPath, getNotifiedService());
						bt.setFirstRun(false);

						String exception = "";
						for (ITestResult result : r.getTestResults()) {
							if (result.getStatus() == result.FAILURE) {
								Throwable throwable = result.getThrowable();
								if (throwable != null) {
									Class bug = throwable.getClass();
									exception = bug.getName();
								}
							}

							if (!exception.contains("RuntimeException")) {
								if (SPLatJS.shouldSample) {
									if (((Sampling.mode == Mode.ONE_DISABLED) && (vars.getNumVarsDisabled() == 1))
											|| (Sampling.mode == Mode.ONE_ENABLED)
											|| (Sampling.mode == Mode.MOST_ENABLED_DISABLED)
											|| (Sampling.mode == Mode.COMBINATION) || (Sampling.mode == Mode.RANDOM)
											|| (Sampling.mode == Mode.PAIRWISE)) {
										print(r);
										SPLatResult splatResult = new SPLatResult(new Configuration(vars.toString()),
												r);
										t_res.add(splatResult);
										// System.err.println(vars.toString());
									}
								} else {
									print(r);
									SPLatResult splatResult = new SPLatResult(new Configuration(vars.toString()), r);
									t_res.add(splatResult);
									// System.err.println(vars.toString());
								}
							}
						}
					} else {
						bt.forceChoose(vars);
						out.printf("<skip>\n");
					}
					traces.add(vars.toString());
					// look for another choice
					bt.backtrack();
					round++;
				} while (bt.hasMore());
//						if(t_res.getNum_confs() != 0)
				this.stats.add(t_res);
//							System.err.println(traces.size());
			}
		} catch (Exception e) {
			e.printStackTrace(); // exception in SPLAT
		} finally {
			out.close();
		}
		return stats;
		// return traces;
	}

	public void printTestHeader(String testName) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		out.print("testname: ");
		out.print(testName);
		out.print(" ");
		out.print(sdf.format(Calendar.getInstance().getTime()));
		out.println();
		vars.restore();
	}

	private int sampleRate;
	Random random;

	private void loadParams() throws FileNotFoundException {
		validate = Boolean.parseBoolean(cmd.getOptionValue("validate"));
		shouldSample = Boolean.parseBoolean(cmd.getOptionValue("shouldsample"));
		sampleRate = (int) (Double.parseDouble(cmd.getOptionValue("samplerate")) * 100);
		if (sampleRate == 1) {
			shouldSample = false;
		} else if (sampleRate == 0) {
			throw new RuntimeException("Invalid choice of sampling rate!");
		} else {
			String seed = cmd.getOptionValue("seed");
			int tmp = 0;
			if (seed == null) {
				out.print("SEED NOT INFORMED!  Using default seed 0");
			} else {
				tmp = Integer.parseInt(seed);
			}
			random = new Random(tmp);
		}
	}

	private void printHeader() {
		// printing parameters
		out.println("# parameters:");
		for (Option opt : cmd.getOptions()) {
			out.printf("#  %s = %s\n", opt.getLongOpt(), opt.getValue());
		}
		// printing map of features
		out.println("# features:");
		int i = 1;
		for (FeatureVar fvar : vars.getStateClone().keySet() /*
																 * should respect ordering
																 */) {
			out.printf("#  %s = %d\n", fvar.getName(), i++);
		}
	}

	private void print(Result r) {
		Throwable throwable = r.getFailure();
//		Throwable throwable = f == null ? null : f.getException();
		boolean timeout = false;
		if (throwable != null) {
			timeout = throwable.toString().contains("test timed out");
			out.printf("** %s%s\n", vars.toString(), timeout ? "T" : "F");
			out.printf("> stack-trace (for config above)\n");
			throwable.printStackTrace(out);
			out.printf("< end stack-trace\n");
		} else {
			out.printf("** %s%s\n", vars.toString(), "P");
		}
		out.printf("--->" + r.getExecTime() + "msec\n");
	}

	/***
	 * Infrastructure for running a single test on TestNG. We want to reuse TestNG
	 * features.
	 * 
	 * @throws Throwable
	 ***/
	private static Result runJavaScriptTest(String testName, String testProjectPath, String servicesPath) {
		List<ITestResult> results = new ArrayList<ITestResult>();
		JavaScriptTestResult testResult = new JavaScriptTestResult();
		final String runTest = "./logWithServicesTouched.sh \"" + testName + "\" " + testProjectPath + " "
				+ servicesPath;
		final String command = "cd $(pwd)/src-subjects/ocariot" + " && " + runTest;
		long tf = 0;

		try {
			long ti = System.currentTimeMillis();
			Process process = new ProcessBuilder(new String[] { "bash", "-c", command }).redirectErrorStream(true)
					.start();
			tf = System.currentTimeMillis() - ti;

			final BufferedReader commandBr = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String errMessage = new String("\n");

			String line = null;
			while ((line = commandBr.readLine()) != null) {
				errMessage = errMessage.concat(line + "\n");
			}

			process.waitFor();
			System.out.println("Test run output value: " + process.exitValue());
			if (process.exitValue() != 0) {
				throw new Exception(errMessage);
			}

			testResult.setStatus(ITestResult.SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResult.setStatus(ITestResult.FAILURE);
			testResult.setThrowable(e);
		}
		results.add(testResult.getResultObject());
		Result r = new Result(results);
		r.setExecTime(tf);
		return r;
	}

	public static class JavaScriptTestResult implements ITestResult {
		int status;
		Throwable throwable;

		public ITestResult getResultObject() {
			return this;
		}

		@Override
		public Object getAttribute(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Set<String> getAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object removeAttribute(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setAttribute(String arg0, Object arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public int compareTo(ITestResult o) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getEndMillis() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getHost() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getInstance() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getInstanceName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ITestNGMethod getMethod() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object[] getParameters() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getStartMillis() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getStatus() {
			// TODO Auto-generated method stub
			return this.status;
		}

		@Override
		public IClass getTestClass() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ITestContext getTestContext() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getTestName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Throwable getThrowable() {
			// TODO Auto-generated method stub
			return this.throwable;
		}

		@Override
		public boolean isSuccess() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setEndMillis(long arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setParameters(Object[] arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setStatus(int arg0) {
			// TODO Auto-generated method stub
			this.status = arg0;
		}

		@Override
		public void setThrowable(Throwable arg0) {
			// TODO Auto-generated method stub
			this.throwable = arg0;
		}
	}

	/***
	 * Result of a test run
	 */
	public static class Result extends JavaScriptTestResult implements ITestResult, experiment.IResult {
		// public static class Result extends TestResult implements ITestResult {
		Throwable failure;
		long time;
		List<ITestResult> testResults;

		public Result(List<ITestResult> results) {
			this.testResults = results;
			for (ITestResult result : this.testResults) {
				if (result.getStatus() == result.FAILURE) {
					this.failure = result.getThrowable();
				}
			}
		}

		public String toString() {
			String str = "";
			for (ITestResult result : this.testResults) {
				str = result.toString() + "\n";
				if (result.getStatus() == result.FAILURE) {
					str += result.getThrowable().toString();
				}
			}
			return str;
		}

		public List<ITestResult> getTestResults() {
			return this.testResults;
		}

		boolean isOK() {
			return this.failure == null;
		}

		public Throwable getFailure() {
			return this.failure;
		}

		public void setExecTime(long time) {// miliseconds
			this.time = time;
		}

		public long getExecTime() {
			return this.time;
		}
	}

}