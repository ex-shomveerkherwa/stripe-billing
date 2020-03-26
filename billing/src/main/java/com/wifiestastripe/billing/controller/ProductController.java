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

import com.stripe.model.Product;
import com.wifiestastripe.billing.service.ProductService;

@RestController
@RequestMapping("/product-mgmt")
public class ProductController {

	private static Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/")
	public void healthcheck() {
		logger.info("Healthcheck of {}: at {}: ", this.getClass().getSimpleName(), System.currentTimeMillis());
	}
	
	@GetMapping("/products")
	public void getAllProducts() {
		List<Product> products = productService.getAllProducts();
		if(Objects.isNull(products)) {
			logger.error("Failed to get products from Stripe");
		} else {
			products.stream().forEach(product -> logger.info("id:{} name:{}", product.getId(), product.getName()));
		}
	}

	@GetMapping("/product/{productId}")
	public void getProduct(@PathVariable("productId") String productId) {
		Product product = productService.getProduct(productId);
		logger.info("Product details of: {} -> {}", productId, product);
	}
	
	@PostMapping("/product")
	public void createProduct() {
		Product product = productService.createProduct();
		if(Objects.isNull(product)) {
			logger.error("Failed to create Product in Stripe");
		} else {
			logger.info("Product: {}", product.toString());
		}
	}
}
