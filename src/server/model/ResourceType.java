package server.model;

/**
 * Created by th3gh0st on 2017/12/22.
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

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/resource/type";
    }
}
