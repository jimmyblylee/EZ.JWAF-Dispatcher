/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : AppConstant.java <br>
 * Package Name : com.lee.jwaf.dto <br>
 * Create Time : 2016-09-18 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.dto;

/**
 * ClassName : AppConstant <br>
 * Description : constant of UI Layer and Controller Layer <br>
 * Create Time : 2016-09-18 <br>
 * Create by : jimmyblylee@126.com
 */
public interface AppConstant {

    /**
     * ClassName : CNS_SERVER <br>
     * Description : controller constant <br>
     * Create Time : 2016-09-18 <br>
     * Create by : jimmyblylee@126.com
     */
    public enum CNS_SERVER {
        /** key of controller name */
        CONTROLLER,
        /** key of method name */
        METHOD;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        };
    };

    /**
     * ClassName : CNS_REQUEST <br>
     * Description : request flag <br>
     * Create Time : Apr 12, 2016 <br>
     * Create by : xiangyu_li@asdc.com.cn <br>
     *
     */
    public enum CNS_REQUEST {
        /** key of result in response */
        RESULT,
        /** if this code is in the workDTO, the controller will control the output stream it self */
        LET_ME_CTRL_THE_STREAM,
        /** SUCCESS flag */
        SUCCESS,
        /** error code */
        ERR_CODE,
        /** error level warning or error */
        ERR_LEVEL,
        /** error message */
        ERR_MESSAGE;

        public final static String CNS_ERROR = "error";
        public final static String CNS_WARNING = "warning";

        @Override
        public String toString() {
            if (this.equals(SUCCESS)) {
                return "success";
            } else if (this.equals(ERR_MESSAGE)) {
                return "errMsg";
            } else if (this.equals(ERR_CODE)) {
                return "errCode";
            } else if (this.equals(ERR_LEVEL)) {
                return "errLevel";
            } else if (this.equals(LET_ME_CTRL_THE_STREAM)) { return "selfControledStream"; }
            return this.name().toLowerCase();
        };
    }

    /**
     * ClassName : CNS_FILE <br>
     * Description : file constant <br>
     * Create Time : 2016-09-18 <br>
     * Create by : jimmyblylee@126.com
     */
    public enum CNS_FILE {
        /** key of file request */
        CNS_FILE_KEY;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        };
    };

    /**
     * ClassName : CNS_LIST_REQUEST <br>
     * Description : list request cns, such as grid, tree, combo <br>
     * Create Time : 2016-09-18 <br>
     * Create by : jimmyblylee@126.com
     */
    public enum CNS_LIST_REQUEST {
        /** key of ids to remove, which are split by comma */
        REMOVE_IDS,
        /** key of total in response */
        TOTAL,
        /** key of page start in request */
        START,
        /** key of page limit in request */
        LIMIT;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        };
    }

    /**
     * ClassName: CNS_SESSIONDTO <br>
     * Description: session dto flag <br>
     * Create Time: Oct 5, 2015 <br>
     * Create by: xiangyu_li@asdc.com.cn
     */
    public enum CNS_SESSIONDTO {
        /** key of token */
        TOKEN_IN_SESSION;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        };
    }
}
