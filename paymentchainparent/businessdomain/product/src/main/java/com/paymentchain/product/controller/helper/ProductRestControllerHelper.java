package com.paymentchain.product.controller.helper;

import com.paymentchain.product.entities.Product;
import com.paymentchain.product.repository.ProductRepository;

import java.util.Optional;

public class ProductRestControllerHelper {

    public static Product getById(ProductRepository productRepository, long id)
    {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    public static Product update(Product fromBD, Product fromUser)
    {
        Product updatedProduct = new Product();
        updatedProduct.setId(fromBD.getId());
        Product from;
        if (null != fromUser.getCode()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedProduct.setCode(from.getCode());

        if (null != fromUser.getCode()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedProduct.setCode(from.getCode());

        if (null != fromUser.getName()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedProduct.setName(from.getName());

        return updatedProduct;
    }

}
