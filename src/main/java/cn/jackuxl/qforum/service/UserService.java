package cn.jackuxl.qforum.service;

import cn.jackuxl.qforum.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    int register(User user);

    List<User> listUser();

    User getUserById(int id);

    User getUserByUserName(String userName);

    User getUserByEmail(String email);


    int setUserName(int id, String newName);

    int setPassword(int id, String newPassword);

    int setIntroduction(int id, String newIntroduction);

    int setAvatarUrl(int id, String newAvatarUrl);


    void setLastLoginIp(int id, String lastLoginIp);
}
