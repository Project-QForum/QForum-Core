package cn.jackuxl.qforum.serviceimpl;

import cn.jackuxl.qforum.mapper.BoardMapper;
import cn.jackuxl.qforum.mapper.ThreadMapper;
import cn.jackuxl.qforum.model.Board;
import cn.jackuxl.qforum.model.Thread;
import cn.jackuxl.qforum.service.BoardService;
import cn.jackuxl.qforum.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardMapper boardMapper;

    @Override
    public int addBoard(Board board) {
        return boardMapper.addBoard(board);
    }

    @Override
    public Board getBoardById(int id){
        return boardMapper.getBoardById(id);
    }
}
