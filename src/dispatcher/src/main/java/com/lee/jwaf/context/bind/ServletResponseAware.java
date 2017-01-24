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

package com.lee.jwaf.context.bind;

import javax.servlet.http.HttpServletResponse;

/**
 * ClassName : ServletResponseAware <br>
 * Description: provide servlet response for action <br>
 * notice: one action implement this interface will be set servlet response <br>
 * Create Time : 2016-09-19 <br>
 * @author jimmyblylee@126.com
 */
public interface ServletResponseAware {

    /**
     * Description : set servlet response <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param response the HttpServletResponse
     */
    void setServletResponse(HttpServletResponse response);
}
