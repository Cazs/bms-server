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
import server.model.QuoteService;
import server.repositories.QuoteServiceRepository;

import java.util.List;

@RepositoryRestController
@RequestMapping("/quotes/services")
public class QuoteServiceController
{
    private PagedResourcesAssembler<QuoteService> pagedAssembler;
    @Autowired
    private QuoteServiceRepository quote_serviceRepository;

    @Autowired
    public QuoteServiceController(PagedResourcesAssembler<QuoteService> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<QuoteService>> getQuoteServices(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling QuoteService GET request for Quote with id: "+ id);
        List<QuoteService> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("quote_id").is(id)), QuoteService.class, "quote_services");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<QuoteService>> getQuotesServices(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling QuoteService GET request {all}");
        List<QuoteService> contents =  IO.getInstance().mongoOperations().findAll(QuoteService.class, "quote_services");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> addQuoteService(@RequestBody QuoteService quote_service)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling QuoteService creation request.");
        //HttpHeaders headers = new HttpHeaders();
        return APIController.putBusinessObject(quote_service, "quote_services", "quotes_timestamp");
    }

    @PostMapping
    public ResponseEntity<String> patchResource(@RequestBody QuoteService quote_service)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling QuoteService update request.");
        return APIController.patchBusinessObject(quote_service, "quote_services", "quotes_timestamp");
    }
}
