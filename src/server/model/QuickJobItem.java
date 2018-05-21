package server.model;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.rest.core.annotation.RestResource;
import server.auxilary.AccessLevel;
import server.auxilary.Globals;
import server.auxilary.IO;

import java.util.List;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */
public class QuickJobItem extends ApplicationObject
{
    private int item_number;
    private int quantity;
    private double unit_cost;
    private double markup;
    private String quickjob_id;
    private String resource_id;
    private String category;
    public static final String TAG = "QuickJobItem";

    public QuickJobItem()
    {}

    public QuickJobItem(String _id)
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

    public int getItem_number()
    {
        return item_number;
    }

    public void setItem_number(int item_number)
    {
        this.item_number = item_number;
    }

    public String getQuickjob_id()
    {
        return quickjob_id;
    }

    public void setQuickjob_id(String quickjob_id)
    {
        this.quickjob_id = quickjob_id;
    }

    public String getResource_id()
    {
        return resource_id;
    }

    public void setResource_id(String resource_id)
    {
        this.resource_id = resource_id;
    }

    /*public QuickjobItemExtraCost[] getExtra_costs()
    {
        QuickjobItemExtraCost[] arr = null;
        List contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("quickjob_item_id").is(get_id())), QuickjobItemExtraCost.class, "quickjob_extra_costs");
        if(contents!=null)
        {
            arr = new QuickjobItemExtraCost[contents.size()];
            contents.toArray(arr);
        }
        return arr;
    }*/

    /**
     * @return Total of extra costs
     */
    // @RestResource(exported = false)
    /*public double computeExtraCostTotal()
    {
        double total = 0.0;
        QuickjobItemExtraCost[] extra_costs = getExtra_costs();
        if(extra_costs!=null)
          for(QuickjobItemExtraCost extraCost: extra_costs)
              total+=extraCost.getMarkedupCost();
        return total;
    }*/

    /**
     * @return Total of extra costs if available, or a message saying no extra costs otherwise.
     */
    public String getExtra_costs_total()
    {
        double total = 0;// computeExtraCostTotal();
        if(total>0)
            return Globals.CURRENCY_SYMBOL.getValue() + " " + total;// TODO: currency format
        else return "No extra costs.";
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public String getUnit()
    {
        Resource res = IO.getInstance().mongoOperations().findOne(new Query(Criteria.where("_id").is(getResource_id())), Resource.class, "resources");
        if(res!=null)
            return res.getUnit();
        return "N/A";
    }

    public String getItem_description()
    {
        Resource res = IO.getInstance().mongoOperations().findOne(new Query(Criteria.where("_id").is(getResource_id())), Resource.class, "resources");
        if(res!=null)
            return res.getResource_description();
        return "N/A";
    }

    public Resource getResource()
    {
        return IO.getInstance().mongoOperations().findOne(new Query(Criteria.where("_id").is(getResource_id())), Resource.class, "resources");
    }

    public double getUnit_cost()
    {
        return unit_cost;
    }

    public void setUnit_cost(double unit_cost)
    {
        this.unit_cost = unit_cost;
    }

    public double getMarkup(){return this.markup;}

    public void setMarkup(double markup){this.markup=markup;}

    public double getRate()
    {
        double markedup_cost = getUnit_cost() + (getUnit_cost() * getMarkup()/100);
        return markedup_cost; // + computeExtraCostTotal();
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getTotal()
    {
        double total = getRate()*getQuantity();
        return Globals.CURRENCY_SYMBOL.getValue() + " " + total;
    }

    @Override
    public String[] isValid()
    {
        if(getResource_id()==null)
            return new String[]{"false", "invalid resource_id value."};
        if(getItem_number()<0)
            return new String[]{"false", "invalid item_number value."};
        if(getQuickjob_id()==null)
            return new String[]{"false", "invalid quickjob_id value."};
        if(getUnit_cost()<0)
            return new String[]{"false", "invalid unit_cost value."};
        if(getQuantity()<0)
            return new String[]{"false", "invalid quantity value."};
        if(getMarkup()<0)
            return new String[]{"false", "invalid markup value."};

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
                case "quickjob_id":
                    setQuickjob_id(String.valueOf(val));
                    break;
                case "resource_id":
                    setResource_id(String.valueOf(val));
                    break;
                case "item_number":
                    setItem_number(Integer.valueOf((String)val));
                    break;
                case "quantity":
                    setQuantity(Integer.valueOf((String)val));
                    break;
                case "unit_cost":
                    setUnit_cost(Double.valueOf((String)val));
                    break;
                case "markup":
                    setMarkup(Double.parseDouble((String) val));
                    break;
                case "category":
                    setCategory((String) val);
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
            case "quickjob_id":
                return getQuickjob_id();
            case "resource_id":
                return getResource_id();
            case "item_number":
                return getItem_number();
            // case "extra_costs":
            //    return getExtra_costs();
            case "quantity":
                return getQuantity();
            case "unit_cost":
            case "value":
                return getUnit_cost();
            case "markup":
                return getMarkup();
            case "category":
                return getCategory();
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return super.toString() + " for QuickJob ["  + getQuickjob_id() + "] material ID [" +getResource_id() + "] [qty: "+getQuantity()+"]";
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/quickjob/resource";
    }
}