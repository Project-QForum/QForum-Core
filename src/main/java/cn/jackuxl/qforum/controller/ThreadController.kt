package cn.jackuxl.qforum.controller;

import cn.jackuxl.qforum.model.Thread;
import cn.jackuxl.qforum.model.User;
import cn.jackuxl.qforum.serviceimpl.BoardServiceImpl;
import cn.jackuxl.qforum.serviceimpl.ThreadServiceImpl;
import cn.jackuxl.qforum.serviceimpl.UserServiceImpl;
import cn.jackuxl.qforum.util.InfoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
public class ThreadController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ThreadServiceImpl threadService;
    @Autowired
    private BoardServiceImpl boardService;
    @Autowired
    private HttpServletResponse response;

    @RequestMapping(value = "/thread/post", produces = "application/json;charset=UTF-8")
    public String postThread(String sessionId, Thread thread) {
        JSONObject result = new JSONObject();
        User user = userService.getUserBySessionId(sessionId);
        if (user != null && sessionId != null) {
            if(boardService.getBoardById(thread.getBoardId())==null){
                result.put("code", 403);
                result.put("error", "no_such_board");
            }
            else if(thread.getTitle().isEmpty()){
                result.put("code", 403);
                result.put("error", "title_cannot_be_empty");
            }
            else{
                thread.setPublisherId(user.getId());
                thread.setPostTime(String.valueOf(System.currentTimeMillis()));
                if (threadService.postThread(thread) > 0) {
                    result.put("code", 200);
                    result.put("msg", "success");
                    List<Thread> threads = threadService.listThreads(thread.getBoardId());
                    result.put("id",threads.get(threads.size()-1).getId());
                } else {
                    result.put("code", 403);
                    result.put("error", "unknown");
                }
            }
        } else {
            result.put("code", 403);
            result.put("error", "no_such_user");
        }
        response.setStatus(result.getInteger("code"));
        return result.toJSONString();
    }

    @RequestMapping(value = "/thread/getThreadDetail", produces = "application/json;charset=UTF-8")
    public String getThreadDetail(int id) {
        JSONObject result = new JSONObject();
        Thread thread  = threadService.getThreadById(id);
        if (thread!=null) {
            result = JSON.parseObject(JSON.toJSONString(thread));
            result.put("code",200);
            InfoUtil.INSTANCE.init(userService);
            result.put("publisher",InfoUtil.INSTANCE.getPublicUserInfo(result.getInteger("publisherId")));
            result.remove("publisherId");
        } else {
            result.put("code", 403);
            result.put("error", "no_such_thread");
        }
        response.setStatus(result.getInteger("code"));
        return result.toJSONString();
    }

    @RequestMapping(value = "/thread/list", produces = "application/json;charset=UTF-8")
    public String listThreads(int boardId) {
        JSONObject result = new JSONObject();
        if(boardService.getBoardById(boardId)!=null){
            result.put("code", 200);
            result.put("msg", "success");
            List<Thread> threads = threadService.listThreads(boardId);
            JSONArray tmp = JSON.parseArray(JSON.toJSONString(threads));
            for(int i = 0;i<tmp.size();i++){
                InfoUtil.INSTANCE.init(userService);
                tmp.getJSONObject(i).put("publisher", InfoUtil.INSTANCE.getPublicUserInfo(tmp.getJSONObject(i).getInteger("publisherId")));
                tmp.getJSONObject(i).remove("publisherId");
            }
            result.put("threadList",tmp);
            result.put("size",threads.size());
        }
        else{
            result.put("code", 403);
            result.put("error", "no_such_board");
        }
        response.setStatus(result.getInteger("code"));
        return result.toJSONString();
    }

    final int LIKE = 0,DISLIKE=1;
    @RequestMapping(value = "/thread/like", produces = "application/json;charset=UTF-8")
    public String likeThread(@NonNull int type, @NonNull int tid,@NonNull String sessionId){
        JSONObject result = new JSONObject();
        User user = userService.getUserBySessionId(sessionId);
        if (user != null) {
            result.put("code", 200);
            result.put("msg", "success");
            switch (type){
                case LIKE:
                    threadService.likeThread(tid,user.getId());
                    result.put("code", 200);
                    result.put("msg", "success");
                    break;
                case DISLIKE:
                    threadService.disLikeThread(tid,user.getId());
                    result.put("code", 200);
                    result.put("msg", "success");
                    break;
                default:
                    result.put("code", 403);
                    result.put("msg", "error_type");
                    break;
            }
        }
        else {
            result.put("code", 403);
            result.put("error", "no_such_user");
        }
        response.setStatus(result.getInteger("code"));
        return result.toJSONString();
    }

}