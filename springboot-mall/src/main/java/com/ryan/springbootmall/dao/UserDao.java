package com.ryan.springbootmall.dao;

import com.ryan.springbootmall.dto.UserRegisterRequest;
import com.ryan.springbootmall.model.User;

public interface UserDao {

    User getUserById(Integer userId);

    Integer createUser(UserRegisterRequest userRegisterRequest);

}
