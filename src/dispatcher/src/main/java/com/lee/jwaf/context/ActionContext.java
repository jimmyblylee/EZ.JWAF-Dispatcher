/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : ActionContext.java <br>
 * Package Name : com.lee.jwaf.context <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lee.jwaf.dto.ApplicationDTO;
import com.lee.jwaf.dto.SessionDTO;
import com.lee.jwaf.dto.WorkDTO;

/**
 * ClassName : ActionContext <br>
 * Description : context of a dispatcher <br>
 * notice: action context is a container of session, parameter, etc. actionContext is a independent copy in every thread <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public class ActionContext implements Serializable {

    private static final long serialVersionUID = 667390814330742187L;

    /**
     * ClassName : CNS <br>
     * Description: constant keys <br>
     * Create Time : 2016-09-19 <br>
     * Create by : jimmyblylee@126.com
     */
    private enum CNS {
        /** description: {@linkplain ApplicationMap} */
        APPLICATION,
        /** description: {@link ParameterMap} */
        PARAMETER,
        /** description: {@link RequestMap} */
        REQUEST,
        /** description: {@link SessionMap} */
        SESSION,
        /** description: {@linkplain ApplicationDTO} */
        APPLICATION_DTO,
        /** description: {@link WorkDTO} */
        WORK_DTO,
        /** description: {@link SessionDTO} */
        SESSION_DTO,
        /** description: servletRequest {@link HttpServletRequest} */
        SERVLET_REQUEST,
        /** description: servletResponse {@link HttpServletResponse} */
        SERVLET_RESPONSE,
        /** description: LOCALE */
        LOCALE;

        public String toString() {
            return name().toLowerCase();
        };
    }

    /** @Fields actionContext: action context in the current thread */
    private static ThreadLocal<ActionContext> actionContext = new ThreadLocal<ActionContext>();

    /**
     * Description : set action context into the current thread <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param context the context
     */
    public static void setContext(ActionContext context) {
        actionContext.set(context);
    }

    /**
     * Description : get the action context from current thread <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return this context in current thread
     */
    public static ActionContext getContext() {
        return (ActionContext) actionContext.get();
    }

    /** action context */
    private Map<String, Object> context;

    public ActionContext(Map<String, Object> context) {
        this.context = context;
    }

    public ActionContext() {
        this.context = new HashMap<String, Object>();
    }

    /**
     * Description : get context map <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return context map
     */
    public Map<String, Object> getContextMap() {
        return context;
    }

    /**
     * Description : set applicationMap <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param applicationMap applicationMap
     */
    public void setApplication(ApplicationMap applicationMap) {
        put(CNS.APPLICATION.toString(), applicationMap);
    }

    /**
     * Description : get ApplicationMap <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return applicationMap
     */
    public ApplicationMap getApplication() {
        return get(CNS.APPLICATION.toString());
    }

    /**
     * Description : set parameter of request <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param parameterMap parameterMap
     */
    public void setParameters(ParameterMap parameterMap) {
        put(CNS.PARAMETER.toString(), parameterMap);
    }

    /**
     * Description : get parameters from request <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return parameters from request
     */
    public ParameterMap getParameters() {
        return get(CNS.PARAMETER.toString());
    }

    /**
     * Description : set attribute map from request <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param requestMap requestMap
     */
    public void setRequst(RequestMap requestMap) {
        put(CNS.REQUEST.toString(), requestMap);
    }

    /**
     * Description : get attribute map from request <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return attribute map from request
     */
    public RequestMap getRequest() {
        return get(CNS.REQUEST.toString());
    }

    /**
     * Description : set session map of servlet <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param sessionMap sessionMap
     */
    public void setSession(SessionMap sessionMap) {
        put(CNS.SESSION.toString(), sessionMap);
    }

    /**
     * Description : get session map of servlet <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return session map of servlet
     */
    public SessionMap getSession() {
        return get(CNS.SESSION.toString());
    }

    /**
     * Description : set ApplicationDTO <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param applicationDTO applicationDTO
     */
    public void setApplicationDTO(ApplicationDTO applicationDTO) {
        put(CNS.APPLICATION_DTO.toString(), applicationDTO);
    }

    /**
     * Description : get ApplicationDTO <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return ApplicationDTO
     */
    public ApplicationDTO getApplicationDTO() {
        return get(CNS.APPLICATION_DTO.toString());
    }

    /**
     * Description : set SessionDTO <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param sessionDTO sessionDTO
     */
    public void setSessionDTO(SessionDTO sessionDTO) {
        put(CNS.SESSION_DTO.toString(), sessionDTO);
    }

    /**
     * Description : get SessionDTO <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return SessionDTO
     */
    public SessionDTO getSessionDTO() {
        return get(CNS.SESSION_DTO.toString());
    }

    /**
     * Description : set workDTO <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param workDTO workDTO
     */
    public void setWorkDTO(WorkDTO workDTO) {
        put(CNS.WORK_DTO.toString(), workDTO);
    }

    /**
     * Description : get workDTO <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return workDTO
     */
    public WorkDTO getWorkDTO() {
        return get(CNS.WORK_DTO.toString());
    }

    /**
     * Description : set HttpServletRequest <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param servletRequest servletRequest
     */
    public void setServletRequest(HttpServletRequest servletRequest) {
        put(CNS.SERVLET_REQUEST.toString(), servletRequest);
    }

    /**
     * Description : get HttpServletRequest <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return HttpServletRequest
     */
    public HttpServletRequest getServletRequest() {
        return get(CNS.SERVLET_REQUEST.toString());
    }

    /**
     * Description : set HttpServletResponse <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param servletResponse servletResponse
     */
    public void setServletResponse(HttpServletResponse servletResponse) {
        put(CNS.SERVLET_RESPONSE.toString(), servletResponse);
    }

    /**
     * Description : get HttpServletResponse <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return HttpServletResponse
     */
    public HttpServletResponse getServletResponse() {
        return get(CNS.SERVLET_RESPONSE.toString());
    }

    /**
     * Description : set local <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param locale locale
     */
    public void setLocale(Locale locale) {
        put(CNS.LOCALE.toString(), locale);
    }

    /**
     * Description : get action local <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return if the local is null, will return {@link java.util.Locale#getDefault()} as default
     */
    public Locale getLocale() {
        Locale locale = (Locale) get(CNS.LOCALE.toString());
        if (locale == null) {
            locale = Locale.getDefault();
            setLocale(locale);
        }
        return locale;
    }

    /**
     * Description : get data from current action context <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param <T> the type which the caller needs
     * @param key the key
     * @return the value
     * @return the value that was found using the key or <tt>null</tt> if the key was not found.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) context.get(key);
    }

    /**
     * Description : put a value into current action context, can get it by the key <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param key key
     * @param value value
     */
    public void put(String key, Object value) {
        context.put(key, value);
    }

}
