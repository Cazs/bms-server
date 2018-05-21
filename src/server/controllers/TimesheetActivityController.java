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
import server.model.TimesheetActivity;
import server.repositories.TimesheetActivityRepository;

/**
 * Created by th3gh0st on 2018/03/16.
 * @author th3gh0st
 */

@RepositoryRestController
public class TimesheetActivityController extends APIController
{
    private PagedResourcesAssembler<TimesheetActivity> pagedAssembler;
    @Autowired
    private TimesheetActivityRepository timesheetActivityRepository;

    @Autowired
    public TimesheetActivityController(PagedResourcesAssembler<TimesheetActivity> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/timesheet/activity/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getTimesheetActivity(@PathVariable("id") String id,
                                                                     @RequestHeader String session_id,
                                                                     Pageable pageRequest,
                                                                     PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new TimesheetActivity(id), "_id", session_id, "timesheet_activities", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/timesheet/activities")
    public ResponseEntity<Page<? extends ApplicationObject>> getTimesheetActivities(@RequestHeader String session_id, Pageable pageRequest,
                                                                      PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new TimesheetActivity(), session_id, "timesheet_activities", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/timesheet/activity")
    public ResponseEntity<String> addTimesheetActivity(@RequestBody TimesheetActivity timesheetActivity, @RequestHeader String session_id)
    {
        return putBusinessObject(timesheetActivity, session_id, "timesheet_activities", "timesheet_activities_timestamp");
    }

    @PostMapping("/timesheet/activity")
    public ResponseEntity<String> patchTimesheetActivity(@RequestBody TimesheetActivity timesheetActivity, @RequestHeader String session_id)
    {
        return patchBusinessObject(timesheetActivity, session_id, "timesheet_activities", "timesheet_activities_timestamp");
    }

//    @PostMapping(value = "/timesheet/activity/mailto")
//    public ResponseEntity<String> emailTimesheetActivity(@RequestHeader String _id, @RequestHeader String session_id,
//                                             @RequestHeader String message, @RequestHeader String subject,
//                                             @RequestHeader String destination, @RequestBody Metafile metafile)
//    {
//        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling TimesheetActivity mailto request.");
//        return emailBusinessObject(_id, session_id, message, subject, destination, metafile, TimesheetActivity.class);
//    }

    @PostMapping(value = "/timesheet/activity/approval_request")
    public ResponseEntity<String> requestTimesheetActivityApproval(@RequestHeader String timesheet_activity_id, @RequestHeader String session_id,
                                                       @RequestHeader String message, @RequestHeader String subject,
                                                       @RequestBody Metafile metafile)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling TimesheetActivity approval request.");
        return requestBusinessObjectApproval(timesheet_activity_id, session_id, message, subject, metafile, new TimesheetActivity().apiEndpoint(), TimesheetActivity.class);
    }

    @GetMapping("/timesheet/activity/approve/{timesheet_activity_id}/{vericode}")
    public ResponseEntity<String> approveTimesheetActivity(@PathVariable("timesheet_activity_id") String timesheetActivity_id, @PathVariable("vericode") String vericode)
    {
        IO.log(getClass().getName(), IO.TAG_INFO, "\nhandling TimesheetActivity "+timesheetActivity_id+" approval request by Vericode.");
        return approveBusinessObjectByVericode(timesheetActivity_id, vericode, "timesheet_activities", "timesheet_activities_timestamp", TimesheetActivity.class);
    }

    @DeleteMapping(path="/timesheet/activity/{timesheet_activity_id}")
    public ResponseEntity<String> deleteTimesheetActivity(@PathVariable String timesheet_activity_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new TimesheetActivity(timesheet_activity_id), session_id, "timesheet_activities", "timesheet_activities_timestamp");
    }
}
