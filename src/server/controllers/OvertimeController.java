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
import server.repositories.OvertimeRepository;

/**
 * Created by ghost on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class OvertimeController extends APIController
{
    private PagedResourcesAssembler<Overtime> pagedAssembler;
    @Autowired
    private OvertimeRepository overtimeRepository;

    @Autowired
    public OvertimeController(PagedResourcesAssembler<Overtime> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/overtime_application/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends BusinessObject>> getOvertime(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Overtime(id), "_id", session_id, "overtime_applications", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/overtime_applications")
    public ResponseEntity<Page<? extends BusinessObject>> getOvertimeApplications(Pageable pageRequest, @RequestHeader String session_id, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Overtime(), session_id, "overtime_applications", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/overtime_application")
    public ResponseEntity<String> addOvertimeRecords(@RequestBody Overtime overtime, @RequestHeader String session_id)
    {
        return putBusinessObject(overtime, session_id, "overtime_applications", "overtime_applications_timestamp");
    }

    @PostMapping("/overtime_application")
    public ResponseEntity<String> patchOvertime(@RequestBody Overtime overtime, @RequestHeader String session_id)
    {
        return patchBusinessObject(overtime, session_id, "overtime_applications", "overtime_applications_timestamp");
    }

    @PostMapping(value = "/overtime_application/mailto")
    public ResponseEntity<String> emailOvertime(@RequestHeader String _id, @RequestHeader String session_id,
                                             @RequestHeader String message, @RequestHeader String subject,
                                             @RequestHeader String destination, @RequestBody Metafile metafile)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Overtime Application mailto request.");
        return emailBusinessObject(_id, session_id, message, subject, destination, metafile, Overtime.class);
    }

    @PostMapping(value = "/overtime_application/approval_request")
    public ResponseEntity<String> requestOvertimeApproval(@RequestHeader String overtime_record_id, @RequestHeader String session_id,
                                                       @RequestHeader String message, @RequestHeader String subject,
                                                       @RequestBody Metafile metafile)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Overtime Application approval request.");
        return requestBusinessObjectApproval(overtime_record_id, session_id, message, subject, metafile, new Overtime().apiEndpoint(), Overtime.class);
    }

    @GetMapping("/overtime_application/approve/{overtime_record_id}/{vericode}")
    public ResponseEntity<String> approveOvertime(@PathVariable("overtime_record_id") String overtime_record_id, @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Overtime Application "+overtime_record_id+" approval request by Vericode.");
        return approveBusinessObjectByVericode(overtime_record_id, vericode, "overtime_applications", "overtime_applications_timestamp", Overtime.class);
    }

    @DeleteMapping(path="/overtime_application/{overtime_application_id}")
    public ResponseEntity<String> delete(@PathVariable String overtime_application_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Overtime(overtime_application_id), session_id, "overtime_applications", "overtime_applications_timestamp");
    }
}