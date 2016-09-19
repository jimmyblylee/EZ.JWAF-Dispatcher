/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : SessionDTOAware.java <br>
 * Package Name : com.lee.jwaf.dto.bind <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.dto.bind;

import com.lee.jwaf.dto.SessionDTO;

/**
 * ClassName : SessionDTOAware <br>
 * Description: provide session dto for action <br>
 * notice: one action implement this interface will be set session dto <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public interface SessionDTOAware {

    /**
     * Description: set session dto <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param sessionDTO the session dto
     */
    public void setSessionDTO(SessionDTO sessionDTO);
}
