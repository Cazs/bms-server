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
import server.model.PurchaseOrder;
import server.model.BusinessObject;
import server.repositories.PurchaseOrderRepository;

import java.util.LinkedList;
import java.util.List;

@RepositoryRestController
@RequestMapping("/purchaseorders")
public class PurchaseOrderController
{
        private PagedResourcesAssembler<PurchaseOrder> pagedAssembler;
        @Autowired
        private PurchaseOrderRepository purchase_orderRepository;

        @Autowired
        public PurchaseOrderController(PagedResourcesAssembler<PurchaseOrder> pagedAssembler)
        {
            this.pagedAssembler = pagedAssembler;
        }

        @GetMapping(path="/{id}", produces = "application/hal+json")
        public ResponseEntity<Page<PurchaseOrder>> getPurchaseOrder(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling PurchaseOrder GET request id: "+ id);
            List<PurchaseOrder> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(id)), PurchaseOrder.class, "purchase_orders");
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }

        @GetMapping
        public ResponseEntity<Page<PurchaseOrder>> getPurchaseOrders(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling PurchaseOrder GET request {all}");
            List<PurchaseOrder> contents =  IO.getInstance().mongoOperations().findAll(PurchaseOrder.class, "purchase_orders");
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }

        @PostMapping
        @PutMapping
        public ResponseEntity<Page<PurchaseOrder>> addPurchaseOrder(@RequestBody PurchaseOrder purchase_order, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling PurchaseOrder creation request: " + purchase_order.asJSON());
            List<BusinessObject> contents = new LinkedList<>();
            contents.add(purchase_order);
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }
}
