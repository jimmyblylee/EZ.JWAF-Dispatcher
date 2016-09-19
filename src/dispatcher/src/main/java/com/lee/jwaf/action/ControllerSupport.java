/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : ControllerSupport.java <br>
 * Package Name : com.lee.jwaf.action <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.lee.jwaf.context.bind.ServletRequestAware;
import com.lee.jwaf.context.bind.ServletResponseAware;
import com.lee.jwaf.dto.SessionDTO;
import com.lee.jwaf.dto.WorkDTO;
import com.lee.jwaf.dto.bind.SessionDTOAware;
import com.lee.jwaf.dto.bind.WorkDTOAware;
import com.lee.jwaf.log.Log;

/**
 * ClassName : ControllerSupport <br>
 * Description : abstract controller support class, that contains common DTO and Servlet. <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public abstract class ControllerSupport implements WorkDTOAware, SessionDTOAware, ServletRequestAware, ServletResponseAware {

    protected SessionDTO sessionDTO;
    protected WorkDTO workDTO;
    protected HttpServletRequest servletRequest;
    protected HttpServletResponse servletResponse;
    @Log
    protected Logger log;

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.servletResponse = response;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.servletRequest = request;
    }

    @Override
    public void setSessionDTO(SessionDTO sessionDTO) {
        this.sessionDTO = sessionDTO;
    }

    @Override
    public void setWorkDTO(WorkDTO workDTO) {
        this.workDTO = workDTO;
    }
}
