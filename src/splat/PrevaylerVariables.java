package splat;

import java.util.Map;

import entry.FeatureVar;



public class PrevaylerVariables extends Variables{

	private static PrevaylerVariables SINGLETON;

	public static PrevaylerVariables getSINGLETON() {
		if (SINGLETON == null) {
			SINGLETON = new PrevaylerVariables();
		}
		return SINGLETON;
	}
	
	private PrevaylerVariables() {
		this.REPLICATION___ = new FeatureVar("REPLICATION___");
		this.GZIP___ = new FeatureVar("GZIP___");
		this.CENSOR___ = new FeatureVar("CENSOR___");
		this.MONITOR___ = new FeatureVar("MONITOR___");
		this.SNAPSHOT___ = new FeatureVar("SNAPSHOT___");
	  restore();

		try {
			guidsl = loadGUIDSL(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }

	/******************/
  @Override
  public void restore() {
    state.clear();
    init();
  }

  private FeatureVar 
    REPLICATION___, GZIP___, CENSOR___, MONITOR___, SNAPSHOT___;
  
  
  private void init() {
    state.put(REPLICATION___, "?");
    state.put(GZIP___, "?");
    state.put(CENSOR___, "?");
    state.put(MONITOR___, "?");
    state.put(SNAPSHOT___, "?");
  }
  
  private String notifyFeatureRead(FeatureVar fvar) {
    String tmp = state.get(fvar);
    if (tmp == "?") {
      /**
       * only makes a choice if it is not already present in the map
       */
      tmp = SPLat.bt.choose(fvar, this) ? "1" : "0";
      //state.put(fvar, tmp);
    }
    return tmp;
  }

	public boolean isREPLICATION___() {
	  return notifyFeatureRead(REPLICATION___).equals("1");
	}

	public boolean isGZIP___() {
	  return notifyFeatureRead(GZIP___).equals("1");
	}

	public boolean isCENSOR___() {
	  return notifyFeatureRead(CENSOR___).equals("1");
	}

	public boolean isMONITOR___() {
	  return notifyFeatureRead(MONITOR___).equals("1");
	}

	public boolean isSNAPSHOT___() {
	  return notifyFeatureRead(SNAPSHOT___).equals("1");
	}

	public void setREPLICATION___(boolean v) {
		state.put(REPLICATION___, (v ? "1" : "0"));
	}

	public void setGZIP___(boolean v) {
		state.put(GZIP___, (v ? "1" : "0"));
	}

	public void setCENSOR___(boolean v) {
		state.put(CENSOR___, (v ? "1" : "0"));
	}

	public void setMONITOR___(boolean v) {
		state.put(MONITOR___, (v ? "1" : "0"));
	}

	public void setSNAPSHOT___(boolean v) {
		state.put(SNAPSHOT___, (v ? "1" : "0"));
	}

	@Override
	public Map<FeatureVar, String> getState() {
		return state;
	}

	@Override
	public String getSPLName() {
		return "prevayler";
	}

}
