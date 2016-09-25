/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : ApplicationMap.java <br>
 * Package Name : com.lee.jwaf.context <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
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
 * Create by : jimmyblylee@126.com
 */
public class ApplicationMap extends AbstractMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 2607913711137553681L;

    private ServletContext context;
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
        Enumeration<?> e = context.getAttributeNames();
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
            entries = new HashSet<Entry<String, Object>>();

            // Add servlet context attributes
            Enumeration<?> enumeration = context.getAttributeNames();

            while (enumeration.hasMoreElements()) {
                final String key = enumeration.nextElement().toString();
                final Object value = context.getAttribute(key);
                entries.add(new Map.Entry<String, Object>() {
                    @SuppressWarnings("unchecked")
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
                        context.setAttribute(key, obj);
                        return value;
                    }
                });
            }

            // Add servlet context init params
            enumeration = context.getInitParameterNames();

            while (enumeration.hasMoreElements()) {
                final String key = enumeration.nextElement().toString();
                final Object value = context.getInitParameter(key);
                entries.add(new Map.Entry<String, Object>() {
                    @SuppressWarnings("unchecked")
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
        String keyString = key.toString();
        Object value = context.getAttribute(keyString);
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
        context.setAttribute(key.toString(), value);
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
        Object value = get(key);
        context.removeAttribute(key.toString());
        return value;
    }

}
