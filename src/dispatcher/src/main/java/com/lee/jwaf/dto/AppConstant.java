/* ***************************************************************************
 * EZ.JWAF/EZ.JCWAP: Easy series Production.
 * Including JWAF(Java-based Web Application Framework)
 * and JCWAP(Java-based Customized Web Application Platform).
 * Copyright (C) 2016-2017 the original author or authors.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of MIT License as published by
 * the Free Software Foundation;
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the MIT License for more details.
 *
 * You should have received a copy of the MIT License along
 * with this library; if not, write to the Free Software Foundation.
 * ***************************************************************************/

package com.lee.jwaf.dto;

/**
 * ClassName : AppConstant <br>
 * Description : constant of UI Layer and Controller Layer <br>
 * Create Time : 2016-09-18 <br>
 *
 * @author jimmyblylee@126.com
 */
public interface AppConstant {

    /**
     * ClassName : CNS_SERVER <br>
     * Description : controller constant <br>
     * Create Time : 2016-09-18 <br>
     * Create by : jimmyblylee@126.com
     */
    enum CNS_SERVER {
        /** Key of controller name. */
        CONTROLLER,
        /** Key of method name. */
        METHOD;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    /**
     * ClassName : CNS_REQUEST <br>
     * Description : request flag <br>
     * Create Time : Apr 12, 2016 <br>
     * Create by : xiangyu_li@asdc.com.cn <br>
     */
    enum CNS_REQUEST {
        /** Key of result in response. */
        RESULT,
        /** If this code is in the workDTO, the controller will control the output stream it self. */
        LET_ME_CTRL_THE_STREAM,
        /** SUCCESS flag. */
        SUCCESS,
        /** Error code. */
        ERR_CODE,
        /** Error level warning or error. */
        ERR_LEVEL,
        /** Error message. */
        ERR_MESSAGE;

        /** Flag for error. */
        public static final String CNS_ERROR = "error";
        /** Flag for waring. */
        public static final String CNS_WARNING = "warning";

        @Override
        public String toString() {
            String result = this.name().toLowerCase();
            if (this.equals(SUCCESS)) {
                result = "success";
            } else if (this.equals(ERR_MESSAGE)) {
                result = "errMsg";
            } else if (this.equals(ERR_CODE)) {
                result = "errCode";
            } else if (this.equals(ERR_LEVEL)) {
                result = "errLevel";
            } else if (this.equals(LET_ME_CTRL_THE_STREAM)) {
                result = "selfControlledStream";
            }
            return result;
        }
    }

    /**
     * ClassName : CNS_FILE <br>
     * Description : file constant <br>
     * Create Time : 2016-09-18 <br>
     * Create by : jimmyblylee@126.com
     */
    enum CNS_FILE {
        /** Key of file request. */
        CNS_FILE_KEY;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    /**
     * ClassName : CNS_LIST_REQUEST <br>
     * Description : list request cns, such as grid, tree, combo <br>
     * Create Time : 2016-09-18 <br>
     * Create by : jimmyblylee@126.com
     */
    enum CNS_LIST_REQUEST {
        /** Key of ids to remove, which are split by comma. */
        REMOVE_IDS,
        /** Key of total in response. */
        TOTAL,
        /** Key of page start in request. */
        START,
        /** Key of page limit in request. */
        LIMIT;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    /**
     * ClassName: CNS_SESSIONDTO <br>
     * Description: session dto flag <br>
     * Create Time: Oct 5, 2015 <br>
     * Create by: xiangyu_li@asdc.com.cn
     */
    enum CNS_SESSIONDTO {
        /** Key of token. */
        TOKEN_IN_SESSION;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
