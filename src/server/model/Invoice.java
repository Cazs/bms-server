/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 * @author ghost
 */
public class Invoice extends BusinessObject
{
    private String job_id;
    private double receivable;

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
    public boolean isValid()
    {
        if(getJob_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid job_id value.");
            return false;
        }
        if(getCreator()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid creator value.");
            return false;
        }
        if(getReceivable()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid receivable value.");
            return false;
        }

        IO.log(getClass().getName(), IO.TAG_INFO,  "valid " + getClass().getName() + " object.");
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
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "unknown "+getClass().getName()+" attribute '" + var + "'.");
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
        Object val = super.get(var);
        if(val==null)
        {
            switch (var.toLowerCase())
            {
                case "job_id":
                    return getJob_id();
                case "receivable":
                    return getReceivable();
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "unknown " + getClass()
                            .getName() + " attribute '" + var + "'.");
                    return null;
            }
        } else return val;
    }
}
