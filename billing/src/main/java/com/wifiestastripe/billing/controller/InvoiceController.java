package com.wifiestastripe.billing.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.CreditNote;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;
import com.stripe.model.Refund;
import com.wifiestastripe.billing.service.InvoiceService;
	
@RestController
@RequestMapping("/invoice-mgmt")
public class InvoiceController {
	
	private final Logger logger = LoggerFactory.getLogger(InvoiceController.class);
	
	@Autowired
	InvoiceService invoiceService;
	
	@GetMapping("/")
	public void healthcheck() {
		logger.info("Healthcheck of {}: at {}:", this.getClass().getSimpleName(), System.currentTimeMillis());
	}

	@GetMapping("/invoice/{id}")
	public void getInvoice(@PathVariable("id") String invoiceId) {
		Invoice invoice = invoiceService.getInvoice(invoiceId);
		if (invoice == null) {
			logger.error("Failed to get invoice: {} from stripe", invoiceId);
		} else {
			logger.info("Invoice :{}", invoice.toString());
			logger.info("invoice ID: {}, status: {}", invoice.getId(), invoice.getStatus());
		}
	}
	
	@GetMapping("/invoice/calculate/{id}")
	public void getInvoiceAndCalculateTotal(@PathVariable("id") String invoiceId) {
		invoiceService.getInvoiceAndCalculateTotal(invoiceId);
	}
	
	@GetMapping("/invoice/item/pending/customer/{id}")
	public void getAllInvoiceItems(@PathVariable("id") String customerId) {
		List<InvoiceItem> invoices = invoiceService.getAllPendingInvoiceItems(customerId);
		if (invoices == null) {
			logger.error("Failed to get invoices from stripe for customer: {} ", customerId);
		} else {
			logger.info("invoice: {}", invoices.size());
		}
	}

	@GetMapping("/invoice/item/{id}")
	public void getInvoiceItem(@PathVariable("id") String itemId) {
		InvoiceItem item = invoiceService.getInvoiceItem(itemId);
		if (item == null) {
			logger.error("Failed to get item from stripe: {} ", itemId);
		} else {
			logger.info("item: {}", item.toString());
		}
	}
	
	@PostMapping("/invoice")
	public void createInvoice(@RequestParam("customerId") String customerId) {
		Invoice invoice = invoiceService.createInvoice(customerId);
		if (invoice == null) {
			logger.error("Failed to create invoice in stripe for customer: {}", customerId);
		} else {
			logger.info("invoice: {}", invoice);
		}
	}
	
	// NOTE: filters are added in the service method. Not customizable as of FEB 10 2020
	@GetMapping("/invoices/customer/{customerId}")
	public void filterAndGetInvoices(@PathVariable("customerId") String customerId) {
		invoiceService.filterAndGetInvoices(customerId);
	}
	
	@PostMapping("/invoice/paid/{id}")
	public void markInvoicePaid(@PathVariable("id") String invoiceId) {
		Invoice invoice = invoiceService.markInvociePaid(invoiceId);
		if (invoice == null) {
			logger.error("Failed to mark invoice paid in stripe : {}", invoiceId);
		} else {
			logger.info("invoice: {}", invoice);
		}
	}
	
	@PostMapping("/invoice/creditNote/{id}")
	public void createCreditNote(@PathVariable("id") String invoiceId) {
		CreditNote creditNote = invoiceService.createCreditNote(invoiceId);
		if (creditNote == null) {
			logger.error("Failed to create credit note for stripe invoice: {}", invoiceId);
		} else {
			logger.info("CreditNote: {}", creditNote);
		}
	}
	
	@PostMapping("/invoice/item")
	public void createInvoiceItem(@RequestParam("customerId") String customerId) {
		InvoiceItem item = invoiceService.createLineItems(customerId);
		if (item == null) {
			logger.error("Failed to create item in stripe for customer: {}", customerId);
		} else {
			logger.info("item: {}", item);
		}
	}
	
	@PostMapping("/invoice/future/item")
	public void createFutureLineItems(@RequestParam("customerId") String customerId) {
		InvoiceItem item = invoiceService.createFutureLineItems(customerId);
		if (item == null) {
			logger.error("Failed to create future item in stripe for customer: {}", customerId);
		} else {
			logger.info("future item: {}", item);
		}
	}
	
	@PostMapping("/invoice/past/item")
	public void createPastLineItems(@RequestParam("customerId") String customerId) {
		InvoiceItem item = invoiceService.createPastLineItems(customerId);
		if (item == null) {
			logger.error("Failed to create past item in stripe for customer: {}", customerId);
		} else {
			logger.info("past item: {}", item);
		}
	}

	@PostMapping("/invoice/current/item")
	public void createCurrentLineItems(@RequestParam("customerId") String customerId) {
		InvoiceItem item = invoiceService.createCurrentLineItems(customerId);
		if (item == null) {
			logger.error("Failed to create past item in stripe for customer: {}", customerId);
		} else {
			logger.info("current item: {}", item);
		}
	}
	
	@GetMapping("/invoice/{id}/send")
	public void getAndSendInvoiceToCustomer(@PathVariable("id") String invoiceId) {
		Invoice sentInvoice = invoiceService.sendInvoice(invoiceId);
		if (sentInvoice == null) {
			logger.error("Failed to send invoice: {} to the customer", invoiceId);
		} else {
			logger.info("invoice: {}", sentInvoice);
		}
	}
	
	@PostMapping("/invoice/{id}/update")
	public void updateInvoice(@PathVariable("id") String invoiceId) {
		Invoice updatedInvoice = invoiceService.updateInvoice(invoiceId);
		if (updatedInvoice == null) {
			logger.error("Failed to update invoice: {} to the customer", invoiceId);
		} else {
			logger.info("invoice: {}", updatedInvoice);
		}
	}
	
	@GetMapping("/invoice/{id}/finalize")
	public void finalizeInvoice(@PathVariable("id") String invoiceId) {
		Invoice finalizeInvoice = invoiceService.finalizeInvoice(invoiceId);
		if (finalizeInvoice == null) {
			logger.error("Failed to send invoice: {} to the customer", invoiceId);
		} else {
			logger.info("invoice: {}", finalizeInvoice);
		}
	}
	// TODO pay of invoice was not successfull , try adding sources and re attempt 
	
	@GetMapping("/invoice/{id}/pay")
	public void payInvoice(@PathVariable("id") String invoiceId) {
		Invoice payedInvoice = invoiceService.payInvoice(invoiceId);
		if (payedInvoice == null) {
			logger.error("Failed to pay invoice: {} of the customer", invoiceId);
		} else {
			logger.info("invoice: {}", payedInvoice);
		}
	}
	
	@GetMapping("/invoice/{id}/void")
	public void voidInvoice(@PathVariable("id") String invoiceId) {
		Invoice payedInvoice = invoiceService.voidInvoice(invoiceId);
		if (payedInvoice == null) {
			logger.error("Failed to void invoice: {} of the customer", invoiceId);
		} else {
			logger.info("invoice: {}", payedInvoice);
		}
	}
	
	@GetMapping("/invoice/{id}/refund")
	public void refundAChargedInvoice(@PathVariable("id") String invoiceId) {
		Refund refund = invoiceService.createRefundForCharge(invoiceId);
		if (refund == null) {
			logger.error("Failed to create refund for charge of invoice: {}", invoiceId);
		} else {
			logger.info("refund: {}", refund);
		}
	}
}
