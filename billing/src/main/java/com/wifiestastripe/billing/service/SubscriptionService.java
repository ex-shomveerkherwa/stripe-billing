package com.wifiestastripe.billing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;
import com.wifiestastripe.billing.util.StripeUtil;

@Service
public class SubscriptionService {

	public List<Subscription> getAllSubscription() {
		Map<String, Object> emptyParams = new HashMap<>();
		try {
			SubscriptionCollection collection = Subscription.list(emptyParams, StripeUtil.getRequestOptions());
			List<Subscription> allSubscriptions = collection.getData();
			return allSubscriptions;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Subscription getSubscription(String subscriptionId) {
		try {
			return Subscription.retrieve(subscriptionId, StripeUtil.getRequestOptions());
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// TODO check if products and plans are necessary for an subscription - Yes a plan has to be present in Stripe
	public Subscription createSubscription() {
		
		List<Object> items = new ArrayList<Object>(); // REQUIRED - list of items , each item corresponds to a plan
		
		Map<String, Object> customItem = new HashMap<String, Object>(); // This item has to be present in Stripe
		customItem.put("plan", "plan_CustomAPIGeneratedPlan");
		
		items.add(customItem); // collection of items to be included in subscription
		
		Map<String, Object> subsParam = new HashMap<>();
		subsParam.put("customer", "cus_GMylvNg4CTARt2");
		subsParam.put("collection_method", "send_invoice");
		subsParam.put("days_until_due", 1); // REQUIRED if collection_method is send_invoice
		subsParam.put("items", items);
		try {
			Subscription sub = Subscription.create(subsParam, StripeUtil.getRequestOptions());
			return sub;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}
}
