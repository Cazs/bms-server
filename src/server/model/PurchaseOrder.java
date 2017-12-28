package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ghost on 2017/01/21.
 */
public class PurchaseOrder extends BusinessObject
{
    private int po_number;
    private String supplier_id;
    private String contact_person_id;
    private double vat;
    private String account_name;
    private int status;
    public static final String TAG = "PurchaseOrder";

    public int getPo_number()
    {
        return po_number;
    }

    public void setPo_number(int po_number)
    {
        this.po_number = po_number;
    }

    public double getVat()
    {
        return vat;
    }

    public void setVat(double vat)
    {
        this.vat= vat;
    }

    public String getSupplier_id()
    {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id)
    {
        this.supplier_id = supplier_id;
    }

    public String getContact_person_id()
    {
        return contact_person_id;
    }

    public void setContact_person_id(String contact_person_id)
    {
        this.contact_person_id=contact_person_id;
    }

    public String getAccount_name()
    {
        return account_name;
    }

    public void setAccount_name(String account_name)
    {
        this.account_name = account_name;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    @Override
    public boolean isValid()
    {
        if(getSupplier_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid supplier_id value.");
            return false;
        }
        if(getContact_person_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid contact_person_id value.");
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
        if(getVat()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid vat value.");
            return false;
        }
        if(getPo_number()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid po_number value.");
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
                case "po_number":
                    setPo_number(Integer.valueOf((String)val));
                    break;
                case "supplier_id":
                    setSupplier_id(String.valueOf(val));
                    break;
                case "contact_person_id":
                    setContact_person_id(String.valueOf(val));
                    break;
                case "vat":
                    setVat(Double.valueOf((String)val));
                    break;
                case "account_name":
                    setAccount_name((String)val);
                    break;
                case "date_logged":
                    setDate_logged(Long.valueOf((String)val));
                    break;
                case "status":
                    setStatus(Integer.valueOf((String)val));
                    break;
                case "creator":
                    setCreator((String)val);
                    break;
                default:
                    IO.log(TAG, IO.TAG_ERROR, "Unknown "+TAG+" attribute '" + var + "'.");
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
            case "po_number":
                return getPo_number();
            case "supplier_id":
                return getSupplier_id();
            case "contact_person_id":
                return getContact_person_id();
            case "vat":
                return getVat();
            case "account_name":
                return getAccount_name();
            case "date_logged":
                return getDate_logged();
            case "creator":
                return getCreator();
            case "status":
                return getStatus();
        }
        return super.get(var);
    }
}