package com.paymentchain.billing.controller.helper;

import com.paymentchain.billing.entities.Billing;
import com.paymentchain.billing.repository.BillingRepository;

import java.util.Optional;

public class BillingRestControllerHelper {

    public static Billing getById(BillingRepository billingRepository, long id)
    {
        Optional<Billing> optionalBilling = billingRepository.findById(id);
        return optionalBilling.orElse(null);
    }

    public static Billing update(Billing fromBD, Billing fromUser)
    {
        Billing updatedBilling = new Billing();
        updatedBilling.setId(fromBD.getId());
        Billing from;
        if (null != fromUser.getCode()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedBilling.setCode(from.getCode());

        if (null != fromUser.getPhone()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedBilling.setPhone(from.getPhone());

        if (null != fromUser.getName()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedBilling.setName(from.getName());

        return updatedBilling;
    }

}
