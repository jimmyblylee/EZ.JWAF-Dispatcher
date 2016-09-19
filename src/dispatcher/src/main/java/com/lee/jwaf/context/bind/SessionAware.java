/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : SessionAware.java <br>
 * Package Name : com.lee.jwaf.context.bind <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.context.bind;

import com.lee.jwaf.context.SessionMap;

/**
 * ClassName : SessionAware <br>
 * Description: provide session map for action <br>
 * notice: one action implement this interface will be set session map <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public interface SessionAware {

    /**
     * Description : set session map <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param sessionMap the SessionMap
     */
    public void setSession(SessionMap sessionMap);
}
