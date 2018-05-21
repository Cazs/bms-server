package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auxilary.BCrypt;
import server.auxilary.IO;
import server.auxilary.RemoteComms;
import server.auxilary.Session;
import server.exceptions.EmployeeNotFoundException;
import server.managers.SessionManager;
import server.model.*;

import java.util.List;

/**
 * Contains behaviour for retrieving timestamps and user authentication etc..
 * Created by th3gh0st on 2017/12/23.
 * @author th3gh0st
 */

@RestController
@RequestMapping("/")
public class RootController
{
    private PagedResourcesAssembler<ApplicationObject> pagedAssembler;

    @Autowired
    public RootController(PagedResourcesAssembler<ApplicationObject> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping
    public String root()
    {
        IO.log(getClass().getName(), IO.TAG_VERBOSE, "handling API root get request.");
        return "You have requested the API's root page, this does absolutely nothing.";
    }

    @GetMapping(path="/timestamp/{id}", produces = "application/hal+json")
    public Counter getTimestamp(@PathVariable("id") String id)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling GET request for Counter: "+ id);
        return CounterController.getCounter(id);
    }

    //TODO: use blowfish/bcrypt
    @PutMapping("/session")
    public ResponseEntity<Page<? extends ApplicationObject>> auth(@RequestHeader String usr, @RequestHeader String pwd)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling auth request ["+usr+":"+pwd+"]");
        // String hashed_pwd = BCrypt.hashpw(pwd, "replace_this"); // BCrypt.gensalt(12)
        // IO.log(getClass().getName(), IO.TAG_INFO, "hashed pwd ["+hashed_pwd+"]");
        String session_id = null;
        // List<Employee> employees =  IO.getInstance().mongoOperations().find(
        //        new Query(Criteria.where("usr").is(usr).and("pwd").is(hashed_pwd)), Employee.class, "employees");
        List<Employee> employees =  IO.getInstance().mongoOperations().find(
                new Query(Criteria.where("usr").is(usr)), Employee.class, "employees");
        if(employees!=null)
        {
            boolean found = false;
            for(Employee employee: employees)
                if(BCrypt.checkpw(pwd, employee.getPwd()))
                {
                    found = true;
                    break;
                }

            // if(employees.size()==1)
            if(found)
            {
                IO.log(getClass().getName(), IO.TAG_VERBOSE, "correct credentials.");
                //"HG58YZ3CR9"
                session_id = IO.generateRandomString(16);
                //found valid usr to pwd match, create session
                Session session = new Session();
                session.setSession_id(session_id);
                session.setUsr(usr);
                session.setDate(System.currentTimeMillis());
                session.setTtl(RemoteComms.TTL);
                SessionManager.getInstance().addSession(session);
                IO.log(getClass().getName(), IO.TAG_INFO, "user ["+session.getUsr()+"] signed in.");

                // return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) persistentEntityResourceAssembler), HttpStatus.OK);
                return new ResponseEntity(session, HttpStatus.OK);
            } else if(employees.size()!=1)
                throw new EmployeeNotFoundException();
        } else//List is null, no Employees found
            throw new EmployeeNotFoundException();
        return new ResponseEntity("Invalid user credentials.", HttpStatus.NOT_FOUND);
//        return "Incorrect Employee credentials.";
    }

    @PostMapping(value = "/mailto")
    public ResponseEntity<String> emailQuote(@RequestHeader String document_id, @RequestHeader String session_id,
                                             @RequestHeader String message, @RequestHeader String subject,
                                             @RequestHeader String destination, @RequestBody Metafile metafile)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling mailto request.");
        return APIController.emailBusinessObject(document_id, session_id, message, subject, destination, metafile);
    }
}
