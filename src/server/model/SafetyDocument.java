package server.model;

import server.auxilary.AccessLevel;
import server.auxilary.IO;

/**
 * Created by th3gh0st on 2018/04/26.
 * @author th3gh0st
 */
public class SafetyDocument extends ApplicationObject
{
    private Metafile document;

    public SafetyDocument()
    {}

    public SafetyDocument(String _id)
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
        return AccessLevel.SUPERUSER;
    }

    public Metafile getDocument()
    {
        return document;
    }

    public void setDocument(Metafile document)
    {
        this.document = document;
    }

    public String getDocument_title()
    {
        Metafile doc = getDocument();
        if(doc != null)
            return doc.getFilename();
        return "No document.";
    }

    public String getDocument_description()
    {
        Metafile doc = getDocument();
        if(doc != null)
            return doc.getOther();
        return "No document.";
    }

    public String getDocument_type()
    {
        Metafile doc = getDocument();
        if(doc != null)
            return doc.getContent_type();
        return "No document.";
    }

    @Override
    public String[] isValid()
    {
        // validate safety document attributes
        Metafile doc = getDocument();
        if(doc==null)
            return new String[]{"false", "invalid document object."};

        String[] doc_validation = doc.isValid();
        if(doc_validation!=null)
            if(!doc_validation[0].toLowerCase().equals("false"))
                return doc_validation;

        return super.isValid();
    }

    @Override
    public void parse(String var, Object val)
    {
        super.parse(var, val);
        try
        {
            switch (var.toLowerCase())
            {
                case "doc":
                case "document":
                    setDocument((Metafile)val);
                    break;
                default:
                    IO.log(getClass().getName(), IO.TAG_ERROR, "unknown "+getClass().getName()+" attribute '" + var + "'.");
                    break;
            }
        } catch (NumberFormatException e)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, e.getMessage());
        }
    }

    @Override
    public Object get(String var)
    {
        switch (var.toLowerCase())
        {
            case "doc":
            case "document":
                return getDocument();
        }
        return super.get(var);
    }

    @Override
    public String toString()
    {
        return super.toString() + " = "  + getDocument_title();
    }

    /**
     * @return this model's root endpoint URL.
     */
    @Override
    public String apiEndpoint()
    {
        return "/document/safety";
    }
}
