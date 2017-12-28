package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ghost on 2017/01/21.
 */
public class Overtime extends BusinessObject
{
    private String usr;
    private String job_id;
    private long date;
    private long time_in;
    private long time_out;
    private int status;
    public static final String TAG = "Overtime";
    public static final int STATUS_PENDING =0;
    public static final int STATUS_APPROVED =1;
    public static final int STATUS_ARCHIVED =2;

    public StringProperty usrProperty(){return new SimpleStringProperty(getUsr());}

    public String getUsr()
    {
        return usr;
    }

    public void setUsr(String usr)
    {
        this.usr = usr;
    }

    public String getJob_id()
    {
        return job_id;
    }

    public void setJob_id(String job_id)
    {
        this.job_id = job_id;
    }

    public long getDate()
    {
        return date;
    }

    public void setDate(long date)
    {
        this.date = date;
    }

    public long getTime_in()
    {
        return time_in;
    }

    public void setTime_in(long time_in)
    {
        this.time_in = time_in;
    }

    public long getTime_out()
    {
        return time_out;
    }

    public void setTime_out(long time_out)
    {
        this.time_out = time_out;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status= status;
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
        if(getDate()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date value.");
            return false;
        }
        if(getTime_in()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid time_in value.");
            return false;
        }
        if(getTime_out()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid time_out value.");
            return false;
        }
        if(getStatus()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid status value.");
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
                case "usr":
                    setUsr(String.valueOf(val));
                    break;
                case "job_id":
                    setJob_id(String.valueOf(val));
                    break;
                case "date":
                    setDate(Long.valueOf(String.valueOf(val)));
                    break;
                case "date_logged":
                    setDate_logged(Long.parseLong(String.valueOf(val)));
                    break;
                case "time_in":
                    setTime_in(Long.parseLong(String.valueOf(val)));
                    break;
                case "time_out":
                    setTime_out(Long.parseLong(String.valueOf(val)));
                    break;
                case "status":
                    setStatus(Integer.parseInt(String.valueOf(val)));
                    break;
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "Unknown "+getClass().getName()+" attribute '" + var + "'.");
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
            case "usr":
                return getUsr();
            case "job_id":
                return getJob_id();
            case "date":
                return getDate();
            case "time_in":
                return getTime_in();
            case "time_out":
                return getTime_out();
            case "status":
                return getStatus();
        }
        return super.get(var);
    }
}