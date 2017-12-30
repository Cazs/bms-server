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
import server.exceptions.InvalidEmployeeException;
import server.model.BusinessObject;
import server.model.Employee;
import server.model.Job;
import server.model.Resource;

import java.util.LinkedList;
import java.util.List;

@RepositoryRestController
@RequestMapping("/employees")
public class EmployeeController
{
    private PagedResourcesAssembler<Employee> pagedAssembler;

    @Autowired
    public EmployeeController(PagedResourcesAssembler<Employee> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<Employee>> getEmployeeById(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling GET request for Employee: "+ id);
        List<Employee> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(id)), Employee.class, "employees");
        if(contents!=null)//if not found by id, try by usr
        {
            if(contents.isEmpty())
            {
                IO.log(getClass().getName(), IO.TAG_WARN, "no Employee object matching _id found, trying usr..");
                contents = IO.getInstance().mongoOperations()
                        .find(new Query(Criteria.where("usr").is(id)), Employee.class, "employees");
            } else  IO.log(getClass().getName(), IO.TAG_INFO, "found Employee object.");
        }
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Employee>> getAllEmployees(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Employee get request {all}");
        List<Employee> contents =  IO.getInstance().mongoOperations().findAll(Employee.class, "employees");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> addEmployee(@RequestBody Employee employee)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Employee creation request.");
        //HttpHeaders headers = new HttpHeaders();
        return APIController.putBusinessObject(employee, "employees", "employees_timestamp");
    }

    @PostMapping
    public ResponseEntity<String> patchEmployee(@RequestBody Employee employee)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Employee update request.");
        return APIController.patchBusinessObject(employee, "employees", "employees_timestamp");
    }
}
