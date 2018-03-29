package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auxilary.IO;
import server.model.ApplicationObject;
import server.model.Metafile;
import server.model.Task;
import server.repositories.TaskRepository;

/**
 * Created by th3gh0st on 2018/03/16.
 * @author th3gh0st
 */

@RepositoryRestController
public class TaskController extends APIController
{
    private PagedResourcesAssembler<Task> pagedAssembler;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    public TaskController(PagedResourcesAssembler<Task> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/task/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getTask(@PathVariable("id") String id,
                                                                     @RequestHeader String session_id,
                                                                     Pageable pageRequest,
                                                                     PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Task(id), "_id", session_id, "tasks", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/tasks")
    public ResponseEntity<Page<? extends ApplicationObject>> getTasks(@RequestHeader String session_id, Pageable pageRequest,
                                                                      PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Task(), session_id, "tasks", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/task")
    public ResponseEntity<String> addTask(@RequestBody Task task, @RequestHeader String session_id)
    {
        return putBusinessObject(task, session_id, "tasks", "tasks_timestamp");
    }

    @PostMapping("/task")
    public ResponseEntity<String> patchTask(@RequestBody Task task, @RequestHeader String session_id)
    {
        return patchBusinessObject(task, session_id, "tasks", "tasks_timestamp");
    }

    @PostMapping(value = "/task/mailto")
    public ResponseEntity<String> emailTask(@RequestHeader String _id, @RequestHeader String session_id,
                                             @RequestHeader String message, @RequestHeader String subject,
                                             @RequestHeader String destination, @RequestBody Metafile metafile)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling handling mailto request.");
        return emailBusinessObject(_id, session_id, message, subject, destination, metafile, Task.class);
    }

    @PostMapping(value = "/task/approval_request")
    public ResponseEntity<String> requestTaskApproval(@RequestHeader String task_id, @RequestHeader String session_id,
                                                       @RequestHeader String message, @RequestHeader String subject,
                                                       @RequestBody Metafile metafile)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Task approval request.");
        return requestBusinessObjectApproval(task_id, session_id, message, subject, metafile, new Task().apiEndpoint(), Task.class);
    }

    @GetMapping("/task/approve/{task_id}/{vericode}")
    public ResponseEntity<String> approveTask(@PathVariable("task_id") String task_id, @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling Task "+task_id+" approval request by Vericode.");
        return approveBusinessObjectByVericode(task_id, vericode, "tasks", "tasks_timestamp", Task.class);
    }

    @DeleteMapping(path="/task/{task_id}")
    public ResponseEntity<String> deleteTask(@PathVariable String task_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Task(task_id), session_id, "tasks", "tasks_timestamp");
    }
}
