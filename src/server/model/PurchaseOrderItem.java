package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ghost on 2017/01/21.
 */
public abstract class PurchaseOrderItem extends BusinessObject
{
    private int item_number;
    private String purchase_order_id;
    private String item_id;
    private int quantity;
    private double discount;
    private double cost;
    private String type;

    public int getItem_number()
    {
        return item_number;
    }

    public void setItem_number(int item_number)
    {
        this.item_number = item_number;
    }

    public String getPurchase_order_id()
    {
        return purchase_order_id;
    }

    public void setPurchase_order_id(String purchase_order_id)
    {
        this.purchase_order_id = purchase_order_id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getItem_id()
    {
        return item_id;
    }

    public void setItem_id(String item_id)
    {
        this.item_id = item_id;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public double getCost()
    {
        return cost;
    }

    public void setCost(double cost)
    {
        this.cost=cost;
    }

    public double getDiscount(){return this.discount;}

    public void setDiscount(double discount)
    {
        this.discount = discount;
    }

    @Override
    public boolean isValid()
    {
        if(getItem_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid item_id value.");
            return false;
        }
        if(getItem_number()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid item_number value.");
            return false;
        }
        if(getPurchase_order_id()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid purchase_order_id value.");
            return false;
        }
        if(getType()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid type value.");
            return false;
        }
        if(getDate_logged()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_logged value.");
            return false;
        }
        if(getCost()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid cost value.");
            return false;
        }
        if(getDiscount()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid discount value.");
            return false;
        }
        if(getQuantity()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid quantity value.");
            return false;
        }

        IO.log(getClass().getName(), IO.TAG_INFO,  "valid " + getClass().getName() + " object.");
        return true;
    }

    @Override
    public void parse(String var, Object val)
    {
        super.parse(var, val);
        try
        {
            switch (var.toLowerCase())
            {
                case "purchase_order_id":
                    setPurchase_order_id(String.valueOf(val));
                    break;
                case "item_id":
                    setItem_id(String.valueOf(val));
                    break;
                case "item_number":
                    setItem_number(Integer.valueOf((String)val));
                    break;
                case "quantity":
                    setQuantity(Integer.valueOf((String)val));
                    break;
                case "discount":
                    setDiscount(Double.parseDouble((String) val));
                    break;
                case "cost":
                    setCost(Double.parseDouble((String) val));
                    break;
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "Unknown "+getClass().getName()+" attribute '" + var + "'.");
                    break;
            }
        }catch (NumberFormatException e)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, e.getMessage());
        }
    }

    @Override
    public Object get(String var)
    {
        switch (var.toLowerCase())
        {
            case "purchase_order_id":
                return getPurchase_order_id();
            case "item_id":
                return getItem_id();
            case "item_number":
                return getItem_number();
            case "cost":
                return getCost();
            case "quantity":
                return getQuantity();
            case "discount":
                return getDiscount();
        }
        return super.get(var);
    }
}