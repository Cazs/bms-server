package server.model;

/**
 * Created by ghost on 2017/01/13.
 */
public class ResourceType extends Type
{
    @Override
    public String apiEndpoint()
    {
        return "/resources/types";
    }
}
