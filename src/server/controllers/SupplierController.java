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
import server.model.Supplier;
import server.repositories.SupplierRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */
@RepositoryRestController
@RequestMapping
public class SupplierController extends APIController
{
    private PagedResourcesAssembler<Supplier> pagedAssembler;
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    public SupplierController(PagedResourcesAssembler<Supplier> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/supplier/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getSupplier(@PathVariable("id") String id,
                                                                         @RequestHeader String session_id,
                                                                         Pageable pageRequest,
                                                                         PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Supplier(id), "_id", session_id, "suppliers", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping(path="/suppliers")
    public ResponseEntity<Page<? extends ApplicationObject>> getSuppliers(@RequestHeader String session_id,Pageable pageRequest,
                                                                          PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Supplier(), session_id, "suppliers", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping(path="/supplier")
    public ResponseEntity<String> addSupplier(@RequestBody Supplier supplier, @RequestHeader String session_id)
    {
        return putBusinessObject(supplier, session_id, "suppliers", "suppliers_timestamp");
    }

    @PostMapping(path="/supplier")
    public ResponseEntity<String> patchSupplier(@RequestBody Supplier supplier, @RequestHeader String session_id)
    {
        return patchBusinessObject(supplier, session_id, "suppliers", "suppliers_timestamp");
    }

    @DeleteMapping(path="/supplier/{supplier_id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable String supplier_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Supplier(supplier_id), session_id, "suppliers", "suppliers_timestamp");
    }
}
