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
import server.model.ResourceType;
import server.repositories.ResourceTypeRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class ResourceTypeController extends APIController
{
    private PagedResourcesAssembler<ResourceType> pagedAssembler;
    @Autowired
    private ResourceTypeRepository resource_typeRepository;

    @Autowired
    public ResourceTypeController(PagedResourcesAssembler<ResourceType> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/resource/types/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> get(@PathVariable("id") String id,
                                                                 @RequestHeader String session_id,
                                                                 Pageable pageRequest,
                                                                 PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new ResourceType(id), "_id", session_id, "resource_types", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/resources/types")
    public ResponseEntity<Page<? extends ApplicationObject>> getAll(@RequestHeader String session_id,
                                                                    Pageable pageRequest,
                                                                    PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new ResourceType(), session_id, "resource_types", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/resource/type")
    public ResponseEntity<String> put(@RequestBody ResourceType resource_type, @RequestHeader String session_id)
    {
        return putBusinessObject(resource_type, session_id, "resource_types", "resources_timestamp");
    }

    @PostMapping("/resource/type")
    public ResponseEntity<String> patch(@RequestBody ResourceType resource_type, @RequestHeader String session_id)
    {
        return patchBusinessObject(resource_type, session_id, "resource_types", "resources_timestamp");
    }

    @DeleteMapping(path="/resource/type/{resource_type_id}")
    public ResponseEntity<String> delete(@PathVariable String resource_type_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new ResourceType(resource_type_id), session_id, "resource_types", "resources_timestamp");
    }
}
