package cn.jackuxl.qforum.service.serviceimpl;

import cn.jackuxl.qforum.entity.Board;
import cn.jackuxl.qforum.mapper.BoardMapper;
import cn.jackuxl.qforum.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardMapper boardMapper;

    @Override
    public int addBoard(Board board) {
        return boardMapper.addBoard(board);
    }

    @Override
    public Board getBoardById(int id) {
        return boardMapper.getBoardById(id);
    }

    @Override
    public List<Board> listBoards() {
        return boardMapper.listBoards();
    }
}
