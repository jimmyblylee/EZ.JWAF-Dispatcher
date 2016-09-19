/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : DispatchExecuter.java <br>
 * Package Name : com.lee.jwaf.action <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.action;

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
import com.lee.jwaf.exception.ErrLevel;
import com.lee.jwaf.message.Messages;
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
            if (StringUtils.isEmpty(controllerName)) { throw dto
                    .setIssue(new AppException("ERR_FRAMEWORK_001", ErrLevel.WARN, Messages.getMsg("ERR_FRAMEWORK_CONTROLLERID_NULL"))); }
            bean = ContextLoader.getCurrentWebApplicationContext().getBean(controllerName);
            if (bean == null) { throw dto
                    .setIssue(new AppException("ERR_FRAMEWORK_001", ErrLevel.WARN, Messages.getMsg("ERR_FRAMEWORK_BEAN_NULL"), controllerName)); }
            if (bean.getClass().getAnnotation(Controller.class) == null) { throw dto
                    .setIssue(new AppException("ERR_FRAMEWORK_001", ErrLevel.WARN, Messages.getMsg("ERR_FRAMEWORK_NONE_CONTROLLER"), controllerName)); }
            if (AopUtils.isAopProxy(bean)) { throw dto
                    .setIssue(new AppException("ERR_FRAMEWORK_001", ErrLevel.WARN, Messages.getMsg("ERR_FRAMEWORK_PROXY_CONTROLLER"), controllerName)); }
        } catch (NoSuchBeanDefinitionException e) {
            throw dto.setIssue(new AppException("ERR_FRAMEWORK_001", ErrLevel.WARN, Messages.getMsg("ERR_FRAMEWORK_CONTROLLER_NULL"), e, controllerName));
        } catch (BeansException | IllegalArgumentException e) {
            throw dto.setIssue(new AppException("ERR_FRAMEWORK_001", ErrLevel.ERR, Messages.getMsg("ERR_FRAMEWORK_CONTROLLER_CREATE"), e, controllerName));
        }

        try {
            if (StringUtils.isEmpty(methodName)) { throw dto.setIssue(new AppException("ERR_FRAMEWORK_001", Messages.getMsg("ERR_FRAMEWORK_METHODID_NULL"))); }
            method = bean.getClass().getMethod(methodName, new Class<?>[0]);
        } catch (SecurityException | NullPointerException | NoSuchMethodException e) {
            throw dto.setIssue(new AppException("ERR_FRAMEWORK_001", ErrLevel.WARN, Messages.getMsg("ERR_FRAMEWORK_METHOD_NULL"), e, methodName,
                    bean.getClass().getName()));
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
                if (ErrLevel.ERR.equals(se.getErrLevel())) {
                    throw se;
                } else {
                    log.warn(se.getMessage());
                }
            } else if (realCause instanceof AccessDeniedException) {
                dto.setWarn("ERR_SECURITY_001", Messages.getMsg("ERR_ACCESS_DENIED"));
                log.warn(Messages.getMsg("ERR_ACCESS_DENIED"));
            } else {
                throw dto.setIssue(new AppException("ERR_UNKNOWN_001", Messages.getMsg("ERR_CONTROLLER_BIZ_UNKNOWN_FAILURE"), realCause,
                        bean.getClass().getName(), method.getName()));
            }
        } catch (Exception e) {
            throw new AppException("ERR_UNKNOWN_001", Messages.getMsg("ERR_CONTROLLER_INVOKE_FAILURE"), e, bean.getClass().getName(), method.getName());
        } finally {
            log.debug(method.getName() + " end");
        }
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
                    switch (field.getType().toString()) {
                        case "com.lee.jwaf.context.ApplicationMap":
                            ReflectionUtils.setField(field, bean, ActionContext.getContext().getApplication());
                            break;
                        case "com.lee.jwaf.context.ParameterMap":
                            ReflectionUtils.setField(field, bean, ActionContext.getContext().getParameters());
                            break;
                        case "com.lee.jwaf.context.RequestMap":
                            ReflectionUtils.setField(field, bean, ActionContext.getContext().getRequest());
                            break;
                        case "com.lee.jwaf.context.SessionMap":
                            ReflectionUtils.setField(field, bean, ActionContext.getContext().getSession());
                            break;
                        case "com.lee.jwaf.dto.ApplicationDTO":
                            ReflectionUtils.setField(field, bean, ActionContext.getContext().getApplicationDTO());
                            break;
                        case "com.lee.jwaf.dto.SessionDTO":
                            ReflectionUtils.setField(field, bean, ActionContext.getContext().getSessionDTO());
                            break;
                        case "com.lee.jwaf.dto.WorkDTO":
                            ReflectionUtils.setField(field, bean, ActionContext.getContext().getWorkDTO());
                            break;
                        case "javax.servlet.http.HttpServletRequest":
                            ReflectionUtils.setField(field, bean, ActionContext.getContext().getServletRequest());
                            break;
                        case "javax.servlet.http.HttpServletResponse":
                            ReflectionUtils.setField(field, bean, ActionContext.getContext().getServletResponse());
                            break;
                        default:
                            break;
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
    public ActionContext createActionContext(HttpServletRequest request, HttpServletResponse response, ServletContext context,
            Map<String, Object> springRequestMap) {
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
