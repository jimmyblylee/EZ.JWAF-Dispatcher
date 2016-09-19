/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : SessionMap.java <br>
 * Package Name : com.lee.jwaf.context <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.context;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * ClassName : SessionMap <br>
 * Description : cover {@link HttpSession} into {@link Map} <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public class SessionMap extends AbstractMap<String, Object> implements Serializable {

    private static final long serialVersionUID = -8488285038786064167L;

    private Set<Map.Entry<String, Object>> entries;
    private HttpSession session;
    private HttpServletRequest request;

    /**
     * Create a new instance of SessionMap.
     * 
     * @param request the HttpServletRequest
     */
    public SessionMap(HttpServletRequest request) {
        this.request = request;
        this.session = request.getSession(false);
    }

    /**
     * Description : Invalidates this session then unbinds any objects bound to it <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @see javax.servlet.http.HttpSession#invalidate()
     */
    public void invalidate() {
        if (session == null) { return; }
        synchronized (session) {
            session.invalidate();
            session = null;
            entries = null;
        }
    }

    /**
     * clear: Removes all attributes from the session as well as clears entries in this map.
     * 
     * @see java.util.AbstractMap#clear()
     */
    @Override
    public void clear() {
        if (session == null) { return; }
        synchronized (session) {
            entries = null;
            Enumeration<String> attributeNamesEnum = session.getAttributeNames();
            while (attributeNamesEnum.hasMoreElements()) {
                session.removeAttribute(attributeNamesEnum.nextElement());
            }
        }

    }

    /**
     * Returns a Set of attributes from the http session.
     * 
     * @return a Set of attributes from the http session.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        if (session == null) { return Collections.emptySet(); }
        synchronized (session) {
            if (entries == null) {
                entries = new HashSet<Map.Entry<String, Object>>();
                Enumeration<? extends Object> enumeration = session.getAttributeNames();
                while (enumeration.hasMoreElements()) {
                    final String key = enumeration.nextElement().toString();
                    final Object value = session.getAttribute(key);
                    entries.add(new Map.Entry<String, Object>() {
                        public boolean equals(Object obj) {
                            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) obj;
                            return ((key == null) ? (entry.getKey() == null) : key.equals(entry.getKey()))
                                    && ((value == null) ? (entry.getValue() == null) : value.equals(entry.getValue()));
                        }

                        public int hashCode() {
                            return ((key == null) ? 0 : key.hashCode()) ^ ((value == null) ? 0 : value.hashCode());
                        }

                        public String getKey() {
                            return key;
                        }

                        public Object getValue() {
                            return value;
                        }

                        public Object setValue(Object obj) {
                            session.setAttribute(key, obj);
                            return value;
                        }
                    });
                }
            }
        }
        return entries;
    }

    /**
     * Returns the session attribute associated with the given key or <tt>null</tt> if it doesn't exist.
     * 
     * @param key the name of the session attribute.
     * @return the session attribute or <tt>null</tt> if it doesn't exist.
     */
    @Override
    public Object get(Object key) {
        if (session == null) { return null; }
        synchronized (session) {
            return session.getAttribute(key.toString());
        }
    }

    /**
     * Saves an attribute in the session.
     * 
     * @param key the name of the session attribute.
     * @param value the value to set.
     * @return the object that was just set.
     */
    @Override
    public Object put(String key, Object value) {
        synchronized (this) {
            if (session == null) {
                session = request.getSession(true);
            }
        }
        synchronized (session) {
            entries = null;
            session.setAttribute(key.toString(), value);

            return get(key);
        }
    }

    /**
     * Removes the specified session attribute.
     * 
     * @param key the name of the attribute to remove.
     * @return the value that was removed or <tt>null</tt> if the value was not found (and hence, not removed).
     */
    @Override
    public Object remove(Object key) {
        if (session == null) { return null; }
        synchronized (session) {
            entries = null;
            Object value = get(key);
            session.removeAttribute(key.toString());
            return value;
        }
    }

    /**
     * Checks if the specified session attribute with the given key exists.
     * 
     * @param key the name of the session attribute.
     * @return <tt>true</tt> if the session attribute exits or <tt>false</tt> if it doesn't exist.
     */
    @Override
    public boolean containsKey(Object key) {
        if (session == null) { return false; }
        synchronized (session) {
            return (session.getAttribute(key.toString()) != null);
        }
    }

}
