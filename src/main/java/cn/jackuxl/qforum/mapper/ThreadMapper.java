package cn.jackuxl.qforum.mapper;

import cn.jackuxl.qforum.model.Thread;
import org.apache.ibatis.annotations.Insert;

public interface ThreadMapper {
    @Insert("insert into qf_thread values(#{userName},#{password},#{email},#{salt})")
    int postThread(Thread thread);
}
