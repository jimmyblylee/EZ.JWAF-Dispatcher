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

import static com.lee.jwaf.message.Messages.Msg;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.ContextLoader;

import com.lee.jwaf.context.ActionContext;
import com.lee.jwaf.context.ApplicationMap;
import com.lee.jwaf.context.Context;
import com.lee.jwaf.context.ParameterMap;
import com.lee.jwaf.context.RequestMap;
import com.lee.jwaf.context.SessionMap;
import com.lee.jwaf.context.bind.ApplicationAware;
import com.lee.jwaf.context.bind.ParameterAware;
import com.lee.jwaf.context.bind.RequestAware;
import com.lee.jwaf.context.bind.ServletRequestAware;
import com.lee.jwaf.context.bind.ServletResponseAware;
import com.lee.jwaf.context.bind.SessionAware;
import com.lee.jwaf.dto.ApplicationDTO;
import com.lee.jwaf.dto.SessionDTO;
import com.lee.jwaf.dto.WorkDTO;
import com.lee.jwaf.dto.bind.ApplicationDTOAware;
import com.lee.jwaf.dto.bind.SessionDTOAware;
import com.lee.jwaf.dto.bind.WorkDTOAware;
import com.lee.jwaf.exception.AppException;
import com.lee.jwaf.exception.WarnException;
import com.lee.util.ObjectUtils;
import com.lee.util.ReflectionUtils;
import com.lee.util.StringUtils;

/**
 * ClassName : DispatchExecutor <br>
 * Description : controller dispatch executer <br>
 * Create Time : 2016-09-19 <br>
 *
 * @author jimmyblylee@126.com
 */
@SuppressWarnings({"checkstyle:classdataabstractioncoupling", "checkstyle:classfanoutcomplexity"})
public final class DispatchExecutor {

    /** Local thread instance of {@link DispatchExecutor}. */
    private static ThreadLocal<DispatchExecutor> instance = new ThreadLocal<>();
    /** Dispatcher name. */
    private static final String BUNDLE_NAME = "dispatcher";

    /**
     * Description : dispatch the request to the target controller by given parameters<br>
     * Create Time: Apr 12, 2016 <br>
     * Create by : xiangyu_li@asdc.com.cn <br>
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws Exception    unknown Exception
     * @throws AppException many cases
     */
    @SuppressWarnings("WeakerAccess")
    public void serviceAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final WorkDTO dto = ActionContext.getContext().getWorkDTO();

        final String controllerName = dto.getController();
        final String methodName = dto.getMethod();
        final Logger dispatcherLog = LoggerFactory.getLogger(getClass());
        dispatcherLog.trace("request info : controller name is {} and method name is {}", controllerName, methodName);
        dto.removeController();
        dto.removeMethod();

        final Object bean = getValidController(dto, controllerName);
        final Method method = getValidMethod(dto, methodName, bean);

        // bind data to target bean
        bindData(bean);

        final Logger log = LoggerFactory.getLogger(bean.getClass());
        log.debug(method.getName() + " start");
        try {
            method.invoke(bean);
        } catch (InvocationTargetException ex) {
            // deal with the real cause exception but not a proxy,
            final Throwable realCause = ex.getCause();
            if (realCause instanceof AppException) {
                // if the real cause exception is a WarnException, we should not throw it but just print it
                //noinspection ThrowableNotThrown
                dto.setIssue((AppException) realCause);
                if (realCause instanceof WarnException) {
                    log.warn(realCause.getMessage());
                } else {
                    throw (AppException) realCause;
                }
            } else if (realCause instanceof AccessDeniedException) {
                // if the access is denied by spring security, we should warn it;
                final String errCode = "ERR_DISPATCHER_002";
                final String msgCode = errCode + "/DispatchExecutor.accessDenied";
                dto.setWarn(errCode, Msg.msg(BUNDLE_NAME, msgCode, null));
                log.warn(Msg.msg(BUNDLE_NAME, "ERR_ACCESS_DENIED", null));
            } else {
                throw dto.setIssue(createEx("ERR_UNKNOWN_001/DispatchExecutor.unknownBzIssue", realCause,
                    bean.getClass().getName(), method.getName()));
            }
            // CSOFF: IllegalCatch
        } catch (Exception ex) {
            // CSON: IllegalCatch
            if (AppException.class.isInstance(ex)) {
                throw ex;
            } else {
                throw createEx("ERR_UNKNOWN_001/DispatchExecutor.canNotInvoke", null,
                    bean.getClass().getName(), method.getName());
            }
        } finally {
            log.debug(method.getName() + " end");
        }
    }

    /**
     * Validate the parameter and try to find the method of the request.
     * @param dto the workDTO
     * @param methodName the name of the method
     * @param bean target bean
     * @return the valid method
     * @throws AppException if the parameter is not valid
     */
    private Method getValidMethod(WorkDTO dto, String methodName, Object bean) throws AppException {
        try {
            if (StringUtils.isEmpty(methodName)) {
                // method name should not be empty.
                throw dto.setIssue(createEx("ERR_DISPATCHER_001/DispatchExecutor.emptyMethodName", null));
            }
            return bean.getClass().getMethod(methodName);
            // CSOFF: IllegalCatch
        } catch (SecurityException | NullPointerException | NoSuchMethodException ex) {
            // CSON: IllegalCatch
            final String msgCode = "ERR_DISPATCHER_001/DispatchExecutor.canNotFindMethod";
            //CSOFF: MultipleStringLiterals
            final String errCode = msgCode.substring(0, msgCode.indexOf("/"));
            //CSON: MultipleStringLiterals
            final String msg = Msg.msg(BUNDLE_NAME, msgCode, new Object[]{methodName, bean.getClass().getName()});
            throw dto.setIssue(new WarnException(errCode, msg, ex));
        }
    }

    /**
     * Validate the parameter and try to find the controller bean in the WebContext.
     * @param dto the workDTO
     * @param controllerName the name of the controller
     * @return the valid method
     * @throws AppException if the parameter is not valid
     */
    private Object getValidController(WorkDTO dto, String controllerName) throws AppException {
        try {
            if (StringUtils.isEmpty(controllerName)) {
                // controller name should not be empty
                throw dto.setIssue(createEx("ERR_DISPATCHER_001/DispatchExecutor.controllerIdEmpty", null));
            }
            final Object bean = ContextLoader.getCurrentWebApplicationContext().getBean(controllerName);
            if (ObjectUtils.isEmpty(bean)) {
                // the bean with given controller name should be found
                throw dto.setIssue(createEx("ERR_DISPATCHER_001/DispatchExecutor.targetBeanNull", null,
                    controllerName));
            }
            if (AnnotationUtils.isAnnotationInherited(Controller.class, bean.getClass())) {
                // the target bean should be a Controller but not service nor dao
                throw dto.setIssue(createEx("ERR_DISPATCHER_001/DispatchExecutor.targetBeanNotController", null,
                    controllerName));
            }
            if (AopUtils.isAopProxy(bean)) {
                // the target bean should not be proxied by any proxy
                throw dto.setIssue(createEx("ERR_DISPATCHER_001/DispatchExecutor.targetControllerIsProxy", null,
                    controllerName));
            }
            return bean;
        } catch (NoSuchBeanDefinitionException ex) {
            throw dto.setIssue(createEx("ERR_DISPATCHER_001/DispatchExecutor.canNotFindController", ex,
                controllerName));
        } catch (BeansException | IllegalArgumentException ex) {
            throw dto.setIssue(createEx("ERR_DISPATCHER_001/DispatchExecutor.canNotCreateController", ex,
                controllerName));
        }
    }

    /**
     *  Description: create new AppException.
     * @param msgCode code of the message
     * @param throwable the exception
     * @param arg the arguments for the message
     * @return the new created AppException
     */
    private AppException createEx(String msgCode, Throwable throwable, Object... arg) {
        final String errCode = msgCode.substring(0, msgCode.indexOf("/"));
        final String msg = Msg.msg(BUNDLE_NAME, msgCode, arg);
        return throwable == null ? new AppException(errCode, msg) : new AppException(errCode, msg, throwable);
    }

    /**
     * Description : bidn the context to the target bean <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param bean the target bean
     */
    private void bindData(Object bean) {
        bindDataByInterface(bean);
        bindDataByAnnotation(bean);
    }

    /**
     * @param bean bind the data.
     */
    private void bindDataByInterface(Object bean) {
        if (bean instanceof ApplicationAware) {
            ((ApplicationAware) bean).setApplication(ActionContext.getContext().getApplication());
        }

        if (bean instanceof ParameterAware) {
            ((ParameterAware) bean).setParameters(ActionContext.getContext().getParameters());
        }

        if (bean instanceof RequestAware) {
            ((RequestAware) bean).setRequest(ActionContext.getContext().getRequest());
        }

        if (bean instanceof SessionAware) {
            ((SessionAware) bean).setSession(ActionContext.getContext().getSession());
        }

        if (bean instanceof ApplicationDTOAware) {
            ((ApplicationDTOAware) bean).setApplicationDTO(ActionContext.getContext().getApplicationDTO());
        }

        if (bean instanceof SessionDTOAware) {
            ((SessionDTOAware) bean).setSessionDTO(ActionContext.getContext().getSessionDTO());
        }

        if (bean instanceof WorkDTOAware) {
            ((WorkDTOAware) bean).setWorkDTO(ActionContext.getContext().getWorkDTO());
        }

        if (bean instanceof ServletRequestAware) {
            ((ServletRequestAware) bean).setServletRequest(ActionContext.getContext().getServletRequest());
        }

        if (bean instanceof ServletResponseAware) {
            ((ServletResponseAware) bean).setServletResponse(ActionContext.getContext().getServletResponse());
        }
    }

    /**
     * @param bean bind data.
     */
    private void bindDataByAnnotation(Object bean) {
        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            if (field.getAnnotation(Context.class) != null) {
                ReflectionUtils.makeAccessible(field);
                if (field.getType().equals(ApplicationMap.class)) {
                    ReflectionUtils.setField(field, bean, ActionContext.getContext().getApplication());
                } else if (field.getType().equals(ParameterMap.class)) {
                    ReflectionUtils.setField(field, bean, ActionContext.getContext().getParameters());
                } else if (field.getType().equals(RequestMap.class)) {
                    ReflectionUtils.setField(field, bean, ActionContext.getContext().getRequest());
                } else if (field.getType().equals(SessionMap.class)) {
                    ReflectionUtils.setField(field, bean, ActionContext.getContext().getSession());
                } else if (field.getType().equals(ApplicationDTO.class)) {
                    ReflectionUtils.setField(field, bean, ActionContext.getContext().getApplicationDTO());
                } else if (field.getType().equals(SessionDTO.class)) {
                    ReflectionUtils.setField(field, bean, ActionContext.getContext().getSessionDTO());
                } else if (field.getType().equals(WorkDTO.class)) {
                    ReflectionUtils.setField(field, bean, ActionContext.getContext().getWorkDTO());
                } else if (field.getType().equals(HttpServletRequest.class)) {
                    ReflectionUtils.setField(field, bean, ActionContext.getContext().getServletRequest());
                } else if (field.getType().equals(HttpServletResponse.class)) {
                    ReflectionUtils.setField(field, bean, ActionContext.getContext().getServletResponse());
                }
            }
        });
    }

    /**
     * Description : create the ActionContext. <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param request          the HttpServletRequest
     * @param response         the HttpServletResponse
     * @param context          the ServletContext
     * @param springRequestMap the springRequestMap
     * @return the ActionContext
     */
    ActionContext createActionContext(HttpServletRequest request, HttpServletResponse response,
                                             ServletContext context, Map<String, Object> springRequestMap) {
        final ActionContext extraContext = new ActionContext();
        // orginal data
        final ApplicationMap applicationMap = new ApplicationMap(context);
        final ParameterMap parameterMap = new ParameterMap(request);
        final RequestMap requestMap = new RequestMap(request);
        final SessionMap sessionMap = new SessionMap(request);

        // app data binding
        final ApplicationDTO applicationDTO = new ApplicationDTO(applicationMap);
        final SessionDTO sessionDTO = new SessionDTO(sessionMap);
        final WorkDTO workDTO = new WorkDTO(springRequestMap);

        extraContext.setApplication(applicationMap);
        extraContext.setParameters(parameterMap);
        extraContext.setRequst(requestMap);
        extraContext.setSession(sessionMap);

        extraContext.setApplicationDTO(applicationDTO);
        extraContext.setSessionDTO(sessionDTO);
        extraContext.setWorkDTO(workDTO);

        extraContext.setServletRequest(request);
        extraContext.setServletResponse(response);

        extraContext.setLocale(request.getLocale());

        return extraContext;
    }

    /**
     * Description : get an instance <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return the instance of DispatchExecutor
     */
    public static DispatchExecutor getInstance() {
        return instance.get();
    }

    /**
     * Description : set DispatchExecutor into current thread <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param instance the DispatchExecutor
     */
    public static void setInstance(DispatchExecutor instance) {
        DispatchExecutor.instance.set(instance);
    }
}
