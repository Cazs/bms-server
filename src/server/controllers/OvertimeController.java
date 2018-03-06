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
import server.repositories.OvertimeRepository;

import java.util.LinkedList;
import java.util.List;

@RepositoryRestController
@RequestMapping("/overtime_records")
public class OvertimeController
{
    private PagedResourcesAssembler<Overtime> pagedAssembler;
    @Autowired
    private OvertimeRepository overtimeRepository;

    @Autowired
    public OvertimeController(PagedResourcesAssembler<Overtime> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<Overtime>> getOvertime(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Overtime GET request id: "+ id);
        List<Overtime> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(id)), Overtime.class, "overtime_records");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Overtime>> getOvertimeRecords(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Overtime GET request {all}");
        List<Overtime> contents =  IO.getInstance().mongoOperations().findAll(Overtime.class, "overtime_records");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> addOvertimeRecords(@RequestBody Overtime overtime)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Overtime record creation request");
        return APIController.putBusinessObject(overtime, "overtime_records", "overtime_records_timestamp");
    }

    @PostMapping
    public ResponseEntity<String> patchOvertime(@RequestBody Overtime overtime)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Overtime record update request.");
        return APIController.patchBusinessObject(overtime, "overtime_records", "overtime_records_timestamp");
    }

    @PostMapping(value = "/mailto")
    public ResponseEntity<String> emailOvertime(@RequestHeader String _id, @RequestHeader String session_id,
                                             @RequestHeader String message, @RequestHeader String subject,
                                             @RequestHeader String destination, @RequestBody FileMetadata fileMetadata)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Overtime mailto request.");
        return APIController.emailBusinessObject(_id, session_id, message, subject, destination, fileMetadata, Overtime.class);
    }

    @PostMapping(value = "/approval_request")
    public ResponseEntity<String> requestOvertimeApproval(@RequestHeader String overtime_record_id, @RequestHeader String session_id,
                                                       @RequestHeader String message, @RequestHeader String subject,
                                                       @RequestBody FileMetadata fileMetadata)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Overtime approval request.");
        return APIController.requestBusinessObjectApproval(overtime_record_id, session_id, message, subject, fileMetadata, new Overtime().apiEndpoint(), Overtime.class);
    }

    @GetMapping("/approve/{overtime_record_id}/{vericode}")
    public ResponseEntity<String> approveOvertime(@PathVariable("overtime_record_id") String overtime_record_id, @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Overtime Record "+overtime_record_id+" approval request by Vericode.");
        return APIController.approveBusinessObjectByVericode(overtime_record_id, vericode, "overtime_records", "overtime_records_timestamp", Overtime.class);
    }
}
