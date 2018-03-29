package server.model;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

public class AssetType extends Type
{
    public AssetType()
    {}

    public AssetType(String _id)
    {
        super(_id);
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/asset/type";
    }
}