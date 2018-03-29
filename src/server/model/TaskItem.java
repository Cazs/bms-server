package server.model;

import server.auxilary.AccessLevel;
import server.auxilary.IO;

import java.io.Serializable;

/**
 * Created by ghost on 2018/03/17.
 * @author ghost
 */
public class TaskItem extends BusinessObject implements Serializable
{
    private long quantity;
    private double unit_cost;
    private double markup;
    private String task_id;
    private String resource_id;
    private String category;
    private String serial;
    public static final String TAG = "TaskItem";

    public TaskItem()
    {}

    public TaskItem(String _id)
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

    public String getTask_id()
    {
        return task_id;
    }

    public void setTask_id(String task_id)
    {
        this.task_id = task_id;
    }

    public String getResource_id()
    {
        return resource_id;
    }

    public void setResource_id(String resource_id)
    {
        this.resource_id = resource_id;
    }

    public String getQuantity()
    {
        return String.valueOf(quantity);
    }

    public long getQuantityValue()
    {
        return quantity;
    }

    public void setQuantity(long quantity)
    {
        this.quantity = quantity;
    }

    public double getUnit_cost()
    {
        return unit_cost;
    }

    public void setUnit_cost(double unit_cost)
    {
        this.unit_cost = unit_cost;
    }

    public String getMarkup(){return String.valueOf(this.markup);}

    public double getMarkupValue(){return this.markup;}

    public void setMarkup(double markup){this.markup=markup;}

    public double getRate()
    {
        return getUnit_cost() + getUnit_cost()*(markup/100);
    }

    public String getCategory()
    {
        if(category!=null)
            return category;
        else return null;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }


    public String getSerial()
    {
        return serial;
    }

    public void setSerial(String serial)
    {
        this.serial = serial;
    }

    public double getTotal()
    {
        return getRate()*getQuantityValue();
    }

    @Override
    public String[] isValid()
    {
        if(getResource_id()==null)
            return new String[]{"false", "invalid resource_id value."};
        if(getTask_id()==null)
            return new String[]{"false", "invalid task_id value."};
        if(getUnit_cost()<0)
            return new String[]{"false", "invalid unit_cost value."};
        if(getQuantityValue()<0)
            return new String[]{"false", "invalid quantity value."};
        if(getMarkupValue()<0)
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
                case "task_id":
                    setTask_id(String.valueOf(val));
                    break;
                case "resource_id":
                    setResource_id(String.valueOf(val));
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
                case "serial":
                    setSerial((String) val);
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
            case "task_id":
                return getTask_id();
            case "resource_id":
                return getResource_id();
            case "quantity":
                return getQuantityValue();
            case "cost":
            case "unitcost":
            case "unit_cost":
                return getUnit_cost();
            case "markup":
                return getMarkupValue();
            case "category":
                return getCategory();
            case "serial":
                return getSerial();
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return super.toString() + " for task ["  + getTask_id() + "] material [" +getResource_id() + "]";
    }

    @Override
    public String apiEndpoint()
    {
        return "/tasks/resources";
    }
}