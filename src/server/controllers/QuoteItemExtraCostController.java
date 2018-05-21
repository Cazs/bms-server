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
import server.model.QuoteItemExtraCost;
import server.repositories.QuoteItemExtraCostRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class QuoteItemExtraCostController extends APIController
{
    private PagedResourcesAssembler<QuoteItemExtraCost> pagedAssembler;
    @Autowired
    private QuoteItemExtraCostRepository quote_item_extra_costRepository;

    @Autowired
    public QuoteItemExtraCostController(PagedResourcesAssembler<QuoteItemExtraCost> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/quote/resource/extra_costs/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getQuoteItem(@PathVariable("id") String id,
                                                                          @RequestHeader String session_id,
                                                                          Pageable pageRequest,
                                                                          PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new QuoteItemExtraCost(id), "quote_item_id", session_id, "quote_extra_costs", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/quotes/resources/extra_costs")
    public ResponseEntity<Page<? extends ApplicationObject>> getQuoteItems(@RequestHeader String session_id,
                                                                           Pageable pageRequest,
                                                                           PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new QuoteItemExtraCost(), session_id, "quote_extra_costs", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/quote/resource/extra_cost")
    public ResponseEntity<String> addQuoteItem(@RequestBody QuoteItemExtraCost extraCost, @RequestHeader String session_id)
    {
        return putBusinessObject(extraCost, session_id, "quote_extra_costs", "quotes_timestamp");
    }

    @PostMapping("/quote/resource/extra_cost")
    public ResponseEntity<String> patchQuoteItem(@RequestBody QuoteItemExtraCost extraCost, @RequestHeader String session_id)
    {
        return patchBusinessObject(extraCost, session_id, "quote_extra_costs", "quotes_timestamp");
    }

    @DeleteMapping(path="/quote/resource/extra_cost/{extra_cost_id}")
    public ResponseEntity<String> delete(@PathVariable String extra_cost_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new QuoteItemExtraCost(extra_cost_id), session_id, "quote_extra_costs", "quotes_timestamp");
    }
}
