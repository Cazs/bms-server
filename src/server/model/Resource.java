/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */
public class Resource extends ApplicationObject
{
    private String brand_name;//optional
    private String resource_description;
    private String resource_code;
    private String resource_type;
    private double resource_value;
    private long quantity;
    private long date_acquired;
    private long date_exhausted;//optional
    private String unit;
    private String supplier_id;//optional
    private String part_number;//optional
    public static final String TAG = "Resource";

    public Resource()
    {}

    public Resource(String _id)
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

    public String getBrand_name()
    {
        return brand_name;
    }

    public void setBrand_name(String brand_name)
    {
        this.brand_name = brand_name;
    }

    public String getResource_description()
    {
        return resource_description;
    }

    public void setResource_description(String description)
    {
        this.resource_description = description;
    }

    public String getResource_code()
{
    return resource_code;
}

    public void setResource_code(String resource_code)
    {
        this.resource_code = resource_code;
    }

    public String getResource_type()
    {
        return resource_type;
    }

    public void setResource_type(String resource_type)
    {
        this.resource_type = resource_type;
    }

    public double getResource_value()
    {
        return resource_value;
    }

    public void setResource_value(double resource_value)
    {
        this.resource_value = resource_value;
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

    public long getDate_acquired()
    {
        return date_acquired;
    }

    public void setDate_acquired(long date_acquired)
    {
        this.date_acquired = date_acquired;
    }

    public long getDate_exhausted()
    {
        return date_exhausted;
    }

    public void setDate_exhausted(long date_exhausted)
    {
        this.date_exhausted = date_exhausted;
    }

    public String getSupplier_id()
    {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id)
    {
        this.supplier_id = supplier_id;
    }

    public String getPart_number()
    {
        return part_number;
    }

    public void setPart_number(String part_number)
    {
        this.part_number = part_number;
    }

    @Override
    public String[] isValid()
    {
        if(getBrand_name()==null && getResource_description()==null)
            return new String[]{"false", "invalid resource_name and/or resource_description value."};
        if(getResource_value()<0)
            return new String[]{"false", "invalid resource_value value."};
        if(getUnit()==null)
            return new String[]{"false", "invalid unit value."};
        if(getQuantity()<0)
            return new String[]{"false", "invalid quantity value."};
        if(getDate_acquired()<0)
            return new String[]{"false", "invalid date_acquired value."};
        if(getDate_exhausted()<getDate_acquired() && getDate_exhausted()!=0)
            return new String[]{"false", "invalid date_exhausted value. Cannot be before date_acquired"};
        // if(getResource_code()==null)
        //    return new String[]{"false", "invalid resource_code value."};
        if(getResource_type()==null)
            return new String[]{"false", "invalid resource_type value."};
        /**is optional field if(getBrand_name()==null)
         return new String[]{"false", "invalid resource_name value."};**/
        /**is optional field if(getSupplier_id()==null)
         return new String[]{"false", "invalid supplier_id value."};**/
        /**is optional field if(getPart_number()==null)
         return new String[]{"false", "invalid part_number value."};**/
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
                case "brand_name":
                    brand_name = (String)val;
                    break;
                case "resource_type":
                    resource_type = (String)val;
                    break;
                case "resource_description":
                    resource_description = (String)val;
                    break;
                case "resource_code":
                    resource_code = (String)val;
                    break;
                case "resource_value":
                    resource_value = Double.parseDouble(String.valueOf(val));
                    break;
                case "date_acquired":
                    date_acquired = Long.parseLong(String.valueOf(val));
                    break;
                case "date_exhausted":
                    date_exhausted = Long.parseLong(String.valueOf(val));
                    break;
                case "quantity":
                    quantity = Long.parseLong(String.valueOf(val));
                    break;
                case "unit":
                    unit = String.valueOf(val);
                    break;
                case "supplier_id":
                    supplier_id = (String)val;
                    break;
                case "part_number":
                    part_number = (String)val;
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
            case "name":
            case "brand_name":
                return getBrand_name();
            case "resource_type":
                return resource_type;
            case "resource_description":
                return resource_description;
            case "resource_code":
                return getResource_code();
            case "cost":
            case "value":
            case "resource_value":
                return getResource_value();
            case "date_acquired":
                return date_acquired;
            case "date_exhausted":
                return date_exhausted;
            case "quantity":
                return quantity;
            case "unit":
                return unit;
            case "supplier_id":
                return getSupplier_id();
            case "part_number":
                return getPart_number();
        }
        return super.get(var);
    }


    @Override
    public String toString()
    {
        return getResource_description();
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/resource";
    }
}
