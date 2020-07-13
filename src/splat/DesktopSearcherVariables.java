package splat;

import java.util.Map;

import entry.FeatureVar;

public class DesktopSearcherVariables extends Variables {

  private static DesktopSearcherVariables SINGLETON;

  public static DesktopSearcherVariables getSINGLETON() {
    if (SINGLETON == null) {
      SINGLETON = new DesktopSearcherVariables();
    }
    return SINGLETON;
  }
  
  private FeatureVar BASE, HTML, TXT, LATEX, USER_INTERFACE, COMMAND_LINE,
	GUI, GUI_PREFERENCES, QUERY_HISTORY, INDEX_HISTORY,
	SINGLE_DIRECTORY, MULTI_DIRECTORY, NORMAL_VIEW, TREE_VIEW, WINDOWS,
	LINUX;

	private DesktopSearcherVariables() {
		this.BASE = new FeatureVar("BASE");
		this.HTML = new FeatureVar("HTML");
		this.TXT = new FeatureVar("TXT");
		this.LATEX = new FeatureVar("LATEX");
		this.USER_INTERFACE = new FeatureVar("USER_INTERFACE");
		this.COMMAND_LINE = new FeatureVar("COMMAND_LINE");
		this.GUI = new FeatureVar("GUI");
		this.GUI_PREFERENCES = new FeatureVar("GUI_PREFERENCES");
		this.QUERY_HISTORY = new FeatureVar("QUERY_HISTORY");
		this.INDEX_HISTORY = new FeatureVar("INDEX_HISTORY");
		this.SINGLE_DIRECTORY = new FeatureVar("SINGLE_DIRECTORY");
		this.MULTI_DIRECTORY = new FeatureVar("MULTI_DIRECTORY");
		this.NORMAL_VIEW = new FeatureVar("NORMAL_VIEW");
		this.TREE_VIEW = new FeatureVar("TREE_VIEW");
		this.WINDOWS = new FeatureVar("WINDOWS");
		this.LINUX = new FeatureVar("LINUX");
		restore();
		

		try {
			guidsl = loadGUIDSL(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

  /**
   * Defines default values for the feature variables.
   */
  private void init() {
    state.put(BASE, "1");
    state.put(HTML, "?");
    state.put(TXT, "?");
    state.put(LATEX, "?");
    state.put(USER_INTERFACE, "?");
    state.put(COMMAND_LINE, "?");
    state.put(GUI, "?");
    state.put(GUI_PREFERENCES, "?");
    state.put(QUERY_HISTORY, "?");
    state.put(INDEX_HISTORY, "?");
    state.put(SINGLE_DIRECTORY, "?");
    state.put(MULTI_DIRECTORY, "?");
    state.put(NORMAL_VIEW, "?");
    state.put(TREE_VIEW, "?");
    state.put(WINDOWS, "?");
    state.put(LINUX, "?");
  }

  private String notifyFeatureRead(FeatureVar fvar) {
    String tmp = state.get(fvar);
    if (tmp == "?") {
      /**
       * only makes a choice if it is not already present in the map
       */
      tmp = SPLat.bt.choose(fvar, this) ? "1" : "0";
     // state.put(fvar, tmp);
    }
    // System.out.println(fvar + " = " + map.get(fvar));//remove
    return tmp;
  }

  public boolean isBASE___() {
    return (notifyFeatureRead(BASE).equals("1"));
  }

  public boolean isCOMMAND_LINE() {
    return (notifyFeatureRead(COMMAND_LINE).equals("1"));
  }

  public boolean isGUI() {
    return (notifyFeatureRead(GUI).equals("1"));
  }

  public boolean isGUI_PREFERENCES() {
    return (notifyFeatureRead(GUI_PREFERENCES).equals("1"));
  }

  public boolean isHTML() {
    return (notifyFeatureRead(HTML).equals("1"));
  }

  public boolean isINDEX_HISTORY() {
    return (notifyFeatureRead(INDEX_HISTORY).equals("1"));
  }

  public boolean isLATEX() {
    return (notifyFeatureRead(LATEX).equals("1"));
  }

  public boolean isLINUX() {
    return (notifyFeatureRead(LINUX).equals("1"));
  }

  public boolean isMULTI_DIRECTORY() {
    return (notifyFeatureRead(MULTI_DIRECTORY).equals("1"));
  }

  public boolean isNORMAL_VIEW() {
    return (notifyFeatureRead(NORMAL_VIEW).equals("1"));
  }

  public boolean isQUERY_HISTORY() {
    return (notifyFeatureRead(QUERY_HISTORY).equals("1"));
  }

  public boolean isSINGLE_DIRECTORY() {
    return (notifyFeatureRead(SINGLE_DIRECTORY).equals("1"));
  }

  public boolean isTREE_VIEW() {
    return (notifyFeatureRead(TREE_VIEW).equals("1"));
  }

  public boolean isTXT() {
    return (notifyFeatureRead(TXT).equals("1"));
  }

  public boolean isUSER_INTERFACE() {
    return (notifyFeatureRead(USER_INTERFACE).equals("1"));
  }

  public boolean isWINDOWS() {
    return (notifyFeatureRead(WINDOWS).equals("1"));
  }

  public void setCOMMAND_LINE(boolean v) {
    state.put(COMMAND_LINE, (v ? "1" : "0"));
  }

  public void setGUI(boolean v) {
    state.put(GUI, (v ? "1" : "0"));
  }

  public void setGUI_PREFERENCES(boolean v) {
    state.put(GUI_PREFERENCES, (v ? "1" : "0"));
  }

  public void setHTML(boolean v) {
    state.put(HTML, (v ? "1" : "0"));
  }

  public void setINDEX_HISTORY(boolean v) {
    state.put(INDEX_HISTORY, (v ? "1" : "0"));
  }

  public void setLATEX(boolean v) {
    state.put(LATEX, (v ? "1" : "0"));
  }

  public void setLINUX(boolean v) {
    state.put(LINUX, (v ? "1" : "0"));
  }

  public void setMULTI_DIRECTORY(boolean v) {
    state.put(MULTI_DIRECTORY, (v ? "1" : "0"));
  }

  public void setNORMAL_VIEW(boolean v) {
    state.put(NORMAL_VIEW, (v ? "1" : "0"));
  }

  public void setQUERY_HISTORY(boolean v) {
    state.put(QUERY_HISTORY, (v ? "1" : "0"));
  }

  public void setSINGLE_DIRECTORY(boolean v) {
    state.put(SINGLE_DIRECTORY, (v ? "1" : "0"));
  }

  public void setTREE_VIEW(boolean v) {
    state.put(TREE_VIEW, (v ? "1" : "0"));
  }

  public void setTXT(boolean v) {
    state.put(TXT, (v ? "1" : "0"));
  }

  public void setUSER_INTERFACE(boolean v) {
    state.put(USER_INTERFACE, (v ? "1" : "0"));
  }

  public void setWINDOWS(boolean v) {
    state.put(WINDOWS, (v ? "1" : "0"));
  }

  /******************/
  @Override
  public void restore() {
    state.clear();
    init();
  }

	@Override
	public Map<FeatureVar, String> getState() {
		return state;
	}

	@Override
	public String getSPLName() {
		return "desktopsearcher";
	}

}
