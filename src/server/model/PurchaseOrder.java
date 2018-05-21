package server.model;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import server.auxilary.AccessLevel;
import server.auxilary.IO;

import java.util.List;

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

    public Supplier getSupplier()
    {
        return IO.getInstance().mongoOperations().findOne(new Query(Criteria.where("_id").is(getSupplier_id())), Supplier.class, "suppliers");
    }

    public String getSupplier_name()
    {
        Supplier supplier = getSupplier();
        if(supplier!=null)
            return supplier.getSupplier_name();
        else return getSupplier_id();
    }

    public Employee getContact()
    {
        return IO.getInstance().mongoOperations().findOne(new Query(Criteria.where("usr").is(getContact_person_id())), Employee.class, "employees");
    }

    public String getContact_person()
    {
        Employee person = getContact();
        if(person!=null)
            return person.getName();
        return getContact_person_id();
    }

    public PurchaseOrderItem[] getResources()
    {
        PurchaseOrderItem[] arr = new PurchaseOrderItem[0];
        List contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("purchase_order_id").is(get_id())), PurchaseOrderItem.class, "purchase_order_resources");
        if(contents!=null)
        {
            arr = new PurchaseOrderItem[contents.size()];
            contents.toArray(arr);
        }
        return arr;
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
        if(getStatus()<0)
            return new String[]{"false", "invalid status value."};

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