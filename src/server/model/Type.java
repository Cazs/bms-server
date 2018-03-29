package server.model;

import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by ghost on 2017/12/23.
 * @author ghost
 */
public abstract class Type extends BusinessObject
{
    private String type_name;
    private String type_description;

    public Type()
    {}

    public Type(String _id)
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
        return AccessLevel.STANDARD;
    }

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

    public void setType_description(String type_description)
    {
        this.type_description = type_description;
    }

    @Override
    public String[] isValid()
    {
        if(getType_name()==null)
            return new String[]{"false", "invalid type_name value."};
        if(getType_description()==null)
            return new String[]{"false", "invalid type_description value."};

        return super.isValid();
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
        return super.toString() + " = "  + getType_name();
    }
}
