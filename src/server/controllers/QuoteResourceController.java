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
import server.auxilary.RemoteComms;
import server.exceptions.InvalidQuoteResourceException;
import server.model.Counter;
import server.model.QuoteItem;
import server.repositories.QuoteItemRepository;

import java.util.List;

@RepositoryRestController
//@RequestMapping("/quotes/resources")
public class QuoteResourceController
{
    private PagedResourcesAssembler<QuoteItem> pagedAssembler;
    @Autowired
    private QuoteItemRepository quote_resourceRepository;

    @Autowired
    public QuoteResourceController(PagedResourcesAssembler<QuoteItem> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/quotes/resources/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<QuoteItem>> getQuoteItem(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling QuoteItem GET request id: "+ id);
        List<QuoteItem> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("quote_id").is(id)), QuoteItem.class, "quote_resources");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @GetMapping("/quotes/resources")
    public ResponseEntity<Page<QuoteItem>> getQuoteItems(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling QuoteItem GET request {all}");
        List<QuoteItem> contents =  IO.getInstance().mongoOperations().findAll(QuoteItem.class, "quote_resources");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @PutMapping("/quotes/resources")
    public ResponseEntity<String> addQuoteItem(@RequestBody QuoteItem quote_resource)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling QuoteResource creation request.");
        if(quote_resource!=null)
        {
            RemoteComms.commitBusinessObjectToDatabase(quote_resource, "quote_resources", "quotes_timestamp");
        }
        return new ResponseEntity<>("Invalid quote_resource", HttpStatus.CONFLICT);
    }
}
