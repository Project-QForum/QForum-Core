package cn.jackuxl.qforum.service;

import cn.jackuxl.qforum.model.Board;
import cn.jackuxl.qforum.model.Thread;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ThreadService {
    int postThread(Thread thread);
    Thread getThreadById(int id);
    List<Thread> listThreads(int boardId);
}
