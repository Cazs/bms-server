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
import server.model.PurchaseOrderAsset;
import server.repositories.PurchaseOrderAssetRepository;

/**
 * Created by ghost on 2017/12/22.
 * @author th3gh0st
 */

@RepositoryRestController
public class PurchaseOrderAssetController extends APIController
{
    private PagedResourcesAssembler<PurchaseOrderAsset> pagedAssembler;
    @Autowired
    private PurchaseOrderAssetRepository purchase_order_assetRepository;

    @Autowired
    public PurchaseOrderAssetController(PagedResourcesAssembler<PurchaseOrderAsset> pagedAssembler)
    {
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping(path="/purchaseorder/assets/{id}", produces = "application/hal+json")
    public ResponseEntity<Page<? extends BusinessObject>> getPurchaseOrderAsset(@PathVariable("id") String id, @RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObject(new PurchaseOrderAsset(id), "_id", session_id, "purchase_order_assets", pagedAssembler, assembler, pageRequest);
    }

    @GetMapping("/purchaseorders/assets")
    public ResponseEntity<Page<? extends BusinessObject>> getPurchaseOrderAssets(@RequestHeader String session_id, Pageable pageRequest, PersistentEntityResourceAssembler assembler)
    {
        return getBusinessObjects(new PurchaseOrderAsset(), session_id, "purchase_order_assets", pagedAssembler, assembler, pageRequest);
    }

    @PutMapping("/purchaseorder/asset")
    public ResponseEntity<String> addPurchaseOrderAsset(@RequestBody PurchaseOrderAsset purchase_order_asset, @RequestHeader String session_id)
    {
        return putBusinessObject(purchase_order_asset, session_id, "purchase_order_assets", "purchase_orders_timestamp");
    }

    @PostMapping("/purchaseorder/asset")
    public ResponseEntity<String> patchPurchaseOrderAsset(@RequestBody PurchaseOrderAsset purchase_order_asset, @RequestHeader String session_id)
    {
        return patchBusinessObject(purchase_order_asset, session_id, "purchase_order_assets", "purchase_orders_timestamp");
    }

    @DeleteMapping(path="/purchaseorder/asset/{purchaseorder_id}")
    public ResponseEntity<String> delete(@PathVariable String purchaseorder_id, @RequestHeader String session_id)
    {
        return deleteBusinessObject(new PurchaseOrderAsset(purchaseorder_id), session_id, "purchase_order_assets", "purchase_orders_timestamp");
    }
}
