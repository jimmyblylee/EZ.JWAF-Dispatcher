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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lee.jwaf.context.ActionContext;

/**
 * ClassName : PrepareOperations <br>
 * Description : prepare operations <br>
 * Create Time : 2016-09-19 <br>
 * @author jimmyblylee@126.com
 */
@SuppressWarnings("WeakerAccess")
public final class PrepareOperations {

    /** The dispatcher execurtor. */
    private DispatchExecutor dispatchExecutor;
    /** The servlet Context. */
    private ServletContext servletContext;

    /**
     *  Default constructor.
     * @param dispatchExecutor the dispatcher executor
     * @param servletContext the servlet context
     */
    public PrepareOperations(DispatchExecutor dispatchExecutor, ServletContext servletContext) {
        this.dispatchExecutor = dispatchExecutor;
        this.servletContext = servletContext;
    }

    /**
     * Description : create action context <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param workDTO the Map of parameters
     * @return the ActionContext
     */
    public ActionContext createActionContext(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> workDTO) {
        final ActionContext ctx;
        final ActionContext oldContext = ActionContext.getContext();
        if (oldContext != null) {
            /* thread will not shut down sometimes if the target is singleton, this is prepare for this */
            ctx = new ActionContext(new HashMap<>(oldContext.getContextMap()));
        } else {
            ctx = dispatchExecutor.createActionContext(request, response, servletContext, workDTO);
        }
        ActionContext.setContext(ctx);
        return ctx;
    }

    /**
     * Description : assign the dispatcher into current thread <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     */
    public void assignDispatcherToThread() {
        DispatchExecutor.setInstance(dispatchExecutor);
    }

    /**
     * Description : set the encoding and locale <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     */
    public void setEncodingAndLocale(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setLocale(request.getLocale());
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Description : cleanup the current context <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     */
    public void cleanup() {
        ActionContext.setContext(null);
        DispatchExecutor.setInstance(null);
    }
}
