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
 * ClassName : AbstractControllerSupport <br>
 * Description : abstract controller support class, that contains common DTO and Servlet. <br>
 * Create Time : 2016-09-19 <br>
 *
 * @author jimmyblylee@126.com
 */
@SuppressWarnings("unused")
public abstract class AbstractControllerSupport
    implements WorkDTOAware, SessionDTOAware, ServletRequestAware, ServletResponseAware {

    // CSOFF: VisibilityModifier
    /** Session DTO. */
    @SuppressWarnings("WeakerAccess")
    protected SessionDTO sessionDTO;
    /** Work DTO. */
    @SuppressWarnings("WeakerAccess")
    protected WorkDTO workDTO;
    /** Servlet Request. */
    @SuppressWarnings("WeakerAccess")
    protected HttpServletRequest servletRequest;
    /** Servlet Response. */
    @SuppressWarnings("WeakerAccess")
    protected HttpServletResponse servletResponse;
    /** Slf4j log. */
    @Log
    @SuppressWarnings("WeakerAccess")
    protected Logger log;
    // CSON: VisibilityModifier

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
