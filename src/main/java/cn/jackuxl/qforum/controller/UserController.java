package cn.jackuxl.qforum.controller;

import cn.jackuxl.qforum.model.User;
import cn.jackuxl.qforum.serviceimpl.UserServiceImpl;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    HttpServletResponse response;

    @RequestMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public String register(User user,Boolean md5){
        if(md5==null||!md5){
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        JSONObject result = new JSONObject();
        Map<String,String> md5res = generate(user.getPassword());
        user.setPassword(md5res.get("result"));
        user.setSalt(md5res.get("salt"));
        if(userService.getUserByUserName(user.getUserName())!=null){
            // 用户名被占用
            result.put("code",403);
            result.put("error","username_already_exists");
        }
        else if(userService.getUserByEmail(user.getEmail())!=null){
            // 邮箱被占用
            result.put("code",403);
            result.put("error","email_already_exists");
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
    public String login(String userName, String password, Boolean md5){
        if(md5==null||!md5){
            password = DigestUtils.md5DigestAsHex(password.getBytes());
        }
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

    @RequestMapping(value = "/test", produces = "application/json;charset=UTF-8")
    public String test(){
        HttpSession session = request.getSession();
        Cookie c=new Cookie("JSESSIONID", session.getId());
        c.setPath(request.getContextPath());
        c.setMaxAge(30*24*60*60);
        response.addCookie(c);
        return session.getId();
    }

    /**
     * 加盐MD5
     * @param password 密码
     * @return 结果，盐
     */
    public static Map<String,String> generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            sb.append("0".repeat(Math.max(0, 16 - len)));
        }
        String salt = sb.toString();
        password = DigestUtils.md5DigestAsHex((password+salt).getBytes());
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        Map<String,String> result = new HashMap<>();
        result.put("result",new String(cs));
        result.put("salt",salt);
        return result;
    }

    /**
     * 校验MD5
     * @param password 密码
     * @param salt 盐
     * @return 结果
     */
    public static boolean verifyPassword(String password,String password_md5,String salt) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            sb.append("0".repeat(Math.max(0, 16 - len)));
        }
        password = DigestUtils.md5DigestAsHex((password+salt).getBytes());
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs).equals(password_md5);
    }

    private void checkPasswordForLogin(String password, JSONObject result, User user) {
        if (user != null) {
            if(verifyPassword(password, user.getPassword(), user.getSalt())){
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
