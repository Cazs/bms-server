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
import server.model.Job;
import server.repositories.JobRepository;

/**
 * Created by ghost on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class JobController extends APIController
{
    private PagedResourcesAssembler<Job> pagedAssembler;
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    public JobController(PagedResourcesAssembler<Job> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/job/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends BusinessObject>> getJob(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Job(id), "_id", session_id, "jobs", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/jobs")
    public ResponseEntity<Page<? extends BusinessObject>> getJobs(Pageable pageRequest, @RequestHeader String session_id, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Job(), session_id, "jobs", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/job")
    public ResponseEntity<String> addJob(@RequestBody Job job, @RequestHeader String session_id)
    {
        return putBusinessObject(job, session_id, "jobs", "jobs_timestamp");
    }

    @PostMapping("/job")
    public ResponseEntity<String> patchJob(@RequestBody Job job, @RequestHeader String session_id)
    {
        return patchBusinessObject(job, session_id, "jobs", "jobs_timestamp");
    }

    @PostMapping(value = "/job/mailto")//, consumes = "text/plain"//value =//, produces = "application/pdf"
    public ResponseEntity<String> emailJob(@RequestHeader String _id, @RequestHeader String session_id,
                                             @RequestHeader String message, @RequestHeader String subject,
                                             @RequestHeader String destination, @RequestBody Metafile metafile)//, @RequestParam("file") MultipartFile file
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling handling mailto request.");
        return emailBusinessObject(_id, session_id, message, subject, destination, metafile, Job.class);
    }

    @PostMapping(value = "/job/approval_request")//, consumes = "text/plain"//value =//, produces = "application/pdf"
    public ResponseEntity<String> requestJobApproval(@RequestHeader String job_id, @RequestHeader String session_id,
                                                       @RequestHeader String message, @RequestHeader String subject,
                                                       @RequestBody Metafile metafile)//, @RequestParam("file") MultipartFile file
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Job approval request.");
        return requestBusinessObjectApproval(job_id, session_id, message, subject, metafile, new Job().apiEndpoint(), Job.class);
    }

    @GetMapping("/job/approve/{job_id}/{vericode}")
    public ResponseEntity<String> approve(@PathVariable("job_id") String job_id, @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Job "+job_id+" approval request by Vericode.");
        return approveBusinessObjectByVericode(job_id, vericode, "jobs", "jobs_timestamp", Job.class);
    }

    @DeleteMapping(path="/job/{job_id}")
    public ResponseEntity<String> delete(@PathVariable String job_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Job(job_id), session_id, "jobs", "jobs_timestamp");
    }
}
