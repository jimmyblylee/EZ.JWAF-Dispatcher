/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : ApplicationDTOAware.java <br>
 * Package Name : com.lee.jwaf.dto.bind <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.dto.bind;

import com.lee.jwaf.dto.ApplicationDTO;

/**
 * ClassName : ApplicationDTOAware <br>
 * Description: provide application dto for action <br>
 * notice: one action implement this interface will be set application dto <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public interface ApplicationDTOAware {

    /**
     * Description: set application dto <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param applicationDTO the application dto
     */
    public void setApplicationDTO(ApplicationDTO applicationDTO);
}
