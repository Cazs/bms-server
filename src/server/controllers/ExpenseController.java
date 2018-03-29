package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.ApplicationObject;
import server.model.Expense;
import server.repositories.ExpenseRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class ExpenseController extends APIController
{
    private PagedResourcesAssembler<Expense> pagedAssembler;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseController(PagedResourcesAssembler<Expense> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path = "/expense/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getExpense(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Expense(id), "_id", session_id, "expenses", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/expenses")
    public ResponseEntity<Page<? extends ApplicationObject>> getExpenses(@RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Expense(), session_id, "expenses", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/expense")
    public ResponseEntity<String> addExpense(@RequestBody Expense expense, @RequestHeader String session_id)
    {
        return putBusinessObject(expense, session_id, "expenses", "expenses_timestamp");
    }

    @PostMapping("/expense")
    public ResponseEntity<String> patchExpense(@RequestBody Expense expense, @RequestHeader String session_id)
    {
        return patchBusinessObject(expense, session_id, "expenses", "expenses_timestamp");
    }

    @DeleteMapping(path = "/expense/{expense_id}")
    public ResponseEntity<String> delete(@PathVariable String expense_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Expense(expense_id), session_id, "expenses", "expenses_timestamp");
    }
}
