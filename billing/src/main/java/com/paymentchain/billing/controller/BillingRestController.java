package com.paymentchain.billing.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paymentchain.billing.common.InvoiceRequestMapper;
import com.paymentchain.billing.common.InvoiceResponseMapper;
import com.paymentchain.billing.controller.helper.BillingRestControllerHelper;
import com.paymentchain.billing.dto.InvoiceRequest;
import com.paymentchain.billing.dto.InvoiceResponse;
import com.paymentchain.billing.entities.Invoice;
import com.paymentchain.billing.repository.BillingRepository;

@Tag(name="Billing API", description = "This APi serves all functionality for management Invoices")
@RestController
@RequestMapping("/billing")
public class BillingRestController {

    private final BillingRepository billingRepository;
    private final InvoiceResponseMapper invoiceResponseMapperImpl;
    private final InvoiceRequestMapper invoiceRequestMapperImpl;

    public BillingRestController(
            BillingRepository billingRepository,
            InvoiceResponseMapper invoiceRequestMapper,
            InvoiceRequestMapper invoiceResponseMapper
    ) {
        this.billingRepository = billingRepository;
        this.invoiceResponseMapperImpl = invoiceRequestMapper;
        this.invoiceRequestMapperImpl = invoiceResponseMapper;
    }

    @Operation(description="Deletes an invoice")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Éxito"),
            @ApiResponse(responseCode = "204", description = "No se ha encontrado la factura con el id proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")}
    )
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id)
    {
        Invoice aBillingFromBD = BillingRestControllerHelper.getById(this.billingRepository, Long.parseLong(id));
        if (null == aBillingFromBD) {
            return ResponseEntity.noContent().build();
        }
        this.billingRepository.delete(aBillingFromBD);

        return ResponseEntity.ok().build();
    }

    @Operation(description="Return all invoices bundled into a response")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Éxito"),
            @ApiResponse(responseCode = "204", description = "No hay datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")}
    )
    @GetMapping()
    public ResponseEntity<List<InvoiceResponse>> list()
    {
        List<Invoice> invoices =  this.billingRepository.findAll();
        if (invoices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(invoiceResponseMapperImpl.invoiceListToInvoiceResponseList(invoices));
    }

    @Operation(description="Return an invoice by id provided")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Éxito"),
            @ApiResponse(responseCode = "204", description = "No se ha encontrado la factura con el id proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")}
    )
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getById(@PathVariable("id") long id)
    {
        Invoice aBilling = BillingRestControllerHelper.getById(this.billingRepository, id);
        if (null == aBilling) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        InvoiceResponse invoiceResponse = invoiceResponseMapperImpl.invoiceToInvoiceResponse(aBilling);
        return new ResponseEntity<>(invoiceResponse, HttpStatus.FOUND);

    }

    @Operation(description="Update an invoice by id provided")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Éxito"),
            @ApiResponse(responseCode = "204", description = "No se ha encontrado la factura con el id proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")}
    )
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> put(@PathVariable("id") String id, @RequestBody InvoiceRequest billingRequest)
    {
        Invoice aBillingFromBD = BillingRestControllerHelper.getById(this.billingRepository, Long.parseLong(id));
        if (null == aBillingFromBD) {
            return ResponseEntity.noContent().build();
        }

        Invoice fromUser = invoiceRequestMapperImpl.invoiceRequestToInvoice(billingRequest);

        Invoice updatedBilling = BillingRestControllerHelper.update(aBillingFromBD, fromUser);

        this.billingRepository.save(updatedBilling);

        InvoiceResponse invoiceResponse = invoiceResponseMapperImpl.invoiceToInvoiceResponse(updatedBilling);

        return new ResponseEntity<>(invoiceResponse, HttpStatus.OK);
    }

    @Operation(description="Creates an invoice")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Éxito"),
            @ApiResponse(responseCode = "403", description = "Operación no permitida: ¿eres admin?"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")}
    )
    @PostMapping()
    public ResponseEntity<InvoiceResponse> post(@RequestBody InvoiceRequest aBillingRequest)
    {
        Invoice aBilling = invoiceRequestMapperImpl.invoiceRequestToInvoice(aBillingRequest);
        Invoice savedBilling = this.billingRepository.save(aBilling);

        InvoiceResponse aBillingResponse = invoiceResponseMapperImpl.invoiceToInvoiceResponse(savedBilling);
        return ResponseEntity.ok(aBillingResponse);
    }


}
