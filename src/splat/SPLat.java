package splat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.internal.TestResult;

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

public class SPLat {

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

	public SPLat(Variables vars) {
		this.vars = vars;
		this.confDefault = vars.toString();
		this.stats = new Stats();
	}

	public static Pairwise pw = null;
	/**
	 * This method is called from a driver Pure SPLat
	 * 
	 * @param bt
	 * @throws Exception
	 */
	// public List<String> run(String[] args, Backtracker st) throws Exception {
	public Stats run(String[] args, Backtracker st, Map<Class,String> tests) throws Exception {
		List<String> traces = new ArrayList<String>();
		Config.mode = ExecutionMode.SPLAT;
		try {
			cmd = Config.loadOptions(args);
			out = new PrintStream(new File(cmd.getOptionValue("logfile")));
			servicesPath = cmd.getOptionValue("services-path");
			loadParams();
			printHeader();

			for (Entry<Class, String> test : tests.entrySet()) {
			
				// run splat for each test
				if(Sampling.mode == Mode.PAIRWISE){
					pw = new Pairwise(vars);
				}	
				
				Class testClass = test.getKey();
				String testName = testClass.getName();
				String mTest = test.getValue();
				TestResults t_res = new TestResults(testName + ":" + mTest);
				vars.setValidate(validate);
				printTestHeader(testName + ":" + mTest);
				bt = new Backtracker(st.pureSPLat, st.isFirstRun(), st.getGeneralTrie());
				do {
					vars.restore();
					boolean sample = true;
					if (SPLat.shouldSample && Sampling.mode == Mode.RANDOM) {
						if(!bt.isFirstRun())
							sample = Sampling.random(random);
					}

					// execute or not
					if (sample) {
						Result r = runTest(testClass);
						vars.notifyServicesLoaded(servicesPath);
						
						bt.setFirstRun(false);
						
						String exception = "";
						for (ITestResult result : r.getTestResults()) {
							if(result.getStatus() == result.FAILURE){
								Throwable throwable = result.getThrowable();
								if (throwable != null) {
									Class bug = throwable.getClass();
									exception = bug.getName();
								}
							}

						if (!exception.contains("RuntimeException")) {
							if (SPLat.shouldSample) {
								if (((Sampling.mode == Mode.ONE_DISABLED) && (vars.getNumVarsDisabled() == 1))
										|| (Sampling.mode == Mode.ONE_ENABLED)
										|| (Sampling.mode == Mode.MOST_ENABLED_DISABLED)
										|| (Sampling.mode == Mode.COMBINATION)
										|| (Sampling.mode == Mode.RANDOM)
										|| (Sampling.mode == Mode.PAIRWISE)) {
									print(r);
									SPLatResult splatResult = new SPLatResult(new Configuration(vars.toString()), r);
									t_res.add(splatResult);
									System.err.println(vars.toString());
								}
							} else {
								print(r);
								SPLatResult splatResult = new SPLatResult(new Configuration(vars.toString()), r);
								t_res.add(splatResult);
								System.err.println(vars.toString());
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
		for (FeatureVar fvar : vars.getStateClone()
				.keySet() /*
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
	 ***/
	private static Result runTest(Class test) {
		List<ITestResult> results = new ArrayList<ITestResult>();
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		//, TestFlowTwoPay.class
		testng.setTestClasses(new Class[] { test });
		testng.addListener(tla);
		
		long ti = System.currentTimeMillis();
		testng.run();
		long tf = System.currentTimeMillis() - ti;
		
		if(!tla.getPassedTests().isEmpty())
			results = tla.getPassedTests();
		else results = tla.getFailedTests();
		
		Result r = new Result(results);
		r.setExecTime(tf);
		
		return r;
	}

	/***
	 * Result of a test run
	 */
	public static class Result extends TestResult implements ITestResult {
		Throwable failure;
		long time;
		List<ITestResult> testResults;
		
		public Result(List<ITestResult> results){
			this.testResults = results;
			for (ITestResult result : this.testResults) {
				if(result.getStatus() == result.FAILURE){
					this.failure = result.getThrowable();
				}
			}
		}
		
		public String toString(){
			String str = "";
			for (ITestResult result : this.testResults) {
				str = result.toString() + "\n";
				if(result.getStatus() == result.FAILURE){
					str += result.getThrowable().toString();
				}
			}
			return str;
		}
		
		public List<ITestResult> getTestResults(){
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

//
///***
// * Infrastructure for running a single test on JUnit. We want to reuse JUnit
// * features. For example, use setup and tear-down functions and force time
// * outs.
// ***/
//static long TIMEOUT = 5000;
//
//public static Result runTest(boolean isJUnit, final Class<?> testClazz, final Method method)
//		throws InitializationError, InterruptedException {
//	Result res = new Result();
//	if (isJUnit) {
//		try {
//			// long ti = System.currentTimeMillis();
//			long ti = System.nanoTime();
//			SeleniumJUnit4ClassRunner runner = new SeleniumJUnit4ClassRunner(testClazz) {
//				protected List<FrameworkMethod> computeTestMethods() {
//					return Arrays.asList(new FrameworkMethod(method));
//				}
//			};
//			runner.run(res); // update result
//			long tf = System.nanoTime() - ti;
//			// long tf = System.currentTimeMillis() - ti;
//			// System.err.println("Test Time=" + tf);
//			res.setExecTime(tf);
//		} catch (Exception e) {
//			System.err.println("Error while running test! " + testClazz.getCanonicalName());
//			e.printStackTrace();
//		}
//
//	} else {
//		long ti = System.currentTimeMillis();
//		NonJUnitTestRunner runner = new NonJUnitTestRunner(testClazz, method);
//		Thread t = new Thread(runner);
//		t.setDaemon(true);
//		t.setPriority(Thread.MIN_PRIORITY);
//		try {
//			t.start();
//			t.join(TIMEOUT);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		if (runner.done) {
//			res.fireTestFailure(new Failure(null, runner.throwable));
//		} else {
//			Throwable tmp = new RuntimeException("test timed out after " + TIMEOUT + " miliseconds");
//			res.fireTestFailure(new Failure(null, tmp));
//		}
//		// uncomment the next line
//		// t.stop();
//		while (t.isAlive()) { // wait for the thread to finish before
//								// continuing
//			System.err.println("waiting for the thread to die");
//			Thread.sleep(5);
//			t.stop();
//		}
//		long tf = System.currentTimeMillis() - ti;
//		System.err.println("THREAD=" + tf);
//	}
//	// System.out.println("Active count: " +
//	// Thread.getAllStackTraces().keySet().size());
//	return res;
//}
//
//static class NonJUnitTestRunner implements Runnable {
//	Class<?> testClazz;
//	Method method;
//	Throwable throwable;
//
//	public NonJUnitTestRunner(Class<?> testClazz, Method method) {
//		super();
//		this.testClazz = testClazz;
//		this.method = method;
//	}
//
//	boolean done;
//
//	@Override
//	public void run() {
//		try {
//			Object testObject = testClazz.newInstance();
//			method.invoke(testObject, new Object[] {});
//		} catch (Exception e) {
//			throwable = e.getCause();
//		}
//		done = true;
//	}
//}

// public Stats run(String[] args, Backtracker st) throws Exception {
// List<String> traces = new ArrayList<String>();
// Config.mode = ExecutionMode.SPLAT;
// try {
// cmd = Config.loadOptions(args);
// out = new PrintStream(new File(cmd.getOptionValue("logfile")));
// loadParams();
// printHeader();
// Class<?> suiteclazz = Class.forName(cmd.getOptionValue("testclass"));
// SuiteClasses testclazzes =
// suiteclazz.getAnnotation(org.junit.runners.Suite.SuiteClasses.class);
// Class<?>[] clazzes = testclazzes.value();
//
// for (Class<?> clazz : clazzes) {
// Method[] methods = clazz.getMethods();
// for (Method mTest : methods) {
// if (mTest.isAnnotationPresent(org.junit.Test.class)) {
// // run splat for each test
// TestResults t_res = new TestResults(clazz.getName() + ":" + mTest.getName());
// vars.setValidate(validate);
// printTestHeader(clazz.getName() + ":" + mTest.getName());
// bt = new Backtracker(st.pureSPLat, st.isFirstRun(), st.getGeneralTrie());
// boolean isJUnitRun = isJUnitRun(mTest);
// int countRuns = 0;
// do {
// if (countRuns < 2 || !shouldSample) {
// vars.restore();
// Result r = runTest(isJUnitRun, clazz, mTest);
// print(r);
// SPLatResult result = new SPLatResult(new Configuration(vars.toString()), r);
// t_res.add(result);
// System.err.println(vars.toString());
// traces.add(vars.toString());
// // look for another choice
// bt.backtrack();
// countRuns++;
// } else {
// boolean sample = true;
// if (shouldSample) {
// switch (Sampling.sampling_algo) {
// case ONE_ENABLED:
// bt.forceChoose(vars);
// sample = Sampling.oneEnabled(vars);
// break;
// case ONE_DISABLED:
// bt.forceChoose(vars);
// sample = Sampling.oneDisabled(vars);
// break;
// // default :
// // sample = false;
// }
// }
//// vars.restore();
// // execute or not
// if (sample) {
// Result r = runTest(isJUnitRun, clazz, mTest);
// print(r);
// SPLatResult result = new SPLatResult(new Configuration(vars.toString()), r);
// t_res.add(result);
// System.err.println(vars.toString());
// }
//// else {
//// bt.forceChoose(vars);
//// out.printf("<skip>\n");
//// }
// traces.add(vars.toString());
// // look for another choice
// bt.backtrack();
// }
//// bt.setNumConfs(traces.size());
//// bt.setMaxDefVarsLastConf(vars.getNumDefinedVars());
// } while (bt.hasMore());
//
// this.stats.add(t_res);
// }
// }
// }
// } catch (Exception e) {
// e.printStackTrace(); // exception in SPLAT
// } finally {
// out.close();
// }
//
// return stats;
// // return traces;
// }

// public Stats run(String[] args, Backtracker st) throws Exception {
// List<String> traces = new ArrayList<String>();
// Config.mode = ExecutionMode.SPLAT;
// try {
// cmd = Config.loadOptions(args);
// out = new PrintStream(new File(cmd.getOptionValue("logfile")));
// loadParams();
// printHeader();
// Class<?> suiteclazz = Class.forName(cmd.getOptionValue("testclass"));
// SuiteClasses testclazzes =
// suiteclazz.getAnnotation(org.junit.runners.Suite.SuiteClasses.class);
// Class<?>[] clazzes = testclazzes.value();
//
// for (Class<?> clazz : clazzes) {
// Method[] methods = clazz.getMethods();
// for (Method mTest : methods) {
// if (mTest.isAnnotationPresent(org.junit.Test.class)) {
// // run splat for each test
// TestResults t_res = new TestResults(clazz.getName() + ":" + mTest.getName());
// vars.setValidate(validate);
// printTestHeader(clazz.getName() + ":" + mTest.getName());
// bt = new Backtracker(st.pureSPLat, st.isFirstRun(), st.getGeneralTrie());
// boolean isJUnitRun = isJUnitRun(mTest);
// do {
// vars.restore();
// boolean sample = true;
// // execute or not
// if (sample) {
// Result r = runTest(isJUnitRun, clazz, mTest);
// print(r);
// SPLatResult result = new SPLatResult(new Configuration(vars.toString()), r);
// t_res.add(result);
// System.err.println(vars.toString());
// } else {
// // bt.forceChoose();
// out.printf("<skip>\n");
// }
// traces.add(vars.toString());
// // look for another choice
// bt.backtrack();
// bt.setMaxDefVarsLastConf(vars.getNumDefinedVars());
// bt.setNumConfs(traces.size());
//
// } while (bt.hasMore());
//
// this.stats.add(t_res);
// }
// }
// }
// } catch (Exception e) {
// e.printStackTrace(); // exception in SPLAT
// } finally {
// out.close();
// }
//
// return stats;
// // return traces;
// }

// public Stats run(String[] args, Backtracker st) throws Exception {
// List<String> traces = new ArrayList<String>();
// Config.mode = ExecutionMode.SPLAT;
// try {
// cmd = Config.loadOptions(args);
// out = new PrintStream(new File(cmd.getOptionValue("logfile")));
// loadParams();
// printHeader();
// Class<?> suiteclazz = Class.forName(cmd.getOptionValue("testclass"));
// SuiteClasses testclazzes =
// suiteclazz.getAnnotation(org.junit.runners.Suite.SuiteClasses.class);
// Class<?>[] clazzes = testclazzes.value();
//
// for (Class<?> clazz : clazzes) {
// Method[] methods = clazz.getMethods();
// for (Method mTest : methods) {
// if (mTest.isAnnotationPresent(org.junit.Test.class)) {
// // run splat for each test
// TestResults t_res = new TestResults(clazz.getName() + ":" + mTest.getName());
// vars.setValidate(validate);
// printTestHeader(clazz.getName() + ":" + mTest.getName());
// bt = new Backtracker(st.pureSPLat, st.isFirstRun(), st.getGeneralTrie());
// boolean isJUnitRun = isJUnitRun(mTest);
//
// vars.restore();
// runTest(traces, clazz, mTest, t_res, isJUnitRun);
// bt.backtrack();
//
// boolean sample = true;
// do {
// vars.restore();
// //next conf
// bt.forceChoose(vars);
// if (!bt.hasMore()) { // empty stack -> bail out
// break;
// }
// sample = Sampling.oneEnabled(vars);
//
// // execute or not
// if (sample) {
// runTest(traces, clazz, mTest, t_res, isJUnitRun);
// }
//
// bt.backtrack();
// } while (bt.hasMore());
//
// this.stats.add(t_res);
// }
// }
// }
// } catch (Exception e) {
// e.printStackTrace(); // exception in SPLAT
// } finally {
// out.close();
// }
//
// return stats;
// // return traces;
// }
// private void runTest(List<String> traces, Class<?> clazz, Method mTest,
// TestResults t_res, boolean isJUnitRun)
// throws InitializationError, InterruptedException {
// Result r = runTest(isJUnitRun, clazz, mTest);
// print(r);
// SPLatResult result = new SPLatResult(new Configuration(vars.toString()), r);
// t_res.add(result);
// System.err.println(vars.toString());
// traces.add(vars.toString());
// }