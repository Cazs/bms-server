/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.rest.core.annotation.RestResource;
import server.auxilary.AccessLevel;
import server.auxilary.IO;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;

/**
 * Created by th3gh0st on 2017/12/23.
 * @author th3gh0st
 */
public class Job extends ApplicationObject
{
    private long planned_start_date;
    private long date_assigned;
    private long date_started;
    private long date_completed;
    private long job_number;
    private String invoice_id;
    private String quote_id;

    public Job()
    {}

    public Job(String _id)
    {
        super(_id);
    }

    @Override
    public AccessLevel getReadMinRequiredAccessLevel()
    {
        return AccessLevel.STANDARD;
    }

    @Override
    public AccessLevel getWriteMinRequiredAccessLevel()
    {
        return AccessLevel.ADMIN;
    }

    public long getPlanned_start_date() {return planned_start_date;}

    public void setPlanned_start_date(long planned_start_date) {this.planned_start_date = planned_start_date;}

    public String getScheduled_date()
    {
        // return date.format(DateTimeFormatter.BASIC_ISO_DATE);
        AbstractMap.SimpleEntry<Integer, LocalDateTime> date_map = IO.isEpochSecondOrMilli(getPlanned_start_date());
        LocalDateTime date = date_map.getValue();

        return IO.getYyyyMMddFormmattedDate(date);
    }

    public long getJob_number()
    {
        return job_number;
    }

    public void setJob_number(long job_number)
    {
        this.job_number = job_number;
    }

    public long getDate_assigned() 
    {
        return date_assigned;
    }

    public void setDate_assigned(long date_assigned) 
    {
        this.date_assigned = date_assigned;
    }

    public long getDate_started() 
    {
        return date_started;
    }

    public String getStart_date()
    {
        // return date.format(DateTimeFormatter.BASIC_ISO_DATE);
        AbstractMap.SimpleEntry<Integer, LocalDateTime> date_map = IO.isEpochSecondOrMilli(getDate_started());
        LocalDateTime date = date_map.getValue();

        return IO.getYyyyMMddFormmattedDate(date);
    }

    public void setDate_started(long date_started) 
    {
        this.date_started = date_started;
    }

    public long getDate_completed() 
    {
        return date_completed;
    }

    public String getEnd_date()
    {
        // return date.format(DateTimeFormatter.BASIC_ISO_DATE);
        AbstractMap.SimpleEntry<Integer, LocalDateTime> date_map = IO.isEpochSecondOrMilli(getDate_completed());
        LocalDateTime date = date_map.getValue();

        return IO.getYyyyMMddFormmattedDate(date);
    }

    public void setDate_completed(long date_completed) 
    {
        this.date_completed = date_completed;
    }

    public boolean isJob_completed()
    {
        return (date_completed>0);
    }

    public String getQuote_id()
    {
        return quote_id;
    }

    public void setQuote_id(String quote_id)
    {
        this.quote_id = quote_id;
    }

    public String getInvoice_id() 
    {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) 
    {
        this.invoice_id = invoice_id;
    }

    public Quote getQuote()
    {
        return IO.getInstance().mongoOperations().findOne(new Query(Criteria.where("_id").is(getQuote_id())), Quote.class, "quotes");
    }

    public String getClient_name()
    {
        Quote quote = getQuote();
        if(quote!=null)
        {
            Client client = quote.getClient();
            if(client!=null)
                return client.getClient_name();
        }
        return "N/A";
    }

    public String getContact_person()
    {
        Quote quote = getQuote();
        if(quote!=null)
        {
            Employee contact = quote.getContact();
            if(contact!=null)
                return contact.getName();
            else return quote.getContact_person_id();
        }
        return "N/A";
    }

    public String getRequest()
    {
        Quote quote = getQuote();
        if(quote!=null)
            return quote.getRequest();
        return "N/A";
    }

    public String getSitename()
    {
        Quote quote = getQuote();
        if(quote!=null)
            return quote.getSitename();
        return "N/A";
    }

    public double getVat()
    {
        Quote quote = getQuote();
        if(quote!=null)
            return quote.getVat();
        return Quote.VAT;
    }

    /**
     * @return latest revision number for associated Quote object
     */
    public double getQuote_revisions()
    {
        Quote quote = getQuote();
        if(quote!=null)
            return quote.getRevision();
        return 0;
    }

    public Task[] getTasks()
    {
        Task[] arr = new Task[0]; // for JS
        List contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("job_id").is(get_id())), Task.class, "tasks");
        if(contents!=null)
        {
            arr = new Task[contents.size()];
            contents.toArray(arr);
        }
        return arr;
    }

    @Override
    @RestResource(exported = false)
    public String[] isValid()
    {
        if(getJob_number()<0)
            return new String[]{"false", "invalid job_number value."};
        if(getQuote_id()==null)
            return new String[]{"false", "invalid quote_id value."};
        /*
        if(getInvoice_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid invoice_id value.");
            return false;
        }
        if(getDate_assigned()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_assigned value.");
            return false;
        }
        if(getPlanned_start_date()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid planned_start_date value.");
            return false;
        }
        if(getDate_started()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_started value.");
            return false;
        }
        if(getDate_completed()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_completed value.");
            return false;
        }*/
        return super.isValid();
    }

    @Override
    public void parse(String var, Object val)
    {
        super.parse(var, val);
        try
        {
            switch (var.toLowerCase())
            {
                case "quote_id":
                    quote_id = (String)val;
                    break;
                case "planned_start_date":
                    planned_start_date = Long.parseLong(String.valueOf(val));
                    break;
                case "date_assigned":
                    date_assigned = Long.parseLong(String.valueOf(val));
                    break;
                case "date_started":
                    date_started = Long.parseLong(String.valueOf(val));
                    break;
                case "date_completed":
                    date_completed = Long.parseLong(String.valueOf(val));
                    break;
                case "job_number":
                    job_number = Long.parseLong(String.valueOf(val));
                    break;
                case "invoice_id":
                    invoice_id = (String)val;
                    break;
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "unknown " +getClass().getName()+ " attribute '" + var + "'.");
                    break;
            }
        }catch (NumberFormatException e)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, e.getMessage());
        }
    }

    @Override
    public Object get(String var)//
    {
        switch (var.toLowerCase())
        {
            case "job_number":
                return getJob_number();
            case "quote_id":
                return getQuote_id();
            case "status":
                return getStatus();
            case "planned_start_date":
                return getPlanned_start_date();
            case "date_assigned":
                return getDate_assigned();
            case "date_started":
                return getDate_started();
            case "date_completed":
                return getDate_completed();
            case "invoice_id":
                return getInvoice_id();
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        String json_obj = "{\"_id\":\""+get_id()+"\"";
        json_obj+=",\"quote_id\":\""+quote_id+"\""
                +",\"status\":\""+getStatus()+"\""
                +",\"creator\":\""+getCreator()+"\""
                +",\"date_logged\":\""+getDate_logged()+"\"";
        if(date_assigned>0)
            json_obj+=",\"date_assigned\":\""+date_assigned+"\"";
        if(planned_start_date>0)
            json_obj+=",\"planned_start_date\":\""+planned_start_date+"\"";
        if(date_started>0)
            json_obj+=",\"date_started\":\""+date_started+"\"";
        if(date_completed>0)
            json_obj+=",\"date_completed\":\""+date_completed+"\"";
        if(invoice_id!=null)
            json_obj+=",\"invoice_id\":\""+invoice_id+"\"";
        json_obj+="}";

        IO.log(getClass().getName(),IO.TAG_INFO, json_obj);
        return json_obj;
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/job";
    }
}