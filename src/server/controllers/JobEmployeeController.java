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
import server.model.BusinessObject;
import server.model.JobEmployee;
import server.repositories.JobEmployeeRepository;

import java.util.LinkedList;
import java.util.List;

@RepositoryRestController
@RequestMapping("/jobs/employees")
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
        @GetMapping(path="/{id}", produces = "application/hal+json")
        public ResponseEntity<Page<JobEmployee>> getJobEmployee(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling Job Employees GET request id: "+ id);
            List<JobEmployee> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("job_id").is(id)), JobEmployee.class, "job_employees");
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }

        @GetMapping
        public ResponseEntity<Page<JobEmployee>> getJobEmployees(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling JobEmployee GET request {all}");
            List<JobEmployee> contents =  IO.getInstance().mongoOperations().findAll(JobEmployee.class, "job_employees");
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }

        @PostMapping
        @PutMapping
        public ResponseEntity<Page<JobEmployee>> addJobEmployee(@RequestBody JobEmployee job_employee, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling JobEmployee creation request: " + job_employee.asJSON());
            List<BusinessObject> contents = new LinkedList<>();
            contents.add(job_employee);
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }
}
