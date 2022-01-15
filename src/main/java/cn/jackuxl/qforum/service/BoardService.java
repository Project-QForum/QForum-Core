package cn.jackuxl.qforum.service;

import cn.jackuxl.qforum.entity.Board;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BoardService {
    int addBoard(Board board);
    Board getBoardById(int id);
    List<Board> listBoards();
}
