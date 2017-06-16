package com.mmall.common;

/**
 * Created by Justin on 2017/6/16.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String  EAML = "email";
    public static final String  USER_NAME ="user_name";

    public interface Role{
        int ROLE_CUSTOMER = 0;//普通用户
        int ROLE_ADMIN = 1;//管理员
    }
}
