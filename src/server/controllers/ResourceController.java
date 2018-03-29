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
import server.model.Resource;
import server.repositories.ResourceRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */
@RepositoryRestController
public class ResourceController extends APIController
{
    private PagedResourcesAssembler<Resource> pagedAssembler;
    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    public ResourceController(PagedResourcesAssembler<Resource> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/resource/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getResource(@PathVariable("id") String id,
                                                                         @RequestHeader String session_id,
                                                                         Pageable pageRequest,
                                                                         PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Resource(id), "_id", session_id, "resources", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/resources")
    public ResponseEntity<Page<? extends ApplicationObject>> getResources(@RequestHeader String session_id,
                                                                          Pageable pageRequest,
                                                                          PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Resource(), session_id, "resources", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/resource")
    public ResponseEntity<String> addResource(@RequestBody Resource resource, @RequestHeader String session_id)
    {
        return putBusinessObject(resource, session_id, "resources", "resources_timestamp");
    }

    @PostMapping("/resource")
    public ResponseEntity<String> patchResource(@RequestBody Resource resource, @RequestHeader String session_id)
    {
        return patchBusinessObject(resource, session_id, "resources", "resources_timestamp");
    }

    @DeleteMapping(path="/resource/{resource_id}")
    public ResponseEntity<String> delete(@PathVariable String resource_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Resource(resource_id), session_id, "resources", "resources_timestamp");
    }
}
