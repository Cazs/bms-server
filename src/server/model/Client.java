/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 * @author ghost
 */
public class Client extends BusinessObject
{
    private String client_name;
    private String physical_address;
    private String postal_address;
    private String tel;
    private String fax;
    private String contact_email;
    private String registration_number;
    private String vat_number;
    private String account_name;
    private long date_partnered;
    private String website;
    private boolean active;

    public String getClient_name()
    {
        return client_name;
    }

    public void setClient_name(String client_name)
    {
        this.client_name = client_name;
    }

    public String getPhysical_address()
    {
        return physical_address;
    }

    public void setPhysical_address(String physical_address)
    {
        this.physical_address = physical_address;
    }

    public String getPostal_address()
    {
        return postal_address;
    }

    public void setPostal_address(String postal_address)
    {
        this.postal_address = postal_address;
    }

    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public String getFax()
    {
        return fax;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public String getContact_email()
    {
        return contact_email;
    }

    public void setContact_email(String contact_email)
    {
        this.contact_email = contact_email;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public long getDate_partnered()
    {
        return date_partnered;
    }

    public void setDate_partnered(long date_partnered)
    {
        this.date_partnered = date_partnered;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public String getRegistration_number()
    {
        return registration_number;
    }

    public void setRegistration_number(String registration_number)
    {
        this.registration_number = registration_number;
    }

    public String getVat_number()
    {
        return vat_number;
    }

    public void setVat_number(String vat_number)
    {
        this.vat_number = vat_number;
    }

    public String getAccount_name()
    {
        return account_name;
    }

    public void setAccount_name(String account_name)
    {
        this.account_name = account_name;
    }

    @Override
    public boolean isValid()
    {
        super.isValid();
        if(getClient_name()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid client_name value.");
            return false;
        }
        if(getContact_email()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid contact_email value.");
            return false;
        }
        if(getTel()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid tel value.");
            return false;
        }
        if(getDate_partnered()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_partnered value.");
            return false;
        }
        if(getAccount_name()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid account_name value.");
            return false;
        }
        if(getWebsite()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid website value.");
            return false;
        }
        if(getPhysical_address()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid physical_address value.");
            return false;
        }
        if(getPostal_address()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid postal_address value.");
            return false;
        }
        if(getRegistration_number()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid registration_number value.");
            return false;
        }
        if(getVat_number()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid vat_number value.");
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
                case "client_name":
                    setClient_name((String) val);
                    break;
                case "physical_address":
                    setPhysical_address((String) val);
                    break;
                case "postal_address":
                    setPostal_address((String) val);
                    break;
                case "tel":
                    setTel((String) val);
                    break;
                case "fax":
                    setFax((String) val);
                    break;
                case "contact_email":
                    setContact_email((String)val);
                    break;
                case "registration_number":
                    setRegistration_number((String) val);
                    break;
                case "vat_number":
                    setVat_number((String) val);
                    break;
                case "account_name":
                    setAccount_name((String)val);
                    break;
                case "date_partnered":
                    setDate_partnered(Long.parseLong(String.valueOf(val)));
                    break;
                case "website":
                    setWebsite((String) val);
                    break;
                case "active":
                    setActive(Boolean.parseBoolean(String.valueOf(val)));
                    break;
                case "other":
                    setOther((String) val);
                    break;
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "unknown Client attribute '" + var + "'.");
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
        Object val = super.get(var);
        if(val==null)
        {
            switch (var.toLowerCase())
            {
                case "client_name":
                    return getClient_name();
                case "physical_address":
                    return getPhysical_address();
                case "postal_address":
                    return getPostal_address();
                case "tel":
                    return getTel();
                case "fax":
                    return getFax();
                case "contact_email":
                    return getContact_email();
                case "registration_number":
                    return getRegistration_number();
                case "vat_number":
                    return getVat_number();
                case "account_name":
                    return getAccount_name();
                case "date_partnered":
                    return getDate_partnered();
                case "website":
                    return getWebsite();
                case "active":
                    return isActive();
                case "other":
                    return getOther();
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "unknown "+getClass().getName()+" attribute '" + var + "'.");
                    return null;
            }
        } else return val;
    }

    @Override
    public String toString()
    {
        return client_name;
    }
}