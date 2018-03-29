package server.model;

/**
 * Created by ghost on 2017/12/23.
 * @author ghost
 */
public class PurchaseOrderAsset extends PurchaseOrderItem
{
    public static final String TAG = "PurchaseOrderAsset";

    public PurchaseOrderAsset()
    {}

    public PurchaseOrderAsset(String _id)
    {
        super(_id);
    }

    @Override
    public String apiEndpoint()
    {
        return "/purchaseorders/assets";
    }
}