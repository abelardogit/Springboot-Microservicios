package com.paymentchain.billing.controller;

import com.paymentchain.billing.controller.helper.BillingRestControllerHelper;
import com.paymentchain.billing.entities.Billing;
import com.paymentchain.billing.repository.BillingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing")
public class BillingRestController {

    private final BillingRepository billingRepository;

    public BillingRestController(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    @GetMapping()
    public List<Billing> list()
    {
        return this.billingRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id)
    {
        Billing aBilling = BillingRestControllerHelper.getById(this.billingRepository, id);
        if (null == aBilling) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(aBilling, HttpStatus.FOUND);

    }

    @PutMapping()
    public ResponseEntity<?> put(@RequestBody Billing billing)
    {
        long billingId = billing.getId();
        Billing aBillingFromBD = BillingRestControllerHelper.getById(this.billingRepository, billingId);
        if (null == aBillingFromBD) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Billing updatedBilling = BillingRestControllerHelper.update(aBillingFromBD, billing);

        this.billingRepository.save(updatedBilling);

        return new ResponseEntity<>(updatedBilling, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Billing aBilling)
    {
        Billing savedBilling = this.billingRepository.save(aBilling);
        return ResponseEntity.ok(savedBilling);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id)
    {
        Billing aBillingFromBD = BillingRestControllerHelper.getById(this.billingRepository, id);
        if (null == aBillingFromBD) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        this.billingRepository.delete(aBillingFromBD);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
