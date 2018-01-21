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
import server.auxilary.RemoteComms;
import server.exceptions.InvalidBusinessObjectException;
import server.model.BusinessObject;
import server.model.Job;
import server.model.JobEmployee;
import server.repositories.JobEmployeeRepository;

import java.rmi.Remote;
import java.util.List;

@RepositoryRestController
public class JobEmployeeController
{
    private PagedResourcesAssembler<JobEmployee> pagedAssembler;

    @Autowired
    private JobEmployeeRepository job_employeeRepository;

    @Autowired
    public JobEmployeeController(PagedResourcesAssembler<JobEmployee> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    //Job Employee Route Handlers
    @GetMapping(path="/jobs/employees/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<JobEmployee>> getJobEmployee(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling JobEmployees GET request job_id: ["+ id+"]");
        List<JobEmployee> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("job_id").is(id)), JobEmployee.class, "job_employees");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @GetMapping("/jobs/employees")
    public ResponseEntity<Page<JobEmployee>> getJobEmployees(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling JobEmployee GET request {all}");
        List<JobEmployee> contents =  IO.getInstance().mongoOperations().findAll(JobEmployee.class, "job_employees");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @PutMapping("/jobs/employees")
    public ResponseEntity<String> addJobEmployee(@RequestBody JobEmployee job_employee)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling JobEmployee creation request.");
        //HttpHeaders headers = new HttpHeaders();
        return APIController.putBusinessObject(job_employee, "job_employees", "jobs_timestamp");
    }
}
