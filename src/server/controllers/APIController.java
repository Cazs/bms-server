package server.controllers;

import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auxilary.AccessLevel;
import server.auxilary.IO;
import server.auxilary.RemoteComms;
import server.auxilary.Session;
import server.exceptions.InvalidBusinessObjectException;
import server.managers.SessionManager;
import server.model.*;
import java.util.List;

/**
 * Contains behaviour for creating, accessing, editing and deleting of server objects.
 * Created by ghost on 2017/12/23.
 * @author ghost
 */

@RestController
@RequestMapping("/")
public class APIController
{
    public static ResponseEntity<String> emailBusinessObject(String _id,
                                                      String session_id,
                                                      String message,
                                                      String subject,
                                                      String destination,
                                                      Metafile metafile)//, @RequestParam("file") MultipartFile file
    {
        if(metafile ==null)
            return new ResponseEntity<>("Invalid attached Metafile object.", HttpStatus.CONFLICT);
        if(session_id!=null)
        {
            // Session user_session = SessionManager.getInstance().getUserSession(session_id);
            // if(user_session!=null)
            {
                if(_id!=null)
                {
                    if(metafile.getFile()!=null)
                    {
                        try
                        {
                            //check if destination param contains multiple emails
                            String[] email_addresses = destination.split(",");
                            //Send email
                            MailjetResponse response = RemoteComms.emailWithAttachment(subject, message, email_addresses, new Metafile[]{metafile});
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
                    } else return new ResponseEntity<>("Invalid attachment.", HttpStatus.CONFLICT);
                } else return new ResponseEntity<>("Invalid _id param", HttpStatus.CONFLICT);
            } // else return new ResponseEntity<>("Invalid user session. Please log in.", HttpStatus.CONFLICT);
        } else return new ResponseEntity<>("Invalid session_id header param", HttpStatus.CONFLICT);
    }

    public ResponseEntity<String> requestBusinessObjectApproval(String _id,
                                                                String session_id,
                                                                String message,
                                                                String subject,
                                                                Metafile metafile,
                                                                String endpoint,
                                                                Class model)//, @RequestParam("file") MultipartFile file
    {
        if(metafile ==null)
            return new ResponseEntity<>("Invalid attached Metafile object.", HttpStatus.CONFLICT);
        if(session_id!=null)
        {
            Session user_session = SessionManager.getInstance().getUserSession(session_id);
            if(user_session!=null)
            {
                if(_id!=null)
                {
                    if(metafile.getFile()!=null)
                    {
                        IO.log(APIController.class.getClass().getName(), IO.TAG_INFO, "creating Vericode for ApplicationObject{"+model.getName()+"["+_id+"]}");
                        //create Vericode object
                        String approval_code = IO.generateRandomString(16);
                        Vericode vericode = new Vericode(_id, approval_code);
                        vericode.setDate_logged(System.currentTimeMillis());
                        vericode.setCreator(user_session.getUsr());

                        //save Vericode object
                        String new_vericode_id = putBusinessObject(vericode, session_id,"vericodes", "vericodes_timestamp").getBody();

                        try
                        {
                            IO.log(APIController.class.getName(), IO.TAG_INFO, "Looking for Employees with approval clearance.");
                            //look for Employees with enough clearance for approval
                            List<Employee> auth_employees = IO.getInstance().mongoOperations().find(
                                    new Query(Criteria.where("access_level").gte(AccessLevel.SUPERUSER.getLevel())),
                                    Employee.class, "employees");

                            //check if any Employees were found
                            if(auth_employees==null)
                                return new ResponseEntity<>("Could not find any Employees with approval clearance.", HttpStatus.CONFLICT);
                            if(auth_employees.isEmpty())
                                return new ResponseEntity<>("Could not find any Employees with approval clearance.", HttpStatus.CONFLICT);

                            IO.log(APIController.class.getName(), IO.TAG_INFO, "Found ["+auth_employees.size()+"] Employees with clearance. Sending[eMailing] ApplicationObject{"+model.getName()+"["+_id+"]} for approval.");
                            Employee[] auth_employees_arr = new Employee[auth_employees.size()];
                            auth_employees.toArray(auth_employees_arr);

                            //create approval link
                            message += "<br/><h3 style=\"text-align:center;\">" +
                                    "Click <a href=\""
                                    +RemoteComms.host+endpoint+"/approve/"+_id+"/"+vericode.getCode()
                                    +"\">here</a> to approve this "+model.getSimpleName()+".</h3>";
                            //Send email with approval link
                            MailjetResponse response = RemoteComms.emailWithAttachment(subject, message,
                                                                    auth_employees_arr, new Metafile[]{metafile});
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
                    } else return new ResponseEntity<>("Invalid attached ApplicationObject{"+model.getName()+"["+_id+"]} PDF", HttpStatus.CONFLICT);
                } else return new ResponseEntity<>("Invalid _id param", HttpStatus.CONFLICT);
            } else return new ResponseEntity<>("Invalid user session. Please log in.", HttpStatus.CONFLICT);
        } else return new ResponseEntity<>("Invalid session_id header param", HttpStatus.CONFLICT);
    }

    //TODO: this won't always have a session_id, find a way around it
    public ResponseEntity<String> approveBusinessObjectByVericode(String _id,
                                                                  String vericode,
                                                                  String collection,
                                                                  String collection_timestamp,
                                                                  Class model)
    {
        List<Vericode> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("code_name").is(_id).and("code").is(vericode)), Vericode.class, "vericodes");
        String response_msg = "<!DOCTYPE html><html>";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "text/html");
        if(contents!=null)
        {
            if (!contents.isEmpty())
            {
                //valid Vericode-ApplicationObject combination - approve ApplicationObject
                List<ApplicationObject> applicationObjects = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(_id)), model, collection);
                if (applicationObjects != null)
                {
                    if (!applicationObjects.isEmpty())
                    {
                        IO.log(APIController.class.getName(), IO.TAG_INFO, "valid ApplicationObject{"+model+"["+_id+"]} approval credentials. Updating status.");

                        ApplicationObject applicationObject = applicationObjects.get(0);
                        applicationObject.parse("status", ApplicationObject.STATUS_AUTHORISED);
                        HttpStatus status = patchBusinessObject(applicationObject, "", collection, collection_timestamp).getStatusCode();

                        if(status==HttpStatus.OK)
                        {
                            IO.log(APIController.class.getClass().getName(), IO.TAG_INFO, "Successfully approved ApplicationObject{"+model+"["+_id+"]} using Vericode ["+vericode+"].");
                            response_msg += "<h3>Successfully approved {"+model+"["+_id+"]} using verification code [" + vericode + "]</h3>"
                                    + "<script>alert('Successfully approved {"+model+"["+_id+"]} using verification code [" + vericode + "]');</script></html>";
                            return new ResponseEntity<>(response_msg, httpHeaders, HttpStatus.OK);
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
        return new ResponseEntity<>(response_msg, httpHeaders, HttpStatus.NOT_FOUND);
        //@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="");
        //return response_msg;
    }

    public ResponseEntity<Page<? extends ApplicationObject>> getBusinessObject(ApplicationObject object,
                                                                               String query_property,
                                                                               String session_id,
                                                                               String collection,
                                                                               PagedResourcesAssembler<? extends ApplicationObject> pagedResourcesAssembler,
                                                                               PersistentEntityResourceAssembler persistentEntityResourceAssembler,
                                                                               Pageable pageRequest)
    {
        if(object==null)
        {
            IO.log(APIController.class.getName(), IO.TAG_ERROR, "Invalid object identifier. Should create an empty ApplicationObject with at least the "+query_property+" attribute set.");
            return new ResponseEntity("Invalid object identifier. Should create an empty ApplicationObject with at least the "+query_property+" attribute set.", HttpStatus.CONFLICT);
        }
        if(object.get(query_property)!=null)
        {
            IO.log(object.getClass().getName(), IO.TAG_INFO, "\nhandling "+object.getClass().getSimpleName()+" GET ["+object.get(query_property)+"] request.");

            if(session_id==null)
            {
                IO.log(APIController.class.getName(), IO.TAG_ERROR, "Session ID is invalid.");
                return new ResponseEntity("Invalid session. Please sign in.", HttpStatus.CONFLICT);
            }

            //get session from session_id
            Session session = SessionManager.getInstance().getUserSession(session_id);
            if(session==null)
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "getBusinessObject()> no sessions associated with session id ["+session_id+"] were found.");
                return new ResponseEntity("Not a valid session. Please sign in.", HttpStatus.CONFLICT);
            }

            Employee employee = session.getEmployee();

            if(employee==null)
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "getBusinessObject()> no employees associated with session id ["+session_id+"] were found.");
                return new ResponseEntity("Not a valid session. Please sign in.", HttpStatus.CONFLICT);
            }

            //check if employee is authorised to read objects of this type
            if(employee.getAccess_level() < object.getReadMinRequiredAccessLevel().getLevel())
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "getBusinessObject()> employee ["+employee.getName()
                        +"]{current="+AccessLevel.values()[employee.getAccess_level()]+"} is not authorised to read " + object.getClass().getName() + "{required="+object.getReadMinRequiredAccessLevel()+"} objects.");
                return new ResponseEntity("You are not authorised to READ " + object.getClass().getSimpleName() + " objects. Minimum read requirement is " + object.getReadMinRequiredAccessLevel(), HttpStatus.UNAUTHORIZED);
            }

            IO.log(APIController.class.getName(), IO.TAG_INFO, "querying object ["+ object.get(query_property) + "] of type [" + object.getClass().getName()+"] from collection ["+collection+"]");

            List<? extends ApplicationObject> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where(query_property).is(object.get(query_property))), object.getClass(), collection);
            if(contents!=null)
                if(contents.size()>0)
                    return new ResponseEntity(pagedResourcesAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) persistentEntityResourceAssembler), HttpStatus.OK);
        }
        return new ResponseEntity("Could not find ApplicationObject with id ["+(object!=null?object.get_id():"null")+"] from collection ["+collection+"]", HttpStatus.CONFLICT);
    }

    public ResponseEntity<Page<? extends ApplicationObject>> getBusinessObjects(ApplicationObject object,
                                                                                String session_id,
                                                                                String collection,
                                                                                PagedResourcesAssembler<? extends ApplicationObject> pagedResourcesAssembler,
                                                                                PersistentEntityResourceAssembler persistentEntityResourceAssembler,
                                                                                Pageable pageRequest)
    {
        if(object!=null)
        {
            IO.log(object.getClass().getName(), IO.TAG_INFO, "\nhandling "+object.getClass().getSimpleName()+" GET ALL request.");

            if(session_id==null)
            {
                IO.log(APIController.class.getName(), IO.TAG_ERROR, "Session ID is invalid.");
                return new ResponseEntity("Invalid  session. Please sign in.", HttpStatus.CONFLICT);
            }

            //get session from session_id
            Session session = SessionManager.getInstance().getUserSession(session_id);
            if(session==null)
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "getBusinessObject()> no sessions associated with session id ["+session_id+"] were found.");
                return new ResponseEntity("Not a valid session. Please sign in.", HttpStatus.CONFLICT);
            }

            Employee employee = session.getEmployee();

            if(employee==null)
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "getBusinessObject()> no employees associated with session id ["+session_id+"] were found.");
                return new ResponseEntity("Not a valid session. Please sign in.", HttpStatus.CONFLICT);
            }

            //check if employee is authorised to read objects of this type
            if(employee.getAccess_level() < object.getReadMinRequiredAccessLevel().getLevel())
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "getBusinessObject()> employee ["+employee.getName()
                        +"]{current="+AccessLevel.values()[employee.getAccess_level()]+"} is not authorised to read " + object.getClass().getName() + "{required="+object.getReadMinRequiredAccessLevel()+"} objects.");
                return new ResponseEntity("You are not authorised to READ " + object.getClass().getSimpleName() + " objects. Minimum read requirement is " + object.getReadMinRequiredAccessLevel(), HttpStatus.UNAUTHORIZED);
            }

            IO.log(APIController.class.getName(), IO.TAG_INFO, "querying all objects of type [" + object.getClass().getName()+"] from collection ["+collection+"]");

            List<? extends ApplicationObject> contents = IO.getInstance().mongoOperations().findAll(object.getClass(), collection);
            if(contents!=null)
                if(contents.size()>0)
                    return new ResponseEntity(pagedResourcesAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) persistentEntityResourceAssembler), HttpStatus.OK);
            return new ResponseEntity("No "+object.getClass().getSimpleName()+" objects were found in the database.", HttpStatus.NO_CONTENT);
        } else
        {
            IO.log(APIController.class.getName(), IO.TAG_ERROR, "Invalid object. Should create an empty ApplicationObject.");
            return new ResponseEntity("Invalid object. Should create an empty ApplicationObject.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> putBusinessObject(ApplicationObject applicationObject,
                                                    String session_id,
                                                    String collection,
                                                    String collection_timestamp)
    {
        if(applicationObject !=null)
        {
            IO.log(applicationObject.getClass().getName(), IO.TAG_INFO, "\nhandling "+ applicationObject.getClass().getSimpleName()+" PUT request.");

            if(applicationObject.get_id()!=null)
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "ApplicationObject "+ applicationObject.getClass().getName()+" already has an ObjectId, redirecting to PATCH method.");
                return patchBusinessObject(applicationObject, session_id, collection, collection_timestamp);
            }

            if(applicationObject instanceof Employee)
            {
                // user creation, skip session & creator checks
                // TODO: review this, allow Employee objects to has creators
            }else
            {
                //get session from session_id
                Session session = SessionManager.getInstance().getUserSession(session_id);
                if (session == null)
                {
                    IO.log(APIController.class.getName(), IO.TAG_ERROR, "putBusinessObject()> no sessions associated with session [" + session_id + "] were found.");
                    return new ResponseEntity<>("Not a valid session", HttpStatus.CONFLICT);
                }

                Employee employee = session.getEmployee();

                if (employee == null)
                {
                    IO.log(APIController.class.getName(), IO.TAG_ERROR, "putBusinessObject()> no employees associated with session [" + session_id + "] were found.");
                    return new ResponseEntity<>("Not a valid session.", HttpStatus.CONFLICT);
                }

                //check if employee is authorised to create objects of this type
                if (employee.getAccess_level() < applicationObject.getWriteMinRequiredAccessLevel().getLevel())
                {
                    IO.log(APIController.class.getName(), IO.TAG_ERROR, "putBusinessObject()> employee [" + employee.getName()
                            + "]{current=" + AccessLevel.values()[employee.getAccess_level()] + "} is not authorised to create " + applicationObject.getClass().getName() + "{required=" + applicationObject.getWriteMinRequiredAccessLevel() + "} objects.");
                    return new ResponseEntity<>("You are not authorised to CREATE " + applicationObject.getClass().getSimpleName() + " objects. Minimum write requirement is " + applicationObject.getWriteMinRequiredAccessLevel(), HttpStatus.UNAUTHORIZED);
                }
            }
            // TODO: remove below
            // applicationObject.setCreator("jivesh");

            IO.log(APIController.class.getName(), IO.TAG_INFO, "attempting to create new ApplicationObject ["+ applicationObject.getClass().getName()+"]: "+ applicationObject.toString()+"");

            try
            {
                //set date object was logged
                applicationObject.setDate_logged(System.currentTimeMillis());

                //get collection count
                long count = IO.getInstance().mongoOperations().count(null, collection);

                //check if object with this object number already exists in the collection or not
                while (IO.getInstance().mongoOperations().exists(new Query(Criteria.where("object_number").is(count)), collection))
                    count++;

                //use collection count as object_number for new ApplicationObject
                applicationObject.setObject_number(count);//set current collection count as object_number for new ApplicationObject

                String new_obj_id = RemoteComms.commitBusinessObjectToDatabase(applicationObject, collection, collection_timestamp);

                return new ResponseEntity<>(new_obj_id, HttpStatus.OK);
            } catch (InvalidBusinessObjectException e)
            {
                IO.log(RemoteComms.class.getName(),IO.TAG_ERROR, "invalid "+ applicationObject.getClass().getName()+" object: {"+e.getMessage()+"}");
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>("Invalid ApplicationObject", HttpStatus.CONFLICT);
    }

    public ResponseEntity<String> patchBusinessObject(ApplicationObject applicationObject,
                                                      String session_id,
                                                      String collection,
                                                      String collection_timestamp)
    {
        if(applicationObject !=null)
        {
            if(applicationObject.get_id()==null)
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "ApplicationObject "+ applicationObject.getClass().getName()+" does NOT have an ObjectId, redirecting to PUT method.");
                //return putBusinessObject(applicationObject, session_id, collection, collection_timestamp);
                return new ResponseEntity<>("invalid "+ applicationObject.getClass().getSimpleName()+" _id attribute.", HttpStatus.CONFLICT);
            }

            IO.log(applicationObject.getClass().getName(), IO.TAG_VERBOSE, "\nhandling "+ applicationObject.getClass().getSimpleName()+" PATCH ["+ applicationObject.get_id()+"] request.");

            //get session from session_id
            Session session = SessionManager.getInstance().getUserSession(session_id);
            if(session==null)
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "patchBusinessObject()> no sessions associated with session ["+session_id+"] were found.");
                return new ResponseEntity<>("Not a valid session", HttpStatus.CONFLICT);
            }

            Employee employee = session.getEmployee();

            if(employee==null)
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "patchBusinessObject()> no employees associated with session ["+session_id+"] were found.");
                return new ResponseEntity<>("Not a valid session.", HttpStatus.CONFLICT);
            }

            //check if employee is authorised to update objects of this type
            if(employee.getAccess_level() < applicationObject.getWriteMinRequiredAccessLevel().getLevel())
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "patchBusinessObject()> employee ["+employee.getName()
                        +"]{current="+AccessLevel.values()[employee.getAccess_level()]+"} is not authorised to update " + applicationObject.getClass().getName() + "{required="+ applicationObject.getWriteMinRequiredAccessLevel()+"} objects.");
                return new ResponseEntity<>("You are not authorised to EDIT " + applicationObject.getClass().getSimpleName() + " objects. Minimum write requirement is " + applicationObject.getWriteMinRequiredAccessLevel(), HttpStatus.UNAUTHORIZED);
            }

            IO.log(APIController.class.getName(), IO.TAG_INFO, "patching "+ applicationObject.getClass().getName()+" ["+ applicationObject.get_id()+"]");

            try
            {
                RemoteComms.commitBusinessObjectToDatabase(applicationObject, collection, collection_timestamp);
                return new ResponseEntity<>(applicationObject.get_id(), HttpStatus.OK);
            } catch (InvalidBusinessObjectException e)
            {
                IO.log(RemoteComms.class.getName(),IO.TAG_ERROR, "invalid "+ applicationObject.getClass().getName()+" object: {"+e.getMessage()+"}");
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>("Invalid ApplicationObject", HttpStatus.CONFLICT);
    }

    public ResponseEntity<String> deleteBusinessObject(ApplicationObject object,
                                                       String session_id,
                                                       String collection,
                                                       String collection_timestamp)
    {
        if(object!=null)
        {
            if(object.get_id()==null)
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "ApplicationObject "+object.getClass().getName()+" does NOT have a valid ObjectId.");
                return new ResponseEntity<>("invalid "+object.getClass().getSimpleName()+" id. Please fix this before trying to delete this object", HttpStatus.CONFLICT);
            }

            IO.log(object.getClass().getName(), IO.TAG_VERBOSE, "\nhandling "+object.getClass().getSimpleName()+" DELETE ["+object.get_id()+"] request.");

            if(session_id==null)
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "deleteBusinessObject()> not a valid session");
                return new ResponseEntity<>("Not a valid session", HttpStatus.CONFLICT);
            }

            //get session from session_id
            Session session = SessionManager.getInstance().getUserSession(session_id);
            if(session==null)
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "deleteBusinessObject()> no sessions associated with session ["+session_id+"] were found.");
                return new ResponseEntity<>("Not a valid session", HttpStatus.CONFLICT);
            }

            Employee employee = session.getEmployee();

            if(employee==null)
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "deleteBusinessObject()> no employees associated with session ["+session_id+"] were found.");
                return new ResponseEntity<>("Not a valid session.", HttpStatus.CONFLICT);
            }

            //check if employee is authorised to delete objects of this type
            if(employee.getAccess_level() < object.getWriteMinRequiredAccessLevel().getLevel())
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "deleteBusinessObject()> employee ["+employee.getName()
                        +"]{current="+AccessLevel.values()[employee.getAccess_level()]+"} is not authorised to delete " + object.getClass().getName() + "{required="+object.getWriteMinRequiredAccessLevel()+"} objects.");
                return new ResponseEntity<>("You are not authorised to DELETE " + object.getClass().getSimpleName() + " objects. Minimum write requirement is " + object.getWriteMinRequiredAccessLevel(), HttpStatus.UNAUTHORIZED);
            }

            IO.log(APIController.class.getName(), IO.TAG_INFO, "deleteBusinessObject()> attempting to DELETE ApplicationObject ["+object.get_id()+"] from collection ["+collection+"]");

            //delete ApplicationObject from DB server
            if(collection!=null)
            {
                IO.getInstance().mongoOperations().remove(new Query(Criteria.where("_id").is(object.get_id())), object.getClass(), collection);
                IO.log(APIController.class.getName(),IO.TAG_INFO, "deleteBusinessObject()> DELETED ApplicationObject: ["+object.get_id()+"]");
            } else
            {
                IO.log(APIController.class.getName(),IO.TAG_ERROR, "deleteBusinessObject()> Could NOT DELETE ApplicationObject: ["+object.get_id()+"] due to an invalid collection name.");
                return null;
            }

            //update respective timestamp
            if(collection_timestamp!=null)
            {
                IO.log(APIController.class.getName(),IO.TAG_INFO, "deleteBusinessObject()> updating collection ["+collection+"]'s timestamp ["+collection_timestamp+"]");
                CounterController.commitCounter(new Counter(collection_timestamp, System.currentTimeMillis()));
            } else
            {
                IO.log(APIController.class.getName(),IO.TAG_WARN, "deleteBusinessObject()> did not find any timestamp to update for collection ["+collection+"]");
                return null;
            }
            return new ResponseEntity<>(object.get_id(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid ApplicationObject", HttpStatus.CONFLICT);
    }
}