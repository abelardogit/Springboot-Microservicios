package com.paymentchain.product.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paymentchain.product.controller.helper.ProductRestControllerHelper;
import com.paymentchain.product.entities.Product;
import com.paymentchain.product.repository.ProductRepository;



@RestController
@RequestMapping("/product")
public class ProductRestController {

    private final ProductRepository productRepository;

    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping()
    public List<Product> list()
    {
        return this.productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id)
    {
        Product aProduct = ProductRestControllerHelper.getById(this.productRepository, id);
        if (null == aProduct) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(aProduct, HttpStatus.FOUND);

    }

    @GetMapping("/getName/{id}")
    public ResponseEntity<?> getNameById(@PathVariable("id") long id)
    {
        Product aProduct = ProductRestControllerHelper.getById(this.productRepository, id);
        if (null == aProduct) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String name = aProduct.getName();

        return new ResponseEntity<>(name, HttpStatus.FOUND);

    }

    @PutMapping()
    public ResponseEntity<?> put(@RequestBody Product product)
    {
        long productId = product.getId();
        Product aProductFromBD = ProductRestControllerHelper.getById(this.productRepository, productId);
        if (null == aProductFromBD) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Product updatedProduct = ProductRestControllerHelper.update(aProductFromBD, product);

        this.productRepository.save(updatedProduct);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Product aProduct)
    {
        Product savedProduct = this.productRepository.save(aProduct);
        return ResponseEntity.ok(savedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id)
    {
        Product aProductFromBD = ProductRestControllerHelper.getById(this.productRepository, id);
        if (null == aProductFromBD) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        this.productRepository.delete(aProductFromBD);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
