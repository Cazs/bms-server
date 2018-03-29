/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import org.springframework.data.rest.core.annotation.RestResource;
import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by ghost on 2018/03/15.
 * @author ghost
 */
public class Task extends BusinessObject
{
    private long date_assigned;
    private long date_started;
    private long date_completed;
    private long date_scheduled;
    private String job_id;
    private String description;
    private String location;
    private int status;
    private String assignees;

    public Task()
    {}

    public Task(String _id)
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

    //Getters and setters

    public long getDate_assigned() 
    {
        return date_assigned;
    }

    public void setDate_assigned(long date_assigned) 
    {
        this.date_assigned = date_assigned;
    }

    public long getDate_started() 
    {
        return date_started;
    }

    public void setDate_started(long date_started) 
    {
        this.date_started = date_started;
    }

    public long getDate_completed() 
    {
        return date_completed;
    }

    public void setDate_completed(long date_completed) 
    {
        this.date_completed = date_completed;
    }

    public long getDate_scheduled() {return date_scheduled;}

    public void setDate_scheduled(long date_scheduled) {this.date_scheduled = date_scheduled;}

    public boolean isCompleted()
    {
        return (date_completed>0 && status==STATUS_APPROVED);
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getJob_id()
    {
        return job_id;
    }

    public void setJob_id(String job_id)
    {
        this.job_id = job_id;
    }

    public String getAssignees()
    {
        return assignees;
    }

    public void setAssignees(String assignees)
    {
        this.assignees=assignees;
    }

    @Override
    @RestResource(exported = false)
    public String[] isValid()
    {
        if(getJob_id()==null)
            return new String[]{"false", "invalid job_id value."};
        if(getDescription()==null)
            return new String[]{"false", "invalid description value."};
        if(getLocation()==null)
            return new String[]{"false", "invalid location value."};
        if(getDate_scheduled()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_scheduled value.");
            return new String[]{"false", "invalid date_scheduled value."};
        }
        if(getDate_started()>getDate_completed())
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid dates, date started cannot be newer than date completed.");
            return new String[]{"false", "invalid dates, date started cannot be newer than date completed."};
        }
        /*if(getDate_scheduled()>getDate_completed())
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_scheduled value. Date scheduled cannot be newer than date completed.");
            return new String[]{"false", "invalid date_scheduled value."};
        }*/
        return super.isValid();
    }

    /**
     * Method to parse Model attribute.
     * @param var Model attribute to be parsed.
     * @param val Model attribute value to be set.
     */

    @Override
    public void parse(String var, Object val)
    {
        super.parse(var, val);
        try
        {
            switch (var.toLowerCase())
            {
                case "job_id":
                    job_id = (String)val;
                    break;
                case "status":
                    status = Integer.parseInt(String.valueOf(val));
                    break;
                case "date_scheduled":
                    date_scheduled = Long.parseLong(String.valueOf(val));
                    break;
                case "date_assigned":
                    date_assigned = Long.parseLong(String.valueOf(val));
                    break;
                case "date_started":
                    date_started = Long.parseLong(String.valueOf(val));
                    break;
                case "date_completed":
                    date_completed = Long.parseLong(String.valueOf(val));
                    break;
                case "description":
                    description = (String)val;
                    break;
                case "location":
                    location = (String)val;
                    break;
                case "assigned_employees":
                case "assignees":
                    assignees = (String) val;
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

    /**
     * @param var Model attribute whose value is to be returned.
     * @return Model attribute value.
     */
    @Override
    public Object get(String var)
    {
        switch (var.toLowerCase())
        {
            case "job_id":
                return getJob_id();
            case "status":
                return getStatus();
            case "date_scheduled":
                return getDate_scheduled();
            case "date_assigned":
                return getDate_assigned();
            case "date_started":
                return getDate_started();
            case "date_completed":
                return getDate_completed();
            case "description":
                return getDescription();
            case "location":
                return getLocation();
            case "assignees":
                return getAssignees();
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return super.toString() + getDescription() + " for Job ["  + getJob_id() + "] at [" +getLocation() + "]";
    }

    /**
     * @return Task model's endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/tasks";
    }
}