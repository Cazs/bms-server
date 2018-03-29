package server.model;

/**
 * Created by ghost on 2017/12/23.
 * @author ghost
 */
public class PurchaseOrderResource extends PurchaseOrderItem
{
    public static final String TAG = "PurchaseOrderResource";

    public PurchaseOrderResource()
    {}

    public PurchaseOrderResource(String _id)
    {
        super(_id);
    }

    @Override
    public String apiEndpoint()
    {
        return "/purchaseorders/resources";
    }
}