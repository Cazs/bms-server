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
import server.model.Client;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class ClientController extends APIController
{
    private PagedResourcesAssembler<Client> pagedAssembler;

    @Autowired
    public ClientController(PagedResourcesAssembler<Client> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/client/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getClientById(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Client(id), "_id", session_id, "clients", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/clients")
    public ResponseEntity<Page<? extends ApplicationObject>> getAllClients(@RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Client(), session_id, "clients", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/client")
    public ResponseEntity<String> addClient(@RequestBody Client client, @RequestHeader String session_id)
    {
        return putBusinessObject(client, session_id, "clients", "clients_timestamp");
    }

    @PostMapping("/client")
    public ResponseEntity<String> patchClient(@RequestBody Client client, @RequestHeader String session_id)
    {
        return patchBusinessObject(client, session_id, "clients", "clients_timestamp");
    }

    @DeleteMapping(path = "/client/{client_id}")
    public ResponseEntity<String> delete(@PathVariable String client_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Client(client_id), session_id, "clients", "clients_timestamp");
    }
}
