package cn.jackuxl.qforum.mapper;

import cn.jackuxl.qforum.model.Thread;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface ThreadMapper {
    @Insert("insert into qf_thread(title,type,publisherId,postTime,boardId,content) values(#{title},#{type},#{publisherId},#{postTime},#{boardId},#{content})")
    int postThread(Thread thread);

    @Select("select * from qf_thread where id=#{param1};")
    Thread getThreadById(int id);
}
