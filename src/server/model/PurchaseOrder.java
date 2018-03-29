package server.model;

import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by th3gh0st on 2017/12/23.
 * @author th3gh0st
 */
public class PurchaseOrder extends ApplicationObject
{
    private int po_number;
    private String supplier_id;
    private String contact_person_id;
    private double vat;
    private String account_name;
    private int status;
    public static final String TAG = "PurchaseOrder";

    public PurchaseOrder()
    {}

    public PurchaseOrder(String _id)
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
        return AccessLevel.ADMIN;
    }

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
    public String[] isValid()
    {
        if(getSupplier_id()==null)
            return new String[]{"false", "invalid supplier_id value."};
        if(getContact_person_id()==null)
            return new String[]{"false", "invalid contact_person_id value."};
        if(getAccount_name()==null)
            return new String[]{"false", "invalid account_name value."};
        if(getVat()<0)
            return new String[]{"false", "invalid vat value."};
        if(getPo_number()<0)
            return new String[]{"false", "invalid po_number value."};
        /*if(getStatus()<0)
            return new String[]{"false", "invalid status value."};*/

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
                    setStatus(Integer.valueOf(String.valueOf(val)));
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

    @Override
    public String toString()
    {
        return super.toString() + " to supplier "  + getSupplier_id();
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/purchaseorder";
    }
}