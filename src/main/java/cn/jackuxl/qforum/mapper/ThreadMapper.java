package cn.jackuxl.qforum.mapper;

import cn.jackuxl.qforum.entity.Thread;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ThreadMapper {
    @Insert("insert into qf_thread(title,type,publisherId,postTime,boardId,content,likeList) values(#{title},#{type},#{publisherId},#{postTime},#{boardId},#{content},#{likeList})")
    int postThread(Thread thread);

    @Select("select * from qf_thread where id=#{param1};")
    Thread getThreadById(int id);

    @Select("select * from qf_thread where boardId=#{param1};")
    List<Thread> listThreads(int boardId);

    @Update("update qf_thread set likeList=#{param2} where id=#{param1};")
    int updateLikeList(int tid,String newLikeList);
}
