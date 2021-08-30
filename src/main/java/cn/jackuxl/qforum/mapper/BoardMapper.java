package cn.jackuxl.qforum.mapper;

import cn.jackuxl.qforum.model.Board;
import cn.jackuxl.qforum.model.Thread;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface BoardMapper {
    @Insert("insert into qf_board(name,priorityLevel) values(#{name},#{priorityLevel})")
    int addBoard(Board board);

    @Select("select * from qf_board where id=#{param1};")
    Board getBoardById(int id);
}
