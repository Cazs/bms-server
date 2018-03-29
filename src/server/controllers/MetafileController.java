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
import server.model.Metafile;
import server.repositories.MetafilesRepository;

/**
 * Created by ghost on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class MetafileController extends APIController
{
    private PagedResourcesAssembler<Metafile> pagedAssembler;
    @Autowired
    private MetafilesRepository file_metadataRepository;

    @Autowired
    public MetafileController(PagedResourcesAssembler<Metafile> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/metafile/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends BusinessObject>> getFileMetadata(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Metafile(id), "_id", session_id, "metafiles", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/metafiles")
    public ResponseEntity<Page<? extends BusinessObject>> getFileMetadatas(Pageable pageRequest, @RequestHeader String session_id, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Metafile(), session_id, "metafiles", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/metafile")
    public ResponseEntity<String> addFileMetadata(@RequestBody Metafile file_metadata, @RequestHeader String session_id)
    {
        return putBusinessObject(file_metadata, session_id, "metafiles", "metafiles_timestamp");
    }

    @PostMapping("/metafile")
    public ResponseEntity<String> patchExpense(@RequestBody Metafile file_metadata, @RequestHeader String session_id)
    {
        return patchBusinessObject(file_metadata, session_id, "metafiles", "metafiles_timestamp");
    }

    @DeleteMapping(path="/metafile/{metafile_id}")
    public ResponseEntity<String> delete(@PathVariable String metafile_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Metafile(metafile_id), session_id, "metafiles", "metafiles_timestamp");
    }
}
