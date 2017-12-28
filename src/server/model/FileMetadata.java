package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ghost on 2017/02/24.
 */
public class FileMetadata extends BusinessObject
{
    private String filename;
    private String label;
    private String path;
    private String content_type;
    //TODO: private String extra;//{"logo_options":{}, "required":false}
    public static final String TAG = "FileMetadata";

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

    @Override
    public boolean isValid()
    {
        super.isValid();
        if(getFilename()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid filename value.");
            return false;
        }
        if(getLabel()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid label value.");
            return false;
        }
        if(get_path()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid creator value.");
            return false;
        }
        if(getContent_type()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid content_type value.");
            return false;
        }
        if(getDate_logged()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_logged value.");
            return false;
        }

        IO.log(getClass().getName(), IO.TAG_INFO,  "valid " + getClass().getName() + " object.");
        return true;
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
                default:
                    return null;
            }
        } else return val;
    }
}

