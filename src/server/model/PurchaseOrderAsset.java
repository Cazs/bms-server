package server.model;

/**
 * Created by th3gh0st on 2017/12/23.
 * @author th3gh0st
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

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/purchaseorder/asset";
    }
}