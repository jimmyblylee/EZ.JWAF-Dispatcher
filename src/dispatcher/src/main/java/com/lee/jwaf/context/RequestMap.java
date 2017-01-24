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
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName : RequestMap <br>
 * Description : cover {@link HttpServletRequest} attribute parameter into {@link Map} <br>
 * Create Time : 2016-09-19 <br>
 * @author jimmyblylee@126.com
 */
public class RequestMap extends AbstractMap<String, Object> implements Serializable {

    private static final long serialVersionUID = -5908350060385001361L;

    /** Mpa entries.*/
    private Set<Entry<String, Object>> entries;
    /** Servlet request. */
    private HttpServletRequest request;

    /**
     * Create a new instance of RequestMap.
     *
     * @param request the HttpServletRequest
     */
    public RequestMap(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Description: Removes all attributes from the request as well as clears entries in this map.
     *
     * @see java.util.AbstractMap#clear()
     */
    @Override
    public void clear() {
        entries = null;
        final Enumeration<?> keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            final String key = (String) keys.nextElement();
            request.removeAttribute(key);
        }
    }

    /**
     * @see java.util.AbstractMap#entrySet()
     */
    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        if (entries == null) {
            entries = new HashSet<>();
            final Enumeration<?> enumeration = request.getAttributeNames();
            //noinspection Duplicates
            while (enumeration.hasMoreElements()) {
                final String key = enumeration.nextElement().toString();
                final Object value = request.getAttribute(key);
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
                        request.setAttribute(key, obj);
                        return value;
                    }
                });
            }
        }

        return entries;
    }

    /**
     * Returns the request attribute associated with the given key or <tt>null</tt> if it doesn't exist.
     *
     * @param key the name of the request attribute.
     * @return the request attribute or <tt>null</tt> if it doesn't exist.
     */
    @Override
    public Object get(Object key) {
        return request.getAttribute(key.toString());
    }

    /**
     * Saves an attribute in the request.
     *
     * @param key the name of the request attribute.
     * @param value the value to set.
     * @return the object that was just set.
     */
    @Override
    public Object put(String key, Object value) {
        entries = null;
        request.setAttribute(key, value);
        return get(key);
    }

    /**
     * Removes the specified request attribute.
     *
     * @param key the name of the attribute to remove.
     * @return the value that was removed or <tt>null</tt> if the value was not found (and hence, not removed).
     */
    @Override
    public Object remove(Object key) {
        entries = null;
        final Object value = get(key);
        request.removeAttribute(key.toString());
        return value;
    }

}
