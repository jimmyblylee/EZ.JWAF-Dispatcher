/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : PrepareOperations.java <br>
 * Package Name : com.lee.jwaf.action <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.action;

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
 * Create by : jimmyblylee@126.com
 */
public final class PrepareOperations {

    private DispatchExecuter dispatchExecuter;
    private ServletContext servletContext;

    public PrepareOperations(DispatchExecuter dispatchExecuter, ServletContext servletContext) {
        this.dispatchExecuter = dispatchExecuter;
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
    public ActionContext createActionContext(HttpServletRequest request, HttpServletResponse response, Map<String, Object> workDTO) {
        ActionContext ctx;
        ActionContext oldContext = ActionContext.getContext();
        if (oldContext != null) {
            /* thread will not shut down sometimes if the target is singleton, this is prepare for this */
            ctx = new ActionContext(new HashMap<String, Object>(oldContext.getContextMap()));
        } else {
            ctx = dispatchExecuter.createActionContext(request, response, servletContext, workDTO);
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
        DispatchExecuter.setInstance(dispatchExecuter);
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
        } catch (Exception e) {
            e.printStackTrace();
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
        DispatchExecuter.setInstance(null);
    }
}
