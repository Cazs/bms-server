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
import server.model.*;
import server.repositories.LeaveRepository;

import java.util.LinkedList;
import java.util.List;

@RepositoryRestController
@RequestMapping("/leave_records")
public class LeaveController
{
    private PagedResourcesAssembler<Leave> pagedAssembler;
    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    public LeaveController(PagedResourcesAssembler<Leave> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<Leave>> getLeave(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Leave GET request id: "+ id);
        List<Leave> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(id)), Leave.class, "leave_records");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Leave>> getLeaveRecords(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Leave GET request {all}");
        List<Leave> contents =  IO.getInstance().mongoOperations().findAll(Leave.class, "leave_records");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> addLeaveRecord(@RequestBody Leave leave)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Leave record creation request");
        return APIController.putBusinessObject(leave, "leave_records", "leave_records_timestamp");
    }

    @PostMapping
    public ResponseEntity<String> patchLeaveRecord(@RequestBody Leave leave)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Leave record update request.");
        return APIController.patchBusinessObject(leave, "leave_records", "leave_records_timestamp");
    }

    @PostMapping(value = "/mailto")
    public ResponseEntity<String> emailLeave(@RequestHeader String _id, @RequestHeader String session_id,
                                             @RequestHeader String message, @RequestHeader String subject,
                                             @RequestHeader String destination, @RequestBody FileMetadata fileMetadata)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Leave mailto request.");
        return APIController.emailBusinessObject(_id, session_id, message, subject, destination, fileMetadata, Leave.class);
    }

    @PostMapping(value = "/approval_request")
    public ResponseEntity<String> requestLeaveApproval(@RequestHeader String leave_record_id, @RequestHeader String session_id,
                                                       @RequestHeader String message, @RequestHeader String subject,
                                                       @RequestBody FileMetadata fileMetadata)//, @RequestParam("file") MultipartFile file
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Leave approval request.");
        return APIController.requestBusinessObjectApproval(leave_record_id, session_id, message, subject, fileMetadata, new Leave().apiEndpoint(), Leave.class);
    }

    @GetMapping("/approve/{leave_record_id}/{vericode}")
    public ResponseEntity<String> approveLeave(@PathVariable("leave_record_id") String leave_record_id, @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Leave Record "+leave_record_id+" approval request by Vericode.");
        return APIController.approveBusinessObjectByVericode(leave_record_id, vericode, "leave_records", "leave_records_timestamp", Leave.class);
    }
}