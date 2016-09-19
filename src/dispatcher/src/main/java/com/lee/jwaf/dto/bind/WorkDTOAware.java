/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : WorkDTOAware.java <br>
 * Package Name : com.lee.jwaf.dto.bind <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.dto.bind;

import com.lee.jwaf.dto.WorkDTO;

/**
 * ClassName : WorkDTOAware <br>
 * Description: provide work dto for action <br>
 * notice: one action implement this interface will be set work dto <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public interface WorkDTOAware {

    /**
     * Description : set work dto <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param workDTO the work dto
     */
    public void setWorkDTO(WorkDTO workDTO);
}
