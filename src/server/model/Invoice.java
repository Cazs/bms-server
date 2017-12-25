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
public class Invoice implements BusinessObject, Serializable
{
    @Id
    private String _id;
    private String job_id;
    private String creator;
    private long date_generated;
    private String extra;
    private boolean marked;
    private double receivable;

    @Override
    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    @Override
    public boolean isMarked() { return marked;}

    @Override
    public void setMarked(boolean marked){this.marked=marked;}

    public long getDate_generated()
    {
        return date_generated;
    }

    public void setDate_generated(long date_generated)
    {
        this.date_generated = date_generated;
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

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public String getExtra()
    {
        return extra;
    }

    public void setExtra(String extra)
    {
        this.extra = extra;
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
        if(getDate_generated()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_generated value.");
            return false;
        }
        if(getReceivable()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid receivable value.");
            return false;
        }

        IO.log(getClass().getName(), IO.TAG_INFO,  "valid " + getClass().getName() + " object.");
        return true;
    }

    @Override
    public void parse(String var, Object val)
    {
        try
        {
            switch (var.toLowerCase())
            {
                case "date_generated":
                    setDate_generated(Long.parseLong(String.valueOf(val)));
                    break;
                case "job_id":
                    setJob_id(String.valueOf(val));
                    break;
                case "creator":
                    setCreator(String.valueOf(val));
                    break;
                case "receivable":
                    setReceivable(Double.valueOf(String.valueOf(val)));
                    break;
                case "extra":
                    setExtra(String.valueOf(val));
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
        switch (var.toLowerCase())
        {
            case "_id":
                return get_id();
            case "job_id":
                return getJob_id();
            case "date_generated":
                return getDate_generated();
            case "creator":
                return getCreator();
            case "receivable":
                return getReceivable();
            case "extra":
                return getExtra();
            default:
                IO.log(getClass().getName(), IO.TAG_ERROR, "unknown "+getClass().getName()+" attribute '" + var + "'.");
                return null;
        }
    }

    @Override
    public String asJSON()
    {
        //Return encoded URL parameters in UTF-8 charset
        StringBuilder result = new StringBuilder();
        try
        {
            /*result.append(URLEncoder.encode("quote_id","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(quote_id), "UTF-8"));*/
            result.append("&" + URLEncoder.encode("job_id","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(job_id), "UTF-8"));
            if(date_generated>0)
                result.append("&" + URLEncoder.encode("date_generated","UTF-8") + "="
                        + URLEncoder.encode(String.valueOf(date_generated), "UTF-8"));
            result.append("&" + URLEncoder.encode("creator","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(creator), "UTF-8"));
            result.append("&" + URLEncoder.encode("receivable","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(receivable), "UTF-8"));
            if(extra!=null)
                result.append(URLEncoder.encode("extra","UTF-8") + "="
                        + URLEncoder.encode(extra, "UTF-8") + "&");
            return result.toString();
        } catch (UnsupportedEncodingException e)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, e.getMessage());
        }
        return null;
    }
}
