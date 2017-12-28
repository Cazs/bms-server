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
import server.exceptions.InvalidQuoteException;
import server.model.Counter;
import server.model.Quote;
import server.repositories.QuoteRepository;

import java.util.List;

@RepositoryRestController
//@RequestMapping("/quotes")
public class QuoteController
{
    private PagedResourcesAssembler<Quote> pagedAssembler;
    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    public QuoteController(PagedResourcesAssembler<Quote> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/quotes/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<Quote>> getQuote(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling Quote GET request id: "+ id);
        List<Quote> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(id)), Quote.class, "quotes");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @GetMapping("/quotes")
    public ResponseEntity<Page<Quote>> getQuotes(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling Quote GET request {all}");
        List<Quote> contents =  IO.getInstance().mongoOperations().findAll(Quote.class, "quotes");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    ////public ResponseEntity<Page<Quote>> addQuote(@RequestBody Quote quote, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    @PutMapping("/quotes")
    public ResponseEntity<String> addQuote(@RequestBody Quote quote)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling Quote creation request.");
        //HttpHeaders headers = new HttpHeaders();
        if(quote!=null)
        {
            long date_logged = System.currentTimeMillis();
            quote.setDate_logged(date_logged);
            if (quote.isValid())
            {
                IO.log(getClass().getName(),IO.TAG_INFO, "creating new Quote addressed to: ["+quote.getClient_id()+"]");
                //commit Quote data to DB server
                IO.getInstance().mongoOperations().insert(quote, "quotes");
                IO.log(getClass().getName(),IO.TAG_INFO, "created quote: "+quote.get_id());
                //update respective timestamp
                CounterController.updateCounter(new Counter("quotes_timestamp", date_logged));
                return new ResponseEntity<>(quote.get_id(), HttpStatus.OK);
            } else throw new InvalidQuoteException();
        }
        IO.log(getClass().getName(),IO.TAG_ERROR, "invalid quote");
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
