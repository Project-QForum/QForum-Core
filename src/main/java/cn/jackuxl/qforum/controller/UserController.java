package cn.jackuxl.qforum.controller;

import cn.jackuxl.qforum.model.User;
import cn.jackuxl.qforum.serviceimpl.UserServiceImpl;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @RequestMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public String register(User user){
        JSONObject result = new JSONObject();
        if(getUserNameList().contains(user.getUserName())){
            // 用户名被占用
            result.put("code",403);
            result.put("msg","username is taken");
        }
        else if(getEmailList().contains(user.getEmail())){
            // 邮箱被占用
            result.put("code",403);
            result.put("msg","email is taken");
        }
        else if(userService.register(user)>0){
            result.put("code",200);
            result.put("msg","success");
        }
        else{
            result.put("code",403);
            result.put("msg","error");
        }
        return result.toJSONString();
    }

    @RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public String login(String userName,String password){
        JSONObject result = new JSONObject();
        List<User> userList = userService.getUserList();
        User user = null;
        if(userName.contains("@")){
            for (User value : userList) {
                if (value.getEmail().equals(userName)) {
                    user = value;
                }
            }
            checkPasswordForLogin(password, result, user);
        }
        else if(getUserNameList().contains(userName)){
            for (User value : userList) {
                if (value.getUserName().equals(userName)) {
                    user = value;
                }
            }
            checkPasswordForLogin(password, result, user);
        }
        else{
            result.put("code",403);
            result.put("msg","error");
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
                result.put("msg","wrong password");
            }
        }
        else{
            result.put("code",403);
            result.put("msg","error");
        }
    }

    public List<String> getUserNameList(){
        List<User> userList = userService.getUserList();
        List<String> result = new ArrayList<>();
        for (User tmp_user : userList) {
            result.add(tmp_user.getUserName());
        }
        return result;
    }

    public List<String> getEmailList(){
        List<User> userList = userService.getUserList();
        List<String> result = new ArrayList<>();
        for (User tmp_user : userList) {
            result.add(tmp_user.getEmail());
        }
        return result;
    }


    public List<Integer> getIdList(){
        List<User> userList = userService.getUserList();
        List<Integer> result = new ArrayList<>();
        for (User tmp_user : userList) {
            result.add(tmp_user.getId());
        }
        return result;
    }
}
