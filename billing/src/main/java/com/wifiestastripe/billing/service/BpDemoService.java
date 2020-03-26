
package com.wifiestastripe.billing.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stripe.model.Customer;
import com.wifiestastripe.billing.repository.BpDemoRepository;

@Service
public class BpDemoService {

	@Autowired
	BpDemoRepository repository;

	@Autowired
	CustomerService customerService;

	@Autowired
	InvoiceService invoiceService;

	private final Logger logger = LoggerFactory.getLogger(BpDemoService.class);

	public void generateBill(String bpId) throws Exception {
		logger.info("bpId: {}", bpId);

		// fetch the BP details by id 
		String bpString = repository.getBusinessProfile(bpId);

		// check if a Bp is returned
		if (bpString == null) {
			throw new Exception("Bp Customer Not Found");
		}

		// get bp branches 
		List<String> bpBranches = repository.getBpBranches(bpId);
		if (bpBranches == null || bpBranches.isEmpty())
		{
			throw new Exception("Bp Customer has no branches");
		}

		// get his coupons 
		List<String> couponIds = repository.getBranchCoupons(bpBranches);

		if (couponIds == null || couponIds.isEmpty()) {
			throw new Exception("Branches have no coupon generated");
		}

		logger.info("{} : coupons found for the bp branches ", couponIds.size());

		// get his stripe customer Id from couchbase 
		String stripeCustomerId = repository.getStripeCustomerId(bpId);

		if (stripeCustomerId == null) {
			throw new Exception("Stripe customer not found for bp : " + bpId);
		}

		logger.info("bp: {} has a Stripe customer : {}", bpId, stripeCustomerId);

		// verify if customer exists in Stripe 
		Customer stripeCustomer = customerService.getCustomer(stripeCustomerId);

		if (stripeCustomer == null) {
			throw new Exception("{}: not found in stripe " + stripeCustomerId);
		}

		// generate line items 
		invoiceService.createLineItems(stripeCustomerId);
		logger.info("Line items created");

		// generate invocie 
		invoiceService.createInvoice(stripeCustomerId);
		logger.info("Invocie generated");

	}
}
