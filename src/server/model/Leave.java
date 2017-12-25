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
public class Leave implements BusinessObject, Serializable
{
    @Id
    private String _id;
    private String usr;
    private long start_date;
    private long end_date;
    private long return_date;
    private long date_logged;
    private int status;
    private String type;
    private String extra;
    private boolean marked;
    public static final String TAG = "Leave";
    public static String[] TYPES = {"ANNUAL", "SICK", "UNPAID", "FAMILY RESPONSIBILITY - See BCEA for definition"};
    public static final int STATUS_PENDING =0;
    public static final int STATUS_APPROVED =1;
    public static final int STATUS_ARCHIVED =2;

    /**
     * Function to get identifier of Quote object.
     * @return Quote identifier.
     */
    @Override
    public String get_id()
    {
        return _id;
    }

    /**
     * Method to assign identifier to this object.
     * @param _id identifier to be assigned to this object.
     */
    public void set_id(String _id)
    {
        this._id = _id;
    }

    @Override
    public boolean isMarked()
    {
        return marked;
    }

    @Override
    public void setMarked(boolean marked){this.marked=marked;}

    public String getUsr()
    {
        return usr;
    }

    public void setUsr(String usr)
    {
        this.usr = usr;
    }

    public long getStart_date()
    {
        return start_date;
    }

    public void setStart_date(long date)
    {
        this.start_date = date;
    }

    public long getEnd_date()
    {
        return end_date;
    }

    public void setEnd_date(long date)
    {
        this.end_date = date;
    }

    public long getReturn_date()
    {
        return return_date;
    }

    public void setReturn_date(long date)
    {
        this.return_date = date;
    }

    public long getDate_logged()
    {
        return date_logged;
    }

    public void setDate_logged(long date_logged)
    {
        this.date_logged = date_logged;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status= status;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
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
        if(getUsr()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid usr value.");
            return false;
        }
        if(getType()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid type value.");
            return false;
        }
        if(getDate_logged()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_logged value.");
            return false;
        }
        if(getStart_date()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid start_date value.");
            return false;
        }
        if(getEnd_date()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid end_date value.");
            return false;
        }
        if(getReturn_date()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid return_date value.");
            return false;
        }
        if(getStatus()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid status value.");
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
                case "usr":
                    setUsr(String.valueOf(val));
                    break;
                case "start_date":
                    setStart_date(Long.valueOf(String.valueOf(val)));
                    break;
                case "end_date":
                    setEnd_date(Long.parseLong(String.valueOf(val)));
                    break;
                case "return_date":
                    setReturn_date(Long.parseLong(String.valueOf(val)));
                    break;
                case "status":
                    setStatus(Integer.parseInt(String.valueOf(val)));
                    break;
                case "type":
                    setType((String)val);
                    break;
                case "extra":
                    setExtra((String)val);
                    break;
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "Unknown Leave attribute '" + var + "'.");
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
            case "start_date":
                return getStart_date();
            case "end_date":
                return getEnd_date();
            case "return_date":
                return getReturn_date();
            case "date_logged":
                return getDate_logged();
            case "status":
                return getStatus();
            case "type":
                return getType();
            case "extra":
                return getExtra();
            default:
                IO.log(TAG, IO.TAG_ERROR, "Unknown Leave attribute '" + var + "'.");
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
            result.append(URLEncoder.encode("usr","UTF-8") + "="
                    + URLEncoder.encode(usr, "UTF-8"));
            result.append("&" + URLEncoder.encode("type","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(getType()), "UTF-8"));
            if(getStatus()>0)
                result.append("&" + URLEncoder.encode("status","UTF-8") + "="
                        + URLEncoder.encode(String.valueOf(getStatus()), "UTF-8"));
            if(getStart_date()>0)
                result.append("&" + URLEncoder.encode("start_date","UTF-8") + "="
                        + URLEncoder.encode(String.valueOf(getStart_date()), "UTF-8"));
            if(getEnd_date()>0)
                result.append("&" + URLEncoder.encode("end_date","UTF-8") + "="
                        + URLEncoder.encode(String.valueOf(getEnd_date()), "UTF-8"));
            if(getReturn_date()>0)
                result.append("&" + URLEncoder.encode("return_date","UTF-8") + "="
                        + URLEncoder.encode(String.valueOf(getReturn_date()), "UTF-8"));
            if(getDate_logged()>0)
                result.append("&" + URLEncoder.encode("date_logged","UTF-8") + "="
                        + URLEncoder.encode(String.valueOf(getDate_logged()), "UTF-8"));
            if(getExtra()!=null)
                if(!getExtra().isEmpty())
                    result.append("&" + URLEncoder.encode("extra","UTF-8") + "="
                            + URLEncoder.encode(getExtra(), "UTF-8"));
            return result.toString();
        } catch (UnsupportedEncodingException e)
        {
            IO.log(TAG, IO.TAG_ERROR, e.getMessage());
        }
        return null;
    }
}