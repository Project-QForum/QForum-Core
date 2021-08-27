package cn.jackuxl.qforum.serviceimpl;

import cn.jackuxl.qforum.mapper.ThreadMapper;
import cn.jackuxl.qforum.model.Thread;
import cn.jackuxl.qforum.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;

public class ThreadServiceImpl implements ThreadService {
    @Autowired
    private ThreadMapper threadMapper;

    @Override
    public int postThread(Thread thread) {
        return threadMapper.postThread(thread);
    }
}
