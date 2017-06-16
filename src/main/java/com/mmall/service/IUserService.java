package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by Justin on 2017/6/16.
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str,String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username,String question,String answer);


}