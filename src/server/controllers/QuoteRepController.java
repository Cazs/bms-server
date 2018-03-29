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
import server.model.QuoteRep;
import server.repositories.QuoteRepRepository;

/**
 * Created by ghost on 2017/12/22.
 * @author th3gh0st
 */
@RepositoryRestController
public class QuoteRepController extends APIController
{
    private PagedResourcesAssembler<QuoteRep> pagedAssembler;
    @Autowired
    private QuoteRepRepository quote_representativeRepository;

    @Autowired
    public QuoteRepController(PagedResourcesAssembler<QuoteRep> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/quote/representatives/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends BusinessObject>> getQuoteRep(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new QuoteRep(id), "_id", session_id, "quote_representatives", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/quotes/representatives")
    public ResponseEntity<Page<? extends BusinessObject>> getQuoteReps(@RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new QuoteRep(), session_id, "quote_representatives", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/quote/representative")
    public ResponseEntity<String> addQuoteRep(@RequestBody QuoteRep quote_rep, @RequestHeader String session_id)
    {
        return putBusinessObject(quote_rep, session_id, "quote_representatives", "quotes_timestamp");
    }

    @PostMapping("/quote/representative")
    public ResponseEntity<String> patchQuote(@RequestBody QuoteRep quote_rep, @RequestHeader String session_id)
    {
        return patchBusinessObject(quote_rep, session_id, "quote_representatives", "quotes_timestamp");
    }

    @DeleteMapping(path="/quote/representative{quote_resource_id}")
    public ResponseEntity<String> delete(@PathVariable String quote_representative_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new QuoteRep(quote_representative_id), session_id, "quote_representatives", "quotes_timestamp");
    }
}
