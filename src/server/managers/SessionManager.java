/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.managers;

import server.auxilary.IO;
import server.auxilary.Session;
import java.util.HashMap;

/**
 *
 * @author ghost
 */
public class SessionManager 
{
    private static final SessionManager sess_mgr = new SessionManager();
    private HashMap<String, Session> sessions = new HashMap<>();
    public static final String TAG = "SessionManager";
    
    private SessionManager(){};
    
    public static SessionManager getInstance()
    {
        return sess_mgr;
    }

    /**
     * Method to add Session to List of Sessions
     * @param session Session object to be added.
     */
    public void addSession(Session session)
    {
        if(session==null)
        {
            IO.logAndAlert(getClass().getName(), "Invalid session.", IO.TAG_ERROR);
            return;
        }
        //if session exists in memory, update creation date, id & ttl
        Session sess_in_mem = getUserSession(session.getUsr());
        if(sess_in_mem!=null)
        {
            sess_in_mem.setDate(session.getDate());
            sess_in_mem.setSession_id(session.getSession_id());
            sess_in_mem.setTtl(session.getTtl());
        } else {//session DNE
            sessions.put(session.getUsr(), session);//one Employee, one Session
        }
    }
    
    public HashMap<String, Session> getSessions()
    {
        return sessions;
    }
    
    public Session getUserSession(String usr)
    {
        if(getSessions()!=null)
            return getSessions().get(usr);
        else return null;
    }
}
