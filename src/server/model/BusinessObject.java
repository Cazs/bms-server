package server.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.rest.core.annotation.RestResource;
import server.auxilary.IO;

import java.io.Serializable;

/**
 * Created by ghost on 2017/01/04.
 */
public abstract class BusinessObject implements Serializable
{
    @Id
    @RestResource(exported = true)
    private String _id;
    private long date_logged;
    private String creator;
    private String other;
    private boolean marked;

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public boolean isMarked()
    {
        return this.marked;
    }

    public void setMarked(boolean marked){this.marked=marked;}

    public long getDate_logged()
    {
        return this.date_logged;
    }

    public void setDate_logged(long date_logged)
    {
        this.date_logged = date_logged;
    }

    public String getCreator()
    {
        return this.creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public String getOther()
    {
        return other;
    }

    public void setOther(String other)
    {
        this.other = other;
    }

    public void parse(String var, Object val)
    {
        switch (var.toLowerCase())
        {
            case "date_logged":
                date_logged = Long.parseLong(String.valueOf(val));
                break;
            case "creator":
                creator = String.valueOf(val);
                break;
            case "other":
                other = String.valueOf(val);
                break;
            case "marked":
                marked = Boolean.valueOf((String) val);
                break;
        }
    }

    public Object get(String var)
    {
        switch (var.toLowerCase())
        {
            case "_id":
                return get_id();
            case "date_logged":
                return getDate_logged();
            case "creator":
                return getCreator();
            case "marked":
                return isMarked();
            case "other":
                return getOther();
            default:
                IO.log(getClass().getName(), IO.TAG_ERROR, "unknown "+getClass().getName()+" attribute '" + var + "'.");
                return null;
        }
    }

    public boolean isValid()
    {
        if(getDate_logged()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_logged value.");
            return false;
        }
        if(getCreator()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid creator value.");
            return false;
        }
        if(getCreator().isEmpty())
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid creator value.");
            return false;
        }
        return true;
    }

    /*public String[] isValid()
    {
        if(getDate_logged()<=0)
            return new String[]{"false", "invalid date_logged value."};
        if(getCreator()==null)
            return new String[]{"false", "invalid creator value."};
        if(getCreator().isEmpty())
        {
            //IO.log(getClass().getName(), IO.TAG_ERROR, "invalid creator value.");
            return new String[]{"false", "invalid creator value."};
        }
        return new String[]{"true", "valid "+getClass().getName()+" object."};
    }*/
    //TODO: public abstract String asURLEncodedString();//TODO: check if models comply
}