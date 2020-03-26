package com.wifiestastripe.billing.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.Address;
import com.stripe.model.Customer;
import com.wifiestastripe.billing.util.StripeUtil;

@Service
public class CustomerService {

	public Customer getCustomer(String customerId) {
		try {
			Customer customer = Customer.retrieve(customerId, StripeUtil.getRequestOptions());
			return customer;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Customer addCustomer() {
		try {
			/*
			 * Address newAddress = new Address(); newAddress.setLine1("yelhanka new town");
			 * newAddress.setState("Karnataka"); newAddress.setCountry("IN");
			 */

			Map<String, String> addressMap = new HashMap<String, String>();
			addressMap.put("line1", "Yelhanka New Town"); // NOTE : this is the only required param
			addressMap.put("line2", "Attur Post");
			addressMap.put("postal_code", "560097");
			addressMap.put("city", "Bangalore");
			addressMap.put("state", "Karnataka");
			addressMap.put("country", "IN");

			String[] locales = new String[] { "en","ja" };

			Map<String, Object> customerParams = new HashMap<String, Object>();
			customerParams.put("name", "Test Address");
			customerParams.put("address", addressMap);
			customerParams.put("preferred_locales", locales);
			//customerParams.put("currency", "usd"); 

			Customer customer = Customer.create(customerParams, StripeUtil.getRequestOptions());
			return customer;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Customer updateCustomer(String customerId) {
		try {
			Customer customer = Customer.retrieve(customerId, StripeUtil.getRequestOptions());
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("currency" , "inr");
			customer = customer.update(params, StripeUtil.getRequestOptions());
			return customer;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}
}
