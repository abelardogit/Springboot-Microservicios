package com.paymentchain.customer.controller;

import com.paymentchain.customer.controller.helper.CustomerRestControllerHelper;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.repository.CustomerRepository;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/customer/v1")
public class CustomerRestController {

    private final CustomerRepository customerRepository;
    private final Environment env;

    public CustomerRestController(
            CustomerRepository customerRepository,
            Environment env
    ) {
        this.customerRepository = customerRepository;
        this.env = env;
    }

    /*
    @Value("${custom.activeProfileName}")
    private String profile;
    */

    @GetMapping("/check")
    public String check() {
        return "Hello, your current environment is " + this.env.getProperty("custom.activeProfileName");
    }

    @GetMapping()
    public ResponseEntity<List<Customer>> list() {
        List<Customer> customers = this.customerRepository.findAll();
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id)
    {
        Customer aCustomer = CustomerRestControllerHelper.getById(this.customerRepository, id);
        if (null == aCustomer) {
            return ResponseEntity.noContent().build();
        }

        CustomerRestControllerHelper.updateAdditionalInfo(aCustomer);

        return new ResponseEntity<>(aCustomer, HttpStatus.FOUND);
    }

    @GetMapping("/fullByCode/{code}")
    public ResponseEntity<?> getByCode(@PathVariable("code") String code)
    {
        Customer aCustomer = customerRepository.getByCode(code);

        if (null == aCustomer) {
            return ResponseEntity.noContent().build();
        }

        CustomerRestControllerHelper.updateAdditionalInfo(aCustomer);

        return new ResponseEntity<>(aCustomer, HttpStatus.FOUND);
    }

    @GetMapping("/fullByIBAN/{iban}")
    public ResponseEntity<?> getByIBAN(@PathVariable("iban") String iban)
    {
        Customer aCustomer = customerRepository.getByIBAN(iban);

        if (null == aCustomer) {
            return ResponseEntity.noContent().build();
        }

        CustomerRestControllerHelper.updateAdditionalInfo(aCustomer);

        return new ResponseEntity<>(aCustomer, HttpStatus.FOUND);
    }

    @PutMapping()
    public ResponseEntity<?> put(@RequestBody Customer customer)
    {
        if (null == customer) {
            return ResponseEntity.badRequest().build();
        }

        long customerId = customer.getId();
        Customer aCustomerFromBD = CustomerRestControllerHelper.getById(this.customerRepository, customerId);
        if (null == aCustomerFromBD) {
            return ResponseEntity.noContent().build();
        }

        Customer updatedCustomer = CustomerRestControllerHelper.update(aCustomerFromBD, customer);

        this.customerRepository.save(updatedCustomer);

        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Customer aCustomer) {
        if (null == aCustomer) {
            return ResponseEntity.badRequest().build();
        }
        try {
            aCustomer.getProducts().forEach(product -> product.setCustomer(aCustomer));
            Customer savedCustomer = this.customerRepository.save(aCustomer);
            return ResponseEntity.ok(savedCustomer);
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id)
    {
        Customer aCustomerFromBD = CustomerRestControllerHelper.getById(this.customerRepository, id);
        if (null == aCustomerFromBD) {
            return ResponseEntity.noContent().build();
        }
        this.customerRepository.delete(aCustomerFromBD);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
