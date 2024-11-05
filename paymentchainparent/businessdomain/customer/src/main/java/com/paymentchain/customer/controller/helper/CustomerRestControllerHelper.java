package com.paymentchain.customer.controller.helper;

import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.http.product.WebClientProductClient;
import com.paymentchain.customer.http.transaction.WebClientTransactionClient;
import com.paymentchain.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
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

    public static void updateProducts(Customer aCustomer) {
        List<CustomerProduct> products = aCustomer.getProducts();
        products.forEach(p -> {
            long productId = p.getProductId();
            String productName = CustomerRestControllerHelper.getProductName(productId);
            p.setProductName(productName);
        });


    }

    public static void updateTransactions(Customer aCustomer) {
        String anIBAN = aCustomer.getIban();
        List<?> transactions = CustomerRestControllerHelper.getTransactions(anIBAN);
        aCustomer.setTransactions(transactions);
    }

    private static List<?> getTransactions(String iban) {
        WebClient webClient = WebClient.builder().build();
        WebClientTransactionClient webClientTransaction = new WebClientTransactionClient(webClient);
        return webClientTransaction.getTransactionByCustomerIban(iban);
    }

    public static void updateAdditionalInfo(Customer aCustomer) {
        CustomerRestControllerHelper.updateProducts(aCustomer);
        //CustomerRestControllerHelper.updateTransactions(aCustomer);
    }
}
