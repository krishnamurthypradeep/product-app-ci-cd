package com.myapp.spring.web.api;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.spring.model.Product;
import com.myapp.spring.repository.ProductRepository;

@SpringBootTest

@AutoConfigureMockMvc(addFilters = false)
public class ProductAPITest {
	
	@MockBean
	private ProductRepository repository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@DisplayName("Test Product by Id - GET /api/v1/products/")
	public void testGetProductsById() throws Exception {
		
		// Prepare Mock Product
		Product product = new Product("Oneplus", "OnePlus9Pro", 70000.00, 4.5);
		product.setProductId(1);
		
		// Prepare Mock Service Method
		
		doReturn(Optional.of(product)).when(repository).findById(product.getProductId());
		
		// Perform GET Request
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}",1))
		// Validate Status should be 200 OK and JSON response received
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate Response Body
		
		.andExpect(jsonPath("$.productId", is(1)))
		.andExpect(jsonPath("$.productName", is("Oneplus")))
		.andExpect(jsonPath("$.description", is("OnePlus9Pro")))
		.andExpect(jsonPath("$.price", is(70000.00)))
		.andExpect(jsonPath("$.starRating", is(4.5)));
		
		
	}
	
	@Test
	@DisplayName("Test All Products /api/v1/products/")
	public void testGetAllProducts() throws Exception {
		
		// Prepare Mock Product
		Product product1 = new Product("Oneplus", "OnePlus9Pro", 70000.00, 4.5);
		product1.setProductId(35);
		
		Product product2 = new Product("Oneplus", "OnePlus8Pro", 60000.00, 4.5);
		product2.setProductId(36);
		
		List<Product> products = new ArrayList<>();
		products.add(product1);
		products.add(product2);
		
		// Prepare Mock Service Method
		
		doReturn(products).when(repository).findAll();
		
		// Perform GET Request
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products"))
		// Validate Status should be 200 OK and JSON response received
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate Response Body
		
		.andExpect(jsonPath("$[0].productId", is(35)))
		.andExpect(jsonPath("$[0].productName", is("Oneplus")))
		.andExpect(jsonPath("$[0].description", is("OnePlus9Pro")))
		.andExpect(jsonPath("$[0].price", is(70000.00)))
		.andExpect(jsonPath("$[0].starRating", is(4.5)))
		
		.andExpect(jsonPath("$[1].productId", is(36)))
		.andExpect(jsonPath("$[1].productName", is("Oneplus")))
		.andExpect(jsonPath("$[1].description", is("OnePlus8Pro")))
		.andExpect(jsonPath("$[1].price", is(60000.00)))
		.andExpect(jsonPath("$[1].starRating", is(4.5)));
		
		
		
		
	}
	
	@Test
	@DisplayName("Test All Products By Price /api/v1/products/{price}")
	public void testGetAllProductsByPrice() throws Exception {
		
		// Prepare Mock Product
		Product product1 = new Product("Oneplus", "OnePlus9Pro", 70000.00, 4.5);
		product1.setProductId(35);
		
		Product product2 = new Product("Oneplus", "OnePlus8Pro", 60000.00, 4.5);
		product2.setProductId(36);
		
		Product product3 = new Product("Iphone", "Iphone12", 80000.00, 4.5);
		product3.setProductId(37);
		
		List<Product> products = new ArrayList<>();
		products.add(product1);
		products.add(product2);
		products.add(product3);
		
		// Prepare Mock Service Method
		double price =50000.00;
		
		doReturn(Optional.of(products)).when(repository)
		.findByPriceGreaterThanEqual(price);
		
		// Perform GET Request
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/findByPrice/{price}",price))
		// Validate Status should be 200 OK and JSON response received
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate Response Body
		
		.andExpect(jsonPath("$[0].productId", is(35)))
		.andExpect(jsonPath("$[0].productName", is("Oneplus")))
		.andExpect(jsonPath("$[0].description", is("OnePlus9Pro")))
		.andExpect(jsonPath("$[0].price", is(70000.00)))
		.andExpect(jsonPath("$[0].starRating", is(4.5)))
		
		.andExpect(jsonPath("$[1].productId", is(36)))
		.andExpect(jsonPath("$[1].productName", is("Oneplus")))
		.andExpect(jsonPath("$[1].description", is("OnePlus8Pro")))
		.andExpect(jsonPath("$[1].price", is(60000.00)))
		.andExpect(jsonPath("$[1].starRating", is(4.5)))
		
		.andExpect(jsonPath("$[2].productId", is(37)))
		.andExpect(jsonPath("$[2].productName", is("Iphone")))
		.andExpect(jsonPath("$[2].description", is("Iphone12")))
		.andExpect(jsonPath("$[2].price", is(80000.00)))
		.andExpect(jsonPath("$[2].starRating", is(4.5)));
		
		
	}
	
	@Test
	@DisplayName("Test All Products By Price /api/v1/products?name=&price")
	public void testGetAllProductsByNameOrPrice() throws Exception {
		
		// Prepare Mock Product
		Product product1 = new Product("Oneplus", "OnePlus9Pro", 70000.00, 4.5);
		product1.setProductId(35);
		
		Product product2 = new Product("Oneplus", "OnePlus8Pro", 60000.00, 4.5);
		product2.setProductId(36);
		
		
		
		List<Product> products = new ArrayList<>();
		products.add(product1);
		products.add(product2);
		
		
		// Prepare Mock Service Method
		Double price =50000.00;
		String productName="Oneplus";
		
		doReturn(Optional.of(products)).when(repository)
		.findByProductNameOrPrice(productName, price);
		
		
		// Perform GET Request
		
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/v1/products/findByPriceOrName")
				.queryParam("productName",productName)
				.queryParam("price", price.toString()))
		// Validate Status should be 200 OK and JSON response received
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate Response Body
		
		.andExpect(jsonPath("$[0].productId", is(35)))
		.andExpect(jsonPath("$[0].productName", is("Oneplus")))
		.andExpect(jsonPath("$[0].description", is("OnePlus9Pro")))
		.andExpect(jsonPath("$[0].price", is(70000.00)))
		.andExpect(jsonPath("$[0].starRating", is(4.5)))
		
		.andExpect(jsonPath("$[1].productId", is(36)))
		.andExpect(jsonPath("$[1].productName", is("Oneplus")))
		.andExpect(jsonPath("$[1].description", is("OnePlus8Pro")))
		.andExpect(jsonPath("$[1].price", is(60000.00)))
		.andExpect(jsonPath("$[1].starRating", is(4.5)));
		
		
		
		
	}
	
	
	
	
	@Test
	@DisplayName("Test Add New Product")
	public void testAddNewProduct() throws Exception {
		
		// Prepare Mock Product
		Product newproduct = new Product("Oneplus", "OnePlus9Pro", 70000.00, 4.5);
		
		Product mockproduct = new Product("Oneplus", "OnePlus9Pro", 70000.00, 4.5);
		mockproduct.setProductId(50);
		// Prepare Mock Service Method
		
		doReturn(mockproduct).when(repository).save(ArgumentMatchers.any());
		
		// Perform GET Request
		
		mockMvc.perform(post("/api/v1/products")
		// Validate Status should be 200 OK and JSON response received
		
		.contentType(MediaType.APPLICATION_JSON_VALUE)
		.content(new ObjectMapper().writeValueAsString(newproduct)))
		
		
		// Validate Response Body
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(jsonPath("$.productId", is(50)))
		.andExpect(jsonPath("$.productName", is("Oneplus")))
		.andExpect(jsonPath("$.description", is("OnePlus9Pro")))
		.andExpect(jsonPath("$.price", is(70000.00)))
		.andExpect(jsonPath("$.starRating", is(4.5)));
		
		
	}
	@Test
	@DisplayName("Test Update Existing Product")
	public void testUpdateExistingProduct() throws Exception {
		
		// Prepare Mock Product
		
		Product mockproduct = new Product("Oneplus", "OnePlus9Pro", 70000.00, 4.5);
		
		Product productToBeUpdated = new Product("Oneplus", "OnePlus10Pro", 70000.00, 4.5);
		productToBeUpdated.setProductId(50);
		
		
		mockproduct.setProductId(50);
		// Prepare Mock Service Method
		
		doReturn(Optional.of(mockproduct)).when(repository).findById(50);
		
		doReturn(mockproduct).when(repository).save(ArgumentMatchers.any());
		
		// Perform GET Request
		
		mockMvc.perform(put("/api/v1/products/{id}",50)
		// Validate Status should be 200 OK and JSON response received
		
		.contentType(MediaType.APPLICATION_JSON_VALUE)
		.content(new ObjectMapper().writeValueAsString(productToBeUpdated)))
		
		
		// Validate Response Body
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(jsonPath("$.productId", is(50)))
		.andExpect(jsonPath("$.productName", is("Oneplus")))
		.andExpect(jsonPath("$.description", is("OnePlus10Pro")))
		.andExpect(jsonPath("$.price", is(70000.00)))
		.andExpect(jsonPath("$.starRating", is(4.5)));
		
		
	}
	

}
