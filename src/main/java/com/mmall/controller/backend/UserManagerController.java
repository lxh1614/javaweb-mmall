package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Justin on 2017/6/21.
 */

@Controller
@RequestMapping("/manage/user")
public class UserManagerController{

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(HttpSession session,String username, String password){

        ServerResponse response= iUserService.login(username,password);
        if(response.isSuccess()){
            User user = (User) response.getData();
            if(user.getRole() == Const.Role.ROLE_ADMIN){
                user.setPassword(StringUtils.EMPTY);
                session.setAttribute(Const.CURRENT_USER,user);
                return response;
            }else{
                return ServerResponse.creatByErrorMessage("你不是管理员，无法登录");
            }
        }
        return response;
    }
}
