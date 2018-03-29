package server.model;

import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */
public class QuoteRep extends ApplicationObject
{
    private String quote_id;
    private String usr;
    public static final String TAG = "QuoteRepresentative";

    public QuoteRep()
    {}

    public QuoteRep(String _id)
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

    public String getQuote_id()
    {
        return quote_id;
    }

    public void setQuote_id(String quote_id)
    {
        this.quote_id = quote_id;
    }

    public String getUsr()
    {
        return usr;
    }

    public void setUsr(String employee_id)
    {
        this.usr = employee_id;
    }

    @Override
    public String[] isValid()
    {
        if(getUsr()==null)
            return new String[]{"false", "invalid usr value."};
        if(getQuote_id()==null)
            return new String[]{"false", "invalid quote_id value."};

        return super.isValid();
    }

    @Override
    public void parse(String var, Object val)
    {
        try
        {
            switch (var.toLowerCase())
            {
                case "quote_id":
                    quote_id = String.valueOf(val);
                    break;
                case "usr":
                    usr = String.valueOf(val);
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
            case "quote_id":
                return quote_id;
            case "usr":
                return usr;
            default:
                IO.log(getClass().getName(), IO.TAG_ERROR, "Unknown "+getClass().getName()+" attribute '" + var + "'.");
                return null;
        }
    }

    @Override
    public String toString()
    {
        return super.toString() + " for Quote ["  + getQuote_id() + "] username [" +getUsr() + "]";
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/quote/representative";
    }
}
