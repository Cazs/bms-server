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
import server.model.QuoteItem;
import server.model.QuoteItem;
import server.repositories.QuoteItemRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class QuoteResourceController extends APIController
{
    private PagedResourcesAssembler<QuoteItem> pagedAssembler;
    @Autowired
    private QuoteItemRepository quote_resourceRepository;

    @Autowired
    public QuoteResourceController(PagedResourcesAssembler<QuoteItem> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/quote/resources/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getQuoteItem(@PathVariable("id") String id,
                                                                          @RequestHeader String session_id,
                                                                          Pageable pageRequest,
                                                                          PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new QuoteItem(id), "_id", session_id, "quote_resources", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/quotes/resources")
    public ResponseEntity<Page<? extends ApplicationObject>> getQuoteItems(@RequestHeader String session_id,
                                                                           Pageable pageRequest,
                                                                           PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new QuoteItem(), session_id, "quote_resources", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/quote/resource")
    public ResponseEntity<String> addQuoteItem(@RequestBody QuoteItem quote_item, @RequestHeader String session_id)
    {
        return putBusinessObject(quote_item, session_id, "quote_resources", "quotes_timestamp");
    }

    @PostMapping("/quote/resource")
    public ResponseEntity<String> patchQuoteItem(@RequestBody QuoteItem quote_item, @RequestHeader String session_id)
    {
        return patchBusinessObject(quote_item, session_id, "quote_resources", "quotes_timestamp");
    }

    @DeleteMapping(path="/quote/resource/{quote_resource_id}")
    public ResponseEntity<String> delete(@PathVariable String quote_resource_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new QuoteItem(quote_resource_id), session_id, "requisitions", "requisitions_timestamp");
    }
}
