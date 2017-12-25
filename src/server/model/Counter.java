package server.model;

import org.springframework.data.annotation.Id;

/**
 * Created by ghost on 2017/09/14.
 */
public class Counter
{
    @Id
    private String counter_name;
    private long count;

    public String getCounter_name()
    {
        return counter_name;
    }

    public void setCounter_name(String counter_name)
    {
        this.counter_name = counter_name;
    }

    public long getCount()
    {
        return count;
    }

    public void setCount(long count)
    {
        this.count = count;
    }

    @Override
    public String toString()
    {
        return "{\"counter_name\":\""+counter_name+"\", "+
                "\"count\":\""+count+"\"}";
    }
}
