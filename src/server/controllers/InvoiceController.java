package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auxilary.IO;
import server.model.*;
import server.repositories.InvoiceRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class InvoiceController extends APIController
{
    private PagedResourcesAssembler<Invoice> pagedAssembler;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceController(PagedResourcesAssembler<Invoice> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/invoice/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getInvoice(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Invoice(id), "_id", session_id, "invoices", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/invoices")
    public ResponseEntity<Page<? extends ApplicationObject>> getInvoices(Pageable pageRequest, @RequestHeader String session_id, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Invoice(), session_id, "invoices", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/invoice")
    public ResponseEntity<String> addInvoice(@RequestBody Invoice invoice, @RequestHeader String session_id)
    {
        return putBusinessObject(invoice, session_id, "invoices", "invoices_timestamp");
    }

    @PostMapping("/invoice")
    public ResponseEntity<String> patchInvoice(@RequestBody Invoice invoice, @RequestHeader String session_id)
    {
        return patchBusinessObject(invoice, session_id, "invoices", "invoices_timestamp");
    }

//    @PostMapping(value = "/invoice/mailto")//, consumes = "text/plain"//value =//, produces = "application/pdf"
//    public ResponseEntity<String> emailInvoice(@RequestHeader String _id, @RequestHeader String session_id,
//                                                     @RequestHeader String message, @RequestHeader String subject,
//                                                     @RequestHeader String destination, @RequestBody Metafile metafile)//, @RequestParam("file") MultipartFile file
//    {
//        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Invoice mailto request.");
//        return emailBusinessObject(_id, session_id, message, subject, destination, metafile, Invoice.class);
//    }

    @PostMapping(value = "/invoice/approval_request")//, consumes = "text/plain"//value =//, produces = "application/pdf"
    public ResponseEntity<String> requestInvoiceApproval(@RequestHeader String invoice_id, @RequestHeader String session_id,
                                                     @RequestHeader String message, @RequestHeader String subject,
                                                     @RequestBody Metafile metafile)//, @RequestParam("file") MultipartFile file
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Invoice approval request.");
        return requestBusinessObjectApproval(invoice_id, session_id, message, subject, metafile, new Invoice().apiEndpoint(), Invoice.class);
    }

    @GetMapping("/invoice/approve/{invoice_id}/{vericode}")
    public ResponseEntity<String> approveInvoice(@PathVariable("invoice_id") String invoice_id, @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Invoice "+invoice_id+" approval request by Vericode.");
        return approveBusinessObjectByVericode(invoice_id, vericode, "invoices", "invoices_timestamp", Invoice.class);
    }

    @DeleteMapping(path="/invoice/{invoice_id}")
    public ResponseEntity<String> delete(@PathVariable String invoice_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Invoice(invoice_id), session_id, "invoices", "invoices_timestamp");
    }
}
