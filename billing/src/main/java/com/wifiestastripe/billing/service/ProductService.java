package com.wifiestastripe.billing.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.model.ProductCollection;
import com.wifiestastripe.billing.util.StripeUtil;

@Service
public class ProductService {

	Logger logger = LoggerFactory.getLogger(ProductService.class);
	
	public List<Product> getAllProducts() {
		// other way of calling Stripe APIS without request options
		Stripe.apiKey = "sk_test_w2LOkBVjefZL9MQP0n6tpqZn006X7Bp05C";

		Map<String, Object> params = new HashMap<>();
		//params.put("limit", 3);

		try {
			ProductCollection productCollection = Product.list(params);
			List<Product> products = productCollection.getData();
			return products;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}

	// TODO remove the ID param to create unique products
	public Product createProduct() {
		// Add required fields to set
		Map<String,Object> productParams = new HashMap();
		productParams.put("name", "prod_UniqueCustomName"); // REQUIRED
		productParams.put("description", "The product’s description, meant to be displayable to the customer. Use this field to optionally store a long form explanation of the product being sold for your own rendering purposes.");
		productParams.put("unit_label", 3);
		productParams.put("id", "prod_GMwoo3eMEWuU9g"); // to test if it throws error on using same id 
		try {
			Product product = Product.create(productParams, StripeUtil.getRequestOptions());
			return product;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 *  To get a specific product by its ID 
	 */
	public Product getProduct(String productId) {
		try {
			//Stripe.apiKey = "sk_test_w2LOkBVjefZL9MQP0n6tpqZn006X7Bp05C";
			logger.info("productId: {}", productId);
			Product product = Product.retrieve(productId, StripeUtil.getRequestOptions());
			//Product product = Product.retrieve(productId);
			return product;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}
}
