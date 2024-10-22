package com.paymentchain.customer.controller.helper;

import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.repository.CustomerRepository;

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

}
