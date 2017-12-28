package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auxilary.IO;
import server.model.Expense;
import server.model.BusinessObject;
import server.repositories.ExpenseRepository;

import java.util.LinkedList;
import java.util.List;

@RepositoryRestController
@RequestMapping("/expenses")
public class ExpenseController
{
        private PagedResourcesAssembler<Expense> pagedAssembler;
        @Autowired
        private ExpenseRepository expenseRepository;

        @Autowired
        public ExpenseController(PagedResourcesAssembler<Expense> pagedAssembler)
        {
            this.pagedAssembler = pagedAssembler;
        }

        @GetMapping(path="/{id}", produces = "application/hal+json")
        public ResponseEntity<Page<Expense>> getExpense(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling Expense GET request id: "+ id);
            List<Expense> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(id)), Expense.class, "expenses");
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }

        @GetMapping
        public ResponseEntity<Page<Expense>> getExpenses(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling Expense GET request {all}");
            List<Expense> contents =  IO.getInstance().mongoOperations().findAll(Expense.class, "expenses");
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }

        @PostMapping
        @PutMapping
        public ResponseEntity<Page<Expense>> addExpense(@RequestBody Expense expense, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling Expense creation request.");
            List<BusinessObject> contents = new LinkedList<>();
            contents.add(expense);
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }
}
