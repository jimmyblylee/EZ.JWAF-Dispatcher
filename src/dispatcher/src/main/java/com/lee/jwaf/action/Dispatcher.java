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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.jwaf.dto.AppConstant;
import com.lee.jwaf.exception.AppException;
import com.lee.jwaf.exception.WarnException;

/**
 * ClassName : Dispatcher <br>
 * Description : dispatcher of all controller <br>
 * Create Time : 2016-09-19 <br>
 * @author jimmyblylee@126.com
 */
@Controller("action.Dispatcher")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping("/dispatch")
@SuppressWarnings("unused")
public class Dispatcher implements ServletContextAware {

    /** Log. */
    private Logger log = LoggerFactory.getLogger(getClass());

    /** Servlet Context. */
    private ServletContext servletContext;

    /**
     * Description : servlet post method <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param workDTO param map
     * @param servletRequest httpRequest
     * @param servletResponse httpResponse
     */
    @RequestMapping(method = RequestMethod.POST)
    public void post(@RequestParam Map<String, Object> workDTO, HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        // prepare context
        final DispatchExecutor dispatchExecutor = new DispatchExecutor();
        final PrepareOperations prepareOperations = new PrepareOperations(dispatchExecutor, servletContext);
        try {
            prepareOperations.createActionContext(servletRequest, servletResponse, workDTO);
            prepareOperations.assignDispatcherToThread();
            prepareOperations.setEncodingAndLocale(servletRequest, servletResponse);

            // try to dispatch
            dispatchExecutor.serviceAction(servletRequest, servletResponse);
            // CSOFF: IllegalCatch
        } catch (Throwable ex) {
            // CSON: IllegalCatch
            if (ex instanceof AppException && !WarnException.class.isInstance(ex)) {
                log.error("{}: {}", ((AppException) ex).getErrCode(), ex.getMessage());
            } else {
                log.error("Error Stacking:", ex);
            }
        } finally {
            final String selfCtrlStreamKey = AppConstant.CNS_REQUEST.LET_ME_CTRL_THE_STREAM.toString();
            // build response result in json for ajax call
            if (!workDTO.containsKey(selfCtrlStreamKey) || Boolean.FALSE.equals(workDTO.get(selfCtrlStreamKey))) {
                buildJsonResultASendToClient(workDTO, servletResponse);
            }
            // clean up the thread
            prepareOperations.cleanup();
        }
    }

    /**
     * Description : http get method, which will be pointed to post method directlly <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param workDTO param map
     * @param servletRequest httpRequest
     * @param servletResponse httpResponse
     */
    @RequestMapping(method = RequestMethod.GET)
    public void get(@RequestParam Map<String, Object> workDTO, HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        post(workDTO, servletRequest, servletResponse);
    }

    /**
     * Description : build result of request by json <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param workDTO the work dto
     * @param response the HttpServletResponse
     */
    private void buildJsonResultASendToClient(Map<String, Object> workDTO, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            if (!"text/html;charset=UTF-8".equals(response.getContentType())) {
                response.setContentType("text/json;charset=UTF-8");
            }
            final String success = "success";
            if (workDTO.containsKey(success) && !(Boolean) workDTO.get(success)) {
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            }
            final ObjectMapper mapper = new ObjectMapper();
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            mapper.setDateFormat(dateFormat);
            mapper.writeValue(response.getOutputStream(), workDTO);
            // CSOFF: IllegalCatch
        } catch (Exception ex) {
            // CSON: IllegalCatch
            try {
                response.flushBuffer();
            } catch (IOException exIO) {
                log.error(ex.getMessage(), exIO);
            }
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
