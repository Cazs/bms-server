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
import server.model.QuickJobItem;
import server.repositories.QuickJobItemRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class QuickJobResourceController extends APIController
{
    private PagedResourcesAssembler<QuickJobItem> pagedAssembler;
    @Autowired
    private QuickJobItemRepository quickjob_resourceRepository;

    @Autowired
    public QuickJobResourceController(PagedResourcesAssembler<QuickJobItem> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/quickjob/resources/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getQuickJobItem(@PathVariable("id") String id,
                                                                          @RequestHeader String session_id,
                                                                          Pageable pageRequest,
                                                                          PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new QuickJobItem(id), "_id", session_id, "quickjob_resources", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/quickjobs/resources")
    public ResponseEntity<Page<? extends ApplicationObject>> getQuickJobItems(@RequestHeader String session_id,
                                                                           Pageable pageRequest,
                                                                           PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new QuickJobItem(), session_id, "quickjob_resources", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/quickjob/resource")
    public ResponseEntity<String> addQuickJobItem(@RequestBody QuickJobItem quickjob_item, @RequestHeader String session_id)
    {
        return putBusinessObject(quickjob_item, session_id, "quickjob_resources", "quickjobs_timestamp");
    }

    @PostMapping("/quickjob/resource")
    public ResponseEntity<String> patchQuickJobItem(@RequestBody QuickJobItem quickjob_item, @RequestHeader String session_id)
    {
        return patchBusinessObject(quickjob_item, session_id, "quickjob_resources", "quickjobs_timestamp");
    }

    @DeleteMapping(path="/quickjob/resource/{quickjob_resource_id}")
    public ResponseEntity<String> delete(@PathVariable String quickjob_resource_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new QuickJobItem(quickjob_resource_id), session_id, "quickjobs", "quickjobs_timestamp");
    }
}
