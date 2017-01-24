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

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.jwaf.exception.AppException;
import com.lee.jwaf.exception.WarnException;

/**
 * ClassName : WorkDTO <br>
 * Description : work DTO in the current thread <br>
 * Create Time : 2016-09-18 <br>
 *
 * @author jimmyblylee@126.com
 */
public class WorkDTO extends AbstractMap<String, Object> implements Serializable, AppConstant {

    private static final long serialVersionUID = 9046766399363299368L;

    /** Real value storage map. */
    private Map<String, Object> map;

    /**
     * Create a new instance of WorkDTO.
     *
     * @param dto the dto
     */
    public WorkDTO(Map<String, Object> dto) {
        this.map = dto;
    }

    /**
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
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for
     *     <tt>key</tt>. (A <tt>null</tt> return can also indicate that the map previously associated <tt>null</tt>
     *     with <tt>key</tt>.)
     */
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

    // ***************** dispatcher *******************//

    /**
     * Description : get controller name <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return controller name
     */
    public String getController() {
        return get(CNS_SERVER.CONTROLLER.toString());
    }

    /**
     * Description : remove the controller name parameter from dto <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     */
    public void removeController() {
        remove(CNS_SERVER.CONTROLLER.toString());
    }

    /**
     * Description : get method name <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return method name
     */
    public String getMethod() {
        return get(CNS_SERVER.METHOD.toString());
    }

    /**
     * Description : remove the method name parameter from dto <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     */
    public void removeMethod() {
        remove(CNS_SERVER.METHOD.toString());
    }

    // ***************** request *******************//

    /**
     * Description : set result list in response <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param result the result
     */
    @SuppressWarnings("unused")
    public void setResult(Object result) {
        put(CNS_REQUEST.RESULT.toString(), result);
    }

    /**
     * Description : set failed error code <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param code the error code
     * @param msg  the error message
     */
    @SuppressWarnings("WeakerAccess")
    public void setError(String code, String msg) {
        this.put(CNS_REQUEST.SUCCESS.toString(), false);
        this.put(CNS_REQUEST.ERR_CODE.toString(), code);
        this.put(CNS_REQUEST.ERR_LEVEL.toString(), CNS_REQUEST.CNS_ERROR);
        this.put(CNS_REQUEST.ERR_MESSAGE.toString(), msg);
    }

    /**
     * Description : set error or warn by given exception <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param exception the AppException with error level, code and message in it
     * @return the exception itself with no change
     */
    public AppException setIssue(AppException exception) {
        if (exception instanceof WarnException) {
            this.setWarn(exception.getErrCode(), exception.getMessage());
        } else {
            this.setError(exception.getErrCode(), exception.getMessage());
        }
        return exception;
    }

    /**
     * Description : set warning error code <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param code the error code
     * @param msg  the error message
     */
    public void setWarn(String code, String msg) {
        this.put(CNS_REQUEST.SUCCESS.toString(), false);
        this.put(CNS_REQUEST.ERR_CODE.toString(), code);
        this.put(CNS_REQUEST.ERR_LEVEL.toString(), CNS_REQUEST.CNS_WARNING);
        this.put(CNS_REQUEST.ERR_MESSAGE.toString(), msg);
    }

    /**
     * Description : set flag that dispatcher won't control the response stream <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     */
    @SuppressWarnings("unused")
    public void letMeControlTheResponseStream() {
        this.put(CNS_REQUEST.LET_ME_CTRL_THE_STREAM.toString(), true);
    }

    // ***************** file *******************//

    /**
     * Description : set file result <br>
     * Create Time: Apr 12, 2016 <br>
     * Create by : xiangyu_li@asdc.com.cn <br>
     *
     * @param fileId   the file id
     * @param fileName the name of file
     * @param url      the file url
     */
    @SuppressWarnings("unused")
    public void setFileResultSuccess(String fileId, String fileName, String url) {
        final Map<String, String> fileInfo = new HashMap<>();
        fileInfo.put("fileId", fileId);
        fileInfo.put("fileName", fileName);
        fileInfo.put("url", url);
        put(CNS_REQUEST.SUCCESS.toString(), true);
        put(CNS_REQUEST.RESULT.toString(), fileInfo);
    }

    /**
     * Description : upload file key <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return file key
     */
    @SuppressWarnings("unused")
    public String getFileKey() {
        return get(CNS_FILE.CNS_FILE_KEY.toString());
    }

    // ***************** list request *******************//

    /**
     * Description : set result totle size in response <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param totle count of list result
     */
    @SuppressWarnings("unused")
    public void setTotle(int totle) {
        put(CNS_LIST_REQUEST.TOTAL.toString(), totle);
    }

    /**
     * Description : get list request start <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return start num of list request
     */
    @SuppressWarnings("unused")
    public int getStart() {
        return getInteger(CNS_LIST_REQUEST.START.toString());
    }

    /**
     * Description : get list request limit <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return limit num of list request
     */
    @SuppressWarnings("unused")
    public int getLimit() {
        return getInteger(CNS_LIST_REQUEST.LIMIT.toString());
    }

    /**
     * Description : remove ids string splited by comma <br>
     * Create Time: 2016-09-18 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return the remove ids by string splited by comma
     */
    @SuppressWarnings("unused")
    public String getRemoveIds() {
        return this.get(CNS_LIST_REQUEST.REMOVE_IDS.toString());
    }

    /**
     * Description : convert the json in workDTO to {@code Map<String, Object} <br>
     * Create Time: Apr 12, 2016 <br>
     * Create by : xiangyu_li@asdc.com.cn <br>
     *
     * @param key the key
     * @return map result
     */
    @SuppressWarnings({"unchecked", "unused"})
    public Map<String, Object> convertJsonToMapByKey(String key) {
        if (containsKey(key)) {
            try {
                return getTemplateObjectMapper().readValue(this.<String>get(key),
                    new TypeReference<HashMap<String, Object>>() { });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return Collections.EMPTY_MAP;
    }

    /**
     * Description : convert the json in workDTO to given type <br>
     * Create Time: Apr 12, 2016 <br>
     * Create by : xiangyu_li@asdc.com.cn <br>
     *
     * @param <T>  the type you want to convert to
     * @param key  the key
     * @param type the type you want to get
     * @return converted object
     */
    @SuppressWarnings("unused")
    public <T> T convertJsonToBeanByKey(String key, Class<T> type) {
        if (containsKey(key)) {
            try {
                return getTemplateObjectMapper().readValue(this.<String>get(key), type);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Description : convert the json in workDTO to list construct with given type <br>
     * Create Time: Apr 12, 2016 <br>
     * Create by : xiangyu_li@asdc.com.cn <br>
     *
     * @param <T>  the type you want to convert to
     * @param key  the key
     * @param type the typ you want convert into
     * @return converted list of object
     */
    @SuppressWarnings("unused")
    public <T> List<T> converJsonToBeanListByKey(String key, Class<T> type) {
        if (!containsKey(key)) {
            final ObjectMapper mapper = getTemplateObjectMapper();
            try {
                return mapper.readValue(this.<String>get(key),
                    mapper.getTypeFactory().constructCollectionType(List.class, type));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @return the mapper.
     */
    private ObjectMapper getTemplateObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        mapper.configure(Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        return mapper;
    }

    // ***************** common *******************//

    /**
     * Description : get integer by key, this will translate the string into integer <br>
     * Create Time: Apr 12, 2016 <br>
     * Create by : xiangyu_li@asdc.com.cn <br>
     *
     * @param key the key
     * @return integer, null for is not a integer or bad number formated string
     */
    @SuppressWarnings("WeakerAccess")
    public Integer getInteger(String key) {
        Integer result = null;
        if (containsKey(key) && get(key) != null) {
            final Object obj = get(key);
            if (obj instanceof Integer) {
                result = (Integer) obj;
            } else {
                try {
                    result = Integer.parseInt(this.get(key).toString());
                } catch (NumberFormatException ex) {
                    // do nothing
                }
            }
        }
        return result;
    }

    /**
     * Description: get Long by key, this will translate the string to Long. <br>
     * Create Time: Oct 5, 2015 <br>
     * Create by: xiangyu_li@asdc.com.cn
     *
     * @param key the key
     * @return Long, null for is not a integer or bad number formated string
     */
    @SuppressWarnings("unused")
    public Long getLong(String key) {
        Long result = null;
        if (containsKey(key) && get(key) != null) {
            final Object obj = get(key);
            if (obj instanceof Long) {
                result = (Long) obj;
            } else {
                try {
                    result = Long.valueOf(this.get(key).toString());
                } catch (NumberFormatException ex) {
                    // do nothing
                }
            }
        }
        return result;
    }

}
