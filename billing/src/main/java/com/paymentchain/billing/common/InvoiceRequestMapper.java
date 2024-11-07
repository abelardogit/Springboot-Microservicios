package com.paymentchain.billing.common;

import com.paymentchain.billing.dto.InvoiceRequest;
import com.paymentchain.billing.entities.Invoice;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceRequestMapper {
    @Mapping(source="customerID", target="customer")
    Invoice invoiceRequestToInvoice(InvoiceRequest source);

    List<Invoice> InvoiceRequestListToInvoiceList(List<InvoiceRequest> source);

    @InheritInverseConfiguration
    InvoiceRequest invoiceToInvoiceRequest(Invoice source);

    @InheritInverseConfiguration
    List<InvoiceRequest> invoiceListToInvoiceRequestList(List<Invoice> source);

}
