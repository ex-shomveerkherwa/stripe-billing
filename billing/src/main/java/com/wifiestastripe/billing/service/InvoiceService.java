package com.wifiestastripe.billing.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.CreditNote;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import com.stripe.model.InvoiceItem;
import com.stripe.model.InvoiceItemCollection;
import com.stripe.model.InvoiceLineItem;
import com.stripe.model.InvoiceLineItemCollection;
import com.stripe.model.Refund;
import com.stripe.param.CreditNoteCreateParams;
import com.wifiestastripe.billing.util.StripeUtil;

@Service
public class InvoiceService {

	private final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

	public Invoice getInvoice(String invoiceId) {
		try {
			logger.info("Get details of invoice: {} from stripe ", invoiceId);
			Invoice invoice = Invoice.retrieve(invoiceId, StripeUtil.getRequestOptions());
			return invoice;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}

	// NOTE 1. Have a seperate method to generate few line items , or other way is
	// to have few subscriptions of the user
	// and then when you call the create invoice , the subscriptions will be pulled
	// in.
	public Invoice createInvoice(String customerId) {
		// logger.info(" create empty invoice");
		// create lineItem to be populated in the invoice or manually subscribe the
		// customer in dashoard
		// createNegativeAmountLineItems(customerId);
		createLineItems(customerId);

		Map<String, Object> invoiceParams = new HashMap<>();
		invoiceParams.put("customer", customerId); // The Only required field as per documentation
		// invoiceParams.put("collection_method", "send_invoice"); // if we want to send
		// the invocie and do not want stripe to charge
		//invoiceParams.put("days_until_due", 3); // required for send_invoice types
		invoiceParams.put("collection_method", "charge_automatically");
		invoiceParams.put("auto_advance", false);
		Map<String, Object> metaDataParams = new HashMap<String, Object>();
		metaDataParams.put("My NAme", "som");
		invoiceParams.put("metadata", metaDataParams);
		try {
			Invoice invoice = Invoice.create(invoiceParams, StripeUtil.getRequestOptions());
			logger.info("invoice: {}", invoice);
			return invoice;
		} catch (StripeException e) {
			logger.info("somaaa cause : {}", e.getCause());
			logger.info("somaaa local message : {}", e.getLocalizedMessage());
			logger.info("somaaa message : {}", e.getMessage());
			logger.info("--------");
			e.printStackTrace();
		}
		return null;
	}

	public InvoiceItem createNegativeAmountLineItems(String customerId) {
		Map<String, Object> invoiceItemParams = new HashMap<>();
		invoiceItemParams.put("customer", customerId);
		invoiceItemParams.put("amount", -3500); // hoping this is 1 dollar
		invoiceItemParams.put("currency", "usd");
		invoiceItemParams.put("description", "discount for unsatisfied customer");
		InvoiceItem invoiceItem = null;
		try {
			invoiceItem = InvoiceItem.create(invoiceItemParams, StripeUtil.getRequestOptions());
			logger.info("Invoice LineItem created " + invoiceItem.getId());
		} catch (StripeException e1) {
			e1.printStackTrace();
		}
		return invoiceItem;
	}

	public InvoiceItem createLineItems(String customerId) {
		Map<String, Object> invoiceItemParams = new HashMap<>();
		Map<String,Object> metadataParams = new HashMap<>();
		metadataParams.put("key1", "val1");
		metadataParams.put("key2", "val2");
		metadataParams.put("key3", "val3");
		
		invoiceItemParams.put("metadata", metadataParams);
		invoiceItemParams.put("customer", customerId);
		// invoiceItemParams.put("amount", 1000); // hoping this is 1 dollar
		invoiceItemParams.put("currency", "usd");
		// invoiceItemParams.put("currency", "inr"); // check if the currency of
		// customer is set to inr for all the rest of trans
		invoiceItemParams.put("description", "testing scheduled invoices");
		invoiceItemParams.put("unit_amount", 3000);
		// TODO check with 0 quantity , replace with a whole number after check
		invoiceItemParams.put("quantity", 12); // NOTE quantity and amount are mutually exclusive
		// invoiceItemParams.put("invoice", "in_1FyubpLu4a5TdA9h8OFYDrRF");
		InvoiceItem invoiceItem = null;
		try {
			invoiceItem = InvoiceItem.create(invoiceItemParams, StripeUtil.getRequestOptions());
			logger.info("Invoice LineItem created " + invoiceItem.getId());
		} catch (StripeException e1) {
			e1.printStackTrace();
		}
		return invoiceItem;
	}

	public InvoiceItem createFutureLineItems(String customerId) {
		Map<String, Object> invoiceItemParams = new HashMap<>();
		invoiceItemParams.put("customer", customerId);
		// invoiceItemParams.put("amount", 1000); // hoping this is 1 dollar
		 invoiceItemParams.put("currency", "usd");
		//invoiceItemParams.put("currency", "inr"); // check if the currency of customer is set to inr for all the rest of
													// trans
		invoiceItemParams.put("description", "wifiesta coupons generated");
		invoiceItemParams.put("unit_amount", 3000);
		// TODO check with 0 quantity , replace with a whole number after check
		invoiceItemParams.put("quantity", 70); // NOTE quantity and amount are mutually exclusive

		// create this item for future
		Map<String, Object> periodMap = new HashMap<>();
		periodMap.put("start", "1580774400");
		periodMap.put("end",   "1581638400");

		invoiceItemParams.put("period", periodMap);

		InvoiceItem invoiceItem = null;
		try {
			invoiceItem = InvoiceItem.create(invoiceItemParams, StripeUtil.getRequestOptions());
			logger.info("Invoice LineItem created " + invoiceItem.getId());
		} catch (StripeException e1) {
			e1.printStackTrace();
		}
		return invoiceItem;
	}

	public InvoiceItem createPastLineItems(String customerId) {
		Map<String, Object> invoiceItemParams = new HashMap<>();
		invoiceItemParams.put("customer", customerId);
		// invoiceItemParams.put("amount", 1000); // hoping this is 1 dollar
		// invoiceItemParams.put("currency", "usd");
		invoiceItemParams.put("currency", "inr"); // check if the currency of customer is set to inr for all the rest of
													// trans
		invoiceItemParams.put("description", "past coupons generated");
		invoiceItemParams.put("unit_amount", 3000);
		// TODO check with 0 quantity , replace with a whole number after check
		invoiceItemParams.put("quantity", 15); // NOTE quantity and amount are mutually exclusive

		// create this item for future
		Map<String, Object> periodMap = new HashMap<>();
		periodMap.put("start", "1557906941");
		periodMap.put("end", "1563177341");

		invoiceItemParams.put("period", periodMap);

		InvoiceItem invoiceItem = null;
		try {
			invoiceItem = InvoiceItem.create(invoiceItemParams, StripeUtil.getRequestOptions());
			logger.info("Invoice LineItem created " + invoiceItem.getId());
		} catch (StripeException e1) {
			e1.printStackTrace();
		}
		return invoiceItem;
	}

	public InvoiceItem createCurrentLineItems(String customerId) {
		Map<String, Object> invoiceItemParams = new HashMap<>();
		invoiceItemParams.put("customer", customerId);
		// invoiceItemParams.put("amount", 1000); // hoping this is 1 dollar
		// invoiceItemParams.put("currency", "usd");
		invoiceItemParams.put("currency", "inr"); // check if the currency of customer is set to inr for all the rest of
													// trans
		invoiceItemParams.put("description", "past coupons generated");
		invoiceItemParams.put("unit_amount", 3000);
		// TODO check with 0 quantity , replace with a whole number after check
		invoiceItemParams.put("quantity", 100); // NOTE quantity and amount are mutually exclusive

		// create this item for future
		Map<String, Object> periodMap = new HashMap<>();
		periodMap.put("start", "1577865341");
		periodMap.put("end", "1580543741");

		invoiceItemParams.put("period", periodMap);

		InvoiceItem invoiceItem = null;
		try {
			invoiceItem = InvoiceItem.create(invoiceItemParams, StripeUtil.getRequestOptions());
			logger.info("Invoice LineItem created " + invoiceItem.getId());
		} catch (StripeException e1) {
			e1.printStackTrace();
		}
		return invoiceItem;
	}

	public Invoice sendInvoice(String invoiceId) {
		Invoice invoice = getInvoice(invoiceId);
		try {
			invoice = invoice.sendInvoice(StripeUtil.getRequestOptions());
			logger.info("invoice sent ");
			return invoice;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Invoice voidInvoice(String invoiceId) {
		Invoice invoice = getInvoice(invoiceId);
		try {
			invoice = invoice.voidInvoice(StripeUtil.getRequestOptions());
			logger.info("invoice voided ");
			return invoice;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Invoice updateInvoice(String invoiceId) {
		Invoice invoice = getInvoice(invoiceId);
		Map<String, Object> invoiceParams = new HashMap<>();
		invoiceParams.put("auto_advance", true);
		try {
			invoice = invoice.update(invoiceParams, StripeUtil.getRequestOptions());
			invoice = invoice.sendInvoice(StripeUtil.getRequestOptions());
			logger.info("invoice updated ");
			return invoice;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Invoice finalizeInvoice(String invoiceId) {
		Invoice invoice = getInvoice(invoiceId);
		try {
			invoice = invoice.finalizeInvoice(StripeUtil.getRequestOptions());
			logger.info("invoice finalized ");
			return invoice;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}

	// TODO verify if the the source information needs to be updated before having a
	// successful pay .
	public Invoice payInvoice(String invoiceId) {
		Invoice invoice = getInvoice(invoiceId);
		Map<String, Object> invoiceParams = new HashMap<String, Object>();
		invoiceParams.put("off_session", "true");

		try {
			invoice = invoice.pay(invoiceParams, StripeUtil.getRequestOptions());
			logger.info("invoice payed status: {} code: {}",invoice.getLastResponse(), invoice.getLastResponse().code());
			return invoice;
		} catch (StripeException e) {
			logger.error("invoice payment error: {} and status code: {}", e.getCode(), e.getStatusCode());
			e.printStackTrace();
		}
		return null;
	}

	public Refund createRefundForCharge(String invoiceId) {
		Invoice invoice = getInvoice(invoiceId);
		logger.info("invoice: {} has amount: {} and charge id: {}", invoice.getId(), invoice.getAmountRemaining(),
				invoice.getCharge());

		Map<String, Object> params = new HashMap<>();
		params.put("charge", invoice.getCharge());
		// if amount is not specified, whole amount is considered for the refund.
		try {
			Refund refund = Refund.create(params, StripeUtil.getRequestOptions());
			return refund;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<InvoiceItem> getAllPendingInvoiceItems(String customerId) {
		Map<String, Object> params = new HashMap<>();
		params.put("pending", true);
		params.put("customer", customerId);
		params.put("limit", 100);
		
		 Map<String, Object> createdParams = new HashMap<>();
	        createdParams.put("lte", "1583340031");
	        params.put("created", createdParams);
		
		try {
			InvoiceItemCollection invoiceItems = InvoiceItem.list(params, StripeUtil.getRequestOptions());
			List<InvoiceItem> items = invoiceItems.getData();
			List<String> itemIds = invoiceItems.getData().stream().map(item -> item.getId()).collect(Collectors.toList());
            logger.info("Pending invoice items: {}", Arrays.toString(itemIds.toArray()));
			logger.info("Total invoice items: {}", items.size());
			logger.info("Item Data:");
			items.stream().forEach(data -> System.out.println(data.getId()));
			return invoiceItems.getData();
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public InvoiceItem getInvoiceItem(String itemId) {
		try {
			InvoiceItem invoiceItem = InvoiceItem.retrieve(itemId, StripeUtil.getRequestOptions());
			return invoiceItem;
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Invoice markInvociePaid(String invoiceId) {
		Invoice invoice = getInvoice(invoiceId);
		Map<String, Object> invoiceParams = new HashMap<String, Object>();
		invoiceParams.put("status", "paid");
		try {
			invoice = invoice.update(invoiceParams, StripeUtil.getRequestOptions());
			logger.info("invoice updated to paid ");
			return invoice;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void getInvoiceAndCalculateTotal(String invoiceId) {
		Invoice invoice = getInvoice(invoiceId);
		logger.info("invocie: {}", invoice);
	}
	
	public void filterAndGetInvoices(String customerId) {
		try {
			
			Map<String, Object> params = new HashMap<>();
			params.put("limit", 3);
			params.put("customer", customerId);
			params.put("status", "draft");

			Map<String, Object> createdParams = new HashMap<>();
			createdParams.put("gte", 1582035695);

			params.put("created", createdParams);
			
			InvoiceCollection invoiceCollection = Invoice.list(params, StripeUtil.getRequestOptions());
			List<Invoice> invoices = invoiceCollection.getData();
			invoices.stream().forEach(inv -> logger.info("invocieId: {} ", inv.getId()));
		} catch(StripeException e) {
			e.printStackTrace();
		}
	}

	public CreditNote createCreditNote(String invoiceId) {
		// 1. get invoice
		Invoice stripeInvoice = getInvoice(invoiceId);
		
		// 2. get amount 
		Long amount = stripeInvoice.getAmountPaid();
		
		// 3. Get details of line items
		InvoiceLineItemCollection collection = stripeInvoice.getLines();
		List<InvoiceLineItem> items = collection.getData();
		List<Object> lines = new ArrayList<>();
		
		for(InvoiceLineItem item : items) {
			Map<String,Object> lineMap = new HashMap<>();
			
			lineMap.put("type", "invoice_line_item");
			lineMap.put("invoice_line_item", item.getId());		
			lineMap.put("quantity", item.getQuantity());
			
			logger.info("invoice_line_item: {} ",item.getId());
			logger.info("quantity: {} ", item.getQuantity());
			lines.add(lineMap);
		}
		
		Map<String, Object> creditNoteparams = new HashMap<>();
		creditNoteparams.put("invoice", invoiceId);
		creditNoteparams.put("lines", lines);
		creditNoteparams.put("refund_amount", amount);
		
		try {
			CreditNote creditNote = CreditNote.create(creditNoteparams, StripeUtil.getRequestOptions());
			return creditNote;
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
