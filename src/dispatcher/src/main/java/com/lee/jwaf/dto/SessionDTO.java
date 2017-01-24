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

package com.lee.jwaf.dto;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import com.lee.jwaf.token.Token;

/**
 * ClassName : SessionDTO <br>
 * Description : session DTO in each thread <br>
 * Create Time : 2016-09-19 <br>
 * @author jimmyblylee@126.com
 */
public class SessionDTO extends AbstractMap<String, Object> implements Serializable, AppConstant {

    private static final long serialVersionUID = -4223597068936675755L;

    /** Real value storage map. */
    private Map<String, Object> map;

    /**
     * @param session the sessionmap.
     */
    public SessionDTO(Map<String, Object> session) {
        map = session;
    }

    /*
     * (non-Javadoc)
     * @see java.util.AbstractMap#entrySet()
     */
    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the
     * key.
     *
     * <p>More formally, if this map contains a mapping from a key {@code k} to a value {@code v} such that
     * {@code (key==null ? k==null : key.equals(k))} , then this method returns {@code v}; otherwise it returns
     * {@code null}. (There can be at most one such mapping.)
     *
     * <p>A return value of {@code null} does not <i>necessarily</i> indicate
     * that the map contains no mapping for the key;
     * it's also possible that the map explicitly maps the key to {@code null}. The {@link #containsKey containsKey}
     * operation may be used to distinguish these two cases.
     *
     * @param <T> the type which the caller needs
     * @param key the key
     * @return the value
     * @see #put(Object, Object)
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) map.get(key);
    }

    /**
     * Associates the specified value with the specified key in this map. If the map previously contained a mapping for
     * the key, the old value is replaced.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for
     *         <tt>key</tt>. (A <tt>null</tt> return can also indicate that the map previously associated <tt>null</tt>
     *         with <tt>key</tt>.)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    /*
     * (non-Javadoc)
     * @see java.util.AbstractMap#putAll(java.util.Map)
     */
    @Override
    public void putAll(Map<? extends String, ?> m) {
        map.putAll(m);
    }

    /*
     * (non-Javadoc)
     * @see java.util.AbstractMap#remove(java.lang.Object)
     */
    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    /*
     * (non-Javadoc)
     * @see java.util.AbstractMap#clear()
     */
    @Override
    public void clear() {
        map.clear();
    }

    /**
     * Description : clear the user token from the session <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     */
    @SuppressWarnings("unused")
    public void clearCurrentToken() {
        if (containsKey(CNS_SESSIONDTO.TOKEN_IN_SESSION.toString())) {
            remove(CNS_SESSIONDTO.TOKEN_IN_SESSION.toString());
        }
    }

    /**
     * Description : set user token into the session <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param token the user token
     */
    @SuppressWarnings("unused")
    public void setActiveUser(Token token) {
        put(CNS_SESSIONDTO.TOKEN_IN_SESSION.toString(), token);
    }

    /**
     * Description : get the current user token from the sesison <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return user token
     */
    @SuppressWarnings("unused")
    public Token currentToken() {
        return get(CNS_SESSIONDTO.TOKEN_IN_SESSION.toString());
    }

    /**
     * Description : whether the session has a user token <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return true if there is a token in the current thread
     */
    @SuppressWarnings("unused")
    public boolean hasToken() {
        return containsKey(CNS_SESSIONDTO.TOKEN_IN_SESSION.toString());
    }

}
