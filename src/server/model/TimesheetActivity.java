package server.model;

import org.springframework.data.rest.core.annotation.RestResource;
import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by th3gh0st on 2018/03/29.
 * @author th3gh0st
 */
public class TimesheetActivity extends ApplicationObject
{
    private String client_id;//or INTERNAL
    private String description;
    private String location;
    private long date_executed;
    private int status;
    public static final int STATUS_COMPLETED = 1;
    public static final String INTERNAL_ACTIVITY = "INTERNAL";

    public TimesheetActivity()
    {}

    public TimesheetActivity(String _id)
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

    //Getters and setters

    public long getDate_executed()
    {
        return date_executed;
    }

    public void setDate_executed(long date_executed)
    {
        this.date_executed = date_executed;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getClient_id()
    {
        return client_id;
    }

    public void setClient_id(String client_id)
    {
        this.client_id = client_id;
    }

    @Override
    @RestResource(exported = false)
    public String[] isValid()
    {
        if(getClient_id()==null)
            return new String[]{"false", "invalid client_id."};
        if(getDescription()==null)
            return new String[]{"false", "invalid description value."};
        if(getLocation()==null)
            return new String[]{"false", "invalid location value."};
        if(getDate_executed()<=0)
            return new String[]{"false", "invalid date_executed."};

        /*if(getDate_date_executed()>LocalDate.now())//has to account for different TimeZones
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_executed value. Date scheduled cannot be newer than current day.");
            return new String[]{"false", "invalid date_scheduled value."};
        }*/
        return super.isValid();
    }

    /**
     * Method to parse Model attribute.
     * @param var TimesheetActivity attribute to be parsed.
     * @param val TimesheetActivity attribute value to be set.
     */
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
                case "status":
                    status = Integer.parseInt(String.valueOf(val));
                    break;
                case "date_executed":
                    date_executed = Long.parseLong(String.valueOf(val));
                    break;
                case "description":
                    description = (String)val;
                    break;
                case "location":
                    location = (String)val;
                    break;
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "unknown "+getClass().getName()+" attribute '" + var + "'.");
                    break;
            }
        } catch (NumberFormatException e)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, e.getMessage());
        }
    }

    /**
     * @param var TimesheetActivity attribute whose value is to be returned.
     * @return TimesheetActivity attribute value.
     */
    @Override
    public Object get(String var)
    {
        switch (var.toLowerCase())
        {
            case "client_id":
                return getClient_id();
            case "status":
                return getStatus();
            case "date_executed":
                return getDate_executed();
            case "description":
                return getDescription();
            case "location":
                return getLocation();
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return super.toString() + getDescription() + " for Client ["  + getClient_id() + "] at [" +getLocation() + "]";
    }

    /**
     * @return TimesheetActivity model's endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/timesheet/activity";
    }
}
