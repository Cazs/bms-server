package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.Asset;
import server.model.ApplicationObject;
import server.repositories.AssetRepository;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class AssetController extends APIController
{
    private PagedResourcesAssembler<Asset> pagedAssembler;
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    public AssetController(PagedResourcesAssembler<Asset> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/asset/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends ApplicationObject>> getAsset(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new Asset(id), "_id", session_id, "assets", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/assets")
    public ResponseEntity<Page<? extends ApplicationObject>> getAssets(@RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new Asset(), session_id, "assets", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/asset")
    public ResponseEntity<String> addAsset(@RequestBody Asset asset, @RequestHeader String session_id)
    {
        return putBusinessObject(asset, session_id, "assets", "assets_timestamp");
    }

    @PostMapping("/asset")
    public ResponseEntity<String> patchAsset(@RequestBody Asset asset, @RequestHeader String session_id)
    {
        return patchBusinessObject(asset, session_id, "assets", "assets_timestamp");
    }

    @DeleteMapping(path = "/asset/{asset_id}")
    public ResponseEntity<String> delete(@PathVariable String asset_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new Asset(asset_id), session_id, "assets", "assets_timestamp");
    }
}
