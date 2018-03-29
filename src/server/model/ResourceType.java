package server.model;

/**
 * Created by ghost on 2017/12/22.
 * @author th3gh0st
 */
public class ResourceType extends Type
{
    public ResourceType()
    {}

    public ResourceType(String _id)
    {
        super(_id);
    }

    @Override
    public String apiEndpoint()
    {
        return "/resources/types";
    }
}
