package com.wifiestastripe.billing.controller;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.Plan;
import com.wifiestastripe.billing.service.PlanService;

@RestController
@RequestMapping("/plan-mgmt")
public class PlanController {

	private static Logger logger = LoggerFactory.getLogger(PlanController.class);
	
	@Autowired
	private PlanService planService;
	
	@GetMapping("/")
	public void healthcheck() {
		logger.info("Healthcheck of {}: at {}:", this.getClass().getSimpleName(), System.currentTimeMillis());
	}
	
	@GetMapping("/plans")
	public void getAllPlans() {
		List<Plan> plans = planService.getAllPlans();
		if(Objects.isNull(plans)) {
			logger.error("Failed to get Plans from stripe");
		}else {
			plans.stream().forEach(plan -> logger.info("id: {}, name: {}, Belongs to product:{}", plan.getId(), plan.getNickname(), plan.getProduct()));
		}
	}
	
	@PostMapping("/plan")
	public void createPlan() {
		Plan plan = planService.createPlan(); // creates a plan with hardcoded details
		if (Objects.isNull(plan)) {
			logger.error("Failed to create plan in stripe");
		} else {
			logger.info("plan: {}", plan);
		}
	}
}
