/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import server.auxilary.AccessLevel;
import server.auxilary.IO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

/**
 * Created by ghost on 2018/02/23.
 * @author ghost
 */
public class Service extends BusinessObject implements Serializable
{
    private String service_title;
    private String service_description;
    public static final String TAG = "Service";

    public Service()
    {}

    public Service(String _id)
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

    public String getService_title()
    {
        return service_title;
    }

    public void setService_title(String service_title)
    {
        this.service_title = service_title;
    }

    public String getService_description()
    {
        return service_description;
    }

    public void setService_description(String description)
    {
        this.service_description = service_description;
    }

    //Properties

    public StringProperty service_titleProperty(){return new SimpleStringProperty(service_title);}
    public StringProperty service_descriptionProperty(){return new SimpleStringProperty(service_description);}

    @Override
    public void parse(String var, Object val)
    {
        super.parse(var, val);
        try
        {
            switch (var.toLowerCase())
            {
                case "service_title":
                    service_title = (String)val;
                    break;
                case "service_description":
                    service_description = (String)val;
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
            case "title":
            case "service_title":
                return getService_title();
            case "description":
            case "service_description":
                return getService_description();
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return getService_title();
    }

    @Override
    public String apiEndpoint()
    {
        return "/services";
    }
}
