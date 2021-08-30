package cn.jackuxl.qforum.service;

import cn.jackuxl.qforum.model.Board;
import cn.jackuxl.qforum.model.Thread;


public interface BoardService {
    int addBoard(Board board);
    Board getBoardById(int id);
}
