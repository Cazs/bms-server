package server.model;

/**
 * Created by ghost on 2017/12/22.
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

    @Override
    public String apiEndpoint()
    {
        return "/assets/types";
    }
}
