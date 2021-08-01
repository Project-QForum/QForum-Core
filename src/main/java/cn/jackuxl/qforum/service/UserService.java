package cn.jackuxl.qforum.service;

import cn.jackuxl.qforum.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    int register(User user);
    List<User> getUserList();
    User getUserById(int id);
    User getUserByUserName(String userName);
    User getUserByEmail(String email);
}
