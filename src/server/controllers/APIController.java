package server.controllers;

import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auxilary.AccessLevels;
import server.auxilary.IO;
import server.auxilary.RemoteComms;
import server.auxilary.Session;
import server.exceptions.EmployeeNotFoundException;
import server.exceptions.InvalidBusinessObjectException;
import server.managers.SessionManager;
import server.model.*;

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

    public static ResponseEntity<String> emailBusinessObject(String _id, String session_id, String message,
                                                             String subject, String destination, FileMetadata fileMetadata, Class model)//, @RequestParam("file") MultipartFile file
    {
        if(fileMetadata==null)
            return new ResponseEntity<>("Invalid attached FileMetadata object.", HttpStatus.CONFLICT);
        if(session_id!=null)
        {
            Session user_session = SessionManager.getInstance().getUserSession(session_id);
            if(user_session!=null)
            {
                if(_id!=null)
                {
                    if(fileMetadata.getFile()!=null)
                    {
                        try
                        {
                            //check if destination param contains multiple emails
                            String[] email_addresses = destination.split(",");
                            //Send email
                            MailjetResponse response = RemoteComms.emailWithAttachment(subject, message, email_addresses, new FileMetadata[]{fileMetadata});
                            if(response==null)
                                return new ResponseEntity<>("Could not send email.", HttpStatus.CONFLICT);
                            else return new ResponseEntity<>("eMail has been sent successfully.", HttpStatus.valueOf(response.getStatus()));
                        } catch (MailjetSocketTimeoutException e)
                        {
                            IO.log(APIController.class.getName(), IO.TAG_ERROR, e.getMessage());
                            return new ResponseEntity<>("Could not send eMail: "+e.getMessage(), HttpStatus.CONFLICT);
                        } catch (MailjetException e)
                        {
                            IO.log(APIController.class.getName(), IO.TAG_ERROR, e.getMessage());
                            return new ResponseEntity<>("Could not send eMail: "+e.getMessage(), HttpStatus.CONFLICT);
                        }
                    } else return new ResponseEntity<>("Invalid attached BusinessObject{"+model.getName()+"["+_id+"]} PDF", HttpStatus.CONFLICT);
                } else return new ResponseEntity<>("Invalid _id param", HttpStatus.CONFLICT);
            } else return new ResponseEntity<>("Invalid user session. Please log in.", HttpStatus.CONFLICT);
        } else return new ResponseEntity<>("Invalid session_id header param", HttpStatus.CONFLICT);
    }

    public static ResponseEntity<String> requestBusinessObjectApproval(String _id, String session_id, String message,
                                                                       String subject, FileMetadata fileMetadata, String endpoint, Class model)//, @RequestParam("file") MultipartFile file
    {
        if(fileMetadata==null)
            return new ResponseEntity<>("Invalid attached FileMetadata object.", HttpStatus.CONFLICT);
        if(session_id!=null)
        {
            Session user_session = SessionManager.getInstance().getUserSession(session_id);
            if(user_session!=null)
            {
                if(_id!=null)
                {
                    if(fileMetadata.getFile()!=null)
                    {
                        IO.log(APIController.class.getClass().getName(), IO.TAG_INFO, "creating Vericode for BusinessObject{"+model.getName()+"["+_id+"]}");
                        //create Vericode object
                        String approval_code = IO.generateRandomString(16);
                        Vericode vericode = new Vericode(_id, approval_code);
                        vericode.setDate_logged(System.currentTimeMillis());
                        vericode.setCreator(user_session.getUsr());

                        //save Vericode object
                        String new_vericode_id = APIController.putBusinessObject(vericode, "vericodes", "vericodes_timestamp").getBody();

                        try
                        {
                            IO.log(APIController.class.getName(), IO.TAG_INFO, "Looking for Employees with approval clearance.");
                            //look for Employees with enough clearance for approval
                            List<Employee> auth_employees = IO.getInstance().mongoOperations().find(
                                    new Query(Criteria.where("access_level").gte(AccessLevels.SUPERUSER.getLevel())),
                                    Employee.class, "employees");

                            //check if any Employees were found
                            if(auth_employees==null)
                                return new ResponseEntity<>("Could not find any Employees with approval clearance.", HttpStatus.CONFLICT);
                            if(auth_employees.isEmpty())
                                return new ResponseEntity<>("Could not find any Employees with approval clearance.", HttpStatus.CONFLICT);

                            IO.log(APIController.class.getName(), IO.TAG_INFO, "Found ["+auth_employees.size()+"] Employees with clearance. Sending[eMailing] BusinessObject{"+model.getName()+"["+_id+"]} for approval.");
                            Employee[] auth_employees_arr = new Employee[auth_employees.size()];
                            auth_employees.toArray(auth_employees_arr);

                            //create approval link
                            message += "<br/><h3 style=\"text-align:center;\">" +
                                    "Click <a href=\""
                                    +RemoteComms.host+endpoint+"/approve/"+_id+"/"+vericode.getCode()
                                    +"\">here</a> to approve this "+model.getName()+".</h3>";
                            //Send email with approval link
                            MailjetResponse response = RemoteComms.emailWithAttachment(subject, message,
                                                                    auth_employees_arr, new FileMetadata[]{fileMetadata});
                            if(response==null)
                                return new ResponseEntity<>("Could not send email for approval.", HttpStatus.CONFLICT);
                            else return new ResponseEntity<>(String.valueOf(response.getStatus()), HttpStatus.valueOf(response.getStatus()));
                        } catch (MailjetSocketTimeoutException e)
                        {
                            IO.log(APIController.class.getName(), IO.TAG_ERROR, e.getMessage());
                            return new ResponseEntity<>("Could not send eMail: "+e.getMessage(), HttpStatus.CONFLICT);
                        } catch (MailjetException e)
                        {
                            IO.log(APIController.class.getName(), IO.TAG_ERROR, e.getMessage());
                            return new ResponseEntity<>("Could not send eMail: "+e.getMessage(), HttpStatus.CONFLICT);
                        }
                    } else return new ResponseEntity<>("Invalid attached BusinessObject{"+model.getName()+"["+_id+"]} PDF", HttpStatus.CONFLICT);
                } else return new ResponseEntity<>("Invalid _id param", HttpStatus.CONFLICT);
            } else return new ResponseEntity<>("Invalid user session. Please log in.", HttpStatus.CONFLICT);
        } else return new ResponseEntity<>("Invalid session_id header param", HttpStatus.CONFLICT);
    }

    public static ResponseEntity<String> approveBusinessObjectByVericode(String _id, String vericode, String collection, String collection_timestamp, Class model)
    {
        List<Vericode> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("code_name").is(_id).and("code").is(vericode)), Vericode.class, "vericodes");
        String response_msg = "<!DOCTYPE html><html>";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "text/html");
        if(contents!=null)
        {
            if (!contents.isEmpty())
            {
                //valid Vericode-BusinessObject combination - approve BusinessObject
                List<BusinessObject> businessObjects = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(_id)), model, collection);
                if (businessObjects != null)
                {
                    if (!businessObjects.isEmpty())
                    {
                        IO.log(APIController.class.getName(), IO.TAG_INFO, "valid BusinessObject{"+model+"["+_id+"]} approval credentials. Updating status.");

                        BusinessObject businessObject = businessObjects.get(0);
                        businessObject.parse("status", BusinessObject.STATUS_APPROVED);
                        HttpStatus status = APIController.patchBusinessObject(businessObject, collection, collection_timestamp).getStatusCode();

                        if(status==HttpStatus.OK)
                        {
                            IO.log(APIController.class.getClass().getName(), IO.TAG_INFO, "Successfully approved BusinessObject{"+model+"["+_id+"]} using Vericode ["+vericode+"].");
                            response_msg += "<h3>Successfully approved {"+model+"["+_id+"]} using verification code [" + vericode + "]</h3>"
                                    + "<script>alert('Successfully approved {"+model+"["+_id+"]} using verification code [" + vericode + "]');</script></html>";
                            return new ResponseEntity(response_msg, httpHeaders, HttpStatus.OK);
                            //return response_msg;
                        } else response_msg += "<h3>Could not approve {"+model+"["+_id+"]}.</h3>"
                                + "<script>alert('Could not approve {"+model+"["+_id+"]}');</script></html>";
                    }
                    else response_msg += "<h3>Could not find any {"+model+"["+_id+"]} matching provided id.</h3>"
                            + "<script>alert('Could not find any {"+model+"["+_id+"]} matching provided id.');</script></html>";
                }
                else response_msg += "<h3>Invalid {"+model+"["+_id+"]} to be approved.</h3>"
                        + "<script>alert('Invalid {"+model+"["+_id+"]} to be approved.');</script></html>";
            }
            else response_msg += "<h3>Could not find any {"+model+"["+_id+"]} verification codes matching provided credentials.</h3>"
                    + "<script>alert('Could not find any {"+model+"["+_id+"]} verification codes matching provided credentials.');</script></html>";
        } else response_msg += "<h3>Invalid {"+model+"["+_id+"]} approval credentials.</h3>"
                + "<script>alert('Invalid {"+model+"["+_id+"]} approval credentials.');</script></html>";

        IO.log(APIController.class.getClass().getName(), IO.TAG_ERROR, "Could not approve {"+model+"["+_id+"]} using verification code [" + vericode + "]: " + response_msg);
        return new ResponseEntity(response_msg, httpHeaders, HttpStatus.NOT_FOUND);
        //@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="");
        //return response_msg;
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
