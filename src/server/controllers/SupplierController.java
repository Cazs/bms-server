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
import server.model.BusinessObject;
import server.model.Supplier;
import server.repositories.SupplierRepository;

import java.util.LinkedList;
import java.util.List;

@RepositoryRestController
@RequestMapping("/suppliers")
public class SupplierController
{
        private PagedResourcesAssembler<Supplier> pagedAssembler;
        @Autowired
        private SupplierRepository supplierRepository;

        @Autowired
        public SupplierController(PagedResourcesAssembler<Supplier> pagedAssembler)
        {
            this.pagedAssembler = pagedAssembler;
        }

        @GetMapping(path="/{id}", produces = "application/hal+json")
        public ResponseEntity<Page<Supplier>> getSupplier(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling Supplier GET request id: "+ id);
            List<Supplier> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(id)), Supplier.class, "suppliers");
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }

        @GetMapping
        public ResponseEntity<Page<Supplier>> getSuppliers(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling Supplier GET request {all}");
            List<Supplier> contents =  IO.getInstance().mongoOperations().findAll(Supplier.class, "suppliers");
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }

        @PostMapping
        @PutMapping
        public ResponseEntity<Page<Supplier>> addSupplier(@RequestBody Supplier supplier, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling Supplier creation request.");
            List<BusinessObject> contents = new LinkedList<>();
            contents.add(supplier);
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }
}