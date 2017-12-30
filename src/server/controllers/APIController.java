package server.controllers;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auxilary.IO;
import server.auxilary.RemoteComms;
import server.auxilary.Session;
import server.exceptions.EmployeeNotFoundException;
import server.exceptions.InvalidBusinessObjectException;
import server.managers.SessionManager;
import server.model.BusinessObject;
import server.model.Counter;
import server.model.Employee;

import java.rmi.Remote;
import java.util.List;

@RestController
@RequestMapping("/")
public class APIController
{
    @GetMapping
    public String root()
    {
        IO.log(getClass().getName(), IO.TAG_VERBOSE, "handling API root get request.");
        return "You have requested API root page, this does absolutely nothing.";
    }

    @GetMapping(path="/timestamp/{id}", produces = "application/hal+json")
    public Counter getTimestamp(@PathVariable("id") String id)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling GET request for Counter: "+ id);
        return CounterController.getCounter(id);
    }

    @PutMapping("/auth")
    public String auth(@RequestHeader String usr, @RequestHeader String pwd)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling auth request.");
        String session_id = null;
        System.out.println(">>>>>>Validating Employee: " + usr+":"+pwd);
        List<Employee> employees =  IO.getInstance().mongoOperations().find(
                new Query(Criteria.where("usr").is(usr).and("pwd").is(pwd)), Employee.class, "employees");
        if(employees!=null)
        {
            if(employees.size()==1)
            {
                //"HG58YZ3CR9"
                session_id = IO.generateRandomString(16);
                //found valid usr to pwd match, create session
                Session session = new Session();
                session.setSession_id(session_id);
                session.setUsr(usr);
                session.setDate(System.currentTimeMillis());
                session.setTtl(RemoteComms.TTL);
                SessionManager.getInstance().addSession(session);
                return session.toString();
            } else if(employees.size()!=1)
                throw new EmployeeNotFoundException();
        } else//List is null, no Employees found
            throw new EmployeeNotFoundException();
        return "Incorrect Employee credentials.";
    }

    public static ResponseEntity<String> putBusinessObject(BusinessObject businessObject, String collection, String collection_timestamp)
    {
        if(businessObject!=null)
        {
            /*if(businessObject.get_id()==null)
            {
                IO.log(Remote.class.getName(),IO.TAG_ERROR, "invalid "+businessObject.getClass().getName()+" _id attribute.");
                return new ResponseEntity<>("invalid "+businessObject.getClass().getName()+" _id attribute.", HttpStatus.CONFLICT);
            }*/

            IO.log(APIController.class.getName(), IO.TAG_INFO, "attempting to create new BusinessObject ["+businessObject.getClass().getName()+"]: "+businessObject.toString()+"");
            try
            {
                businessObject.setDate_logged(System.currentTimeMillis());
                RemoteComms.commitBusinessObjectToDatabase(businessObject, collection, collection_timestamp);
                return new ResponseEntity<>(businessObject.get_id(), HttpStatus.OK);
            } catch (InvalidBusinessObjectException e)
            {
                IO.log(Remote.class.getName(),IO.TAG_ERROR, "invalid "+businessObject.getClass().getName()+" object: {"+e.getMessage()+"}");
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>("Invalid BusinessObject", HttpStatus.CONFLICT);
    }

    public static ResponseEntity<String> patchBusinessObject(BusinessObject businessObject, String collection, String collection_timestamp)
    {
        if(businessObject!=null)
        {
            if(businessObject.get_id()==null)
            {
                IO.log(Remote.class.getName(),IO.TAG_ERROR, "invalid "+businessObject.getClass().getName()+" _id attribute.");
                return new ResponseEntity<>("invalid "+businessObject.getClass().getName()+" _id attribute.", HttpStatus.CONFLICT);
            }

            IO.log(APIController.class.getName(), IO.TAG_INFO, "patching "+businessObject.getClass().getName()+" ["+businessObject.get_id()+"]");
            try
            {
                RemoteComms.commitBusinessObjectToDatabase(businessObject, collection, collection_timestamp);
                return new ResponseEntity<>(businessObject.get_id(), HttpStatus.OK);
            } catch (InvalidBusinessObjectException e)
            {
                IO.log(Remote.class.getName(),IO.TAG_ERROR, "invalid "+businessObject.getClass().getName()+" object: {"+e.getMessage()+"}");
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>("Invalid BusinessObject", HttpStatus.CONFLICT);
    }
}
