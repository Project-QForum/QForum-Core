package cn.jackuxl.qforum.controller;

import cn.jackuxl.qforum.entity.User;
import cn.jackuxl.qforum.model.Result;
import cn.jackuxl.qforum.model.ResultEntity;
import cn.jackuxl.qforum.service.serviceimpl.UserServiceImpl;
import cn.jackuxl.qforum.util.BasicUtil;
import cn.jackuxl.qforum.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    public ResultEntity<Object> register(User user, Boolean md5) {
        if (md5 == null || !md5) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        Map<String, String> md5Info = generate(user.getPassword());
        user.setPassword(md5Info.get("result"));
        user.setSalt(md5Info.get("salt"));
        user.setLastLoginIp(getRemoteHost());
        user.setAdmin(false);

        BasicUtil.assertTool(userService.getUserByUserName(user.getUserName()) == null, "username_already_exists");
        BasicUtil.assertTool(!user.getUserName().contains("@"), "username_cannot_contain_at");
        BasicUtil.assertTool(userService.getUserByEmail(user.getEmail()) == null, "email_already_exists");

        BasicUtil.assertTool(userService.register(user) > 0, "unknown");
        return Result.INSTANCE.ok("success");
    }

    @RequestMapping(value = "/user/login", produces = "application/json;charset=UTF-8")
    public ResultEntity<User> login(String userName, String password, Boolean md5) {
        if (md5 == null || !md5) {
            password = DigestUtils.md5DigestAsHex(password.getBytes());
        }
        User user = null;
        if (userName.contains("@")) {
            user = userService.getUserByEmail(userName);
        } else if (userService.getUserByUserName(userName) != null) {
            user = userService.getUserByUserName(userName);
        }
        BasicUtil.assertTool(user != null, "no_such_user");
        BasicUtil.assertTool(verifyPassword(password, user.getPassword(), user.getSalt()), "password_mismatch");

        try {
            userService.setLastLoginIp(user.getId(), getRemoteHost());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String sessionId = getSessionId();
        userService.setSessionId(user.getId(), sessionId);

        user.setSessionId(sessionId);
        user.setLastLoginIp(null);
        user.setPassword(null);
        user.setSalt(null);

        return Result.INSTANCE.ok("success", user);
    }

    @RequestMapping(value = "/user/logout", produces = "application/json;charset=UTF-8")
    public ResultEntity<String> logout(String sessionId) {
        User user = userService.getUserBySessionId(sessionId);
        BasicUtil.assertTool(user != null, "no_such_user");
        userService.setSessionId(user.getId(), null);
        return Result.INSTANCE.ok("success");
    }

    @RequestMapping(value = "/user/setUserName", produces = "application/json;charset=UTF-8")
    public ResultEntity<String> setUserName(String sessionId, String newName) {
        User user = userService.getUserBySessionId(sessionId);
        BasicUtil.assertTool(user != null && sessionId != null, "no_such_user");
        BasicUtil.assertTool(userService.getUserByUserName(newName) == null, "username_already_exists");

        BasicUtil.assertTool(userService.setUserName(user.getId(), newName) > 0, "unknown");
        return Result.INSTANCE.ok("success");
    }

    @RequestMapping(value = "/user/setIntroduction", produces = "application/json;charset=UTF-8")
    public ResultEntity<String> setIntroduction(String sessionId, String newIntroduction) {
        User user = userService.getUserBySessionId(sessionId);
        BasicUtil.assertTool(user != null && sessionId != null, "no_such_user");

        BasicUtil.assertTool(userService.setIntroduction(user.getId(), newIntroduction) > 0, "unknown");
        return Result.INSTANCE.ok("success");
    }

    @RequestMapping(value = "/user/setAvatarUrl", produces = "application/json;charset=UTF-8")
    public ResultEntity<String> setAvatarUrl(String sessionId, String newAvatarUrl) {
        User user = userService.getUserBySessionId(sessionId);
        BasicUtil.assertTool(user != null && sessionId != null, "no_such_user");

        BasicUtil.assertTool(userService.setAvatarUrl(user.getId(), newAvatarUrl) > 0, "unknown");
        return Result.INSTANCE.ok("success");
    }

    @RequestMapping(value = "/user/setPassword", produces = "application/json;charset=UTF-8")
    public ResultEntity<String> setPassword(String sessionId, String oldPassword, String newPassword, Boolean md5) {
        User user = userService.getUserBySessionId(sessionId);
        BasicUtil.assertTool(user != null && sessionId != null, "no_such_user");
        if (md5 == null || !md5) {
            newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
            oldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        }
        BasicUtil.assertTool(verifyPassword(oldPassword, user.getPassword(), user.getSalt()), "password_mismatch");
        newPassword = generate(newPassword, user.getSalt());

        BasicUtil.assertTool(userService.setPassword(user.getId(), newPassword) > 0, "unknown");

        return Result.INSTANCE.ok("success");
    }

    @RequestMapping(value = "/user/checkLogin", produces = "application/json;charset=UTF-8")
    public ResultEntity<String> checkLogin(String sessionId) {
        User user = userService.getUserBySessionId(sessionId);
        BasicUtil.assertTool(user != null, "no_such_user");
        return Result.INSTANCE.ok("success");
    }

    @RequestMapping(value = "/user/getProfile", produces = "application/json;charset=UTF-8")
    public ResultEntity<UserVo> getProfile(Integer id, String userName) {
        User user = null;

        if (id != null) {
            user = userService.getUserById(id);
        } else if (userName != null) {
            user = userService.getUserByUserName(userName);
        }

        BasicUtil.assertTool(user != null, "no_such_user");

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);

        return Result.INSTANCE.ok("success", userVo);
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
