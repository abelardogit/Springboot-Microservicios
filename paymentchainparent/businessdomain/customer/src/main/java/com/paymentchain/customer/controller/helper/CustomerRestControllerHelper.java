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
        Customer from;
        if (null != fromUser.getCode()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedCustomer.setCode(from.getCode());

        if (null != fromUser.getPhone()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedCustomer.setPhone(from.getPhone());

        if (null != fromUser.getName()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedCustomer.setName(from.getName());

        return updatedCustomer;
    }

}
