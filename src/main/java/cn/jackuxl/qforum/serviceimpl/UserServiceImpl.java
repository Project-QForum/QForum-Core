package cn.jackuxl.qforum.serviceimpl;

import cn.jackuxl.qforum.mapper.UserMapper;
import cn.jackuxl.qforum.model.User;
import cn.jackuxl.qforum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public int register(User user) {
        return userMapper.register(user);
    }

    @Override
    public List<User> getUserList(){
        return userMapper.getUserList();
    }
}
