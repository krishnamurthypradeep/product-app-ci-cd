package com.myapp.spring.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.spring.model.Product;
import com.myapp.spring.repository.ProductRepository;


// This is a Class Which exposes Rest API's
@RestController
@RequestMapping("/api/v1/products")
public class ProductAPI {
	
	// Dependency Injection
	@Autowired
	private ProductRepository repository;
	
	// http://localhost:8080/api/v1/products
	@GetMapping
	public ResponseEntity<List<Product>> findAll(){
		
	return new ResponseEntity<List<Product>>(repository.findAll(), HttpStatus.OK);
	}
	
	// http://localhost:8080/api/v1/products/45000
		@GetMapping("/findByPrice/{price}")
		public ResponseEntity<List<Product>> findProductsByPrice
		(@PathVariable("price") Double price){
			
		return new ResponseEntity<List<Product>>
		(repository.findByPriceGreaterThanEqual(price).get(), HttpStatus.OK);
		}
		// http://localhost:8080/api/v1/products/findByPriceOrName?price=
		@GetMapping("/findByPriceOrName")
		public ResponseEntity<List<Product>> findProductsByPriceOrName
		(@RequestParam("price") Optional<Double> price,
				@RequestParam("productName") Optional<String> productName){
			return new ResponseEntity<List<Product>>
		(repository.findByProductNameOrPrice(productName.orElse(""), price.orElse(0.0)).get(), HttpStatus.OK);
		}
	
	// http://localhost:8080/api/v1/products
	@PostMapping
	public ResponseEntity<Product> saveNewProduct(@RequestBody Product product){
		
	return new ResponseEntity<Product>(repository.save(product), HttpStatus.CREATED);
	}
	
	@PostMapping("/bulk")
	public ResponseEntity<List<Product>> bulkProductsInsert
	(@RequestBody List<Product> products){
		
	return new ResponseEntity<List<Product>>(repository.saveAll(products), 
			HttpStatus.CREATED);
	}
	
	
	// http://localhost:8080/api/v1/products/1
	@GetMapping("/{id}")
	public ResponseEntity<Product> findById(@PathVariable("id") Integer id){
		
return new ResponseEntity<Product>(repository.findById(id).get(), HttpStatus.OK);
	}

	
	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable("id") Integer id,
			@RequestBody Product product) throws ResourceNotFoundException{
		Product existingProduct = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException
	    		   ("Product not found for this id :: " + id));
		
		BeanUtils.copyProperties(product, existingProduct);
		
	return new ResponseEntity<Product>(repository.save(existingProduct), 
			HttpStatus.CREATED);
	}
	
	
	@DeleteMapping("/{id}")
	public Map<String, Boolean> delete(@PathVariable(value = "id") Integer id)
	  throws ResourceNotFoundException {
	    Product product = repository.findById(id)
	       .orElseThrow(() -> new ResourceNotFoundException
	    		   ("Product not found for this id :: " + id));

	    repository.delete(product);
	     Map<String, Boolean> response = new HashMap<>();
	     response.put("deleted", Boolean.TRUE);
	     return response;
	}

	
	

}
