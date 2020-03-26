package com.wifiestastripe.billing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.CreditNote;
import com.wifiestastripe.billing.service.CreditService;

@RestController
@RequestMapping("credit-mgmt")
public class CreditController {

	private final Logger logger = LoggerFactory.getLogger(CreditController.class);

	@Autowired
	CreditService creditService;

	@GetMapping("/")
	public void healthcheck() {
		logger.info("Healthcheck of {}: at {}:", this.getClass().getSimpleName(), System.currentTimeMillis());
	}

	@PostMapping("/credit/{id}")
	public void createCreditNote(@PathVariable("id") String invoiceId) {
		CreditNote creditNote = creditService.createCreditForInvoice(invoiceId);
		if (creditNote == null) {
			logger.info("Failed to create credit note");
		} else {
			logger.info("credit note: {}", creditNote);
		}
	}
}
