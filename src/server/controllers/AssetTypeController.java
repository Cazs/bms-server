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
import server.model.AssetType;
import server.repositories.AssetTypeRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class AssetTypeController extends APIController
{
    private PagedResourcesAssembler<AssetType> pagedAssembler;
    @Autowired
    private AssetTypeRepository asset_typeRepository;

    @Autowired
    public AssetTypeController(PagedResourcesAssembler<AssetType> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/asset/types/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getAssetType(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new AssetType(id), "_id", session_id, "asset_types", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/assets/types")
    public ResponseEntity<Page<? extends ApplicationObject>> getAssetTypes(Pageable pageRequest, @RequestHeader String session_id, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new AssetType(), session_id, "asset_types", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/asset/type")
    public ResponseEntity<String> addAssetType(@RequestBody AssetType asset_type, @RequestHeader String session_id)
    {
        return putBusinessObject(asset_type, session_id,  "asset_types", "assets_timestamp");
    }

    @PostMapping("/asset/type")
    public ResponseEntity<String> patchAssetType(@RequestBody AssetType asset_type, @RequestHeader String session_id)
    {
        return patchBusinessObject(asset_type, session_id, "asset_types", "assets_timestamp");
    }

    @DeleteMapping(path = "/asset/type/{asset_type_id}")
    public ResponseEntity<String> delete(@PathVariable String asset_type_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new AssetType(asset_type_id), session_id, "asset_types", "assets_timestamp");
    }
}
