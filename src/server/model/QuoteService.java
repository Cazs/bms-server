package server.model;

import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by ghost on 2018/02/23.
 * @author ghost
 */
public class QuoteService extends BusinessObject
{
    private String quote_id;
    private String service_id;
    public static final String TAG = "QuoteService";

    public QuoteService()
    {}

    public QuoteService(String _id)
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

    public String getService_id()
    {
        return service_id;
    }

    @Override
    public String[] isValid()
    {
        if(getQuote_id()==null)
            return new String[]{"false", "invalid quote_id value."};
        if(getQuote_id().isEmpty())
            return new String[]{"false", "invalid quote_id value."};
        if(getService_id()==null)
            return new String[]{"false", "invalid service_id value."};
        if(getService_id().isEmpty())
            return new String[]{"false", "invalid service_id value."};

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
                case "quote_id":
                    quote_id = String.valueOf(val);
                    break;
                case "service_id":
                    service_id = String.valueOf(val);
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

    @Override
    public Object get(String var)
    {
        switch (var.toLowerCase())
        {
            case "quote_id":
                return quote_id;
            case "service_id":
                return service_id;
        }
        return super.get(var);
    }

    @Override
    public String apiEndpoint()
    {
        return "/quotes/services";
    }
}
