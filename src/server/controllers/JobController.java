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
import server.exceptions.InvalidJobException;
import server.model.Counter;
import server.model.Job;
import server.repositories.JobRepository;

import java.util.List;

@RepositoryRestController
//@RequestMapping("/jobs")
public class JobController
{
    private PagedResourcesAssembler<Job> pagedAssembler;
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    public JobController(PagedResourcesAssembler<Job> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/jobs/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<Job>> getJob(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling Job GET request id: "+ id);
        List<Job> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(id)), Job.class, "jobs");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @GetMapping("/jobs")
    public ResponseEntity<Page<Job>> getJobs(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling Job GET request {all}");
        List<Job> contents =  IO.getInstance().mongoOperations().findAll(Job.class, "jobs");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @PutMapping("/jobs")
    public ResponseEntity<String> addJob(@RequestBody Job job)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling Job creation request.");
        if(job!=null)
        {
            long date_logged = System.currentTimeMillis();
            job.setDate_logged(date_logged);
            if (job.isValid())
            {
                IO.log(getClass().getName(),IO.TAG_INFO, "creating new job.");
                //commit Job data to DB server
                IO.getInstance().mongoOperations().insert(job, "jobs");
                IO.log(getClass().getName(),IO.TAG_INFO, "created job: "+job.get_id());
                //update respective timestamp
                CounterController.updateCounter(new Counter("jobs_timestamp", date_logged));
                return new ResponseEntity<>(job.get_id(), HttpStatus.OK);
            } else throw new InvalidJobException();
        }
        IO.log(getClass().getName(),IO.TAG_ERROR, "invalid job");
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
