package com.wifiestastripe.billing.controller;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.Subscription;
import com.wifiestastripe.billing.service.SubscriptionService;

@RestController
@RequestMapping("/subs-mgmt")
public class SubscriptionController {

	private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);
	
	@Autowired
	private SubscriptionService subscriptionService; 
	
	@GetMapping("/")
	public void healthcheck() {
		logger.info("Healthcheck of {}: at {}:", this.getClass().getSimpleName(), System.currentTimeMillis());
	}
	
	@GetMapping("/subs")
	public void getAllSubscriptions() {
		List<Subscription> subs = subscriptionService.getAllSubscription();
		if(Objects.isNull(subs)) {
			logger.error("Failed to get subscriptions form stripe");
		}else {
			subs.stream().forEach(sub -> logger.info("sub: {}", sub));
		}
	}
	
	@GetMapping("/subs/{id}")
	public void getAllSubscriptions(@PathVariable("id") String subscriptionId) {
		Subscription sub = subscriptionService.getSubscription(subscriptionId);
		if (Objects.isNull(sub)) {
			logger.error("Failed to get {} subscription form stripe", subscriptionId);
		} else {
			logger.info("Susbscription {}", sub);
		}
	}
	
	@PostMapping("/sub")
	public void createSubscription() {
		Subscription sub = subscriptionService.createSubscription();
		if (Objects.isNull(sub)) {
			logger.error("Failed to create Subscription in stripe");
		} else {
			logger.info("New Susbscription {}", sub);
		}
	}
}
