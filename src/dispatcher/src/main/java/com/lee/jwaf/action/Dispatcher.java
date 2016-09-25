/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : Dispatcher.java <br>
 * Package Name : com.lee.jwaf.action <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
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
 * Create by : jimmyblylee@126.com
 */
@Controller("action.Dispatcher")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping("/dispatch")
public class Dispatcher implements ServletContextAware {

    private Logger log = LoggerFactory.getLogger(getClass());

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
        DispatchExecuter dispatchExecuter = new DispatchExecuter();
        PrepareOperations prepareOperations = new PrepareOperations(dispatchExecuter, servletContext);
        try {
            prepareOperations.createActionContext(servletRequest, servletResponse, workDTO);
            prepareOperations.assignDispatcherToThread();
            prepareOperations.setEncodingAndLocale(servletRequest, servletResponse);

            // try to dispatch
            dispatchExecuter.serviceAction(servletRequest, servletResponse);
        } catch (Throwable e) {
            if (AppException.class.isInstance(e) && !WarnException.class.isInstance(e)) {
                log.error("{}: {}", ((AppException) e).getErrCode(), ((AppException) e).getMessage());
            } else {
                log.error("Error Stacking:", e);
            }
        } finally {
            String selfCtrlStreamKey = AppConstant.CNS_REQUEST.LET_ME_CTRL_THE_STREAM.toString();
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
            if (workDTO.containsKey("success") && !(Boolean) workDTO.get("success")) {
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            }
            ObjectMapper mapper = new ObjectMapper();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            mapper.setDateFormat(dateFormat);
            mapper.writeValue(response.getOutputStream(), workDTO);
        } catch (Exception e) {
            try {
                response.flushBuffer();
            } catch (IOException e1) {
                log.error(e.getMessage(), e1);
            }
            log.error(e.getMessage(), e);
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
