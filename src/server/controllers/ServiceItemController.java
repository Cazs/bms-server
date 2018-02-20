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
import server.model.ServiceItem;
import server.repositories.ServiceItemRepository;

import java.util.List;

@RepositoryRestController
@RequestMapping("/services/items")
public class ServiceItemController
{
    private PagedResourcesAssembler<ServiceItem> pagedAssembler;
    @Autowired
    private ServiceItemRepository service_itemRepository;

    @Autowired
    public ServiceItemController(PagedResourcesAssembler<ServiceItem> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<ServiceItem>> getServiceItem(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling ServiceItem GET request id: "+ id);
        List<ServiceItem> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("service_id").is(id)), ServiceItem.class, "service_items");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ServiceItem>> getServiceItems(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling ServiceItem GET request {all}");
        List<ServiceItem> contents =  IO.getInstance().mongoOperations().findAll(ServiceItem.class, "service_items");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> addServiceItem(@RequestBody ServiceItem service_item)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling ServiceItem creation request.");
        //HttpHeaders headers = new HttpHeaders();
        return APIController.putBusinessObject(service_item, "service_items", "services_timestamp");
    }

    @PostMapping
    public ResponseEntity<String> patchResource(@RequestBody ServiceItem service_item)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling ServiceItem update request.");
        return APIController.patchBusinessObject(service_item, "service_items", "services_timestamp");
    }
}
