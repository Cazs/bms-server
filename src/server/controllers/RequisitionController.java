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
import server.model.BusinessObject;
import server.model.Metafile;
import server.model.Requisition;
import server.model.Resource;
import server.repositories.RequisitionRepository;

/**
 * Created by ghost on 2018/01/13.
 * @author ghost
 */

@RepositoryRestController
public class RequisitionController extends APIController
{
    private PagedResourcesAssembler<Requisition> pagedAssembler;
    @Autowired
    private RequisitionRepository requisitionRepository;

    @Autowired
    public RequisitionController(PagedResourcesAssembler<Requisition> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/requisition/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends BusinessObject>> getRequisition(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Requisition(id), "_id", session_id, "requisitions", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/requisitions")
    public ResponseEntity<Page<? extends BusinessObject>> getRequisitions(@RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Requisition(), session_id, "requisitions", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/requisition")
    public ResponseEntity<String> addRequisitionRecord(@RequestBody Requisition requisition, @RequestHeader String session_id)
    {
        return putBusinessObject(requisition, session_id, "requisitions", "requisitions_timestamp");
    }

    @PostMapping("/requisition")
    public ResponseEntity<String> patchRequisitionRecord(@RequestBody Requisition requisition, @RequestHeader String session_id)
    {
        return patchBusinessObject(requisition, session_id, "requisitions", "requisitions_timestamp");
    }

    @PostMapping(value = "/requisition/mailto")//, consumes = "text/plain"//value =//, produces = "application/pdf"
    public ResponseEntity<String> emailRequisition(@RequestHeader String _id, @RequestHeader String session_id,
                                               @RequestHeader String message, @RequestHeader String subject,
                                               @RequestHeader String destination, @RequestBody Metafile metafile)//, @RequestParam("file") MultipartFile file
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Requisition mailto request.");
        return emailBusinessObject(_id, session_id, message, subject, destination, metafile, Requisition.class);
    }

    @PostMapping(value = "/requisition/request_approval")//, consumes = "text/plain"//value =//, produces = "application/pdf"
    public ResponseEntity<String> requestRequisitionApproval(@RequestHeader String requisition_id, @RequestHeader String session_id,
                                                       @RequestHeader String message, @RequestHeader String subject,
                                                       @RequestBody Metafile metafile)//, @RequestParam("file") MultipartFile file
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Requisition approval request.");
        return requestBusinessObjectApproval(requisition_id, session_id, message, subject, metafile, new Requisition().apiEndpoint(), Requisition.class);
    }

    @GetMapping("/requisition/approve/{requisition_id}/{vericode}")
    public ResponseEntity<String> approveRequisition(@PathVariable("requisition_id") String requisition_id, @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Requisition "+requisition_id+" approval request by Vericode.");
        return approveBusinessObjectByVericode(requisition_id, vericode, "requisitions", "requisitions_timestamp", Requisition.class);
    }

    @DeleteMapping(path="/requisition/{requisition_id}")
    public ResponseEntity<String> delete(@PathVariable String requisition_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Requisition(requisition_id), session_id, "requisitions", "requisitions_timestamp");
    }
}