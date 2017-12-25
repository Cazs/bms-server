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
import server.model.BusinessObject;
import server.model.QuoteItem;
import server.repositories.QuoteItemRepository;

import java.util.LinkedList;
import java.util.List;

@RepositoryRestController
@RequestMapping("/quotes/resources")
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

        @GetMapping(path="/{id}", produces = "application/hal+json")
        public ResponseEntity<Page<QuoteItem>> getQuoteItem(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling QuoteItem GET request id: "+ id);
            List<QuoteItem> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("quote_id").is(id)), QuoteItem.class, "quote_resources");
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }

        @GetMapping
        public ResponseEntity<Page<QuoteItem>> getQuoteItems(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling QuoteItem GET request {all}");
            List<QuoteItem> contents =  IO.getInstance().mongoOperations().findAll(QuoteItem.class, "quote_resources");
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }

        @PostMapping
        @PutMapping
        public ResponseEntity<Page<QuoteItem>> addQuoteItem(@RequestBody QuoteItem quote_resource, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling QuoteItem creation request: " + quote_resource.asJSON());
            List<BusinessObject> contents = new LinkedList<>();
            contents.add(quote_resource);
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }
}
