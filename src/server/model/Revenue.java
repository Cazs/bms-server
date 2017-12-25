package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ghost on 2017/01/21.
 */
public class Revenue implements BusinessObject, Serializable
{
    @Id
    private String _id;
    private String revenue_title;
    private String revenue_description;
    private double revenue_value;
    private long date_logged;
    private String creator;
    private String account_name;
    private String other;
    private Employee creator_employee;
    private boolean marked;
    public static final String TAG = "Revenue";

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

    public String getRevenue_title()
    {
        return revenue_title;
    }

    public void setRevenue_title(String revenue_title)
    {
        this.revenue_title = revenue_title;
    }

    public String getRevenue_description()
    {
        return revenue_description;
    }

    public void setRevenue_description(String revenue_description)
    {
        this.revenue_description = revenue_description;
    }

    public double getRevenue_value()
    {
        return revenue_value;
    }

    public void setRevenue_value(double revenue_value)
    {
        this.revenue_value = revenue_value;
    }

    public long getDate_logged()
    {
        return date_logged;
    }

    public void setDate_logged(long date_logged)
    {
        this.date_logged = date_logged;
    }

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public String getAccount_name()
    {
        return account_name;
    }

    public void setAccount_name(String account_name)
    {
        this.account_name = account_name;
    }

    public String getOther()
    {
        return other;
    }

    public void setOther(String other)
    {
        this.other = other;
    }

    @Override
    public boolean isValid()
    {
        if(getRevenue_title()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid revenue_title value.");
            return false;
        }
        if(getRevenue_description()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid revenue_description value.");
            return false;
        }
        if(getRevenue_value()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid revenue_value value.");
            return false;
        }
        if(getCreator()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid creator value.");
            return false;
        }
        if(getAccount_name()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid account_name value.");
            return false;
        }
        if(getDate_logged()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_logged value.");
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
                case "revenue_title":
                    revenue_title = (String)val;
                    break;
                case "revenue_description":
                    revenue_description = (String)val;
                    break;
                case "revenue_value":
                    revenue_value = Double.parseDouble(String.valueOf(val));
                    break;
                case "date_logged":
                    date_logged = Long.parseLong(String.valueOf(val));
                    break;
                case "creator":
                    creator = String.valueOf(val);
                    break;
                case "account_name":
                    account_name = String.valueOf(val);
                    break;
                case "other":
                    other = String.valueOf(val);
                    break;
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR,"unknown Revenue attribute '" + var + "'.");
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
                return _id;
            case "revenue_title":
                return revenue_title;
            case "revenue_description":
                return revenue_description;
            case "revenue_value":
                return revenue_value;
            case "date_logged":
                return date_logged;
            case "creator":
                return creator;
            case "account_name":
                return account_name;
            case "other":
                return other;
            default:
                IO.log(getClass().getName(), IO.TAG_ERROR,"unknown Revenue attribute '" + var + "'.");
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
            result.append(URLEncoder.encode("revenue_title","UTF-8") + "="
                    + URLEncoder.encode(revenue_title, "UTF-8"));
            result.append("&" + URLEncoder.encode("revenue_description","UTF-8") + "="
                    + URLEncoder.encode(revenue_description, "UTF-8"));
            result.append("&" + URLEncoder.encode("revenue_value","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(revenue_value), "UTF-8"));
            if(date_logged>0)
                result.append("&" + URLEncoder.encode("date_logged","UTF-8") + "="
                        + URLEncoder.encode(String.valueOf(date_logged), "UTF-8"));
            result.append("&" + URLEncoder.encode("creator","UTF-8") + "="
                    + URLEncoder.encode(creator, "UTF-8"));
            result.append("&" + URLEncoder.encode("account_name","UTF-8") + "="
                    + URLEncoder.encode(account_name, "UTF-8"));
            if(other!=null)
                if(!other.isEmpty())
                    result.append("&" + URLEncoder.encode("other","UTF-8") + "="
                            + URLEncoder.encode(other, "UTF-8"));
            return result.toString();
        } catch (UnsupportedEncodingException e)
        {
            IO.log(TAG, IO.TAG_ERROR, e.getMessage());
        }
        return null;
    }

    @Override
    public String toString()
    {
        return this.revenue_title;
    }
}
