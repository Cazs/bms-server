package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ghost on 2017/02/03.
 */
public class JobEmployee extends BusinessObject
{
    private String job_id;
    private String usr;
    public static final String TAG = "JobEmployee";

    public String getJob_id()
    {
        return job_id;
    }

    public void setJob_id(String job_id)
    {
        this.job_id = job_id;
    }

    public String getUsr()
    {
        return usr;
    }

    public void setUsr(String usr)
    {
        this.usr = usr;
    }

    @Override
    public boolean isValid()
    {
        if(getUsr()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid usr value.");
            return false;
        }
        if(getJob_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid job_id value.");
            return false;
        }
        if(getDate_logged()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_logged value.");
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
                    job_id = String.valueOf(val);
                    break;
                case "usr":
                    usr = String.valueOf(val);
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
                return job_id;
            case "usr":
                return usr;
        }
        return super.get(var);
    }
}
