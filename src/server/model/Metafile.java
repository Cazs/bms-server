package server.model;

import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by th3gh0st on 2017/12/22.
 * @author th3gh0st
 */

public class Metafile extends ApplicationObject
{
    private String filename;
    private String label;
    private String path;
    private String content_type;
    private String file;//Base64 String representation of file
    //TODO: private String extra;//{"logo_options":{}, "required":false}
    public static final String TAG = "Metafile";

    public Metafile()
    {}

    public Metafile(String _id)
    {
        super(_id);
    }

    @Override
    public AccessLevel getReadMinRequiredAccessLevel()
    {
        return AccessLevel.STANDARD;
    }

    @Override
    public AccessLevel getWriteMinRequiredAccessLevel()
    {
        return AccessLevel.STANDARD;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String get_path()
    {
        return path;
    }

    public void set_path(String path)
    {
        this.path = path;
    }

    public String getContent_type()
    {
        return content_type;
    }

    public void setContent_type(String type)
    {
        this.content_type = type;
    }

    public String getFile()
    {
        return file;
    }

    public void setFile(String file)
    {
        this.file = file;
    }

    @Override
    public String[] isValid()
    {
        if(getFilename()==null)
            return new String[]{"false", "invalid filename value."};
        if(getLabel()==null)
            return new String[]{"false", "invalid label value."};
        if(get_path()==null)
            return new String[]{"false", "invalid path value."};
        if(getContent_type()==null)
            return new String[]{"false", "invalid content_type value."};

        return super.isValid();
    }

    @Override
    public void parse(String var, Object val)
    {
        super.parse(var, val);
        switch (var.toLowerCase())
        {
            case "filename":
                filename = (String)val;
                break;
            case "label":
                label=(String)val;
                break;
            case "path":
                path=(String)val;
                break;
            case "content_type":
                content_type=(String)val;
                break;
            case "file":
                file=(String)val;
                break;
            default:
                IO.log(TAG, IO.TAG_ERROR, "unknown "+TAG+" attribute '" + var + "'");
                break;
        }
    }

    @Override
    public Object get(String var)
    {
        Object val = super.get(var);
        if(val==null)
        {
            switch (var.toLowerCase())
            {
                case "filename":
                    return filename;
                case "label":
                    return label;
                case "path":
                    return path;
                case "content_type":
                    return content_type;
                case "file":
                    return file;
                default:
                    return null;
            }
        } else return val;
    }

    @Override
    public String toString()
    {
        return super.toString() + " = "  + getFilename();
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/metafile";
    }
}