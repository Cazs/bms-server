package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ghost on 2017/01/21.
 */
public abstract class PurchaseOrderItem implements BusinessObject, Serializable
{
    @Id
    private String _id;
    private int item_number;
    private String purchase_order_id;
    private String item_id;
    private int quantity;
    private double discount;
    private double cost;
    private long date_logged;
    private boolean marked;
    private String extra;
    private String type;

    /**
     * Function to get identifier of Quote object.
     * @return Quote identifier.
     */
    @Override
    public String get_id()
    {
        return _id;
    }

    /**
     * Method to assign identifier to this object.
     * @param _id identifier to be assigned to this object.
     */
    public void set_id(String _id)
    {
        this._id = _id;
    }


    @Override
    public boolean isMarked()
    {
        return marked;
    }

    @Override
    public void setMarked(boolean marked){this.marked=marked;}

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

    public long getDate_logged()
    {
        return date_logged;
    }

    public void setDate_logged(long date_logged)
    {
        this.date_logged = date_logged;
    }

    /*public abstract String getItem_name();

    public abstract String getItem_description();

    public abstract String getUnit();*/

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

    public String getExtra()
    {
        return extra;
    }

    public void setExtra(String extra)
    {
        this.extra = extra;
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
        /*if(getItem_name()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid item_name value.");
            return false;
        }
        if(getItem_description()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid item_description value.");
            return false;
        }*/
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
                case "extra":
                    setExtra(String.valueOf(val));
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
            case "_id":
                return get_id();
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
            case "extra":
                return extra;
            default:
                IO.log(getClass().getName(), IO.TAG_ERROR, "Unknown "+getClass().getName()+" attribute '" + var + "'.");
                return null;
        }
    }

    @Override
    public String asJSON()
    {
        //Return encoded URL parameters in UTF-8 charset
        StringBuilder result = new StringBuilder();
        try
        {
            result.append(URLEncoder.encode("purchase_order_id","UTF-8") + "="
                    + URLEncoder.encode(purchase_order_id, "UTF-8"));
            result.append("&" + URLEncoder.encode("item_id","UTF-8") + "="
                    + URLEncoder.encode(item_id, "UTF-8"));
            result.append("&" + URLEncoder.encode("item_number","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(item_number), "UTF-8"));
            result.append("&" + URLEncoder.encode("quantity","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(quantity), "UTF-8"));
            result.append("&" + URLEncoder.encode("discount","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(discount), "UTF-8"));
            result.append("&" + URLEncoder.encode("cost","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(cost), "UTF-8"));
            if(date_logged>0)
                result.append("&" + URLEncoder.encode("date_logged","UTF-8") + "="
                        + URLEncoder.encode(String.valueOf(date_logged), "UTF-8"));
            if(extra!=null)
                if(!extra.isEmpty())
                    result.append("&" + URLEncoder.encode("extra","UTF-8") + "="
                            + URLEncoder.encode(extra, "UTF-8"));
            return result.toString();
        } catch (UnsupportedEncodingException e)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, e.getMessage());
        }
        return null;
    }
}