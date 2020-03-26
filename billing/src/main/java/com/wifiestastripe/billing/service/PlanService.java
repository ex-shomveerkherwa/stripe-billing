package com.wifiestastripe.billing.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import com.wifiestastripe.billing.util.StripeUtil;

@Service
public class PlanService {
	
	public List<Plan> getAllPlans(){
		Map<String,Object> params = new HashMap<>();
		try {
			PlanCollection planCollection = Plan.list(params, StripeUtil.getRequestOptions());
			List<Plan> plans = planCollection.getData();
			return plans;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// TODO check the product field for proper functioning, It cannot be empty, it cannot be null , a product ID should exist in the system 
	public Plan createPlan() {
		Map<String, Object> planParams = new HashMap<>();
		planParams.put("id", "plan_CustomAPIGeneratedPlan");
		planParams.put("interval", "month"); // NOTE - strictlly restricted to day, week, month or year.
		planParams.put("currency", "usd"); // lowercase
		planParams.put("product", "prod_GMwoo3eMEWuU9g");
		planParams.put("billing_scheme", "per_unit");
		planParams.put("amount_decimal", 4.00); // it sets 0.04 $ TODO check how to add 4$
		try {
			Plan plan = Plan.create(planParams, StripeUtil.getRequestOptions());
			return plan;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}
}
