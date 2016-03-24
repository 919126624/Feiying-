package com.wuyizhiye.framework.listener;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

/**
 * @ClassName LocalSessionContext
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
public class LocalSessionContext {
	private static HashMap sessionMap = new HashMap();

    public static synchronized void AddSession(HttpSession session) {
        if (session != null) {
        	sessionMap.put(session.getId(), session);
        }
    }

    public static synchronized void DelSession(HttpSession session) {
        if (session != null) {
        	sessionMap.remove(session.getId());
        }
    }

    public static synchronized HttpSession getSession(String session_id) {
        if (session_id == null) 
        return null;
        return (HttpSession) sessionMap.get(session_id);
    }

}
