/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : ApplicationAware.java <br>
 * Package Name : com.lee.jwaf.context.bind <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.context.bind;

import com.lee.jwaf.context.ApplicationMap;

/**
 * ClassName : ApplicationAware <br>
 * Description: provide application map for action <br>
 * notice: one action implement this interface will be set application map <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public interface ApplicationAware {

    /**
     * setApplication: set application map <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param applicationMap the applicationMap
     */
    public void setApplication(ApplicationMap applicationMap);
}
