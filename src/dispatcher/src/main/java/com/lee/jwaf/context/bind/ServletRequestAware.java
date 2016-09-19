/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : ServletRequestAware.java <br>
 * Package Name : com.lee.jwaf.context.bind <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.context.bind;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName : ServletRequestAware <br>
 * Description: provide servlet request for action <br>
 * notice: one action implement this interface will be set servlet request <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public interface ServletRequestAware {

    /**
     * Description : set servlet request <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param request the HttpServletRequest
     */
    public void setServletRequest(HttpServletRequest request);
}
