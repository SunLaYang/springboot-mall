package com.ryan.springbootmall.service;

import com.ryan.springbootmall.dto.UserRegisterRequest;
import com.ryan.springbootmall.model.User;

public interface UserService {

    User getUserById(Integer userId);
    Integer register(UserRegisterRequest userRegisterRequest);


}
