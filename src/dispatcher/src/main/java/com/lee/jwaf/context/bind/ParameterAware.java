/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : ParameterAware.java <br>
 * Package Name : com.lee.jwaf.context.bind <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.context.bind;

import com.lee.jwaf.context.ParameterMap;

/**
 * ClassName : ParameterAware <br>
 * Description: provide http attribute parameter map for action <br>
 * notice: one action implement this interface will be set http attribute parameter map <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public interface ParameterAware {

    /**
     * setParameters: set http servlet attribute parameter map <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param parameters parameters
     */
    public void setParameters(ParameterMap parameters);
}
