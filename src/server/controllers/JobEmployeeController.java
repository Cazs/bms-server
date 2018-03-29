package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.ApplicationObject;
import server.model.JobEmployee;
import server.repositories.JobEmployeeRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class JobEmployeeController extends APIController
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
    @GetMapping(path="/job/employees/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> get(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new JobEmployee(id), "_id", session_id, "job_employees", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/jobs/employees")
    public ResponseEntity<Page<? extends ApplicationObject>> getAll(Pageable pageRequest, @RequestHeader String session_id, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new JobEmployee(), session_id, "job_employees", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/job/employee")
    public ResponseEntity<String> put(@RequestBody JobEmployee job_employee, @RequestHeader String session_id)
    {
        return putBusinessObject(job_employee, session_id, "job_employees", "jobs_timestamp");
    }

    @PostMapping("/job/employee")
    public ResponseEntity<String> patch(@RequestBody JobEmployee jobEmployee, @RequestHeader String session_id)
    {
        return patchBusinessObject(jobEmployee, session_id, "job_employees", "jobs_timestamp");
    }

    @DeleteMapping(path="/job/employee/{job_employee_id}")
    public ResponseEntity<String> delete(@PathVariable String job_employee_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new JobEmployee(job_employee_id), session_id, "job_employees", "jobs_timestamp");
    }
}
