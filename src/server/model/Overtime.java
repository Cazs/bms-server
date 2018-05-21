package server.model;

import server.auxilary.AccessLevel;
import server.auxilary.IO;

import java.time.LocalDateTime;
import java.util.AbstractMap;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

public class Overtime extends ApplicationObject
{
    private String usr;
    private String job_id;
    private long date;
    private long time_in;
    private long time_out;
    public static final String TAG = "Overtime";

    public Overtime()
    {}

    public Overtime(String _id)
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

    public String getUsr()
    {
        return usr;
    }

    public void setUsr(String usr)
    {
        this.usr = usr;
    }

    public String getJob_id()
    {
        return job_id;
    }

    public void setJob_id(String job_id)
    {
        this.job_id = job_id;
    }

    public long getDate()
    {
        return date;
    }

    public void setDate(long date)
    {
        this.date = date;
    }

    public String getOvertime_date()
    {
        // return date.format(DateTimeFormatter.BASIC_ISO_DATE);
        AbstractMap.SimpleEntry<Integer, LocalDateTime> date_map = IO.isEpochSecondOrMilli(getDate());
        LocalDateTime date = date_map.getValue();

        return IO.getYyyyMMddFormmattedDate(date);
    }

    public long getTime_in()
    {
        return time_in;
    }

    public void setTime_in(long time_in)
    {
        this.time_in = time_in;
    }

    public String getIn_time()
    {
        // TODO: fix this
        // return date.format(DateTimeFormatter.BASIC_ISO_DATE);
        AbstractMap.SimpleEntry<Integer, LocalDateTime> date_map = IO.isEpochSecondOrMilli(getTime_in());
        LocalDateTime date = date_map.getValue();

        return IO.getYyyyMMddFormmattedDate(date);
    }

    public long getTime_out()
    {
        return time_out;
    }

    public void setTime_out(long time_out)
    {
        this.time_out = time_out;
    }

    public String getOut_time()
    {
        // TODO: fix this
        // return date.format(DateTimeFormatter.BASIC_ISO_DATE);
        AbstractMap.SimpleEntry<Integer, LocalDateTime> date_map = IO.isEpochSecondOrMilli(getTime_out());
        LocalDateTime date = date_map.getValue();

        return IO.getYyyyMMddFormmattedDate(date);
    }

    @Override
    public String[] isValid()
    {
        if(getUsr()==null)
            return new String[]{"false", "invalid usr value."};
        if(getJob_id()==null)
            return new String[]{"false", "invalid job_id value."};
        if(getDate()<=0)
            return new String[]{"false", "invalid date value."};
        if(getTime_in()<=0)
            return new String[]{"false", "invalid time_in value."};
        if(getTime_out()<=0)
            return new String[]{"false", "invalid time_out value."};
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
                case "usr":
                    setUsr(String.valueOf(val));
                    break;
                case "job_id":
                    setJob_id(String.valueOf(val));
                    break;
                case "date":
                    setDate(Long.valueOf(String.valueOf(val)));
                    break;
                case "date_logged":
                    setDate_logged(Long.parseLong(String.valueOf(val)));
                    break;
                case "time_in":
                    setTime_in(Long.parseLong(String.valueOf(val)));
                    break;
                case "time_out":
                    setTime_out(Long.parseLong(String.valueOf(val)));
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
            case "_id":
                return get_id();
            case "usr":
                return getUsr();
            case "job_id":
                return getJob_id();
            case "date":
                return getDate();
            case "time_in":
                return getTime_in();
            case "time_out":
                return getTime_out();
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return super.toString() + " = "  + getUsr();
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/overtime_application";
    }
}