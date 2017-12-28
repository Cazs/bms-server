package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ghost on 2017/01/21.
 */
public class Revenue extends BusinessObject
{
    private String revenue_title;
    private String revenue_description;
    private double revenue_value;
    private String account_name;
    public static final String TAG = "Revenue";

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

    public String getAccount_name()
    {
        return account_name;
    }

    public void setAccount_name(String account_name)
    {
        this.account_name = account_name;
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
                case "revenue_title":
                    revenue_title = (String)val;
                    break;
                case "revenue_description":
                    revenue_description = (String)val;
                    break;
                case "revenue_value":
                    revenue_value = Double.parseDouble(String.valueOf(val));
                    break;
                case "account_name":
                    account_name = String.valueOf(val);
                    break;
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR,"unknown "+getClass().getName()+" attribute '" + var + "'.");
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
            case "revenue_title":
                return revenue_title;
            case "revenue_description":
                return revenue_description;
            case "revenue_value":
                return revenue_value;
            case "account_name":
                return account_name;
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return this.revenue_title;
    }
}
