package com.wifiestastripe.billing.util;

import com.stripe.net.RequestOptions;

public class StripeUtil {

	public static final String API_KEY = "sk_test_w2LOkBVjefZL9MQP0n6tpqZn006X7Bp05C";
	
	public static RequestOptions getRequestOptions() {
		return RequestOptions.builder().setApiKey(API_KEY).build();
	}
}
