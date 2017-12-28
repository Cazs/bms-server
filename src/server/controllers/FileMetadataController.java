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
import server.model.BusinessObject;
import server.model.FileMetadata;
import server.repositories.FileMetadataRepository;

import java.util.LinkedList;
import java.util.List;

@RepositoryRestController
@RequestMapping("/files/metadata")
public class FileMetadataController
{
        private PagedResourcesAssembler<FileMetadata> pagedAssembler;
        @Autowired
        private FileMetadataRepository file_metadataRepository;

        @Autowired
        public FileMetadataController(PagedResourcesAssembler<FileMetadata> pagedAssembler)
        {
            this.pagedAssembler = pagedAssembler;
        }

        @GetMapping(path="/{id}", produces = "application/hal+json")
        public ResponseEntity<Page<FileMetadata>> getFileMetadata(@PathVariable("id") String id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling FileMetadata GET request id: "+ id);
            List<FileMetadata> contents = IO.getInstance().mongoOperations().find(new Query(Criteria.where("_id").is(id)), FileMetadata.class, "file_metadatas");
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }

        @GetMapping
        public ResponseEntity<Page<FileMetadata>> getFileMetadatas(Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling FileMetadata GET request {all}");
            List<FileMetadata> contents =  IO.getInstance().mongoOperations().findAll(FileMetadata.class, "file_metadatas");
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }

        @PostMapping
        @PutMapping
        public ResponseEntity<Page<FileMetadata>> addFileMetadata(@RequestBody FileMetadata file_metadata, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
        {
            IO.log(getClass().getName(), IO.TAG_INFO, "handling FileMetadata creation request.");
            List<BusinessObject> contents = new LinkedList<>();
            contents.add(file_metadata);
            return new ResponseEntity(pagedAssembler.toResource(new PageImpl(contents, pageRequest, contents.size()), (ResourceAssembler) assembler), HttpStatus.OK);
        }
}