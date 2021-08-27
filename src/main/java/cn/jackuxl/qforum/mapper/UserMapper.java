package cn.jackuxl.qforum.mapper;

import cn.jackuxl.qforum.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper {
    @Insert("insert into qf_user(username,password,email,salt,lastLoginIp) values(#{userName},#{password},#{email},#{salt},#{lastLoginIp})")
    int register(User user);

    @Select("select * from qf_user order by id desc")
    List<User> getUserList();

    @Select("select * from qf_user where id=#{id};")
    User getUserById(int id);

    @Select("select * from qf_user where username=#{userName};")
    User getUserByUserName(String userName);

    @Select("select * from qf_user where email=#{email};")
    User getUserByEmail(String email);

    @Select("select * from qf_user where sessionId=#{sessionId};")
    User getUserBySessionId(String sessionId);

    @Update("update qf_user set username=#{newName} where id=#{id};")
    int setUserName(int id, String newName);

    @Update("update qf_user set password=#{newPassword} where id=#{id};")
    int setPassword(int id, String newPassword);

    @Update("update qf_user set sessionId=#{newSessionId} where id=#{id};")
    void setSessionId(int id, String newSessionId);

    @Update("update qf_user set lastLoginIp=#{ip} where id=#{id};")
    void setLastLoginIp(int id, String ip);
}
