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
import server.auxilary.AccessLevels;
import server.auxilary.IO;
import server.model.Employee;

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
        //TODO: check access_level
        if(employee!=null)
        {
            //only superusers can create accounts with access_level >= ADMIN
            if(employee.getAccess_level()>= AccessLevels.ADMIN.getLevel())//if account to be created has access rights >= ADMIN
            {
                //check if a super user account already exists in the database
                List<Employee> superusers = IO.getInstance().mongoOperations().find(new Query(Criteria.where("access_level").gte(AccessLevels.SUPERUSER.getLevel())), Employee.class, "employees");
                if(!superusers.isEmpty())
                {
                    //get user that's attempting to create the new superuser object
                    List<Employee> employee_creator = IO.getInstance().mongoOperations().find(new
                            Query(Criteria.where("usr").is(employee.getCreator())), Employee.class, "employees");
                    if(!employee_creator.isEmpty())
                    {
                        //found user, check if they're authorized to create superusers
                        if(employee_creator.get(0).getAccess_level()>=AccessLevels.SUPERUSER.getLevel())
                        {
                            //user creating new superuser is a superuser, create new superuser
                            return APIController.putBusinessObject(employee, "employees", "employees_timestamp");
                        } else
                        {
                            IO.log(getClass().getName(), IO.TAG_ERROR, "Employee ["+employee_creator.get(0).getName()+"] is not authorised to create users with this level of access. Can't create ADMIN/SUPER account.");
                            return new ResponseEntity<>("Employee ["+employee_creator.get(0).getName()+"] is not authorised to create users with this level of access. Please log in with an existing superuser account to create superusers and administrators.", HttpStatus.CONFLICT);
                        }
                    } else
                    {
                        IO.log(getClass().getName(), IO.TAG_ERROR, "Could not find employee that created this employee object. Can't create ADMIN/SUPER account.");
                        return new ResponseEntity<>("You are not authorised to create users with this level of access. Please log in with an existing superuser account to create superusers and administrators.", HttpStatus.NOT_FOUND);//superuser ID could not be found on db
                    }
                } else//no superusers in DB
                {
                    if(employee.getAccess_level()==AccessLevels.ADMIN.getLevel())
                    {
                        //Employee to be created is first Employee in DB and is ADMIN account, throw error
                        IO.log(getClass().getName(), IO.TAG_ERROR, "Could not create ADMIN account. First account in the database must be a superuser.");
                        return new ResponseEntity<>("Could not create administrator account. First account in the database must be a superuser.", HttpStatus.NOT_FOUND);//superuser ID could not be found on db
                    } else //new Employee is the first superuser Employee, create Employee object
                        return APIController.putBusinessObject(employee, "employees", "employees_timestamp");
                }
            } else if(employee.getAccess_level() == AccessLevels.STANDARD.getLevel())//if account to be created has access rights == STANDARD
            {
                //get Employee that's attempting to create the new Standard Employee object
                List<Employee> employee_creator = IO.getInstance().mongoOperations().find(new
                        Query(Criteria.where("usr").is(employee.getCreator())), Employee.class, "employees");
                if(!employee_creator.isEmpty())
                {
                    //found Employee, check if they're authorized to create STANDARD Employees
                    if(employee_creator.get(0).getAccess_level()>=AccessLevels.ADMIN.getLevel())
                    {
                        //user creating new STANDARD user is at least an ADMIN user, create user
                        return APIController.putBusinessObject(employee, "employees", "employees_timestamp");
                    } else
                    {
                        //user creating new STANDARD user is not authorised to create user
                        IO.log(getClass().getName(), IO.TAG_ERROR, "Employee ["+employee_creator.get(0).getName()+"] is not authorised to create users with this level of access [STANDARD].");
                        return new ResponseEntity<>("Employee ["+employee_creator.get(0).getName()+"] is not authorised to create users with this level of access. Please log in with an administrator/superuser account to create standard employee accounts.", HttpStatus.CONFLICT);
                    }
                } else
                {
                    IO.log(getClass().getName(), IO.TAG_ERROR, "Could not find account of Employee that's creating the new STANDARD Employee account.");
                    return new ResponseEntity<>("You are not authorised to create users with this level of access. Please log in with an administrator/superuser account to create standard employee accounts.", HttpStatus.NOT_FOUND);//superuser ID could not be found on db
                }
            } else //is a NO_ACCESS Employee, create object on db
                return APIController.putBusinessObject(employee, "employees", "employees_timestamp");
        } else
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "Invalid Employee object to be created.");
            return new ResponseEntity<>("Invalid Employee object to be created.", HttpStatus.CONFLICT);
        }
    }

    @PostMapping
    public ResponseEntity<String> patchEmployee(@RequestBody Employee employee)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Employee update request.");
        return APIController.patchBusinessObject(employee, "employees", "employees_timestamp");
    }
}
