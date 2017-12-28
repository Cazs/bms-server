package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ghost on 2017/01/13.
 */
public class Type extends BusinessObject
{
    private String type_name;
    private String type_description;

    public String getType_name()
    {
        return type_name;
    }

    public void setType_name(String type_name)
    {
        this.type_name = type_name;
    }

    public String getType_description()
    {
        return type_description;
    }

    public void setType_description(String type_name)
    {
        this.type_description = type_description;
    }

    @Override
    public boolean isValid()
    {
        super.isValid();
        if(getType_name()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid type_name value.");
            return false;
        }
        if(getType_description()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid type_description value.");
            return false;
        }

        IO.log(getClass().getName(), IO.TAG_INFO,  "valid " + getClass().getName() + " object.");
        return true;
    }

    @Override
    public void parse(String var, Object val)
    {
        super.parse(var, val);
        switch (var.toLowerCase())
        {
            case "type_name":
                type_name = (String)val;
                break;
            case "type_description":
                type_description = (String)val;
                break;
            default:
                IO.log(getClass().getName(), "Unknown "+getClass().getName()+" attribute '" + var + "'.", IO.TAG_ERROR);
                break;
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
                case "type_name":
                    return type_name;
                case "type_description":
                    return type_description;
                default:
                    IO.log(getClass().getName(), "Unknown "+getClass().getName()+" attribute '" + var + "'.", IO.TAG_ERROR);
                    return null;
            }
        } else return val;
    }

    @Override
    public String toString()
    {
        return this.type_name;
    }
}