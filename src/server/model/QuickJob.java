package server.model;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import server.auxilary.AccessLevel;
import server.auxilary.IO;

import java.util.List;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */
public class QuickJob extends ApplicationObject
{
    private String requisition_id;//optional
    private String client_id;
    private String contact_person_id;
    private String sitename;
    private String request;
    private double vat;
    private String account_name;
    public static final String TAG = "Quote";

    public QuickJob()
    {}

    public QuickJob(String _id)
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

    public QuickJobItem[] getResources()
    {
        QuickJobItem[] arr = null;
        List contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("quickjob_id").is(get_id())), QuoteItem.class, "quickjob_resources");
        if(contents!=null)
        {
            arr = new QuickJobItem[contents.size()];
            contents.toArray(arr);
        }
        return arr;
    }

    public Client getClient()
    {
        return IO.getInstance().mongoOperations().findOne(new Query(Criteria.where("_id").is(getClient_id())), Client.class, "clients");
    }

    public String getClient_name()
    {
        Client client = getClient();
        if(client!=null)
            return client.getClient_name();
        else return getClient_id();
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
        if(getClient_id()==null)
            return new String[]{"false", "invalid client_id value."};
        /*if(getParent_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid parent_id value.");
            return false;
        }*/
        if(getRequest()==null)
            return new String[]{"false", "invalid request value."};
        if(getSitename()==null)
            return new String[]{"false", "invalid sitename value."};
        if(getContact_person_id()==null)
            return new String[]{"false", "invalid contact_person_id value."};
        if(getCreator()==null)
            return new String[]{"false", "invalid creator value."};
        if(getAccount_name()==null)
            return new String[]{"false", "invalid account_name value."};
        if(getVat()<0)
            return new String[]{"false", "invalid VAT value."};
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
            case "vat":
                return vat;
            case "account_name":
                return account_name;
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return super.toString() + " for client "  + getClient_id() + " at " + getSitename();
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/quickjob";
    }
}
