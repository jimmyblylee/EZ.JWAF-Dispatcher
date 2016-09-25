/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : DispatchExecuter.java <br>
 * Package Name : com.lee.jwaf.action <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.action;

import static com.lee.jwaf.message.Messages.Msg;

import java.lang.reflect.Field;
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
import com.lee.util.ReflectionUtils.FieldCallback;
import com.lee.util.StringUtils;

/**
 * ClassName : DispatchExecuter <br>
 * Description : controller dispatch executer <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public final class DispatchExecuter {

    private static ThreadLocal<DispatchExecuter> instance = new ThreadLocal<DispatchExecuter>();
    private final static String BUNDLE_NAME = "dispatcher";

    /**
     * Description : dispatch the request to the target controller by given parameters<br>
     * Create Time: Apr 12, 2016 <br>
     * Create by : xiangyu_li@asdc.com.cn <br>
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws Exception unknown Exception
     * @throws AppException many cases
     */
    public void serviceAction(HttpServletRequest request, HttpServletResponse response) throws Exception, AppException {
        WorkDTO dto = ActionContext.getContext().getWorkDTO();

        String controllerName = dto.getController();
        String methodName = dto.getMethod();
        Logger dispatcherLog = LoggerFactory.getLogger(getClass());
        dispatcherLog.trace("request info : controller name is {} and method name is {}", controllerName, methodName);
        dto.removeController();
        dto.removeMethod();

        Object bean = null;
        Method method = null;

        try {
            if (StringUtils.isEmpty(controllerName)) {
                AppException ex = createEx("ERR_DISPATCHER_001/DispatchExecuter.controllerIdEmpty", null);
                throw dto.setIssue(ex);
            }
            bean = ContextLoader.getCurrentWebApplicationContext().getBean(controllerName);
            if (ObjectUtils.isEmpty(bean)) {
                AppException ex = createEx("ERR_DISPATCHER_001/DispatchExecuter.targetBeanNull", null, controllerName);
                throw dto.setIssue(ex);
            }
            if (AnnotationUtils.isAnnotationInherited(Controller.class, bean.getClass())) {
                AppException ex = createEx("ERR_DISPATCHER_001/DispatchExecuter.targetBeanNotController", null,
                        controllerName);
                throw dto.setIssue(ex);
            }
            if (AopUtils.isAopProxy(bean)) {
                AppException ex = createEx("ERR_DISPATCHER_001/DispatchExecuter.targetControllerIsProxy", null,
                        controllerName);
                throw dto.setIssue(ex);
            }
        } catch (NoSuchBeanDefinitionException e) {
            AppException ex = createEx("ERR_DISPATCHER_001/DispatchExecuter.canNotFindController", e, controllerName);
            throw dto.setIssue(ex);
        } catch (BeansException | IllegalArgumentException e) {
            AppException ex = createEx("ERR_DISPATCHER_001/DispatchExecuter.canNotCreateController", e, controllerName);
            throw dto.setIssue(ex);
        }

        try {
            if (StringUtils.isEmpty(methodName)) {
                AppException ex = createEx("ERR_DISPATCHER_001/DispatchExecuter.emptyMethodName", null);
                throw dto.setIssue(ex);
            }
            method = bean.getClass().getMethod(methodName, new Class<?>[0]);
        } catch (SecurityException | NullPointerException | NoSuchMethodException e) {
            String msgCode = "ERR_DISPATCHER_001/DispatchExecuter.canNotFindMethod";
            String errCode = msgCode.substring(0, msgCode.indexOf("/"));
            String msg = Msg.msg(BUNDLE_NAME, msgCode, new Object[] { methodName, bean.getClass().getName() });
            AppException ex = new WarnException(errCode, msg, e);
            throw dto.setIssue(ex);
        }
        // bind data to target bean
        bindData(bean);

        Logger log = LoggerFactory.getLogger(bean.getClass());
        log.debug(method.getName() + " start");
        try {
            method.invoke(bean, new Object[0]);
        } catch (InvocationTargetException e) {
            Throwable realCause = e.getCause();
            if (realCause instanceof AppException) {
                AppException se = (AppException) realCause;
                dto.setIssue(se);
                if (se instanceof WarnException) {
                    log.warn(se.getMessage());
                } else {
                    throw se;
                }
            } else if (realCause instanceof AccessDeniedException) {
                String errCode = "ERR_DISPATCHER_002", msgCode = errCode + "/DispatchExecuter.accessDenied";
                dto.setWarn(errCode, Msg.msg(BUNDLE_NAME, msgCode, null));
                log.warn(Msg.msg(BUNDLE_NAME, "ERR_ACCESS_DENIED", null));
            } else {
                AppException ex = createEx("ERR_DISPATCHER_001/DispatchExecuter.unkownBzIssue", realCause,
                        bean.getClass().getName(), method.getName());
                throw dto.setIssue(ex);
            }
        } catch (Exception e) {
            AppException ex = createEx("ERR_DISPATCHER_001/DispatchExecuter.canNotInvok", null,
                    bean.getClass().getName(), method.getName());
            throw ex;
        } finally {
            log.debug(method.getName() + " end");
        }
    }

    private AppException createEx(String msgCode, Throwable e, Object... arg) {
        String errCode = msgCode.substring(0, msgCode.indexOf("/"));
        String msg = Msg.msg(BUNDLE_NAME, msgCode, arg);
        return e == null ? new AppException(errCode, msg) : new AppException(errCode, msg, e);
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
            ServletRequestAware sra = (ServletRequestAware) bean;
            sra.setServletRequest(ActionContext.getContext().getServletRequest());
        }

        if (bean instanceof ServletResponseAware) {
            ((ServletResponseAware) bean).setServletResponse(ActionContext.getContext().getServletResponse());
        }
    }

    private void bindDataByAnnotation(Object bean) {
        ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
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
            }
        });
    }

    /**
     * Description : create the ActionContext <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param context the ServletContext
     * @param springRequestMap the springRequestMap
     * @return the ActionContext
     */
    public ActionContext createActionContext(HttpServletRequest request, HttpServletResponse response,
            ServletContext context, Map<String, Object> springRequestMap) {
        ActionContext extraContext = new ActionContext();
        // orginal data
        ApplicationMap applicationMap = new ApplicationMap(context);
        ParameterMap parameterMap = new ParameterMap(request);
        RequestMap requestMap = new RequestMap(request);
        SessionMap sessionMap = new SessionMap(request);

        // app data binding
        ApplicationDTO applicationDTO = new ApplicationDTO(applicationMap);
        SessionDTO sessionDTO = new SessionDTO(sessionMap);
        WorkDTO workDTO = new WorkDTO(springRequestMap);

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
     * @return the instance of DispatchExecuter
     */
    public static DispatchExecuter getInstance() {
        return instance.get();
    }

    /**
     * Description : set DispatchExecuter into current thread <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param instance the DispatchExecuter
     */
    public static void setInstance(DispatchExecuter instance) {
        DispatchExecuter.instance.set(instance);
    }
}
