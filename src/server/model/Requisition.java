package server.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by ghost on 2018/01/13.
 * @author ghost
 */
public class Requisition extends BusinessObject
{
    private String client_id;
    private String responsible_person_id;
    private String description;
    private String type;
    private int status;
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

    public StringProperty client_idProperty(){return new SimpleStringProperty(client_id);}

    public String getClient_id()
    {
        return client_id;
    }

    public void setClient_id(String client_id)
    {
        this.client_id = client_id;
    }

    public StringProperty responsible_person_idProperty(){return new SimpleStringProperty(responsible_person_id);}

    public String getResponsible_person_id()
    {
        return responsible_person_id;
    }

    public void setResponsible_person_id(String responsible_person_id)
    {
        this.responsible_person_id = responsible_person_id;
    }

    public StringProperty typeProperty(){return new SimpleStringProperty(type);}

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public StringProperty descriptionProperty(){return new SimpleStringProperty(description);}

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    private StringProperty statusProperty(){return new SimpleStringProperty(String.valueOf(status));}

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
        if(getClient_id()==null)
            return new String[]{"false", "invalid client_id value."};
        /*if(getParent_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid parent_id value.");
            return false;
        }*/
        if(getDescription()==null)
            return new String[]{"false", "invalid description value."};
        if(getType()==null)
            return new String[]{"false", "invalid requisition type value."};
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
                case "responsible_person_id":
                    responsible_person_id = (String)val;
                    break;
                case "type":
                    type = String.valueOf(val);
                    break;
                case "description":
                    description = String.valueOf(val);
                    break;
                case "status":
                    status = Integer.parseInt(String.valueOf(val));
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
            case "responsible_person_id":
                return responsible_person_id;
            case "type":
                return type;
            case "description":
                return description;
            case "status":
                return status;
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return super.toString() + " type [" + getType() + "] "  + getDescription() + " for client [" +getClient_id() + "]";
    }

    @Override
    public String apiEndpoint()
    {
        return "/requisitions";
    }
}
