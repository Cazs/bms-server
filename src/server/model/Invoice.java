/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by ghost on 2017/12/23.
 * @author ghost
 */
public class Invoice extends BusinessObject
{
    private String job_id;
    private double receivable;
    private String quote_revision_numbers;
    private int status;

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

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
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
                case "status":
                    status = Integer.parseInt(String.valueOf(val));
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
            case "status":
                return getStatus();
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

    @Override
    public String apiEndpoint()
    {
        return "/invoices";
    }
}
