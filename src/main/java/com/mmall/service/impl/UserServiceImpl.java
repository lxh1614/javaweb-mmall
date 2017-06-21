package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.UUID;

/**
 * Created by Justin on 2017/6/16.
 */

@Service("iUserService")
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int result = userMapper.checkUserName(username);
        if(result == 0){
            return ServerResponse.creatByErrorMessage("用户名不存在");
        }
        String md5password = MD5Util.MD5EncodeUtf8(password);

         User user = userMapper.selectLogin(username,md5password);
        if(user == null){
            return ServerResponse.creatByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.creatBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse<String> register(User user) {

       ServerResponse response = checkValid(user.getUsername(),Const.USER_NAME);
        if(!response.isSuccess()){
            return response;
        }
        response = checkValid(user.getEmail(),Const.EAML);
        if(!response.isSuccess()){
            return response;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //md5 加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int result = userMapper.insert(user);
        if(result == 0){
            return ServerResponse.creatByErrorMessage("注册失败");
        }
        return ServerResponse.creatBySuccessMsg("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {

        if(StringUtils.isNotBlank(str)){
            if(Const.USER_NAME.equals(type)) {
                int result = userMapper.checkUserName(str);
                if (result > 0) {
                    return ServerResponse.creatByErrorMessage("用已存在");
                }
            }else if(Const.EAML.equals(type)) {
               int  result = userMapper.checkEmail(str);
                if(result > 0){
                    return ServerResponse.creatByErrorMessage("email已存在");
                }
            }
        }else{
            return ServerResponse.creatByErrorMessage("参数错误");
        }
        return ServerResponse.creatBySuccess();
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse response = checkValid(username,Const.USER_NAME);
        if(response.isSuccess()){
            //用户不存在
            return ServerResponse.creatByErrorMessage("用户不存在");
        }

       String qustion =  userMapper.seletctQuestionByUsername(username);
        if(StringUtils.isNotBlank(qustion)){
            return ServerResponse.creatBySuccess(qustion);
        }
        return ServerResponse.creatByErrorMessage("找回密码问题为空，未设置");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username,String question,String answer) {
           int result =  userMapper.checkAnswer(username,question,answer);
           if(result >0){
               //用户信息正确
               String forgetToke = UUID.randomUUID().toString();
               TokenCache.setKey(TokenCache.TOKEN_RPEFIX+username,forgetToke);
               return ServerResponse.creatBySuccess(forgetToke);
           }
            return ServerResponse.creatByErrorMessage("问题答案错误");
    }

    @Override
    public  ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.creatByErrorMessage("参数错误，token 需要传递");
        }
        ServerResponse response = checkValid(username,Const.USER_NAME);
        if(response.isSuccess()){
            //用户不存在
            return ServerResponse.creatByErrorMessage("用户不存在");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_RPEFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.creatByErrorMessage("token无效或过期");
        }
        if(StringUtils.equals(token,forgetToken)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
          int rowResult = userMapper.updatePasswordByUserName(username,md5Password);
            if (rowResult > 0) {
                return ServerResponse.creatBySuccessMsg("密码修改成功");
            }
        }else{
            return ServerResponse.creatBySuccessMsg("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.creatBySuccessMsg("修改密码失败");
    }

    @Override
    public  ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        int resultCount = userMapper.checkPassword(passwordOld,user.getId());
        if(resultCount > 0){
            user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
           resultCount =  userMapper.updateByPrimaryKeySelective(user);
           if(resultCount >0){
               return  ServerResponse.creatBySuccessMsg("密码更新成功");
           }else{
               return ServerResponse.creatByErrorMessage("密码更新失败");
           }
        }else{
            return ServerResponse.creatByErrorMessage("旧密码错误");
        }
    }

    public  ServerResponse<User> updateInformation(User user){

        int result = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(result > 0){
            return ServerResponse.creatByErrorMessage("email 已经存在，请重新输入");
        }

        User updateUser = new User();
        updateUser.setUsername(user.getUsername());
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int udpateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (udpateCount >0){
            return  ServerResponse.creatBySuccess("更新个人信息成功",updateUser);
        }else{
            return ServerResponse.creatByErrorMessage("更新个人信息失败");
        }
    }

    public  ServerResponse<User> getInformation(Integer userid){

        User user = userMapper.selectByPrimaryKey(userid);
        if(user == null){
            return ServerResponse.creatByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.creatBySuccess(user);
    }
 }
