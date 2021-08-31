package cn.jackuxl.qforum.serviceimpl;

import cn.jackuxl.qforum.mapper.ThreadMapper;
import cn.jackuxl.qforum.model.Board;
import cn.jackuxl.qforum.model.Thread;
import cn.jackuxl.qforum.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public class ThreadServiceImpl implements ThreadService {
    @Autowired
    private ThreadMapper threadMapper;

    @Override
    public int postThread(Thread thread) {
        return threadMapper.postThread(thread);
    }

    @Override
    public Thread getThreadById(int id) {
        return threadMapper.getThreadById(id);
    }

    @Override
    public List<Thread> listThreads(int boardId){
        return threadMapper.listThreads(boardId);
    }
}
