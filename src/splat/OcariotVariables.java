package splat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import entry.FeatureVar;
import util.FileUtils;

public class OcariotVariables extends Variables {
	
	private static OcariotVariables SINGLETON;
	
	public static OcariotVariables getSINGLETON() {
		if (SINGLETON == null) {
			SINGLETON = new OcariotVariables();
		}
		return SINGLETON;
	}
	
	private FeatureVar mongo_account, mongo_iot_tracking, mongo_ds_agent, mongo_quest, 
	mongo_gamification, mongo_food, mongo_notification, mysql_missions, redis_api_gateway,
	redis_ds_agent, api_gateway, account, iot_tracking, ds_agent, quest, gamification,
	food, missions, notification, rabbitmq;
	
	private OcariotVariables() {
		this.mongo_account = new FeatureVar("mongo-account");
		this.mongo_iot_tracking = new FeatureVar("mongo-iot-tracking");
		this.mongo_ds_agent = new FeatureVar("mongo-ds-agent");
		this.mongo_quest = new FeatureVar("mongo-quest");
		this.mongo_gamification = new FeatureVar("mongo-gamification");
		this.mongo_food = new FeatureVar("mongo-food");
		this.mongo_notification = new FeatureVar("mongo-notification");
		this.mysql_missions  = new FeatureVar("mysql-missions");
		this.redis_api_gateway  = new FeatureVar("redis-api-gateway");
		this.redis_ds_agent = new FeatureVar("redis-ds-agent");
		this.api_gateway = new FeatureVar("api-gateway");
		this.account = new FeatureVar("account");
		this.iot_tracking = new FeatureVar("iot-tracking");
		this.ds_agent = new FeatureVar("ds-agent");
		this.quest = new FeatureVar("quest");
		this.gamification = new FeatureVar("gamification");
		this.food = new FeatureVar("food");
		this.missions = new FeatureVar("missions");
		this.notification = new FeatureVar("notification");
		this.rabbitmq = new FeatureVar("rabbitmq");
	}
	
	private void init() {
		state.put(mongo_account, "?");
		state.put(mongo_iot_tracking, "?");
		state.put(mongo_ds_agent, "?");
		state.put(mongo_quest, "?");
		state.put(mongo_gamification, "?");
		state.put(mongo_food, "?");
		state.put(mongo_notification, "?");
		state.put(mysql_missions, "?");
		state.put(redis_api_gateway, "1");
		state.put(redis_ds_agent, "?");
		state.put(api_gateway, "1");
		state.put(account, "?");
		state.put(iot_tracking, "?");
		state.put(ds_agent, "?");
		state.put(quest, "?");
		state.put(gamification, "?");
		state.put(food, "?");
		state.put(missions, "?");
		state.put(notification, "?");
		state.put(rabbitmq, "1");
	}
	
	private FeatureVar getService(String serviceName) {
		final Iterator it = state.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			FeatureVar fvar = (FeatureVar) pair.getKey();
			if (serviceName.equals(fvar.getName()))
				return fvar;
		}
		return null;

	}
		

	@Override
	public Map<FeatureVar, String> getState() {
		return state;
	}

	@Override
	public String getSPLName() {
		return "OCARIoT";
	}

	@Override
	public void restore() {
		state.clear();
		init();
	}
	
	private String notifyFeatureRead(FeatureVar fvar) {
		String value = state.get(fvar);
		if (value == "?") {
			/**
			 * only makes a choice if it is not already present in the map
			 */
			value = SPLatJS.bt.choose(fvar, this) ? "1" : "0";
		}
		// System.out.println("\nValor da variavel " + value);
		return value;

	}
	
	@Override
	public void notifyServicesLoaded(String servicesPath){
		final HashMap<String, String> services = (HashMap<String, String>) FileUtils.loadLinesFrom(servicesPath);
		for (Entry<String, String> pair : services.entrySet()) {
			String nameVar = pair.getKey().toString();
			getSINGLETON().notifyFeatureRead(getSINGLETON().getService(nameVar));
		}
	}

}
