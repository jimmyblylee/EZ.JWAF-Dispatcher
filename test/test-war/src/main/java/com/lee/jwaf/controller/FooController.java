/**
 * Project Name : jwaf-dispatcher-test <br>
 * File Name : FooController.java <br>
 * Package Name : com.lee.jwaf.controller <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.controller;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lee.jwaf.context.Context;
import com.lee.jwaf.dto.WorkDTO;
import com.lee.jwaf.log.Log;

/**
 * ClassName : FooController <br>
 * Description : Foo <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@Controller("FooController")
@Scope(value = SCOPE_PROTOTYPE)
public class FooController {
    
    @Context
    private WorkDTO workDTO;
    @Log
    private Logger log;

    public void foo() {
        log.info("hello {}", workDTO.<String>get("name"));
    }
}
