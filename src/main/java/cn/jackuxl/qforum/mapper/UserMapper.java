package cn.jackuxl.qforum.mapper;

import cn.jackuxl.qforum.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper {
    @Insert("insert into qf_user(username,password,email,salt,lastLoginIp,introduction,avatarUrl) values(#{userName},#{password},#{email},#{salt},#{lastLoginIp},#{introduction},#{avatarUrl})")
    int register(User user);

    @Select("select * from qf_user order by id asc")
    List<User> listUser();

    @Select("select * from qf_user where id=#{param1};")
    User getUserById(int id);

    @Select("select * from qf_user where username=#{param1};")
    User getUserByUserName(String userName);

    @Select("select * from qf_user where email=#{param1};")
    User getUserByEmail(String email);

    @Update("update qf_user set username=#{param2} where id=#{param1};")
    int setUserName(int id, String newName);

    @Update("update qf_user set password=#{param2} where id=#{param1};")
    int setPassword(int id, String newPassword);

    @Update("update qf_user set introduction=#{param2} where id=#{param1};")
    int setIntroduction(int id, String newIntroduction);

    @Update("update qf_user set avatarUrl=#{param2} where id=#{param1};")
    int setAvatarUrl(int id, String newAvatarUrl);

    @Update("update qf_user set lastLoginIp=#{param2} where id=#{param1};")
    void setLastLoginIp(int id, String ip);
}
