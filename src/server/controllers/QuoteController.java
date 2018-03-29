package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auxilary.IO;
import server.model.*;
import server.repositories.QuoteRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */
@RepositoryRestController
public class QuoteController extends APIController
{
    private PagedResourcesAssembler<Quote> pagedAssembler;
    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    public QuoteController(PagedResourcesAssembler<Quote> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/quote/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getQuote(@PathVariable("id") String id,
                                                                      @RequestHeader String session_id,
                                                                      Pageable pageRequest,
                                                                      PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Quote(id), "_id", session_id, "quotes", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/quotes")
    public ResponseEntity<Page<? extends ApplicationObject>> getQuotes(Pageable pageRequest,
                                                                       @RequestHeader String session_id,
                                                                       PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Quote(), session_id, "quotes", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/quote")
    public ResponseEntity<String> addQuote(@RequestBody Quote quote, @RequestHeader String session_id)
    {
        return putBusinessObject(quote, session_id, "quotes", "quotes_timestamp");
    }

    @PostMapping("/quote")
    public ResponseEntity<String> patchQuote(@RequestBody Quote quote, @RequestHeader String session_id)
    {
        return patchBusinessObject(quote, session_id, "quotes", "quotes_timestamp");
    }

    @PostMapping(value = "/quote/mailto")
    public ResponseEntity<String> emailQuote(@RequestHeader String _id, @RequestHeader String session_id,
                                                        @RequestHeader String message, @RequestHeader String subject,
                                                        @RequestHeader String destination, @RequestBody Metafile metafile)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Quote mailto request.");
        return emailBusinessObject(_id, session_id, message, subject, destination, metafile, Quote.class);
    }

    @PostMapping(value = "/quote/approval_request")
    public ResponseEntity<String> requestQuoteApproval(@RequestHeader String quote_id, @RequestHeader String session_id,
                                                       @RequestHeader String message, @RequestHeader String subject,
                                                       @RequestBody Metafile metafile)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Quote approval request.");
        return requestBusinessObjectApproval(quote_id, session_id, message, subject, metafile, new Quote().apiEndpoint(), Quote.class);
    }

    @GetMapping("/quote/approve/{quote_id}/{vericode}")
    public ResponseEntity<String> approveQuote(@PathVariable("quote_id") String quote_id,
                                               @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Quote "+quote_id+" approval request by Vericode.");
        return approveBusinessObjectByVericode(quote_id, vericode, "quotes", "quotes_timestamp", Quote.class);
    }

    @DeleteMapping(path="/quote/{quote_id}")
    public ResponseEntity<String> delete(@PathVariable String quote_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Quote(quote_id), session_id, "quotes", "quotes_timestamp");
    }
}
