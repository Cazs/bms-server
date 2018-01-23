package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auxilary.IO;
import server.auxilary.RemoteComms;
import server.exceptions.InvalidBusinessObjectException;
import server.model.*;
import server.repositories.InvoiceRepository;

import java.rmi.Remote;
import java.util.LinkedList;
import java.util.List;

@RepositoryRestController
@RequestMapping("/invoices")
public class InvoiceController
{
    private PagedResourcesAssembler<Invoice> pagedAssembler;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceController(PagedResourcesAssembler<Invoice> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<Invoice>> getInvoice(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Invoice GET request id: "+ id);
        List<Invoice> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(id)), Invoice.class, "invoices");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Invoice>> getInvoices(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Invoice GET request {all}");
        List<Invoice> contents =  IO.getInstance().mongoOperations().findAll(Invoice.class, "invoices");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> addInvoice(@RequestBody Invoice invoice)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Invoice creation request");
        return APIController.putBusinessObject(invoice, "invoices", "invoices_timestamp");
    }

    @PostMapping
    public ResponseEntity<String> patchInvoice(@RequestBody Invoice invoice)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Invoice update request.");
        return APIController.patchBusinessObject(invoice, "invoices", "invoices_timestamp");
    }

    @PostMapping(value = "/mailto")//, consumes = "text/plain"//value =//, produces = "application/pdf"
    public ResponseEntity<String> emailInvoice(@RequestHeader String _id, @RequestHeader String session_id,
                                                     @RequestHeader String message, @RequestHeader String subject,
                                                     @RequestHeader String destination, @RequestBody FileMetadata fileMetadata)//, @RequestParam("file") MultipartFile file
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Invoice mailto request.");
        return APIController.emailBusinessObject(_id, session_id, message, subject, destination, fileMetadata, Invoice.class);
    }

    @PostMapping(value = "/approval_request")//, consumes = "text/plain"//value =//, produces = "application/pdf"
    public ResponseEntity<String> requestInvoiceApproval(@RequestHeader String invoice_id, @RequestHeader String session_id,
                                                     @RequestHeader String message, @RequestHeader String subject,
                                                     @RequestBody FileMetadata fileMetadata)//, @RequestParam("file") MultipartFile file
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Invoice approval request.");
        return APIController.requestBusinessObjectApproval(invoice_id, session_id, message, subject, fileMetadata, new Invoice().apiEndpoint(), Invoice.class);
    }

    @GetMapping("/approve/{invoice_id}/{vericode}")
    public ResponseEntity<String> approveInvoice(@PathVariable("invoice_id") String invoice_id, @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Invoice "+invoice_id+" approval request by Vericode.");
        return APIController.approveBusinessObjectByVericode(invoice_id, vericode, "invoices", "invoices_timestamp", Invoice.class);
    }
}
