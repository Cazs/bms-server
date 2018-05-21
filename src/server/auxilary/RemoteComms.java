/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.auxilary;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import server.controllers.CounterController;
import server.exceptions.InvalidBusinessObjectException;
import server.model.ApplicationObject;
import server.model.Counter;
import server.model.Employee;
import server.model.Metafile;

/**
 * Contains behaviour to communicate with the database and email server
 * Created by ghost on 2017/12/23.
 * @author ghost
 */
public class RemoteComms
{
    public static String host = null;
    public static String DB_IP = null;
    public static int DB_PORT = 0;
    public static String DB_NAME = null;
    public static int TTL = 60*60*2; // 2 hours in sec
    public static String SYSTEM_EMAIL = null;
    public static String MAILJET_API_KEY = null;
    public static String MAILJET_API_KEY_SECRET = null;
    public static final String TAG = "RemoteComms";

    public static String commitBusinessObjectToDatabase(ApplicationObject applicationObject, String collection, String timestamp_name)
    {
        if(applicationObject !=null)
        {
            String[] is_valid = applicationObject.isValid();
            if(is_valid==null)
            {
                IO.log(RemoteComms.class.getName(),IO.TAG_INFO, "invalid isValid() response from ApplicationObject{"+ applicationObject.getClass().getName()+"}");
                return null;
            }
            if(is_valid.length!=2)
            {
                IO.log(RemoteComms.class.getName(),IO.TAG_INFO, "invalid isValid() response array length from ApplicationObject{"+ applicationObject.getClass().getName()+"}");
                return null;
            }
            if (is_valid[0].toLowerCase().equals("true"))
            {
                IO.log(RemoteComms.class.getName(),IO.TAG_INFO, is_valid[1]);//print message from isValid() call
                IO.log(RemoteComms.class.getName(),IO.TAG_INFO, "committing ApplicationObject{"+ applicationObject.getClass().getName()+"}: "+ applicationObject.toString());

                //commit ApplicationObject to DB server
                if(collection!=null)
                {
                    IO.getInstance().mongoOperations().save(applicationObject, collection);
                    IO.log(RemoteComms.class.getName(),IO.TAG_INFO, "committed ApplicationObject:{"+ applicationObject.getClass().getName()+"} ["+ applicationObject.getObject_number()+"]");

                    //get new object's ID by using object number
                    ApplicationObject new_obj = IO.getInstance().mongoOperations().findOne(new Query(Criteria.where("object_number").is(applicationObject.getObject_number())), applicationObject.getClass(), collection);
                    if(new_obj!=null)
                        applicationObject.set_id(new_obj.get_id());
                    else
                    {
                        IO.log(RemoteComms.class.getName(), IO.TAG_WARN, "could not find newly created object of type " + applicationObject.getClass() + " in the database.");
                        return null;
                    }
                } else
                {
                    IO.log(RemoteComms.class.getName(),IO.TAG_ERROR, "Could NOT commit ApplicationObject:{"+ applicationObject.getClass().getName()+"} ["+ applicationObject.get_id()+"] due to an invalid collection name.");
                    return null;
                }

                //update respective timestamp
                if(timestamp_name!=null)
                {
                    IO.log(RemoteComms.class.getName(),IO.TAG_INFO, "updating collection ["+collection+"]'s timestamp ["+timestamp_name+"]");
                    CounterController.commitCounter(new Counter(timestamp_name, System.currentTimeMillis()));
                } else
                {
                    IO.log(RemoteComms.class.getName(),IO.TAG_WARN, "Did not find any timestamp to update for collection ["+collection+"]");
                    return null;
                }
                return applicationObject.get_id();
            } else throw new InvalidBusinessObjectException(is_valid[1]);
        } else throw new InvalidBusinessObjectException("invalid[null] ApplicationObject.");
    }

    /**
     *
     * @param subject email subject
     * @param message email message
     * @param recipient_employees recipient Employees
     * @param fileMetadata email attachment files
     * @return Mailjet email send response object
     * @throws MailjetSocketTimeoutException
     * @throws MailjetException
     */
    public static MailjetResponse emailWithAttachment(String subject, String message, Employee[] recipient_employees, Metafile[] fileMetadata) throws MailjetSocketTimeoutException, MailjetException
    {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;

        //setup recipients
        JSONArray recipients = new JSONArray();
        for(Employee recipient:recipient_employees)
            recipients.put(new JSONObject()
                    .put("Email", recipient.getEmail())
                    .put("Name", recipient.getFirstname()+" "+recipient.getLastname()));

        //setup files to be emailed
        JSONArray files = new JSONArray();
        for(Metafile file: fileMetadata)
            files.put(new JSONObject()
                    .put("ContentType", file.getContent_type())
                    .put("Filename", file.getFilename().toLowerCase().replaceAll(" ", "-") + "."
                            + (file.getContent_type().split("/").length > 0 ? file.getContent_type().split("/")[1] : file.getContent_type()))
                    .put("Base64Content", file.getFile()));


        client = new MailjetClient(MAILJET_API_KEY, MAILJET_API_KEY_SECRET, new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", SYSTEM_EMAIL)
                                        .put("Name", "BMS"))
                                .put(Emailv31.Message.TO, recipients)
                                .put(Emailv31.Message.SUBJECT, subject)
                                //.put(Emailv31.Message.TEXTPART, "Dear passenger 1, welcome to Mailjet! May the delivery force be with you!")
                                .put(Emailv31.Message.HTMLPART, message)
                                .put(Emailv31.Message.ATTACHMENTS, files)));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
        return response;
    }

    /**
     *
     * @param subject email subject
     * @param message email message
     * @param recipient_addresses recipient email addresses
     * @param fileMetadata email attachment files
     * @return Mailjet email send response object
     * @throws MailjetSocketTimeoutException
     * @throws MailjetException
     */
    public static MailjetResponse emailWithAttachment(String subject, String message, String[] recipient_addresses, Metafile[] fileMetadata) throws MailjetSocketTimeoutException, MailjetException
    {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;

        System.out.println("Sending mail to: " + recipient_addresses[0] + " of " + recipient_addresses.length);

        //setup recipients
        JSONArray recipients_json = new JSONArray();
        for(String recipient:recipient_addresses)
        {
            recipients_json.put(new JSONObject()
                    .put("Email", recipient));
                    //.put("Name", recipient));//recipient.getFirstname()+" "+recipient.getLastname()
        }

        //setup files to be emailed
        JSONArray files = new JSONArray();
        for(Metafile file: fileMetadata)
            files.put(new JSONObject()
                    .put("ContentType", file.getContent_type())
                    .put("Filename", file.getFilename().toLowerCase().replaceAll(" ", "-") + "."
                                        + (file.getContent_type().split("/").length > 0 ? file.getContent_type().split("/")[1] : file.getContent_type()))
                    .put("Base64Content", file.getFile()));//"VGhpcyBpcyB5b3VyIGF0dGFjaGVkIGZpbGUhISEK"


        client = new MailjetClient(MAILJET_API_KEY, MAILJET_API_KEY_SECRET, new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", SYSTEM_EMAIL)
                                        .put("Name", "BMS"))
                                .put(Emailv31.Message.TO, recipients_json)
                                .put(Emailv31.Message.SUBJECT, subject)
                                //.put(Emailv31.Message.TEXTPART, "Dear passenger 1, welcome to Mailjet! May the delivery force be with you!")
                                .put(Emailv31.Message.HTMLPART, message)
                                .put(Emailv31.Message.ATTACHMENTS, files)));
        response = client.post(request);
        System.out.println("Sent request, response: " + response);
        IO.log(RemoteComms.class.getName(), IO.TAG_INFO, String.valueOf(response.getStatus()));
        IO.log(RemoteComms.class.getName(), IO.TAG_INFO, String.valueOf(response.getData()));
        return response;
    }
}
