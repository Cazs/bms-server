/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import org.springframework.data.annotation.Id;
import server.auxilary.IO;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 * @author ghost
 */
public class Employee implements BusinessObject, Serializable
{
    @Id
    private String _id;
    private String usr;
    private String pwd;//hashed
    private String firstname;
    private String lastname;
    private String gender;
    private String email;
    private long date_joined;
    private String tel;
    private String cell;
    private int access_level;
    private boolean active;
    private String other;
    private boolean marked;
    public static final String TAG = "Employee";
    public static int ACCESS_LEVEL_NONE = 0;
    public static int ACCESS_LEVEL_NORMAL = 1;
    public static int ACCESS_LEVEL_ADMIN = 2;
    public static int ACCESS_LEVEL_SUPER = 3;

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

    public String getUsr()
    {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getPwd()
    {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getAccessLevel() {
        return access_level;
    }

    public void setAccessLevel(int access_level)
    {
        this.access_level = access_level;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getOther() 
    {
        return other;
    }

    public void setOther(String other) 
    {
        this.other = other;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public long getDate_joined()
    {
        return date_joined;
    }

    public void setDate_joined(long date_joined)
    {
        this.date_joined = date_joined;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public String getCell()
    {
        return cell;
    }

    public void setCell(String cell)
    {
        this.cell = cell;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    @Override
    public boolean isValid()
    {
        if(getUsr()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid usr value.");
            return false;
        }
        if(getPwd()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid pwd value.");
            return false;
        }
        if(getFirstname()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid firstname value.");
            return false;
        }
        if(getLastname()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid lastname value.");
            return false;
        }
        if(getDate_joined()<=0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid date_joined value.");
            return false;
        }
        if(getCell()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid cell value.");
            return false;
        }
        if(getTel()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid tel value.");
            return false;
        }
        if(getEmail()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid email value.");
            return false;
        }
        if(getGender()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid gender value.");
            return false;
        }
        if(getPwd()==null)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid pwd value.");
            return false;
        }
        if(getAccessLevel()<0)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, "invalid access_level value.");
            return false;
        }

        IO.log(getClass().getName(), IO.TAG_INFO,  "valid " + getClass().getName() + " object.");
        return true;
    }

    @Override
    public void parse(String var, Object val)
    {
        try
        {
            switch (var.toLowerCase())
            {
                case "firstname":
                    setFirstname((String)val);
                    break;
                case "lastname":
                    setLastname((String)val);
                    break;
                case "usr":
                    setUsr((String)val);
                    break;
                case "gender":
                    setGender((String)val);
                    break;
                case "email":
                    setEmail((String)val);
                    break;
                case "access_level":
                    setAccessLevel(Integer.parseInt((String)val));
                    break;
                case "tel":
                    setTel((String)val);
                    break;
                case "cell":
                    setCell((String)val);
                    break;
                case "date_joined":
                    setDate_joined(Long.parseLong(String.valueOf(val)));
                    break;
                case "active":
                    setActive(Boolean.parseBoolean((String)val));
                    break;
                case "other":
                    setOther((String)val);
                    break;
                default:
                    IO.log(TAG, IO.TAG_WARN, String.format("unknown Employee attribute '%s'", var));
                    break;
            }
        }catch (NumberFormatException e)
        {
            IO.log(getClass().getName(), IO.TAG_ERROR, e.getMessage());
        }
    }

    @Override
    public Object get(String var)
    {
        switch (var.toLowerCase())
        {
            case "firstname":
                return firstname;
            case "lastname":
                return lastname;
            case "usr":
                return usr;
            case "access_level":
                return access_level;
            case "gender":
                return gender;
            case "email":
                return email;
            case "tel":
                return tel;
            case "cell":
                return cell;
            case "date_joined":
                return date_joined;
            case "active":
                return active;
            case "other":
                return other;
            default:
                IO.log(TAG, IO.TAG_WARN, String.format("unknown Employee attribute '%s'", var));
                return null;
        }
    }

    @Override
    public String toString()
    {
        //return String.format("[id = %s, firstname = %s, lastname = %s]", get_id(), getFirstname(), getLastname());
        return "{\"_id\":\""+_id+"\", "+
                "\"lastname\":\""+lastname+"\","+
                "\"usr\":\""+usr+"\","+
                "\"pwd\":\""+pwd+"\","+
                "\"access_level\":\""+access_level+"\","+
                "\"gender\":\""+gender+"\","+
                "\"email\":\""+email+"\","+
                "\"tel\":\""+tel+"\","+
                "\"cell\":\""+cell+"\","+
                "\"date_joined\":\""+date_joined+"\","+
                "\"active\":\""+active+"\","+
                "\"other\":\""+other+"\"}";
    }

    public String getInitials(){return new String(firstname.substring(0,1) + lastname.substring(0,1));}

    @Override
    public String asJSON()
    {
        //Return encoded URL parameters in UTF-8 charset
        StringBuilder result = new StringBuilder();
        try
        {
            result.append(URLEncoder.encode("usr","UTF-8") + "="
                    + URLEncoder.encode(usr, "UTF-8") + "&");
            result.append(URLEncoder.encode("pwd","UTF-8") + "="
                    + URLEncoder.encode(pwd, "UTF-8") + "&");
            result.append(URLEncoder.encode("access_level","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(access_level), "UTF-8") + "&");
            result.append(URLEncoder.encode("firstname","UTF-8") + "="
                    + URLEncoder.encode(firstname, "UTF-8") + "&");
            result.append(URLEncoder.encode("lastname","UTF-8") + "="
                    + URLEncoder.encode(lastname, "UTF-8") + "&");
            result.append(URLEncoder.encode("gender","UTF-8") + "="
                    + URLEncoder.encode(gender, "UTF-8") + "&");
            result.append(URLEncoder.encode("email","UTF-8") + "="
                    + URLEncoder.encode(email, "UTF-8") + "&");
            result.append(URLEncoder.encode("tel","UTF-8") + "="
                    + URLEncoder.encode(tel, "UTF-8") + "&");
            result.append(URLEncoder.encode("cell","UTF-8") + "="
                    + URLEncoder.encode(cell, "UTF-8") + "&");
            result.append(URLEncoder.encode("date_joined","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(date_joined), "UTF-8") + "&");
            result.append(URLEncoder.encode("active","UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(active), "UTF-8"));
            if(other!=null)
                if(!other.isEmpty())
                    result.append("&" + URLEncoder.encode("other","UTF-8") + "="
                        + URLEncoder.encode(other, "UTF-8"));
            return result.toString();
        } catch (UnsupportedEncodingException e)
        {
            IO.log(TAG, IO.TAG_ERROR, e.getMessage());
        }
        return null;
    }
}
