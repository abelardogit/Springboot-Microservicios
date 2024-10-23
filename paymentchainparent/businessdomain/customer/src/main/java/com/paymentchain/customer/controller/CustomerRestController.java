package com.paymentchain.customer.controller;

import com.paymentchain.customer.controller.helper.CustomerRestControllerHelper;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerRestController {

    private final CustomerRepository customerRepository;

    public CustomerRestController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping()
    public List<Customer> list() {
        return this.customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id)
    {
        Customer aCustomer = CustomerRestControllerHelper.getById(this.customerRepository, id);
        if (null == aCustomer) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<CustomerProduct> products = aCustomer.getProducts();
        products.forEach(p -> {
            long productId = p.getProductId();
            String productName = CustomerRestControllerHelper.getProductName(productId);
            p.setProductName(productName);
        });

        return new ResponseEntity<>(aCustomer, HttpStatus.FOUND);

    }

    @PutMapping()
    public ResponseEntity<?> put(@RequestBody Customer customer)
    {
        long customerId = customer.getId();
        Customer aCustomerFromBD = CustomerRestControllerHelper.getById(this.customerRepository, customerId);
        if (null == aCustomerFromBD) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Customer updatedCustomer = CustomerRestControllerHelper.update(aCustomerFromBD, customer);

        this.customerRepository.save(updatedCustomer);

        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Customer aCustomer) {
        aCustomer.getProducts().forEach(product -> product.setCustomer(aCustomer));
        Customer savedCustomer = this.customerRepository.save(aCustomer);
        return ResponseEntity.ok(savedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id)
    {
        Customer aCustomerFromBD = CustomerRestControllerHelper.getById(this.customerRepository, id);
        if (null == aCustomerFromBD) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        this.customerRepository.delete(aCustomerFromBD);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
