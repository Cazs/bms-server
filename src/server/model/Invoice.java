/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import server.auxilary.IO;

/**
 *
 * @author ghost
 */
public class Invoice extends BusinessObject
{
    private String job_id;
    private double receivable;
    private String quote_revision_numbers;

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
                case "creator":
                    setCreator(String.valueOf(val));
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
        Object val = super.get(var);
        if(val==null)
        {
            switch (var.toLowerCase())
            {
                case "job_id":
                    return getJob_id();
                case "receivable":
                    return getReceivable();
                case "quote_revision_numbers":
                    return getQuote_revision_numbers();
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "unknown " + getClass()
                            .getName() + " attribute '" + var + "'.");
                    return null;
            }
        } else return val;
    }

    @Override
    public String apiEndpoint()
    {
        return "/invoices";
    }
}
