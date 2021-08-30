package cn.jackuxl.qforum.service;

import cn.jackuxl.qforum.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    int register(User user);

    List<User> listUser();

    User getUserById(int id);

    User getUserByUserName(String userName);

    User getUserByEmail(String email);

    User getUserBySessionId(String sessionId);

    int setUserName(int id, String newName);

    int setPassword(int id, String newPassword);

    void setSessionId(int id, String newSessionId);

    void setLastLoginIp(int id, String lastLoginIp);
}
