package splat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import entry.FeatureVar;
import util.FileUtils;

public class TrainTicketVariables extends Variables {

	private static TrainTicketVariables SINGLETON;

	public static TrainTicketVariables getSINGLETON() {
		if (SINGLETON == null) {
			SINGLETON = new TrainTicketVariables();
		}
		return SINGLETON;
	}

	// serviços externos
	private FeatureVar rabbitmq, kafka, redis;

	// serviços do mongo
	private FeatureVar ts_account_mongo, ts_route_mongo, ts_order_mongo, ts_verification_code_mongo, ts_contacts_mongo,
			ts_order_other_mongo, ts_config_mongo, ts_station_mongo, ts_train_mongo, ts_travel_mongo, ts_travel2_mongo,
			ts_price_mongo, ts_security_mongo, ts_assurance_mongo, ts_news_mongo, ts_inside_payment_mongo,
			ts_ticket_office_mongo, ts_rebook_mongo, ts_food_map_mongo, ts_payment_mongo, ts_consign_mongo,
			ts_consign_price_mongo, ts_food_mongo;

	// serviços "normais"
	private FeatureVar ts_ui_dashboard, ts_auth_service, ts_login_service, ts_router_service, ts_register_service,
			ts_sso_service, ts_verification_code_service, ts_contacts_service, ts_order_service, ts_order_other_service,
			ts_config_service, ts_station_service, ts_train_service, ts_travel_service, ts_travel2_service,
			ts_preserve_service, ts_preserve_other_service, ts_basic_service, ts_ticketinfo_service, ts_price_service,
			ts_notification_service, ts_security_service, ts_inside_payment_service, ts_execute_service,
			ts_payment_service, ts_rebook_service, ts_food_service, ts_consign_service, ts_cancel_service,
			ts_assurance_service, ts_seat_service, ts_travel_plan_service, ts_ticket_office_service, ts_news_service,
			ts_voucher_mysql, ts_voucher_service, ts_food_map_service, ts_consign_price_service,
			ts_admin_basic_info_service, ts_admin_order_service, ts_admin_route_service, ts_admin_travel_service,
			ts_admin_user_service;

	private TrainTicketVariables() {

		this.kafka = new FeatureVar("kafka");
		this.rabbitmq = new FeatureVar("rabbitmq");
		this.redis = new FeatureVar("redis");
		this.ts_account_mongo = new FeatureVar("ts-account-mongo");
		this.ts_ui_dashboard = new FeatureVar("ts-ui-dashboard");
		this.ts_auth_service = new FeatureVar("ts-auth-service");
		this.ts_login_service = new FeatureVar("ts-login-service");

		this.ts_router_service = new FeatureVar("ts-route-service");
		this.ts_route_mongo = new FeatureVar("ts-route-mongo");
		this.ts_register_service = new FeatureVar("ts-register-service");
		this.ts_sso_service = new FeatureVar("ts-sso-service");
		this.ts_verification_code_service = new FeatureVar("ts-verification-code-service");
		this.ts_verification_code_mongo = new FeatureVar("ts-verification-code-mongo");
		this.ts_contacts_service = new FeatureVar("ts-contacts-service");
		this.ts_contacts_mongo = new FeatureVar("ts-contacts-mongo");
		this.ts_order_service = new FeatureVar("ts-order-service");
		this.ts_order_mongo = new FeatureVar("ts-order-mongo");
		this.ts_order_other_service = new FeatureVar("ts-order-other-service");
		this.ts_order_other_mongo = new FeatureVar("ts-order-other-mongo");
		this.ts_config_service = new FeatureVar("ts-config-service");
		this.ts_config_mongo = new FeatureVar("ts-config-mongo");
		this.ts_station_service = new FeatureVar("ts-station-service");

		this.ts_station_mongo = new FeatureVar("ts-station-mongo");
		this.ts_train_service = new FeatureVar("ts-train-service");
		this.ts_train_mongo = new FeatureVar("ts-train-mongo");
		this.ts_travel_service = new FeatureVar("ts-travel-service");
		this.ts_travel_mongo = new FeatureVar("ts-travel-mongo");
		this.ts_travel2_service = new FeatureVar("ts-travel2-service");
		this.ts_travel2_mongo = new FeatureVar("ts-travel2-mongo");
		this.ts_preserve_service = new FeatureVar("ts-preserve-service");
		this.ts_preserve_other_service = new FeatureVar("ts-preserve-other-service");
		this.ts_basic_service = new FeatureVar("ts-basic-service");
		this.ts_ticketinfo_service = new FeatureVar("ts-ticketinfo-service");
		this.ts_price_service = new FeatureVar("ts-price-service");
		this.ts_price_mongo = new FeatureVar("ts-price-mongo");
		this.ts_notification_service = new FeatureVar("ts-notification-service");
		this.ts_security_service = new FeatureVar("ts-security-service");
		this.ts_security_mongo = new FeatureVar("ts-security-mongo");

		this.ts_inside_payment_service = new FeatureVar("ts-inside-payment-service");
		this.ts_inside_payment_mongo = new FeatureVar("ts-inside-payment-mongo");
		this.ts_execute_service = new FeatureVar("ts-execute-service");
		this.ts_payment_service = new FeatureVar("ts-payment-service");
		this.ts_payment_mongo = new FeatureVar("ts-payment-mongo");
		this.ts_rebook_service = new FeatureVar("ts-rebook-service");
		this.ts_rebook_mongo = new FeatureVar("ts-rebook-mongo");
		this.ts_cancel_service = new FeatureVar("ts-cancel-service");
		this.ts_assurance_service = new FeatureVar("ts-assurance-service");
		this.ts_assurance_mongo = new FeatureVar("ts-assurance-mongo");
		this.ts_seat_service = new FeatureVar("ts-seat-service");
		this.ts_travel_plan_service = new FeatureVar("ts-travel-plan-service");
		this.ts_ticket_office_service = new FeatureVar("ts-ticket-office-service");
		this.ts_ticket_office_mongo = new FeatureVar("ts-ticket-office-mongo");
		this.ts_news_service = new FeatureVar("ts-news-service");
		this.ts_news_mongo = new FeatureVar("ts-news-mongo");

		this.ts_voucher_mysql = new FeatureVar("ts-voucher-mysql");
		this.ts_voucher_service = new FeatureVar("ts-voucher-service");
		this.ts_food_map_service = new FeatureVar("ts-food-map-service");
		this.ts_food_map_mongo = new FeatureVar("ts-food-map-mongo");
		this.ts_food_service = new FeatureVar("ts-food-service");
		this.ts_consign_service = new FeatureVar("ts-consign-service");
		this.ts_consign_mongo = new FeatureVar("ts-consign-mongo");
		this.ts_consign_price_service = new FeatureVar("ts-consign-price-service");
		this.ts_consign_price_mongo = new FeatureVar("ts-consign-price-mongo");
		this.ts_food_mongo = new FeatureVar("ts-food-mongo");
		this.ts_admin_basic_info_service = new FeatureVar("ts-admin-basic-info-service");
		this.ts_admin_order_service = new FeatureVar("ts-admin-order-service");
		this.ts_admin_route_service = new FeatureVar("ts-admin-route-service");
		this.ts_admin_travel_service = new FeatureVar("ts-admin-travel-service");
		this.ts_admin_user_service = new FeatureVar("ts-admin-user-service");
		restore();

	}
	
	private void init() {
		state.put(ts_account_mongo, "?");
		state.put(ts_route_mongo, "?");
		state.put(ts_order_mongo, "?");
		state.put(ts_verification_code_mongo, "?");
		state.put(ts_contacts_mongo, "?");
		state.put(ts_order_other_mongo, "?");
		state.put(ts_config_mongo, "?");
		state.put(ts_station_mongo, "?");
		state.put(ts_train_mongo, "?");
		state.put(ts_travel_mongo, "?");
		state.put(ts_travel2_mongo, "?");
		state.put(ts_price_mongo, "?");
		state.put(ts_security_mongo, "?");
		state.put(ts_assurance_mongo, "?");
		state.put(ts_news_mongo, "?");
		state.put(ts_inside_payment_mongo, "?");
		state.put(ts_ticket_office_mongo, "?");
		state.put(ts_rebook_mongo, "?");
		state.put(ts_food_map_mongo, "?");
		state.put(ts_payment_mongo, "?");
		state.put(ts_consign_mongo, "?");
		state.put(ts_consign_price_mongo, "?");
		state.put(ts_food_mongo, "?");

		state.put(ts_ui_dashboard, "?");
		state.put(ts_auth_service, "?");
		state.put(ts_login_service, "?");
		state.put(ts_router_service, "?");
		state.put(ts_register_service, "?");
		state.put(ts_sso_service, "?");
		state.put(ts_verification_code_service, "?");
		state.put(ts_contacts_service, "?");
		state.put(ts_order_service, "?");
		state.put(ts_order_other_service, "?");
		state.put(ts_config_service, "?");
		state.put(ts_station_service, "?");
		state.put(ts_train_service, "?");
		state.put(ts_travel_service, "?");
		state.put(ts_travel2_service, "?");
		state.put(ts_preserve_service, "?");
		state.put(ts_preserve_other_service, "?");
		state.put(ts_basic_service, "?");
		state.put(ts_ticketinfo_service, "?");
		state.put(ts_price_service, "?");
		state.put(ts_notification_service, "?");
		state.put(ts_security_service, "?");
		state.put(ts_inside_payment_service, "?");
		state.put(ts_execute_service, "?");
		state.put(ts_payment_service, "?");
		state.put(ts_rebook_service, "?");
		state.put(ts_food_service, "?");
		state.put(ts_consign_service, "?");
		state.put(ts_cancel_service, "?");
		state.put(ts_assurance_service, "?");
		state.put(ts_seat_service, "?");
		state.put(ts_travel_plan_service, "?");
		state.put(ts_ticket_office_service, "?");
		state.put(ts_news_service, "?");
		state.put(ts_voucher_mysql, "?");
		state.put(ts_voucher_service, "?");
		state.put(ts_food_map_service, "?");
		state.put(ts_consign_price_service, "?");
		state.put(ts_admin_basic_info_service, "?");
		state.put(ts_admin_order_service, "?");
		state.put(ts_admin_route_service, "?");
		state.put(ts_admin_travel_service, "?");
		state.put(ts_admin_user_service, "?");
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
		return "train-ticket";
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
			value = SPLat.bt.choose(fvar, this) ? "1" : "0";
		}
		System.out.println("\nValor da variavel " + value);
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
	
	public static void main(String[] args) throws IOException {
		getSINGLETON().init();
		File file = new File("/home/caio/Documents/tcc/repositories/docker-script/target/touchedMicroservices");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String commandLine = null;
		final HashMap<String, String> servicos = new HashMap<String, String>();
		while ((commandLine = br.readLine()) != null) {
			servicos.put(commandLine, "1");
		}
		final Iterator it = servicos.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			String nomeVariavel = pair.getKey().toString();
			getSINGLETON().notifyFeatureRead(getSINGLETON().getService(nomeVariavel));
			it.remove();
		}
	}

}
