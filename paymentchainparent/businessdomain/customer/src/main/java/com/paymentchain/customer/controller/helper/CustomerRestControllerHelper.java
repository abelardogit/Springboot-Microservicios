package com.paymentchain.customer.controller.helper;

import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.http.WebClientProductClient;
import com.paymentchain.customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

public class CustomerRestControllerHelper {

    public static Customer getById(CustomerRepository customerRepository, long id)
    {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.orElse(null);
    }

    public static Customer update(Customer fromBD, Customer fromUser)
    {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(fromBD.getId());

        Customer from  = (null != fromUser.getCode())
                ? fromUser
                : fromBD
        ;
        updatedCustomer.setCode(from.getCode());

        from  = (null != fromUser.getPhone())
                ? fromUser
                : fromBD
        ;
        updatedCustomer.setPhone(from.getPhone());


        from  = (null != fromUser.getName())
                ? fromUser
                : fromBD
        ;
        updatedCustomer.setName(from.getName());

        return updatedCustomer;
    }

    public static String getProductName(long id) {
        WebClient webClient = WebClient.builder().build();
        WebClientProductClient webClientProductClient = new WebClientProductClient(webClient);
        return webClientProductClient.getProductName(id);
    }

    public static ResponseEntity<?> getResponseEntity(Customer aCustomer) {
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

}
