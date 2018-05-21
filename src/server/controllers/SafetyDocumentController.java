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
import server.model.ApplicationObject;
import server.model.Metafile;
import server.model.SafetyDocument;
import server.repositories.SafetyDocumentsRepository;

/**
 * Created by th3gh0st on 2018/03/16.
 * @author th3gh0st
 */

@RepositoryRestController
public class SafetyDocumentController extends APIController
{
    private PagedResourcesAssembler<SafetyDocument> pagedAssembler;
    @Autowired
    private SafetyDocumentsRepository safety_documentRepository;

    @Autowired
    public SafetyDocumentController(PagedResourcesAssembler<SafetyDocument> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/document/safety/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getSafetyDocument(@PathVariable("id") String id,
                                                                     @RequestHeader String session_id,
                                                                     Pageable pageRequest,
                                                                     PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new SafetyDocument(id), "_id", session_id, "safety_documents", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/documents/safety")
    public ResponseEntity<Page<? extends ApplicationObject>> getSafetyDocuments(@RequestHeader String session_id, Pageable pageRequest,
                                                                      PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new SafetyDocument(), session_id, "safety_documents", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/document/safety")
    public ResponseEntity<String> addSafetyDocument(@RequestBody SafetyDocument safety_document, @RequestHeader String session_id)
    {
        return putBusinessObject(safety_document, session_id, "safety_documents", "safety_documents_timestamp");
    }

    @PostMapping("/document/safety")
    public ResponseEntity<String> patchSafetyDocument(@RequestBody SafetyDocument safety_document, @RequestHeader String session_id)
    {
        return patchBusinessObject(safety_document, session_id, "safety_documents", "safety_documents_timestamp");
    }

//    @PostMapping(value = "/document/safety/mailto")
//    public ResponseEntity<String> emailSafetyDocument(@RequestHeader String _id, @RequestHeader String session_id,
//                                             @RequestHeader String message, @RequestHeader String subject,
//                                             @RequestHeader String destination, @RequestBody Metafile metafile)
//    {
//        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling handling mailto request.");
//        return emailBusinessObject(_id, session_id, message, subject, destination, metafile, SafetyDocument.class);
//    }

    @PostMapping(value = "/document/safety/approval_request")
    public ResponseEntity<String> requestSafetyDocumentApproval(@RequestHeader String safety_document_id, @RequestHeader String session_id,
                                                       @RequestHeader String message, @RequestHeader String subject,
                                                       @RequestBody Metafile metafile)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling SafetyDocument approval request.");
        return requestBusinessObjectApproval(safety_document_id, session_id, message, subject, metafile, new SafetyDocument().apiEndpoint(), SafetyDocument.class);
    }

    @GetMapping("/document/safety/approve/{safety_document_id}/{vericode}")
    public ResponseEntity<String> approveSafetyDocument(@PathVariable("safety_document_id") String safety_document_id, @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling SafetyDocument "+safety_document_id+" approval request by Vericode.");
        return approveBusinessObjectByVericode(safety_document_id, vericode, "safety_documents", "safety_documents_timestamp", SafetyDocument.class);
    }

    @DeleteMapping(path="/document/safety/{safety_document_id}")
    public ResponseEntity<String> deleteSafetyDocument(@PathVariable String safety_document_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new SafetyDocument(safety_document_id), session_id, "safety_documents", "safety_documents_timestamp");
    }
}
