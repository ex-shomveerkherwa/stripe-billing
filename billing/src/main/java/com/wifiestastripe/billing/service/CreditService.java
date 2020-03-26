package com.wifiestastripe.billing.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.CreditNote;
import com.wifiestastripe.billing.util.StripeUtil;

@Service
public class CreditService {

	public CreditNote createCreditForInvoice(String invoiceId) {
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("invoice", invoiceId);
		params.put("amount", 1700);   // 17$
		try {
			CreditNote creditNote = CreditNote.create(params, StripeUtil.getRequestOptions());
			return creditNote;
		} catch (StripeException e) {
			e.printStackTrace();
		}	
		return null;
	}

}
