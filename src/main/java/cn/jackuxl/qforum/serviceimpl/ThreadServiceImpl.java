package cn.jackuxl.qforum.serviceimpl;

import cn.jackuxl.qforum.mapper.ThreadMapper;
import cn.jackuxl.qforum.entity.Thread;
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
    public List<Thread> listThreads(int boardId){
        List<Thread> threads = threadMapper.listThreads(boardId);
        for (Thread thread : threads) {
            if (Objects.equals(thread.getLikeList(), "null") || thread.getLikeList() == null) {
                threadMapper.updateLikeList(thread.getId(), "[]");
                thread.setLikeList("[]");
            }
        }
        return threads;
    }

    @Override
    public int likeThread(int tid, int uid) {
        JSONArray likeList = JSON.parseArray(threadMapper.getThreadById(tid).getLikeList());
        for(int i = 0;i<likeList.size();i++){
            if(likeList.getInteger(i)==uid){
                return 0;
            }
        }
        likeList.add(uid);
        return threadMapper.updateLikeList(tid,likeList.toJSONString());
    }

    @Override
    public int disLikeThread(int tid, int uid) {
        JSONArray likeList = JSON.parseArray(threadMapper.getThreadById(tid).getLikeList());
        for(int i = 0;i<likeList.size();i++){
            if(likeList.getInteger(i)==uid){
                likeList.remove(i);
            }
        }
        return threadMapper.updateLikeList(tid,likeList.toJSONString());
    }
}
