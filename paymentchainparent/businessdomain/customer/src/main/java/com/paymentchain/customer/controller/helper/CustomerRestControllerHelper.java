package com.paymentchain.customer.controller.helper;

import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.exceptions.BusinessRuleException;
import com.paymentchain.customer.http.product.WebClientProductClient;
import com.paymentchain.customer.http.transaction.WebClientTransactionClient;
import com.paymentchain.customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CustomerRestControllerHelper {

    public static Customer getById(CustomerRepository customerRepository, long id)
    {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.orElse(null);
    }

    public static String getProductName(long id) {
        WebClient webClient = WebClient.builder().build();
        WebClientProductClient webClientProductClient = new WebClientProductClient(webClient);
        return webClientProductClient.getProductName(id);
    }

    public static String getNotFoundProductName(long id) throws UnknownHostException {
        WebClient webClient = WebClient.builder().build();
        WebClientProductClient webClientProductClient = new WebClientProductClient(webClient);
        return webClientProductClient.getNotFoundProductName(id, true);
    }

    public static Customer post(CustomerRepository customerRepository, Customer aCustomer) throws BusinessRuleException, UnknownHostException {
        if (null == aCustomer) {
            Object[] params = {"1025", "Customer not provided", HttpStatus.PRECONDITION_FAILED};
            throw BusinessRuleException.fromCode(params);
        }

        if (null == aCustomer.getProducts()) {
            Object[] params = {"1026", "Customer products not provided", HttpStatus.PRECONDITION_FAILED};
            throw BusinessRuleException.fromCode(params);
        }
        Iterator<CustomerProduct> customerProductIterator = aCustomer.getProducts().iterator();
        boolean notFound = false; long idNotFound = -1;
        while(customerProductIterator.hasNext() && !notFound) {
            CustomerProduct customerProduct = customerProductIterator.next();
            String productName = getProductName(customerProduct.getId());
           // String productName = getNotFoundProductName(customerProduct.getId());
            notFound = (null == productName) || productName.isEmpty();
            idNotFound = customerProduct.getId();
        }
        if (notFound) {
            Object[] params = {"1027", "Some product name with product id " + idNotFound + " was not provided", HttpStatus.PRECONDITION_FAILED};
            throw BusinessRuleException.fromCode(params);
        }

        aCustomer.getProducts().forEach(product -> product.setCustomer(aCustomer));

        return customerRepository.save(aCustomer);
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

    public static void updateAdditionalInfo(Customer aCustomer) {
        CustomerRestControllerHelper.updateProducts(aCustomer);
        //CustomerRestControllerHelper.updateTransactions(aCustomer);
    }

    private static List<?> getTransactions(String iban) {
        WebClient webClient = WebClient.builder().build();
        WebClientTransactionClient webClientTransaction = new WebClientTransactionClient(webClient);
        return webClientTransaction.getTransactionByCustomerIban(iban);
    }

    private static void updateProducts(Customer aCustomer) {
        List<CustomerProduct> products = aCustomer.getProducts();
        products.forEach(p -> {
            long productId = p.getProductId();
//            String productName = CustomerRestControllerHelper.getProductName(productId);
            String productName = null;
            try {
                // Fake
                productName = CustomerRestControllerHelper.getNotFoundProductName(productId);
            } catch (UnknownHostException e) {
                Logger.getLogger(CustomerRestControllerHelper.class.getName()).log(Level.SEVERE, null, e);
            }
            p.setProductName(productName);
        });
    }

    private static void updateTransactions(Customer aCustomer) {
        String anIBAN = aCustomer.getIban();
        List<?> transactions = CustomerRestControllerHelper.getTransactions(anIBAN);
        aCustomer.setTransactions(transactions);
    }
}
