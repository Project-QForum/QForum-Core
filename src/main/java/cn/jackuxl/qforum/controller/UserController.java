package cn.jackuxl.qforum.controller;

import cn.jackuxl.qforum.model.User;
import cn.jackuxl.qforum.serviceimpl.UserServiceImpl;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    HttpServletResponse response;
    @Autowired
    HttpServletRequest request;
    @RequestMapping(value = "/user/register", produces = "application/json;charset=UTF-8")
    public String register(User user, Boolean md5) {
        if (md5 == null || !md5) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        JSONObject result = new JSONObject();
        Map<String, String> md5Info = generate(user.getPassword());
        user.setPassword(md5Info.get("result"));
        user.setSalt(md5Info.get("salt"));
        user.setLastLoginIp(getRemoteHost());
        if (userService.getUserByUserName(user.getUserName()) != null) {
            // 用户名被占用
            result.put("code", 403);
            result.put("error", "username_already_exists");
        } else if (userService.getUserByEmail(user.getEmail()) != null) {
            // 邮箱被占用
            result.put("code", 403);
            result.put("error", "email_already_exists");
        } else if (userService.register(user) > 0) {
            result.put("code", 200);
            result.put("msg", "success");
        } else {
            result.put("code", 403);
            result.put("error", "unknown");
        }
        response.setStatus(result.getInteger("code"));
        return result.toJSONString();
    }

    @RequestMapping(value = "/user/login", produces = "application/json;charset=UTF-8")
    public String login(String userName, String password, Boolean md5) {
        if (md5 == null || !md5) {
            password = DigestUtils.md5DigestAsHex(password.getBytes());
        }
        JSONObject result = new JSONObject();
        User user;
        if (userName.contains("@")) {
            user = userService.getUserByEmail(userName);
            result = checkPasswordForLogin(password, user);
        } else if (userService.getUserByUserName(userName) != null) {
            user = userService.getUserByUserName(userName);
            result = checkPasswordForLogin(password, user);
        } else {
            result.put("code", 403);
            result.put("error", "no_such_user");
        }
        response.setStatus(result.getInteger("code"));
        return result.toJSONString();
    }

    @RequestMapping(value = "/user/logout", produces = "application/json;charset=UTF-8")
    public String logout(String sessionId) {
        JSONObject result = new JSONObject();
        User user = userService.getUserBySessionId(sessionId);
        if (user != null) {
            userService.setSessionId(user.getId(), null);
            result.put("code", 200);
            result.put("msg", "success");
        } else {
            result.put("code", 403);
            result.put("error", "no_such_user");
        }
        response.setStatus(result.getInteger("code"));
        return result.toJSONString();
    }

    @RequestMapping(value = "/user/setUserName", produces = "application/json;charset=UTF-8")
    public String setUserName(String sessionId, String newName) {
        JSONObject result = new JSONObject();
        User user = userService.getUserBySessionId(sessionId);
        if (userService.getUserByUserName(newName) == null) {
            // 用户名被占用
            result.put("code", 403);
            result.put("error", "username_already_exists");
        } else if (user != null && sessionId != null) {
            if (userService.setUserName(user.getId(), newName) > 0) {
                result.put("code", 200);
                result.put("msg", "success");
            } else {
                result.put("code", 403);
                result.put("error", "unknown");
            }
        } else {
            result.put("code", 403);
            result.put("error", "no_such_user");
        }
        response.setStatus(result.getInteger("code"));
        return result.toJSONString();
    }

    @RequestMapping(value = "/user/setPassword", produces = "application/json;charset=UTF-8")
    public String setPassword(String sessionId, String oldPassword, String newPassword, Boolean md5) {
        JSONObject result = new JSONObject();
        User user = userService.getUserBySessionId(sessionId);
        if (user != null && sessionId != null) {
            if (Objects.equals(user.getPassword(), oldPassword)) {
                if (md5 == null || !md5) {
                    newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
                }
                newPassword = generate(newPassword, user.getSalt());
                if (userService.setPassword(user.getId(), newPassword) > 0) {
                    result.put("code", 200);
                    result.put("msg", "success");
                } else {
                    result.put("code", 403);
                    result.put("error", "unknown");
                }
            } else {
                result.put("error", "password_mismatch");
            }
        } else {
            result.put("code", 403);
            result.put("error", "no_such_user");
        }
        response.setStatus(result.getInteger("code"));
        return result.toJSONString();
    }



    /**
     * 加盐MD5
     *
     * @param password 密码
     * @return 结果，盐
     */
    public static Map<String, String> generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            sb.append("0".repeat(Math.max(0, 16 - len)));
        }
        String salt = sb.toString();
        password = DigestUtils.md5DigestAsHex((password + salt).getBytes());
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        Map<String, String> result = new HashMap<>();
        result.put("result", new String(cs));
        result.put("salt", salt);
        return result;
    }

    /**
     * 加盐MD5
     *
     * @param password 密码
     * @return 结果
     */
    public static String generate(String password, String salt) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            sb.append("0".repeat(Math.max(0, 16 - len)));
        }
        password = DigestUtils.md5DigestAsHex((password + salt).getBytes());
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * 校验MD5
     *
     * @param password 密码
     * @param salt     盐
     * @return 结果
     */
    public static boolean verifyPassword(String password, String password_md5, String salt) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            sb.append("0".repeat(Math.max(0, 16 - len)));
        }
        password = DigestUtils.md5DigestAsHex((password + salt).getBytes());
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs).equals(password_md5);
    }

    private JSONObject checkPasswordForLogin(String password, User user) {
        JSONObject result = new JSONObject();
        if (user != null) {
            if (verifyPassword(password, user.getPassword(), user.getSalt())) {
                result.put("code", 200);
                result.put("msg", "success");
                result.put("username", user.getUserName());
                result.put("email",user.getEmail());
                String sessionId = getSessionId();
                try{
                    result.put("sessionId", sessionId);
                } catch (JSONException e){
                    e.printStackTrace();
                }
                try{
                    userService.setLastLoginIp(user.getId(), getRemoteHost());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                userService.setSessionId(user.getId(), sessionId);
            } else {
                result.put("code", 403);
                result.put("error", "password_mismatch");
            }
        } else {
            result.put("code", 403);
            result.put("error", "no_such_user");
        }
        return result;
    }

    private String getSessionId() {
        return request.getSession().getId();
    }

    public String getRemoteHost() {

        String ip = request.getHeader("X-Forwarded-For");

        if (ObjectUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ObjectUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
