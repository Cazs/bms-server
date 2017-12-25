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
import server.exceptions.InvalidEmployeeException;
import server.model.BusinessObject;
import server.model.Employee;
import server.model.Job;

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
        IO.log(getClass().getName(), IO.TAG_INFO, "handling GET request for Employee: "+ id);
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
        IO.log(getClass().getName(), IO.TAG_INFO, "handling Employee get request {all}");
        List<Employee> contents =  IO.getInstance().mongoOperations().findAll(Employee.class, "employees");
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }

    //return new ResponseEntity < String > ("Response from POST method", HttpStatus.OK);
    @PutMapping
    public ResponseEntity<Page<Employee>> addEmployee(@RequestBody Employee employee, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling Employee creation request.");
        List<BusinessObject> contents = new LinkedList<>();
        if(employee!=null)
        {
            employee.setDate_joined(System.currentTimeMillis());
            if (employee.isValid())
            {
                System.out.println(">>>>>>>>>>>>>creating employee: "+employee.toString());
                IO.getInstance().mongoOperations().save(employee, "employees");
                List<Employee> employees = IO.getInstance().mongoOperations()
                        .find(new Query(Criteria.where("usr").is(employee.getUsr())), Employee.class, "employees");
                if (employees != null)
                {
                    if (!employees.isEmpty())
                        contents.add(employees.get(0));
                    else IO.log(getClass().getName(), IO.TAG_ERROR, "could not find added Employee object in the database.");
                } else
                    IO.log(getClass().getName(), IO.TAG_ERROR, "MongoDB operation returned null Employee object.");
            } else
                throw new InvalidEmployeeException();
        }
        return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents
                .size()), (ResourceAssembler) assembler), HttpStatus.OK);
    }
}
