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
public class Employee extends BusinessObject
{
    private String usr;
    private String pwd;//hashed
    private String firstname;
    private String lastname;
    private String gender;
    private String email;
    private String tel;
    private String cell;
    private int access_level;
    private boolean active;
    public static final String TAG = "Employee";
    public static int ACCESS_LEVEL_NONE = 0;
    public static int ACCESS_LEVEL_NORMAL = 1;
    public static int ACCESS_LEVEL_ADMIN = 2;
    public static int ACCESS_LEVEL_SUPER = 3;

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
        super.isValid();
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
        super.parse(var, val);
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
                case "active":
                    setActive(Boolean.parseBoolean((String)val));
                    break;
                default:
                    IO.log(TAG, IO.TAG_WARN, String.format("unknown "+getClass().getName()+" attribute '%s'", var));
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
        Object val = super.get(var);
        if(val==null)
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
                case "active":
                    return active;
                default:
                    IO.log(TAG, IO.TAG_WARN, String.format("unknown "+getClass().getName()+" attribute '%s'", var));
                    return null;
            }
        } else return val;
    }

    @Override
    public String toString()
    {
        //return String.format("[id = %s, firstname = %s, lastname = %s]", get_id(), getFirstname(), getLastname());
        return "{\"_id\":\""+get_id()+"\", "+
                "\"lastname\":\""+lastname+"\","+
                "\"usr\":\""+usr+"\","+
                "\"pwd\":\""+pwd+"\","+
                "\"access_level\":\""+access_level+"\","+
                "\"gender\":\""+gender+"\","+
                "\"email\":\""+email+"\","+
                "\"tel\":\""+tel+"\","+
                "\"cell\":\""+cell+"\","+
                "\"date_logged\":\""+getDate_logged()+"\","+
                "\"active\":\""+active+"\","+
                "\"other\":\""+getOther()+"\"}";
    }

    public String getInitials(){return new String(firstname.substring(0,1) + lastname.substring(0,1));}
}