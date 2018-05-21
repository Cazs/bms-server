/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.rest.core.annotation.RestResource;
import server.auxilary.AccessLevel;
import server.auxilary.IO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by th3gh0st on 2018/03/15.
 * @author th3gh0st
 */
public class Task extends ApplicationObject
{
    private long date_assigned;
    private long date_started;
    private long date_completed;
    private long date_scheduled;
    private String job_id;
    private String description;
    private String location;
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_STARTED = 1;
    public static final int STATUS_COMPLETE = 2;

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

    public boolean isComplete()
    {
        // return (date_completed>0 && status== STATUS_AUTHORISED);
        return getStatus() == STATUS_COMPLETE;
    }

    @Override
    public String getStatus_description()
    {
        switch (getStatus())
        {
            case STATUS_PENDING:
                return "Pending";
            case STATUS_STARTED:
                return "Started";
            case STATUS_COMPLETE:
                return "Completed";
            default:
                return "Unknown";
        }
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

    public Employee[] getAssignees()
    {
        Employee[] employees_arr = new Employee[]{}; // for JavaScript based clients

        List<JobEmployee> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("task_id").is(get_id())), JobEmployee.class, "job_employees");
        ArrayList<Employee> employees_list = new ArrayList<>();
        if(contents!=null)
        {
            if(employees_list.size() > 0)
            {
                System.out.println("Task: " + get_id() + " has " + employees_list.size() + " assignees");
                for (JobEmployee jobEmployee : contents)
                    employees_list.add(jobEmployee.getEmployee());

                employees_arr = new Employee[employees_list.size()];
                employees_list.toArray(employees_arr);
            }
        }

        return employees_arr;
    }

    public String[] getAssignee_names()
    {
        Employee[] assignees = getAssignees();
        if(assignees != null && assignees.length>0)
        {
            String[] assignee_names = new String[assignees.length];
            System.arraycopy(assignees, 0, assignee_names, 0, assignees.length); // will call Employee.toString() which in turn calls getName()
            return assignee_names;
        }
        return new String[]{};// for JavaScript based clients
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
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/task";
    }
}