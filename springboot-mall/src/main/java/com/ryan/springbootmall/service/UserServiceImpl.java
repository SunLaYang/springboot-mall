package com.ryan.springbootmall.service;

import com.ryan.springbootmall.dao.UserDao;
import com.ryan.springbootmall.dto.UserLoginRequest;
import com.ryan.springbootmall.dto.UserRegisterRequest;
import com.ryan.springbootmall.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

//所有業務邏輯都在service層做判斷
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
        //使用 MD5生成密碼雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        //創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    //檢查mail 跟 密碼是否一致
    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());//根據前端傳進來的mail查詢那筆數據出來

        //檢查user是否存在
        if(user == null){
            log.warn("該 email {} 還未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //使用 MD5生成密碼雜湊值
        String hashedPasswrd = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        //比較密碼
        //如果傳進來的密碼跟資料庫比對一樣  比較字串一定要用equals
        //比較雜湊值
        if(user.getPassword().equals(hashedPasswrd)){
            return user;
        }else{
            log.warn("mail {} 的密碼不正確", userLoginRequest.getEmail() );
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
