/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : ServletResponseAware.java <br>
 * Package Name : com.lee.jwaf.context.bind <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.context.bind;

import javax.servlet.http.HttpServletResponse;

/**
 * ClassName : ServletResponseAware <br>
 * Description: provide servlet response for action <br>
 * notice: one action implement this interface will be set servlet response <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public interface ServletResponseAware {

    /**
     * Description : set servlet response <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param response the HttpServletResponse
     */
    public void setServletResponse(HttpServletResponse response);
}
