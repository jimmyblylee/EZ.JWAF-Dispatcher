/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : Context.java <br>
 * Package Name : com.lee.jwaf.context <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.context;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ClassName : Context <br>
 * Description : fields with built-in type, which annotated with this annotation will be set with the target instance <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface Context {

}
