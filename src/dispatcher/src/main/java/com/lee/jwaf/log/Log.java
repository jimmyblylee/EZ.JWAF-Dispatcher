/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : Log.java <br>
 * Package Name : com.lee.jwaf.log <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.log;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ClassName : Log <br>
 * Description : Annotation to inject log to the field <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface Log {

}
