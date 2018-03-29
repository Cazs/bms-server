package server.controllers;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import server.auxilary.IO;
import server.auxilary.RemoteComms;
import server.auxilary.Session;
import server.exceptions.EmployeeNotFoundException;
import server.managers.SessionManager;
import server.model.Counter;
import server.model.Employee;
import java.util.List;

/**
 * Contains behaviour for retrieving timestamps and user authentication etc..
 * Created by ghost on 2017/12/23.
 * @author ghost
 */

@RestController
@RequestMapping("/")
public class RootController
{
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
    public String auth(@RequestHeader String usr, @RequestHeader String pwd)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling auth request.");
        String session_id = null;
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
                IO.log(getClass().getName(), IO.TAG_INFO, "user ["+session.getUsr()+"] signed in.");
                return session.toString();
            } else if(employees.size()!=1)
                throw new EmployeeNotFoundException();
        } else//List is null, no Employees found
            throw new EmployeeNotFoundException();
        return "Incorrect Employee credentials.";
    }
}
