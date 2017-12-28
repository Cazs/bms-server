/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import org.springframework.data.rest.core.annotation.RestResource;
import server.auxilary.IO;

/**
 *
 * @author ghost
 */
public class Job extends BusinessObject
{
    private long planned_start_date;
    private long date_assigned;
    private long date_started;
    private long date_completed;
    private long job_number;
    private String invoice_id;
    private String quote_id;
    private boolean signed;
    private String signed_job;
    private Employee[] assigned_employees;
    private FileMetadata[] safety_catalogue;

    public long getPlanned_start_date() {return planned_start_date;}

    public void setPlanned_start_date(long planned_start_date) {this.planned_start_date = planned_start_date;}

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

    public void setDate_started(long date_started) 
    {
        this.date_started = date_started;
    }

    public long getDate_completed() 
    {
        return date_completed;
    }

    public void setDate_completed(long date_completed) 
    {
        this.date_completed = date_completed;
    }

    public boolean isJob_completed()
    {
        return (date_completed>0);
    }

    public void setSigned(boolean signed)
    {
        this.signed=signed;
    }

    public boolean isSigned()
    {
        return signed;
    }

    /**
     * @return base64 encoded string of signed Job
     */
    public String getSigned_job()
    {
        return signed_job;
    }

    /**
     * @param signed_job Base64 representation of signed Job
     */
    public void setSigned_job(String signed_job)
    {
        this.signed_job = signed_job;
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

    @Override
    @RestResource(exported = false)
    public boolean isValid()
    {
        super.isValid();
        if(getJob_number()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid job_number value.");
            return false;
        }
        if(getQuote_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid quote_id value.");
            return false;
        }
        if(getCreator()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid creator value.");
            return false;
        }
        if(getDate_logged()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_logged value.");
            return false;
        }
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

        IO.log(getClass().getName(), IO.TAG_INFO,  "valid " + getClass().getName() + " object.");
        return true;
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
                case "signed":
                    signed = (Boolean) val;
                    break;
                case "signed_job":
                    signed_job = (String) val;
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
                case "assigned_employees":
                    if(val!=null)
                        assigned_employees = (Employee[]) val;
                    else IO.log(getClass().getName(), IO.TAG_WARN, "value to be casted to Employee[] is null.");
                    break;
                case "safety_catalogue":
                    if(val!=null)
                        safety_catalogue = (FileMetadata[]) val;
                    else IO.log(getClass().getName(), IO.TAG_WARN, "value to be casted to FileMetadata[] is null.");
                    break;
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "unknown Job attribute '" + var + "'.");
                    break;
            }
        }catch (NumberFormatException e)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, e.getMessage());
        }
    }

    @Override
    public Object get(String var)
    {
        switch (var.toLowerCase())
        {
            case "job_number":
                return getJob_number();
            case "quote_id":
                return getQuote_id();
            case "signed":
                return isSigned();
            case "signed_job":
                return getSigned_job();
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
}