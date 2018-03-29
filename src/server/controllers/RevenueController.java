package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.BusinessObject;
import server.model.Revenue;
import server.repositories.RevenueRepository;

/**
 * Created by ghost on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class RevenueController extends APIController
{
    private PagedResourcesAssembler<Revenue> pagedAssembler;
    @Autowired
    private RevenueRepository revenueRepository;

    @Autowired
    public RevenueController(PagedResourcesAssembler<Revenue> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/revenue/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends BusinessObject>> getRevenue(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Revenue(id), "_id", session_id, "revenues", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/revenues")
    public ResponseEntity<Page<? extends BusinessObject>> getRevenues(@RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Revenue(), session_id, "revenues", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/revenue")
    public ResponseEntity<String> addRevenue(@RequestBody Revenue revenue, @RequestHeader String session_id)
    {
        return putBusinessObject(revenue, session_id, "revenues", "revenues_timestamp");
    }

    @PostMapping("/revenue")
    public ResponseEntity<String> patchRevenue(@RequestBody Revenue revenue, @RequestHeader String session_id)
    {
        return patchBusinessObject(revenue, session_id, "revenues", "revenues_timestamp");
    }

    @DeleteMapping(path="/revenue/{revenue_id}")
    public ResponseEntity<String> delete(@PathVariable String revenue_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Revenue(revenue_id), session_id, "revenues", "revenues_timestamp");
    }
}