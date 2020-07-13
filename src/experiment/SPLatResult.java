package experiment;

import java.io.Serializable;

import configuration.Configuration;
import splat.SPLat.Result;

public class SPLatResult implements Serializable{
	
	Configuration conf;
	Result testRes;
	
	public SPLatResult(Configuration configuration, Result r){
		this.conf = configuration;
		this.testRes = r;
	}
	
	public Configuration getConfiguration(){
		return this.conf;
	}
	
	public Result getTestResult(){
		return this.testRes;
	}
	
	
}
