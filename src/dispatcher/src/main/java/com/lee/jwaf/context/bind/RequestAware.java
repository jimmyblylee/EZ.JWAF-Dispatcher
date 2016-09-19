/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : RequestAware.java <br>
 * Package Name : com.lee.jwaf.context.bind <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.context.bind;

import com.lee.jwaf.context.RequestMap;

/**
 * ClassName : RequestAware <br>
 * Description: provide http request map for action <br>
 * notice: one action implement this interface will be set http request map <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public interface RequestAware {

    /**
     * Description : set http request <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param request request
     */
    public void setRequest(RequestMap request);
}
