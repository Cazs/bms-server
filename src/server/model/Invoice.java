/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import server.auxilary.AccessLevel;
import server.auxilary.IO;

import java.time.LocalDateTime;
import java.util.AbstractMap;

/**
 * Created by th3gh0st on 2017/12/23.
 * @author th3gh0st
 */
public class Invoice extends ApplicationObject
{
    private String job_id;
    private double receivable;
    private String quote_revision_numbers;

    public Invoice()
    {}

    public Invoice(String _id)
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

    public String getQuote_revision_numbers()
    {
        return quote_revision_numbers;
    }

    public void setQuote_revision_numbers(String quote_revision_numbers)
    {
        this.quote_revision_numbers = quote_revision_numbers;
    }

    public double getReceivable()
    {
        return receivable;
    }

    public void setReceivable(double receivable)
    {
        this.receivable = receivable;
    }

    public String getJob_id()
    {
        return job_id;
    }

    public void setJob_id(String job_id)
    {
        this.job_id = job_id;
    }

    public Job getJob()
    {
        return IO.getInstance().mongoOperations().findOne(new Query(Criteria.where("_id").is(getJob_id())), Job.class, "jobs");
    }

    public String getClient_name()
    {
        Job job = getJob();
        if(job!=null)
            return job.getClient_name();
        return "N/A";
    }

    public String getContact_person()
    {
        Job job = getJob();
        if(job!=null)
            return job.getContact_person();
        return "N/A";
    }

    public String getRequest()
    {
        Job job = getJob();
        if(job!=null)
            return job.getRequest();
        return "N/A";
    }

    public String getSitename()
    {
        Job job = getJob();
        if(job!=null)
            return job.getSitename();
        return "N/A";
    }

    public long getJob_number()
    {
        Job job = getJob();
        if(job!=null)
            return job.getObject_number();
        return -1;
    }

    public String getStart_date()
    {
        Job job = getJob();
        if(job!=null)
            return job.getStart_date();
        return "1970-01-01";
    }

    public String getEnd_date()
    {
        Job job = getJob();
        if(job!=null)
            return job.getEnd_date();
        return "1970-01-01";
    }

    public String getScheduled_date()
    {
        Job job = getJob();
        if(job!=null)
            return job.getScheduled_date();
        return "1970-01-01";
    }

    public double getVat()
    {
        Job job = getJob();
        if(job!=null)
            return job.getVat();
        return Quote.VAT;
    }

    public long getQuote_number()
    {
        Job job = getJob();
        if(job!=null)
        {
            Quote quote = job.getQuote();
            if(quote!=null)
                return quote.getObject_number();
        }
        return -1;
    }

    @Override
    public String[] isValid()
    {
        if(getJob_id()==null)
            return new String[]{"false", "invalid job_id value."};
        if(getReceivable()<0)
            return new String[]{"false", "invalid receivable value."};
        if(getStatus()<0)
            return new String[]{"false", "invalid status value."};

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
                case "job_id":
                    setJob_id(String.valueOf(val));
                    break;
                case "receivable":
                    setReceivable(Double.valueOf(String.valueOf(val)));
                    break;
                case "quote_revision_numbers":
                    setQuote_revision_numbers(String.valueOf(val));
                    break;
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "unknown "+getClass().getName()+" attribute '" + var + "'.");
                    break;
            }
        } catch (NumberFormatException e)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, e.getMessage());
        }
    }

    @Override
    public Object get(String var)
    {
        switch (var.toLowerCase())
        {
            case "job_id":
                return getJob_id();
            case "quote_revision_numbers":
                return getQuote_revision_numbers();
            case "receivable":
                return getReceivable();
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return super.toString() + " = for job "  + getJob_id();
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/invoice";
    }
}
