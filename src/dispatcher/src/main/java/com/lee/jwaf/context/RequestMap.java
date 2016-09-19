/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : RequestMap.java <br>
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

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName : RequestMap <br>
 * Description : cover {@link HttpServletRequest} attribute parameter into {@link Map} <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public class RequestMap extends AbstractMap<String, Object> implements Serializable {

    private static final long serialVersionUID = -5908350060385001361L;

    private Set<Entry<String, Object>> entries;
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
     * clear: Removes all attributes from the request as well as clears entries in this map.
     * 
     * @see java.util.AbstractMap#clear()
     */
    @Override
    public void clear() {
        entries = null;
        Enumeration<?> keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            request.removeAttribute(key);
        }
    }

    /**
     * @see java.util.AbstractMap#entrySet()
     */
    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        if (entries == null) {
            entries = new HashSet<Entry<String, Object>>();
            Enumeration<?> enumeration = request.getAttributeNames();
            while (enumeration.hasMoreElements()) {
                final String key = enumeration.nextElement().toString();
                final Object value = request.getAttribute(key);
                entries.add(new Entry<String, Object>() {
                    @SuppressWarnings("unchecked")
                    public boolean equals(Object obj) {
                        Entry<Object, Object> entry = (Entry<Object, Object>) obj;
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
        request.setAttribute(key.toString(), value);
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
        Object value = get(key);
        request.removeAttribute(key.toString());
        return value;
    }

}
