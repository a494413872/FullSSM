package com.lvmama.scenic.comm.vo;

public class Constant {

    public final static String DEFAULT_LOCATION = "www";
    public static final String LV_SESSION_ID = "lvsessionid";
    public final static String SESSION_FRONT_USER = "SESSION_FRONT_USER";
    public final static String SESSION_BACK_USER = "SESSION_BACK_USER";
    public final static String SYSTEM_USER = "admin";

    public static enum LOGIN_TYPE {
        MOBILE("手机"), HTML5("wap");
        private String cnName;

        LOGIN_TYPE(String name) {
            this.cnName = name;
        }

        public String getCode() {
            return this.name();
        }

        public String getCnName() {
            return this.cnName;
        }

        public static String getCnName(String code) {
            for (LOGIN_TYPE item : LOGIN_TYPE.values()) {
                if (item.getCode().equals(code)) {
                    return item.getCnName();
                }
            }
            return code;
        }

        @Override
        public String toString() {
            return "code:" + this.name() + ",cnName:" + this.cnName;
        }
    }
}
