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
public class FileMetadata implements BusinessObject, Serializable
{
    @Id
    private String _id;
    private String filename;
    private String label;
    private String path;
    private long date_logged;
    private String content_type;
    private String extra;//{"logo_options":{}, "required":false}
    private boolean marked;
    public static final String TAG = "FileMetadata";

    @Override
    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    @Override
    public boolean isMarked()
    {
        return marked;
    }

    @Override
    public void setMarked(boolean marked){this.marked=marked;}

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

    public long getDate_logged()
    {
        return date_logged;
    }

    public void setDate_logged(long date_logged)
    {
        this.date_logged = date_logged;
    }

    public String getExtra()
    {
        return extra;
    }

    public void setExtra(String extra)
    {
        this.extra = extra;
    }

    @Override
    public boolean isValid()
    {
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
            case "extra":
                extra=(String) val;
                break;
            default:
                IO.log(TAG, IO.TAG_ERROR, "unknown "+TAG+" attribute '" + var + "'");
                break;
        }
    }

    @Override
    public Object get(String var)
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
            case "extra":
                return extra;
            default:
                return null;
        }
    }

    @Override
    public String asJSON()
    {
        //Return encoded URL parameters in UTF-8 charset
        StringBuilder result = new StringBuilder();
        try
        {
            result.append(URLEncoder.encode("filename","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(filename), "UTF-8") + "&");
            result.append(URLEncoder.encode("label","UTF-8") + "="
                    + URLEncoder.encode(label, "UTF-8") + "&");
            result.append(URLEncoder.encode("path","UTF-8") + "="
                    + URLEncoder.encode(path, "UTF-8") + "&");
            result.append(URLEncoder.encode("content_type","UTF-8") + "="
                    + URLEncoder.encode(content_type, "UTF-8") + "&");
            if(extra!=null)
                result.append(URLEncoder.encode("extra","UTF-8") + "="
                        + URLEncoder.encode(String.valueOf(extra), "UTF-8"));

            return result.toString();
        } catch (UnsupportedEncodingException e)
        {
            IO.log(TAG, IO.TAG_ERROR, e.getMessage());
        }
        return null;
    }
}

