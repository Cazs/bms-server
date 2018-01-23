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
import server.model.FileMetadata;
import server.model.Invoice;
import server.model.Requisition;
import server.model.Requisition;
import server.repositories.RequisitionRepository;

import java.util.List;

@RepositoryRestController
@RequestMapping("/requisitions")
public class RequisitionController
{
    private PagedResourcesAssembler<Requisition> pagedAssembler;
    @Autowired
    private RequisitionRepository requisitionRepository;

    @Autowired
    public RequisitionController(PagedResourcesAssembler<Requisition> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<Requisition>> getRequisition(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Requisition GET request _id: "+ id);
        List<Requisition> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(id)), Requisition.class, "requisitions");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Requisition>> getRequisitions(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Requisition GET request {all}");
        List<Requisition> contents =  IO.getInstance().mongoOperations().findAll(Requisition.class, "requisitions");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> addRequisitionRecord(@RequestBody Requisition requisition)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Requisition creation request");
        return APIController.putBusinessObject(requisition, "requisitions", "requisitions_timestamp");
    }

    @PostMapping
    public ResponseEntity<String> patchRequisitionRecord(@RequestBody Requisition requisition)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Requisition update request.");
        return APIController.patchBusinessObject(requisition, "requisitions", "requisitions_timestamp");
    }

    @PostMapping(value = "/mailto")//, consumes = "text/plain"//value =//, produces = "application/pdf"
    public ResponseEntity<String> emailRequisition(@RequestHeader String _id, @RequestHeader String session_id,
                                               @RequestHeader String message, @RequestHeader String subject,
                                               @RequestHeader String destination, @RequestBody FileMetadata fileMetadata)//, @RequestParam("file") MultipartFile file
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Requisition mailto request.");
        return APIController.emailBusinessObject(_id, session_id, message, subject, destination, fileMetadata, Requisition.class);
    }

    @PostMapping(value = "/request_approval")//, consumes = "text/plain"//value =//, produces = "application/pdf"
    public ResponseEntity<String> requestRequisitionApproval(@RequestHeader String requisition_id, @RequestHeader String session_id,
                                                       @RequestHeader String message, @RequestHeader String subject,
                                                       @RequestBody FileMetadata fileMetadata)//, @RequestParam("file") MultipartFile file
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Requisition approval request.");
        return APIController.requestBusinessObjectApproval(requisition_id, session_id, message, subject, fileMetadata, new Requisition().apiEndpoint(), Requisition.class);
    }

    @GetMapping("/approve/{requisition_id}/{vericode}")
    public ResponseEntity<String> approveRequisition(@PathVariable("requisition_id") String requisition_id, @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Requisition "+requisition_id+" approval request by Vericode.");
        return APIController.approveBusinessObjectByVericode(requisition_id, vericode, "requisitions", "requisitions_timestamp", Requisition.class);
    }
}