package cn.jackuxl.qforum.mapper;

import cn.jackuxl.qforum.model.Board;
import cn.jackuxl.qforum.model.Thread;
import cn.jackuxl.qforum.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BoardMapper {
    @Insert("insert into qf_board(name,description,priorityLevel) values(#{name},#{description},#{priorityLevel})")
    int addBoard(Board board);

    @Select("select * from qf_board where id=#{param1};")
    Board getBoardById(int id);

    @Select("select * from qf_board order by priorityLevel desc")
    List<Board> listBoards();
}
