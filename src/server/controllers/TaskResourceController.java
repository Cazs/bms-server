package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.BusinessObject;
import server.model.TaskItem;
import server.repositories.TaskItemRepository;

/**
 * Created by ghost on 2018/03/22
 * @author ghost
 */
@RepositoryRestController
public class TaskResourceController extends APIController
{
    private PagedResourcesAssembler<TaskItem> pagedAssembler;
    @Autowired
    private TaskItemRepository task_resourceRepository;

    @Autowired
    public TaskResourceController(PagedResourcesAssembler<TaskItem> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/task/resources/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends BusinessObject>> getTaskItem(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new TaskItem(id), "_id", session_id, "task_resources", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/tasks/resources")
    public ResponseEntity<Page<? extends BusinessObject>> getTaskItems(@RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new TaskItem(), session_id, "task_resources", pagedAssembler, assembler, pageRequest);
    }


    @PutMapping("/task/resource")
    public ResponseEntity<String> addTaskRep(@RequestBody TaskItem task_item, @RequestHeader String session_id)
    {
        return putBusinessObject(task_item, session_id, "task_resources", "tasks_timestamp");
    }

    @PostMapping("/task/resource")
    public ResponseEntity<String> patchTask(@RequestBody TaskItem task_item, @RequestHeader String session_id)
    {
        return patchBusinessObject(task_item, session_id, "task_resources", "tasks_timestamp");
    }

    @DeleteMapping(path="/task/resource/{task_res_id}")
    public ResponseEntity<String> deleteTaskResource(@PathVariable String task_res_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new TaskItem(task_res_id), session_id, "task_resources", "tasks_timestamp");
    }
}
