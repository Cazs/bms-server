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
import server.model.QuickJob;
import server.repositories.QuickJobRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */
@RepositoryRestController
public class QuickJobController extends APIController
{
    private PagedResourcesAssembler<QuickJob> pagedAssembler;
    @Autowired
    private QuickJobRepository quickjobRepository;

    @Autowired
    public QuickJobController(PagedResourcesAssembler<QuickJob> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/quickjob/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getQuickJob(@PathVariable("id") String id,
                                                                      @RequestHeader String session_id,
                                                                      Pageable pageRequest,
                                                                      PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new QuickJob(id), "_id", session_id, "quickjobs", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/quickjobs")
    public ResponseEntity<Page<? extends ApplicationObject>> getQuickJobs(Pageable pageRequest,
                                                                       @RequestHeader String session_id,
                                                                       PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new QuickJob(), session_id, "quickjobs", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/quickjob")
    public ResponseEntity<String> addQuickJob(@RequestBody QuickJob quickjob, @RequestHeader String session_id)
    {
        return putBusinessObject(quickjob, session_id, "quickjobs", "quickjobs_timestamp");
    }

    @PostMapping("/quickjob")
    public ResponseEntity<String> patchQuickJob(@RequestBody QuickJob quickjob, @RequestHeader String session_id)
    {
        return patchBusinessObject(quickjob, session_id, "quickjobs", "quickjobs_timestamp");
    }

//    @PostMapping(value = "/quickjob/mailto")
//    public ResponseEntity<String> emailQuickJob(@RequestHeader String quickjob_id, @RequestHeader String session_id,
//                                                        @RequestHeader String message, @RequestHeader String subject,
//                                                        @RequestHeader String destination, @RequestBody Metafile metafile)
//    {
//        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling QuickJob mailto request.");
//        return emailBusinessObject(quickjob_id, session_id, message, subject, destination, metafile, QuickJob.class);
//    }

    @PostMapping(value = "/quickjob/approval_request")
    public ResponseEntity<String> requestQuickJobApproval(@RequestHeader String quickjob_id, @RequestHeader String session_id,
                                                       @RequestHeader String message, @RequestHeader String subject,
                                                       @RequestBody Metafile metafile)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling QuickJob approval request.");
        return requestBusinessObjectApproval(quickjob_id, session_id, message, subject, metafile, new QuickJob().apiEndpoint(), QuickJob.class);
    }

    @GetMapping("/quickjob/approve/{quickjob_id}/{vericode}")
    public ResponseEntity<String> approveQuickJob(@PathVariable("quickjob_id") String quickjob_id,
                                               @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling QuickJob "+quickjob_id+" approval request by Vericode.");
        return approveBusinessObjectByVericode(quickjob_id, vericode, "quickjobs", "quickjobs_timestamp", QuickJob.class);
    }

    @DeleteMapping(path="/quickjob/{quickjob_id}")
    public ResponseEntity<String> delete(@PathVariable String quickjob_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new QuickJob(quickjob_id), session_id, "quickjobs", "quickjobs_timestamp");
    }
}
