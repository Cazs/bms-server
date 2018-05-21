package server.model;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by th3gh0st on 2018/01/13.
 * @author th3gh0st
 */
public class Requisition extends ApplicationObject
{
    // private String client_id;
    private String supplier_id;
    private String contact_person_id;
    private String description;
    private String type;
    public static final String TAG = "Requisition";

    public Requisition()
    {}

    public Requisition(String _id)
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
        return AccessLevel.STANDARD;
    }

    /* public String getClient_id()
    {
        return client_id;
    }

    public void setClient_id(String client_id)
    {
        this.client_id = client_id;
    } */

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
        this.contact_person_id = contact_person_id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    /* public Client getClient()
    {
        return IO.getInstance().mongoOperations().findOne(new Query(Criteria.where("_id").is(getClient_id())), Client.class, "clients");
    }

    public String getClient_name()
    {
        Client client = getClient();
        if(client!=null)
            return client.getClient_name();
        else return getClient_id();
    }*/

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

    @Override
    public String[] isValid()
    {
        if(getSupplier_id()==null)
            return new String[]{"false", "invalid supplier_id value."};
        /*
            if(getClient_id()==null)
                return new String[]{"false", "invalid client_id value."};
            if(getParent_id()==null)
            {
                IO.log(getClass().getName(), IO.TAG_ERROR, "invalid parent_id value.");
                return false;
            }
        */

        /* if(getDescription()==null)
            return new String[]{"false", "invalid description value."};*/
        /* if(getType()==null)
            return new String[]{"false", "invalid requisition type value."}; */
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
                /*case "client_id":
                    client_id = (String)val;
                    break;*/
                case "supplier_id":
                    supplier_id = (String)val;
                    break;
                case "contact_person_id":
                    contact_person_id = (String)val;
                    break;
                case "type":
                    type = String.valueOf(val);
                    break;
                case "description":
                    description = String.valueOf(val);
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
            // case "client_id":
            //    return client_id;
            case "supplier_id":
                return supplier_id;
            case "contact_person_id":
            return contact_person_id;
            case "type":
                return type;
            case "description":
                return description;
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return super.toString() + " type [" + getType() + "] "  + getDescription() + " to supplier [" +getSupplier_id() + "]";
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/requisition";
    }
}
