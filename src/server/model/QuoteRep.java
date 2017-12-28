package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ghost on 2017/01/29.
 */
public class QuoteRep extends BusinessObject
{
    private String quote_id;
    private String usr;
    public static final String TAG = "QuoteRepresentative";

    public String getQuote_id()
    {
        return quote_id;
    }

    public void setQuote_id(String quote_id)
    {
        this.quote_id = quote_id;
    }

    public String getUsr()
    {
        return usr;
    }

    public void setUsr(String employee_id)
    {
        this.usr = employee_id;
    }

    @Override
    public boolean isValid()
    {
        if(getUsr()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid usr value.");
            return false;
        }
        if(getQuote_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid quote_id value.");
            return false;
        }

        IO.log(getClass().getName(), IO.TAG_INFO,  "valid " + getClass().getName() + " object.");
        return super.isValid();
    }

    @Override
    public void parse(String var, Object val)
    {
        try
        {
            switch (var.toLowerCase())
            {
                case "quote_id":
                    quote_id = String.valueOf(val);
                    break;
                case "usr":
                    usr = String.valueOf(val);
                    break;
                default:
                    System.err.println("Unknown "+getClass().getName()+" attribute '" + var + "'.");
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
            case "quote_id":
                return quote_id;
            case "usr":
                return usr;
            default:
                IO.log(getClass().getName(), IO.TAG_ERROR, "Unknown "+getClass().getName()+" attribute '" + var + "'.");
                return null;
        }
    }
}