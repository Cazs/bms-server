package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ghost on 2017/01/21.
 */
public class Quote extends BusinessObject
{
    private String client_id;
    private String contact_person_id;
    private String parent_id;
    private String sitename;
    private String request;
    private double vat;
    private String account_name;
    private double revision;
    private int status;
    public static final String TAG = "Quote";
    public static final int STATUS_PENDING =0;
    public static final int STATUS_APPROVED =1;
    public static final int STATUS_ARCHIVED =2;

    public String getClient_id()
    {
        return client_id;
    }

    public void setClient_id(String client_id)
    {
        this.client_id = client_id;
    }

    public String getContact_person_id()
    {
        return contact_person_id;
    }

    public void setContact_person_id(String contact_person_id)
    {
        this.contact_person_id = contact_person_id;
    }

    public String getSitename()
    {
        return sitename;
    }

    public void setSitename(String sitename)
    {
        this.sitename = sitename;
    }

    public String getRequest()
    {
        return request;
    }

    public void setRequest(String request)
    {
        this.request = request;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public double getVat()
    {
        return vat;
    }

    public void setVat(double vat)
    {
        this.vat = vat;
    }

    public String getAccount_name()
    {
        return account_name;
    }

    public void setAccount_name(String account_name)
    {
        this.account_name = account_name;
    }

    public String getParent_id(){return this.parent_id;}

    public void setParent_id(String parent_id)
    {
        this.parent_id = parent_id;
    }

    public double getRevision()
    {
        return revision;
    }

    public void setRevision(double revision)
    {
        this.revision = revision;
    }

    @Override
    public boolean isValid()
    {
        if(getClient_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid client_id value.");
            return false;
        }
        /*if(getParent_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid parent_id value.");
            return false;
        }*/
        if(getRequest()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid request value.");
            return false;
        }
        if(getSitename()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid sitename value.");
            return false;
        }
        if(getContact_person_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid contact_person_id value.");
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
        if(getVat()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid VAT value.");
            return false;
        }
        if(getStatus()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid status value.");
            return false;
        }
        if(getRevision()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid revision value.");
            return false;
        }

        IO.log(getClass().getName(), IO.TAG_INFO,  "valid " + getClass().getName() + " object.");
        return super.isValid();
    }

    @Override
    public String toString()
    {
        return get_id();
    }

    @Override
    public void parse(String var, Object val)
    {
        super.parse(var, val);
        try
        {
            switch (var.toLowerCase())
            {
                case "client_id":
                    client_id = (String)val;
                    break;
                case "contact_person_id":
                    contact_person_id = (String)val;
                    break;
                case "sitename":
                    sitename = String.valueOf(val);
                    break;
                case "request":
                    request = String.valueOf(val);
                    break;
                case "status":
                    status = Integer.parseInt(String.valueOf(val));
                    break;
                case "parent_id":
                    parent_id = String.valueOf(val);
                    break;
                case "revision":
                    revision = Integer.parseInt(String.valueOf(val));
                    break;
                case "vat":
                    vat = Double.parseDouble(String.valueOf(val));
                    break;
                case "account_name":
                    account_name = String.valueOf(val);
                    break;
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "Unknown "+getClass().getName()+" attribute '" + var + "'.");
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
            case "client_id":
                return client_id;
            case "contact_person_id":
                return contact_person_id;
            case "sitename":
                return sitename;
            case "request":
                return request;
            case "status":
                return status;
            case "parent_id":
                return parent_id;
            case "vat":
                return vat;
            case "account_name":
                return account_name;
            case "revision":
                return revision;
        }
        return super.get(var);
    }
}
