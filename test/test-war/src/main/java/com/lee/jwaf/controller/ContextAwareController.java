/**
 * Project Name : jwaf-dispatcher-test <br>
 * File Name : ContextAwareController.java <br>
 * Package Name : com.lee.jwaf.controller <br>
 * Create Time : 2016-09-20 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.controller;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

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
import com.lee.jwaf.log.Log;

/**
 * ClassName : ContextAwareController <br>
 * Description : for context aware integreation test <br>
 * Create Time : 2016-09-20 <br>
 * Create by : jimmyblylee@126.com
 */
@Controller("ContextAwareController")
@Scope(value = SCOPE_PROTOTYPE)
public class ContextAwareController implements ApplicationAware, ParameterAware, RequestAware, ServletResponseAware, ServletRequestAware, SessionAware,
        ApplicationDTOAware, SessionDTOAware, WorkDTOAware {

    @Log
    private Logger log;
    private ApplicationMap appMapAwared;
    private ParameterMap pMapAwared;
    private RequestMap rMapAwared;
    private SessionMap sMapAwared;
    private HttpServletRequest reqAwared;
    private HttpServletResponse resAwared;
    private ApplicationDTO appDTOAwared;
    private SessionDTO sDTOAwared;
    private WorkDTO wDTOAwared;
    @Context
    private ApplicationMap appMapAnnotated;
    @Context
    private ParameterMap pMapAnnotated;
    @Context
    private RequestMap rMapAnnotated;
    @Context
    private SessionMap sMapAnnotated;
    @Context
    private HttpServletRequest reqAnnotated;
    @Context
    private HttpServletResponse resAnnotated;
    @Context
    private ApplicationDTO appDTOAnnotated;
    @Context
    private SessionDTO sDTOAnnotated;
    @Context
    private WorkDTO wDTOAnnotated;
    
    public void test() throws AppException {
        if (appMapAwared == null) {
            throw new AppException("Interface aware: ApplicationMap is null");
        }
        if (pMapAwared == null) {
            throw new AppException("Interface aware: ParameterMap is null");
        }
        if (rMapAwared == null) {
            throw new AppException("Interface aware: RequestMap is null");
        }
        if (sMapAwared == null) {
            throw new AppException("Interface aware: SessionMap is null");
        }
        if (reqAwared == null) {
            throw new AppException("Interface aware: HttpServletRequest is null");
        }
        if (resAwared == null) {
            throw new AppException("Interface aware: HttpServletResponse is null");
        }
        if (appDTOAwared == null) {
            throw new AppException("Interface aware: ApplicationDTO is null");
        }
        if (sDTOAwared == null) {
            throw new AppException("Interface aware: SessionDTO is null");
        }
        if (wDTOAwared == null) {
            throw new AppException("Interface aware: WorkDTO is null");
        }
        

        if (appMapAnnotated == null) {
            throw new AppException("Annotation aware: ApplicationMap is null");
        }
        if (pMapAnnotated == null) {
            throw new AppException("Annotation aware: ParameterMap is null");
        }
        if (rMapAnnotated == null) {
            throw new AppException("Annotation aware: RequestMap is null");
        }
        if (sMapAnnotated == null) {
            throw new AppException("Annotation aware: SessionMap is null");
        }
        if (reqAnnotated == null) {
            throw new AppException("Annotation aware: HttpServletRequest is null");
        }
        if (resAnnotated == null) {
            throw new AppException("Annotation aware: HttpServletResponse is null");
        }
        if (appDTOAnnotated == null) {
            throw new AppException("Annotation aware: ApplicationDTO is null");
        }
        if (sDTOAnnotated == null) {
            throw new AppException("Annotation aware: SessionDTO is null");
        }
        if (wDTOAnnotated == null) {
            throw new AppException("Annotation aware: WorkDTO is null");
        }
        
        if (log == null) {
            throw new AppException("log is null");
        }
        wDTOAnnotated.clear();
        wDTOAnnotated.setResult("valid");
    }

    @Override
    public void setWorkDTO(WorkDTO workDTO) {
        this.wDTOAwared = workDTO;
    }

    @Override
    public void setSessionDTO(SessionDTO sessionDTO) {
        this.sDTOAwared = sessionDTO;
    }

    @Override
    public void setApplicationDTO(ApplicationDTO applicationDTO) {
        this.appDTOAwared = applicationDTO;
    }

    @Override
    public void setSession(SessionMap sessionMap) {
        this.sMapAwared = sessionMap;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.reqAwared = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.resAwared = response;
    }

    @Override
    public void setRequest(RequestMap request) {
        this.rMapAwared = request;
    }

    @Override
    public void setParameters(ParameterMap parameters) {
        this.pMapAwared = parameters;
    }

    @Override
    public void setApplication(ApplicationMap applicationMap) {
        this.appMapAwared = applicationMap;
    }
}
