package cn.jackuxl.qforum.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.jackuxl.qforum.constants.StaticProperty;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@CrossOrigin
@RestController
@RequestMapping(value = "/user/", produces = "application/json;charset=UTF-8")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "register")
    public ResultEntity<Object> register(User user, Boolean md5) {
        if (md5 == null || !md5) {
            user.setPassword(DigestUtils.md5DigestAsHex(Objects.requireNonNull(user.getPassword()).getBytes()));
        }
        Map<String, String> md5Info = generate(user.getPassword());
        user.setPassword(md5Info.get("result"));
        user.setSalt(md5Info.get("salt"));
        user.setLastLoginIp(getRemoteHost());
        user.setAdmin(false);
        user.setOfficial(null);

        BasicUtil.assertTool(userService.getUserByUserName(user.getUserName()) == null, StaticProperty.USERNAME_ALREADY_EXISTS);
        BasicUtil.assertTool(!Objects.requireNonNull(user.getUserName()).contains("@"), StaticProperty.USERNAME_CANNOT_CONTAIN_AT);
        BasicUtil.assertTool(userService.getUserByEmail(user.getEmail()) == null, StaticProperty.EMAIL_ALREADY_EXISTS);

        BasicUtil.assertTool(userService.register(user) > 0, StaticProperty.UNKNOWN);
        return Result.INSTANCE.ok(StaticProperty.SUCCESS);
    }

    @RequestMapping(value = "login")
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
        BasicUtil.assertTool(user != null, StaticProperty.NO_SUCH_USER);
        BasicUtil.assertTool(verifyPassword(password, user.getPassword(), user.getSalt()), StaticProperty.PASSWORD_MISMATCH);

        try {
            userService.setLastLoginIp(user.getId(), getRemoteHost());
        } catch (Exception e) {
            e.printStackTrace();
        }

        StpUtil.login(user.getId());

        user.setToken(StpUtil.getTokenValueByLoginId(user.getId()));
        user.setLastLoginIp(null);
        user.setPassword(null);
        user.setSalt(null);

        return Result.INSTANCE.ok(StaticProperty.SUCCESS, user);
    }

    @RequestMapping(value = "logout")
    public ResultEntity<String> logout(String token) {
        StpUtil.logoutByTokenValue(token);
        return Result.INSTANCE.ok(StaticProperty.SUCCESS);
    }

    @RequestMapping(value = "setUserName")
    public ResultEntity<String> setUserName(String newName) {

        BasicUtil.assertTool(StpUtil.isLogin(), StaticProperty.NO_SUCH_USER);
        BasicUtil.assertTool(userService.getUserByUserName(newName) == null, StaticProperty.USERNAME_ALREADY_EXISTS);

        BasicUtil.assertTool(userService.setUserName(StpUtil.getLoginIdAsInt(), newName) > 0, StaticProperty.UNKNOWN);
        return Result.INSTANCE.ok(StaticProperty.SUCCESS);
    }

    @RequestMapping(value = "setIntroduction")
    public ResultEntity<String> setIntroduction(String newIntroduction) {
        BasicUtil.assertTool(StpUtil.isLogin(), StaticProperty.NO_SUCH_USER);
        BasicUtil.assertTool(userService.setIntroduction(StpUtil.getLoginIdAsInt(), newIntroduction) > 0, StaticProperty.UNKNOWN);
        return Result.INSTANCE.ok(StaticProperty.SUCCESS);
    }

    @RequestMapping(value = "setAvatarUrl")
    public ResultEntity<String> setAvatarUrl(String newAvatarUrl) {
        BasicUtil.assertTool(StpUtil.isLogin(), StaticProperty.NO_SUCH_USER);
        BasicUtil.assertTool(userService.setAvatarUrl(StpUtil.getLoginIdAsInt(), newAvatarUrl) > 0, StaticProperty.UNKNOWN);
        return Result.INSTANCE.ok(StaticProperty.SUCCESS);
    }

    @RequestMapping(value = "setPassword")
    public ResultEntity<String> setPassword(String oldPassword, String newPassword, Boolean md5) {
        BasicUtil.assertTool(StpUtil.isLogin(), StaticProperty.NO_SUCH_USER);

        User user = userService.getUserById(StpUtil.getLoginIdAsInt());
        if (md5 == null || !md5) {
            newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
            oldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        }
        BasicUtil.assertTool(verifyPassword(oldPassword, user.getPassword(), user.getSalt()), StaticProperty.PASSWORD_MISMATCH);
        newPassword = generate(newPassword, user.getSalt());

        BasicUtil.assertTool(userService.setPassword(user.getId(), newPassword) > 0, StaticProperty.UNKNOWN);

        return Result.INSTANCE.ok(StaticProperty.SUCCESS);
    }

    @RequestMapping(value = "checkLogin")
    public ResultEntity<String> checkLogin() {
        BasicUtil.assertTool(StpUtil.isLogin(), StaticProperty.NO_SUCH_USER);
        return Result.INSTANCE.ok(StaticProperty.SUCCESS);
    }

    @RequestMapping(value = "getProfile")
    public ResultEntity<UserVo> getProfile(Integer id, String userName) {
        User user = null;

        if (id != null) {
            user = userService.getUserById(id);
        } else if (userName != null) {
            user = userService.getUserByUserName(userName);
        }

        BasicUtil.assertTool(user != null, StaticProperty.NO_SUCH_USER);

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);

        return Result.INSTANCE.ok(StaticProperty.SUCCESS, userVo);
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


    public String getRemoteHost() {

        String ip = request.getHeader("X-Forwarded-For");

        if (ObjectUtils.isEmpty(ip) || StaticProperty.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ObjectUtils.isEmpty(ip) || StaticProperty.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
