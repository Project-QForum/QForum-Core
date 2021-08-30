package cn.jackuxl.qforum.service;

import cn.jackuxl.qforum.model.Board;
import cn.jackuxl.qforum.model.Thread;

import java.util.List;


public interface BoardService {
    int addBoard(Board board);
    Board getBoardById(int id);
    List<Board> listBoards();
}
