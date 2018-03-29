package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.*;
import server.repositories.LeaveRepository;

/**
 * Created by ghost on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class LeaveController extends APIController
{
    private PagedResourcesAssembler<Leave> pagedAssembler;
    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    public LeaveController(PagedResourcesAssembler<Leave> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/leave_application/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends BusinessObject>> getLeave(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Leave(id), "_id", session_id, "leave_applications", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/leave_applications")
    public ResponseEntity<Page<? extends BusinessObject>> getLeaveRecords(@RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Leave(), session_id, "leave_applications", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/leave_application")
    public ResponseEntity<String> addLeaveRecord(@RequestBody Leave leave, @RequestHeader String session_id)
    {
        return putBusinessObject(leave, session_id, "leave_applications", "leave_applications_timestamp");
    }

    @PostMapping("/leave_application")
    public ResponseEntity<String> patchLeaveRecord(@RequestBody Leave leave, @RequestHeader String session_id)
    {
        return patchBusinessObject(leave, session_id, "leave_applications", "leave_applications_timestamp");
    }

    @PostMapping(value = "/leave_application/mailto")
    public ResponseEntity<String> emailLeave(@RequestHeader String _id, @RequestHeader String session_id,
                                             @RequestHeader String message, @RequestHeader String subject,
                                             @RequestHeader String destination, @RequestBody Metafile metafile)
    {
        return emailBusinessObject(_id, session_id, message, subject, destination, metafile, Leave.class);
    }

    @PostMapping(value = "/leave_application/approval_request")
    public ResponseEntity<String> requestLeaveApproval(@RequestHeader String leave_record_id, @RequestHeader String session_id,
                                                       @RequestHeader String message, @RequestHeader String subject,
                                                       @RequestBody Metafile metafile)//, @RequestParam("file") MultipartFile file
    {
        return requestBusinessObjectApproval(leave_record_id, session_id, message, subject, metafile, new Leave().apiEndpoint(), Leave.class);
    }

    @GetMapping("/leave_application/approve/{leave_record_id}/{vericode}")
    public ResponseEntity<String> approveLeave(@PathVariable("leave_record_id") String leave_record_id, @PathVariable("vericode") String vericode)
    {
        return approveBusinessObjectByVericode(leave_record_id, vericode, "leave_applications", "leave_applications_timestamp", Leave.class);
    }

    @DeleteMapping(path="/leave_application/{leave_application_id}")
    public ResponseEntity<String> delete(@PathVariable String leave_application_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Leave(leave_application_id), session_id, "leave_applications", "leave_applications_timestamp");
    }
}