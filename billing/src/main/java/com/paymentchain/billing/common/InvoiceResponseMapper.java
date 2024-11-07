package com.paymentchain.billing.common;

import com.paymentchain.billing.dto.InvoiceResponse;
import com.paymentchain.billing.entities.Invoice;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceResponseMapper {
    @Mapping(source="customerID", target="customer")
    Invoice invoiceResponseToInvoice(InvoiceResponse source);

    List<Invoice> invoiceResponseListToInvoiceList(List<InvoiceResponse> source);

    @InheritInverseConfiguration
    InvoiceResponse invoiceToInvoiceResponse(Invoice source);

    @InheritInverseConfiguration
    List<InvoiceResponse> invoiceListToInvoiceResponseList(List<Invoice> source);

}
