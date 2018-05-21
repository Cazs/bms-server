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
import server.repositories.PurchaseOrderRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class PurchaseOrderController extends APIController
{
    private PagedResourcesAssembler<PurchaseOrder> pagedAssembler;

    @Autowired
    private PurchaseOrderRepository purchase_orderRepository;

    @Autowired
    public PurchaseOrderController(PagedResourcesAssembler<PurchaseOrder> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/purchaseorder/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getPurchaseOrder(@PathVariable("id") String id,
                                                                              @RequestHeader String session_id,
                                                                              Pageable pageRequest,
                                                                              PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new PurchaseOrder(id), "_id", session_id, "purchase_orders", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/purchaseorders")
    public ResponseEntity<Page<? extends ApplicationObject>> getPurchaseOrders(Pageable pageRequest,
                                                                               @RequestHeader String session_id,
                                                                               PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new PurchaseOrder(), session_id, "purchase_orders", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/purchaseorder")
    public ResponseEntity<String> addPurchaseOrder(@RequestBody PurchaseOrder purchase_order,
                                                   @RequestHeader String session_id)
    {
        return putBusinessObject(purchase_order, session_id, "purchase_orders", "purchase_orders_timestamp");
    }

    @PostMapping("/purchaseorder")
    public ResponseEntity<String> patchPurchaseOrder(@RequestBody PurchaseOrder purchase_order,
                                                     @RequestHeader String session_id)
    {
        return patchBusinessObject(purchase_order, session_id, "purchase_orders", "purchase_orders_timestamp");
    }

//    @PostMapping(value = "/purchaseorder/mailto")
//    public ResponseEntity<String> emailPurchaseOrder(@RequestHeader String _id, @RequestHeader String session_id,
//                                                     @RequestHeader String message, @RequestHeader String subject,
//                                                     @RequestHeader String destination, @RequestBody Metafile metafile)
//    {
//        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling handling PurchaseOrder mailto request.");
//        return emailBusinessObject(_id, session_id, message, subject, destination, metafile, PurchaseOrder.class);
//    }

    @PostMapping("/purchaseorder/approval_request")//, consumes = "text/plain"//value =//, produces = "application/pdf"
    public ResponseEntity<String> requestPurchaseOrderApproval(@RequestHeader String purchaseorder_id,
                                                               @RequestHeader String session_id, @RequestHeader String message,
                                                               @RequestHeader String subject, @RequestBody Metafile metafile)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling PurchaseOrder approval request.");
        return requestBusinessObjectApproval(purchaseorder_id, session_id, message, subject, metafile, new PurchaseOrder().apiEndpoint(), PurchaseOrder.class);
    }

    @GetMapping("/purchaseorder/approve/{purchaseorder_id}/{vericode}")
    public ResponseEntity<String> approvePurchaseOrder(@PathVariable("purchaseorder_id") String purchaseorder_id,
                                                       @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling PurchaseOrder "+purchaseorder_id+" approval request by Vericode.");
        return approveBusinessObjectByVericode(purchaseorder_id, vericode, "purchase_orders", "purchase_orders_timestamp", PurchaseOrder.class);
    }

    @DeleteMapping(path="/purchaseorder/{purchaseorder_id}")
    public ResponseEntity<String> delete(@PathVariable String purchaseorder_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new PurchaseOrder(purchaseorder_id), session_id, "purchase_orders", "purchase_orders_timestamp");
    }
}
