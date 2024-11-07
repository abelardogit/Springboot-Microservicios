package com.paymentchain.billing.controller.helper;

import com.paymentchain.billing.entities.Invoice;
import com.paymentchain.billing.repository.BillingRepository;

import java.util.Optional;

public class BillingRestControllerHelper {

    public static Invoice getById(BillingRepository billingRepository, long id)
    {
        Optional<Invoice> optionalBilling = billingRepository.findById(id);
        return optionalBilling.orElse(null);
    }

    public static Invoice update(Invoice fromBD, Invoice fromUser)
    {
        Invoice updatedBilling = new Invoice();
        updatedBilling.setId(fromBD.getId());
        Invoice from;
        if (null != fromUser.getNumber()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedBilling.setNumber(from.getNumber());

        if (null != fromUser.getDetail()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedBilling.setDetail(from.getDetail());

        return updatedBilling;
    }

}
