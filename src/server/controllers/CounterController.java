package server.controllers;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import server.auxilary.IO;
import server.model.Counter;

import java.util.List;

@RepositoryRestController
@RequestMapping("/counter")
public class CounterController
{
    @GetMapping("/{counter_name}")
    public String timestamp(@RequestParam String counter_name)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "handling counter request ["+counter_name+"].");
        String session_id = null;
        List<Counter> timestamps = IO.getInstance().mongoOperations().find(
                new Query(Criteria.where("counter_name").is(counter_name)), Counter.class, "counters");
        if(timestamps!=null)
            if(!timestamps.isEmpty())
                return timestamps.get(0).toString();
        return "Counter not found.";
    }
}
