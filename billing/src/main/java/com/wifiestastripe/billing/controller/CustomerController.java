package com.wifiestastripe.billing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.Customer;
import com.wifiestastripe.billing.service.CustomerService;

@RestController
@RequestMapping("/customer-mgmt")
public class CustomerController {

private final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/")
	public void healthcheck() {
		logger.info("Healthcheck of {}: at {}:", this.getClass().getSimpleName(), System.currentTimeMillis());
	}
	
	@GetMapping("/customer/{id}")
	public void getCustomer(@PathVariable("id") String customerId) {
		Customer customer = customerService.getCustomer(customerId);
		if (customer == null) {
			logger.error("Failed to get customer: {} from stripe", customerId);
		} else {
			logger.info("customer: {}", customer);
		}
	}
	
	@PutMapping("/customer/{id}")
	public void updateCustomer(@PathVariable("id") String customerId) {
		Customer customer = customerService.updateCustomer(customerId);
		if (customer == null) {
			logger.error("Failed to update customer: {} in stripe", customerId);
		} else {
			logger.info("customer: {}", customer);
		}
	}
	
	@PostMapping("/customer")
	public void addCustomer() {
		Customer customer = customerService.addCustomer();
		if (customer == null) {
			logger.error("Failed to create customer in stripe");
		} else {
			logger.info("customer created : {}", customer);
		}
	}
}
