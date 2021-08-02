package cn.jackuxl.qforum.mapper;

import cn.jackuxl.qforum.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface UserMapper {
    @Insert("insert into qf_user(username,password,email,salt) values(#{userName},#{password},#{email},#{salt})")
    int register(User user);

    @Select("select * from qf_user order by id desc")
    List<User> getUserList();

    @Select("select * from qf_user where id=#{id};")
    User getUserById(int id);

    @Select("select * from qf_user where username=#{userName};")
    User getUserByUserName(String userName);

    @Select("select * from qf_user where email=#{email};")
    User getUserByEmail(String email);

    @Select("select * from qf_user where session=#{session};")
    User getUserBySession(String cookie);
}
