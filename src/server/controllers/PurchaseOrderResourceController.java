package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.ApplicationObject;
import server.model.PurchaseOrderResource;
import server.repositories.PurchaseOrderResourceRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class PurchaseOrderResourceController extends APIController
{
    private PagedResourcesAssembler<PurchaseOrderResource> pagedAssembler;
    @Autowired
    private PurchaseOrderResourceRepository purchase_order_resourceRepository;

    @Autowired
    public PurchaseOrderResourceController(PagedResourcesAssembler<PurchaseOrderResource> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/purchaseorder/resources/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getPurchaseOrderResource(@PathVariable("id") String id,
                                                                                      @RequestHeader String session_id,
                                                                                      Pageable pageRequest,
                                                                                      PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new PurchaseOrderResource(id), "_id", session_id, "purchase_order_resources", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/purchaseorders/resources")
    public ResponseEntity<Page<? extends ApplicationObject>> getPurchaseOrderResources(Pageable pageRequest,
                                                                                       @RequestHeader String session_id,
                                                                                       PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new PurchaseOrderResource(), session_id, "purchase_order_resources", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/purchaseorder/resource")
    public ResponseEntity<String> addPurchaseOrderResource(@RequestBody PurchaseOrderResource purchase_order_resource,
                                                           @RequestHeader String session_id)
    {
        return putBusinessObject(purchase_order_resource, session_id, "purchase_order_resources", "purchase_orders_timestamp");
    }

    @PostMapping("/purchaseorder/resource")
    public ResponseEntity<String> patchPurchaseOrderResource(@RequestBody PurchaseOrderResource purchase_order_resource,
                                                             @RequestHeader String session_id)
    {
        return patchBusinessObject(purchase_order_resource, session_id, "purchase_order_resources", "purchase_orders_timestamp");
    }

    @DeleteMapping(path="/purchaseorder/resource/{purchaseorder_resource_id}")
    public ResponseEntity<String> delete(@PathVariable String purchaseorder_resource_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new PurchaseOrderResource(purchaseorder_resource_id), session_id, "purchase_order_resources", "purchase_orders_timestamp");
    }
}
