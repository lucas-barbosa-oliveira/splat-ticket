package experiment;

import java.io.Serializable;

import configuration.Configuration;

public class SPLatResult implements Serializable {

	Configuration conf;
	IResult testRes;

	public SPLatResult(Configuration configuration, IResult r) {
		this.conf = configuration;
		this.testRes = r;
	}

	public Configuration getConfiguration() {
		return this.conf;
	}

	public IResult getTestResult() {
		return this.testRes;
	}

}
