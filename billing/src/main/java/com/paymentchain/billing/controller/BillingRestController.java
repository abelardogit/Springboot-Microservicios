package com.paymentchain.billing.controller;

import com.paymentchain.billing.controller.helper.BillingRestControllerHelper;
import com.paymentchain.billing.dto.InvoiceRequest;
import com.paymentchain.billing.dto.InvoiceResponse;
import com.paymentchain.billing.entities.Invoice;
import com.paymentchain.billing.repository.BillingRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(description="Return all invoices bundled into a response")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Éxito"),
            @ApiResponse(responseCode = "204", description = "No hay datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")})
    @GetMapping()
    public List<InvoiceResponse> list()
    {
        List<Invoice> invoices =  this.billingRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id)
    {
        Invoice aBilling = BillingRestControllerHelper.getById(this.billingRepository, id);
        if (null == aBilling) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(aBilling, HttpStatus.FOUND);

    }

    @PutMapping()
    public ResponseEntity<?> put(@RequestBody InvoiceRequest billing)
    {
        long billingId = billing.getId();
        Invoice aBillingFromBD = BillingRestControllerHelper.getById(this.billingRepository, billingId);
        if (null == aBillingFromBD) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Invoice updatedBilling = BillingRestControllerHelper.update(aBillingFromBD, billing);

        this.billingRepository.save(updatedBilling);

        return new ResponseEntity<>(updatedBilling, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody InvoiceRequest aBilling)
    {
        Invoice savedBilling = this.billingRepository.save(aBilling);
        return ResponseEntity.ok(savedBilling);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id)
    {
        Invoice aBillingFromBD = BillingRestControllerHelper.getById(this.billingRepository, id);
        if (null == aBillingFromBD) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        this.billingRepository.delete(aBillingFromBD);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
