package com.ryan.springbootmall.service;

import com.ryan.springbootmall.dao.UserDao;
import com.ryan.springbootmall.dto.UserRegisterRequest;
import com.ryan.springbootmall.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService{

    //噴出log資訊 制式化寫法
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

   @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {

        //判斷email是否有被註冊過
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        //如果傳進來的mail不是空，就會返回錯誤給前端
        if(user != null){

            //{}會把後面變數加進去
            log.warn("mail {} 已被註冊過", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return userDao.createUser(userRegisterRequest);
    }
}
