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

package com.lee.jwaf.action;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;

import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.pojo.javassist.SerializableProxy;

// CSOFF: AbstractClassName
/**
 * Description: 清理代理.<br>
 * Created by Jimmybly Lee on 2017/7/5.
 *
 * @author Jimmybly Lee
 */
public abstract class ProxyStripper {

    /**
     * Strip the proxy of target entity or collections of entity.
     * @param value target entity
     * @param <T> entity Type
     * @return clean entity
     * @throws IntrospectionException can not clean
     */
    public static <T> T cleanFromProxies(T value) throws IntrospectionException {
        final T result = unproxyObject(value);
        cleanFromProxies(result, new ArrayList<>());
        return result;
    }

    private static void cleanFromProxies(Object value, ArrayList<Object> handledObjects) throws IntrospectionException {
        if ((value != null) && (!isProxy(value)) && !containsTotallyEqual(handledObjects, value)) {
            handledObjects.add(value);
            if (value instanceof Iterable) {
                for (Object item : (Iterable<?>) value) {
                    cleanFromProxies(item, handledObjects);
                }
            } else if (value.getClass().isArray()) {
                for (Object item : (Object[]) value) {
                    cleanFromProxies(item, handledObjects);
                }
            }
            final BeanInfo beanInfo = Introspector.getBeanInfo(value.getClass());
            if (beanInfo != null) {
                for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
                    try {
                        if ((property.getWriteMethod() != null) && (property.getReadMethod() != null)) {
                            Object fieldValue = property.getReadMethod().invoke(value);
                            if (isProxy(fieldValue)) {
                                fieldValue = unproxyObject(fieldValue);
                                property.getWriteMethod().invoke(value, fieldValue);
                            }
                            cleanFromProxies(fieldValue, handledObjects);
                        }
                    } catch (Exception uex) {
                        final IntrospectionException ex = new IntrospectionException("***** Get property writer() or reader() exception! *****");
                        ex.addSuppressed(uex);
                        throw ex;
                    }
                }
            }
        }
    }

    private static boolean isProxy(Object value) {
        return value != null && (value instanceof HibernateProxy || value instanceof PersistentCollection);
    }

    @SuppressWarnings("unchecked")
    private static <T> T unproxyObject(T object) {
        if (isProxy(object)) {
            if (object instanceof PersistentCollection) {
                return (T) unproxyPersistentCollection((PersistentCollection) object);
            } else if (object instanceof HibernateProxy) {
                return (T) unproxyHibernateProxy((HibernateProxy) object);
            } else {
                return null;
            }
        }
        return object;
    }

    private static Object unproxyHibernateProxy(HibernateProxy hibernateProxy) {
        final Object result = hibernateProxy.writeReplace();
        if (!(result instanceof SerializableProxy)) {
            return result;
        }
        return null;
    }

    private static Object unproxyPersistentCollection(PersistentCollection persistentCollection) {
        if (persistentCollection instanceof PersistentSet) {
            return persistentCollection.getStoredSnapshot() == null ? null : unproxyPersistentSet((Map<?, ?>) persistentCollection.getStoredSnapshot());
        }
        return persistentCollection.getStoredSnapshot();
    }

    private static <T> Set<T> unproxyPersistentSet(Map<T, ?> persistenceSet) {
        return new LinkedHashSet<>(persistenceSet.keySet());
    }

    private static boolean containsTotallyEqual(Collection<?> collection, Object value) {
        if (collection == null || collection.isEmpty()) {
            return false;
        }
        for (Object object : collection) {
            if (object == value) {
                return true;
            }
        }
        return false;
    }
}
