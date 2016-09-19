/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : ParameterMap.java <br>
 * Package Name : com.lee.jwaf.context <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.context;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName : ParameterMap <br>
 * Description : cover {@link HttpServletRequest#getParameter(String)} parameter of request into {@link Map} <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public class ParameterMap extends AbstractMap<String, String[]> implements Serializable {

    private static final long serialVersionUID = 2176770669304823533L;

    private Map<String, String[]> map;

    /**
     * Create a new instance of ParameterMap.
     * 
     * @param request the HttpServletRequest
     */
    public ParameterMap(HttpServletRequest request) {
        map = request.getParameterMap();
    }

    /**
     * @see java.util.AbstractMap#entrySet()
     * @return the entry set
     */
    @Override
    public Set<java.util.Map.Entry<String, String[]>> entrySet() {
        return map.entrySet();
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the key.
     * 
     * <p>
     * More formally, if this map contains a mapping from a key {@code k} to a value {@code v} such that {@code (key==null ? k==null : key.equals(k))} , then
     * this method returns {@code v}; otherwise it returns {@code null}. (There can be at most one such mapping.)
     * 
     * <p>
     * A return value of {@code null} does not <i>necessarily</i> indicate that the map contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}. The {@link #containsKey containsKey} operation may be used to distinguish these two cases.
     * 
     * @param key the key
     * @return the parameter
     * @see AbstractMap#put(Object, Object)
     */
    public String[] get(String key) {
        return map.get(key);
    }

}
