package com.ryan.springbootmall.controller;

import com.ryan.springbootmall.dto.UserLoginRequest;
import com.ryan.springbootmall.dto.UserRegisterRequest;
import com.ryan.springbootmall.model.User;
import com.ryan.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //創建會員
    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
       Integer userId = userService.register(userRegisterRequest);

       User user = userService.getUserById(userId);

       return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    //登入功能
    @PostMapping("/users/login")//因為密碼比較隱密 所以用post
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
        User user = userService.login(userLoginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
