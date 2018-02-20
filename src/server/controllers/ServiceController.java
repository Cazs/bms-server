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
import server.model.Service;
import server.repositories.ServiceRepository;

import java.util.List;

@RepositoryRestController
@RequestMapping("/services")
public class ServiceController
{
    private PagedResourcesAssembler<Service> pagedAssembler;
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    public ServiceController(PagedResourcesAssembler<Service> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<Service>> getService(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Service GET request id: "+ id);
        List<Service> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(id)), Service.class, "services");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Service>> getServices(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Service GET request {all}");
        List<Service> contents =  IO.getInstance().mongoOperations().findAll(Service.class, "services");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> addService(@RequestBody Service service)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Service creation request.");
        //HttpHeaders headers = new HttpHeaders();
        return APIController.putBusinessObject(service, "services", "services_timestamp");
    }

    @PostMapping
    public ResponseEntity<String> patchService(@RequestBody Service service)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Service update request.");
        return APIController.patchBusinessObject(service, "services", "services_timestamp");
    }
}
