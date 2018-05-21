package server.model;

import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by th3gh0st on 2018/05/25.
 * @author th3gh0st
 */
public class QuoteItemExtraCost extends ApplicationObject
{
    private String quote_item_id;
    private String title;
    private double cost = 0.0;
    private double markup = 0.0;

    public static final String TAG = "QuoteItem";

    public QuoteItemExtraCost()
    {}

    public QuoteItemExtraCost(String _id)
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

    public String getQuote_item_id()
    {
        return quote_item_id;
    }

    public void setQuote_item_id(String quote_item_id)
    {
        this.quote_item_id = quote_item_id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public double getCost()
    {
        return cost;
    }

    public void setCost(double cost)
    {
        this.cost = cost;
    }

    public double getMarkup()
    {
        return markup;
    }

    public void setMarkup(double markup)
    {
        this.markup = markup;
    }

    double getMarkedupCost()
    {
        return cost + cost * markup/100;
    }


    @Override
    public String[] isValid()
    {
        if(quote_item_id==null)
            return new String[]{"false", "invalid extra cost's quote item _id."};
        if(quote_item_id.isEmpty())
            return new String[]{"false", "Empty extra cost's quote item _id."};
        if(title==null)
            return new String[]{"false", "invalid extra cost's title."};
        if(title.isEmpty())
            return new String[]{"false", "invalid extra cost's title."};
        if(cost < 0)
            return new String[]{"false", "invalid extra cost's cost value."};
        if(markup<0)
            return new String[]{"false", "invalid extra cost's markup value."};

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
                case "title":
                    title = String.valueOf(val);
                    break;
                case "cost":
                    cost = Double.valueOf((String)val);
                    break;
                case "markup":
                    markup = Double.valueOf((String)val);
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
            case "title":
                return title;
            case "cost":
                return cost;
            case "markup":
                return markup;
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return super.toString() + " for QuoteItem ["  + quote_item_id + "], title: [" + title + "], total cost: ["+getMarkedupCost()+"]";
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/quote/resource/extra_cost";
    }
}