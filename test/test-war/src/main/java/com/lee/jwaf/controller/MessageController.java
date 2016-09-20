/**
 * Project Name : jwaf-dispatcher-test <br>
 * File Name : MessageController.java <br>
 * Package Name : com.lee.jwaf.controller <br>
 * Create Time : 2016-09-20 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.controller;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lee.jwaf.context.Context;
import com.lee.jwaf.dto.WorkDTO;
import com.lee.jwaf.exception.AppException;
import com.lee.jwaf.message.Messages;

/**
 * ClassName : MessageController <br>
 * Description : for context aware integreation test <br>
 * Create Time : 2016-09-20 <br>
 * Create by : jimmyblylee@126.com
 */
@Controller("MessageController")
@Scope(value = SCOPE_PROTOTYPE)
public class MessageController {

    @Context
    private WorkDTO dto;

    public void test() throws AppException {
        dto.clear();
        dto.put("msg", Messages.getMsg("ERR_UNKNOWN_001"));
    }
}
