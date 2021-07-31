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
    @Insert("insert into qf_user(username,password,email) values(#{userName},#{password},#{email})")
    int register(User user);

    @Select("select * from qf_user order by id desc")
    List<User> getUserList();
}
