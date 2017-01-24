/* ***************************************************************************
 * EZ.JWAF/EZ.JCWAP: Easy series Production.
 * Including JWAF(Java-based Web Application Framework)
 * and JCWAP(Java-based Customized Web Application Platform).
 * Copyright (C) 2016-2017 the original author or authors.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of MIT License as published by
 * the Free Software Foundation;
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the MIT License for more details.
 *
 * You should have received a copy of the MIT License along
 * with this library; if not, write to the Free Software Foundation.
 * ***************************************************************************/

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
 *
 * @author jimmyblylee@126.com
 */
public class SessionMap extends AbstractMap<String, Object> implements Serializable {

    private static final long serialVersionUID = -8488285038786064167L;

    /** Map entries. */
    private Set<Map.Entry<String, Object>> entries;
    /** Http session. */
    private HttpSession session;
    /** Servlet request. */
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
    @SuppressWarnings("unused")
    public void invalidate() {
        if (session != null) {
            synchronized (this) {
                session.invalidate();
                session = null;
                entries = null;
            }
        }
    }

    /**
     * Description: Removes all attributes from the session as well as clears entries in this map.
     *
     * @see java.util.AbstractMap#clear()
     */
    @Override
    public void clear() {
        if (session != null) {
            //noinspection SynchronizeOnNonFinalField
            synchronized (session) {
                entries = null;
                final Enumeration<String> attributeNamesEnum = session.getAttributeNames();
                while (attributeNamesEnum.hasMoreElements()) {
                    session.removeAttribute(attributeNamesEnum.nextElement());
                }
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
        if (session == null) {
            return Collections.emptySet();
        }
        //noinspection SynchronizeOnNonFinalField
        synchronized (session) {
            if (entries == null) {
                entries = new HashSet<>();
                final Enumeration<String> enumeration = session.getAttributeNames();
                //noinspection Duplicates
                while (enumeration.hasMoreElements()) {
                    final String key = enumeration.nextElement();
                    final Object value = session.getAttribute(key);
                    //noinspection Duplicates
                    entries.add(new Map.Entry<String, Object>() {
                        public boolean equals(Object obj) {
                            return obj instanceof Entry && key.equals(((Entry) obj).getKey())
                                && value.equals(((Entry) obj).getValue());
                        }

                        public int hashCode() {
                            if (key == null || value == null) {
                                return 0;
                            } else {
                                return key.hashCode() ^ value.hashCode();
                            }
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
        if (session == null) {
            return null;
        }
        //noinspection SynchronizeOnNonFinalField
        synchronized (session) {
            return session.getAttribute(key.toString());
        }
    }

    /**
     * Saves an attribute in the session.
     *
     * @param key   the name of the session attribute.
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
        //noinspection SynchronizeOnNonFinalField
        synchronized (session) {
            entries = null;
            session.setAttribute(key, value);

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
        if (session == null) {
            return null;
        }
        //noinspection SynchronizeOnNonFinalField
        synchronized (session) {
            entries = null;
            final Object value = get(key);
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
        if (session == null) {
            return false;
        }
        //noinspection SynchronizeOnNonFinalField
        synchronized (session) {
            return session.getAttribute(key.toString()) != null;
        }
    }

}
