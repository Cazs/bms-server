/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import server.auxilary.IO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
/**
 *
 * @author ghost
 */
public class ServiceItem extends BusinessObject implements Serializable
{
    private String service_id;
    private String item_name;
    private String item_description;
    private double item_rate;
    private long quantity;
    private String unit;
    public static final String TAG = "ServiceItem";

    public String getService_id()
    {
        return service_id;
    }

    public void setService_id(String service_id)
    {
        this.service_id = service_id;
    }

    public String getItem_name()
    {
        return item_name;
    }

    public void setItem_name(String item_name)
    {
        this.item_name = item_name;
    }

    public String getItem_description()
    {
        return item_description;
    }

    public void setItem_description(String description)
    {
        this.item_description = description;
    }

    public double getItem_rate()
    {
        return item_rate;
    }

    public void setItem_rate(double item_rate)
    {
        this.item_rate = item_rate;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public long getQuantity()
    {
        return quantity;
    }

    public void setQuantity(long quantity)
    {
        this.quantity = quantity;
    }

    @Override
    public void parse(String var, Object val)
    {
        super.parse(var, val);
        try
        {
            switch (var.toLowerCase())
            {
                case "service_id":
                    service_id = (String)val;
                    break;
                case "item_name":
                    item_name = (String)val;
                    break;
                case "item_description":
                    item_description = (String)val;
                    break;
                case "item_rate":
                    item_rate = Double.parseDouble(String.valueOf(val));
                    break;
                case "quantity":
                    quantity = Long.parseLong(String.valueOf(val));
                    break;
                case "unit":
                    unit = String.valueOf(val);
                    break;
                default:
                    IO.log(TAG, IO.TAG_ERROR,"Unknown "+getClass().getName()+" attribute '" + var + "'.");
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
            case "service_id":
                return getService_id();
            case "name":
            case "item_name":
                return getItem_name();
            case "description":
            case "item_description":
                return getItem_description();
            case "cost":
            case "value":
            case "item_rate":
                return getItem_rate();
            case "quantity":
                return quantity;
            case "unit":
                return unit;
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return getItem_name();
    }

    @Override
    public String apiEndpoint()
    {
        return "/services/items";
    }
}
