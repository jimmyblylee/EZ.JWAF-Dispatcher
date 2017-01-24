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

package com.lee.jwaf.log;

import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.lee.util.ReflectionUtils;

/**
 * ClassName : LoggerPostProcesor <br>
 * Description : inject log into every {@link Log} annotated field by spring <br>
 * Create Time : 2016-09-19 <br>
 * @author  jimmyblylee@126.com
 */
public final class LoggerPostProcesor implements BeanPostProcessor {

    /**
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object,
     *      java.lang.String)
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object,
     *      java.lang.String)
     */
    @Override
    public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            if (field.getAnnotation(Log.class) != null) {
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, bean, LoggerFactory.getLogger(bean.getClass()));
            }
        });
        return bean;
    }
}
