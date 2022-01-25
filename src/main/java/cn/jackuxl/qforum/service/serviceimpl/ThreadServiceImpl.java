package cn.jackuxl.qforum.service.serviceimpl;

import cn.jackuxl.qforum.entity.Thread;
import cn.jackuxl.qforum.mapper.ThreadMapper;
import cn.jackuxl.qforum.service.ThreadService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
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
    public int deleteThreadById(int id) {
        return threadMapper.deleteThreadById(id);
    }

    @Override
    public List<Thread> listThreads(int boardId) {
        List<Thread> threads = threadMapper.listThreads(boardId);
        for (Thread thread : threads) {
            if (Objects.equals(thread.getLikeList(), "null")) {
                threadMapper.updateLikeList(thread.getId(), "[]");
                thread.setLikeList("[]");
            } else {
                thread.getLikeList();
            }
        }
        return threads;
    }

    @Override
    public int likeThread(int tid, int uid) {
        JSONArray likeList = JSON.parseArray(threadMapper.getThreadById(tid).getLikeList());
        if (likeList.contains(uid)) {
            return 0;
        }
        likeList.add(uid);
        return threadMapper.updateLikeList(tid, likeList.toJSONString());
    }

    @Override
    public int disLikeThread(int tid, int uid) {
        JSONArray likeList = JSON.parseArray(threadMapper.getThreadById(tid).getLikeList());
        likeList.removeIf(o -> Integer.parseInt(o.toString()) == uid);
        return threadMapper.updateLikeList(tid, likeList.toJSONString());
    }
}
