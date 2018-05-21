package server.model;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by th3gh0st on 2017/12/23.
 * @author th3gh0st
 */
public class JobEmployee extends ApplicationObject
{
    private String job_id;
    private String task_id;
    private String usr;
    public static final String TAG = "JobEmployee";

    public JobEmployee()
    {}

    public JobEmployee(String _id)
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

    public String getJob_id()
    {
        return job_id;
    }

    public void setJob_id(String job_id)
    {
        this.job_id = job_id;
    }

    public String getTask_id()
    {
        return task_id;
    }

    public void setTask_id(String task_id)
    {
        this.task_id = task_id;
    }

    public String getUsr()
    {
        return usr;
    }

    public void setUsr(String usr)
    {
        this.usr = usr;
    }

    public Employee getEmployee()
    {
        return IO.getInstance().mongoOperations().findOne(new Query(Criteria.where("usr").is(getUsr())), Employee.class, "employees");
    }

    public Task getTask()
    {
        return IO.getInstance().mongoOperations().findOne(new Query(Criteria.where("task_id").is(getTask_id())), Task.class, "tasks");
    }

    public Job getJob()
    {
        return IO.getInstance().mongoOperations().findOne(new Query(Criteria.where("job_id").is(getJob_id())), Job.class, "jobs");
    }

    @Override
    public String[] isValid()
    {
        if(getUsr()==null)
            return new String[]{"false", "invalid usr value."};
        if(getJob_id()==null)
            return new String[]{"false", "invalid job_id value."};

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
                case "job_id":
                    job_id = String.valueOf(val);
                    break;
                case "task_id":
                    task_id = String.valueOf(val);
                    break;
                case "usr":
                    usr = String.valueOf(val);
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

    @Override
    public Object get(String var)
    {
        switch (var.toLowerCase())
        {
            case "job_id":
                return job_id;
            case "task_id":
                return task_id;
            case "usr":
                return usr;
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return super.toString() + " = "  + getUsr() + " -> " + getJob_id();
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/job/employee";
    }
}
