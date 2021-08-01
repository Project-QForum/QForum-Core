package cn.jackuxl.qforum.controller;

import cn.jackuxl.qforum.model.User;
import cn.jackuxl.qforum.serviceimpl.UserServiceImpl;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @RequestMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public String register(User user){
        JSONObject result = new JSONObject();
        if(userService.getUserByUserName(user.getUserName())!=null){
            // 用户名被占用
            result.put("code",403);
            result.put("error","username_is_taken");
        }
        else if(userService.getUserByEmail(user.getEmail())!=null){
            // 邮箱被占用
            result.put("code",403);
            result.put("error","email_is_taken");
        }
        else if(userService.register(user)>0){
            result.put("code",200);
            result.put("msg","success");
        }
        else{
            result.put("code",403);
            result.put("error","unknown");
        }
        return result.toJSONString();
    }

    @RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public String login(String userName,String password){
        JSONObject result = new JSONObject();
        User user;
        if(userName.contains("@")){
            user= userService.getUserByEmail(userName);
            checkPasswordForLogin(password, result, user);
        }
        else if(userService.getUserByUserName(userName)!=null){
            user= userService.getUserByUserName(userName);
            checkPasswordForLogin(password, result, user);
        }
        else{
            result.put("code",403);
            result.put("error","no_such_user");
        }
        return result.toJSONString();
    }

    private void checkPasswordForLogin(String password, JSONObject result, User user) {
        if (user != null) {
            if(user.getPassword().equals(password)){
                result.put("code",200);
                result.put("msg","success");
            }
            else{
                result.put("code",403);
                result.put("error","password_mismatch");
            }
        }
        else{
            result.put("code",403);
            result.put("error","no_such_user");
        }
    }
}
