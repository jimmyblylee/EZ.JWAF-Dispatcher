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

import javax.servlet.ServletContext;

/**
 * ClassName : ApplicationMap <br>
 * Description : cover {@link ServletContext} into {@link Map} <br>
 * Create Time : 2016-09-19 <br>
 * @author jimmyblylee@126.com
 */
public class ApplicationMap extends AbstractMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 2607913711137553681L;

    /** The servlet context. */
    private ServletContext context;
    /** The map entries. */
    private Set<Entry<String, Object>> entries;

    /**
     * Create a new instance of ApplicationMap.
     *
     * @param context the context
     */
    public ApplicationMap(ServletContext context) {
        this.context = context;
    }

    /**
     * Removes all entries from the Map and removes all attributes from the servlet context.
     */
    public void clear() {
        entries = null;
        final Enumeration<?> e = context.getAttributeNames();
        while (e.hasMoreElements()) {
            context.removeAttribute(e.nextElement().toString());
        }
    }

    /**
     * Creates a Set of all servlet context attributes as well as context init parameters.
     *
     * @return a Set of all servlet context attributes as well as context init parameters.
     */
    public Set<Entry<String, Object>> entrySet() {
        if (entries == null) {
            entries = new HashSet<>();

            // Add servlet context attributes
            Enumeration<?> enumeration = context.getAttributeNames();

            //noinspection Duplicates
            while (enumeration.hasMoreElements()) {
                final String key = enumeration.nextElement().toString();
                final Object value = context.getAttribute(key);
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
                        context.setAttribute(key, obj);
                        return value;
                    }
                });
            }

            // Add servlet context init params
            enumeration = context.getInitParameterNames();

            //noinspection Duplicates
            while (enumeration.hasMoreElements()) {
                final String key = enumeration.nextElement().toString();
                final Object value = context.getAttribute(key);
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
                        context.setAttribute(key, obj);
                        return value;
                    }
                });
            }

        }

        return entries;
    }

    /**
     * Returns the servlet context attribute or init parameter based on the given key. If the entry is not found,
     * <tt>null</tt> is returned.
     *
     * @param key the entry key.
     * @return the servlet context attribute or init parameter or <tt>null</tt> if the entry is not found.
     */
    public Object get(Object key) {
        // Try context attributes first, then init params
        // This gives the proper shadowing effects
        final String keyString = key.toString();
        final Object value = context.getAttribute(keyString);
        return (value == null) ? context.getInitParameter(keyString) : value;
    }

    /**
     * Sets a servlet context attribute given a attribute name and value.
     *
     * @param key the name of the attribute.
     * @param value the value to set.
     * @return the attribute that was just set.
     */
    public Object put(String key, Object value) {
        entries = null;
        context.setAttribute(key, value);
        return get(key);
    }

    /**
     * Removes the specified servlet context attribute.
     *
     * @param key the attribute to remove.
     * @return the entry that was just removed.
     */
    public Object remove(Object key) {
        entries = null;
        final Object value = get(key);
        context.removeAttribute(key.toString());
        return value;
    }

}
