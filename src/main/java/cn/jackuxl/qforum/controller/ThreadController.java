package cn.jackuxl.qforum.controller;

import cn.jackuxl.qforum.model.Thread;
import cn.jackuxl.qforum.model.User;
import cn.jackuxl.qforum.serviceimpl.BoardServiceImpl;
import cn.jackuxl.qforum.serviceimpl.ThreadServiceImpl;
import cn.jackuxl.qforum.serviceimpl.UserServiceImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    public String postThread(String sessionId, Thread thread,int boardId) {
        JSONObject result = new JSONObject();
        User user = userService.getUserBySessionId(sessionId);
        if (user != null && sessionId != null) {
            if(boardService.getBoardById(boardId)==null){
                result.put("code", 403);
                result.put("error", "no_such_board");
            }
            else{
                thread.setPublisherId(user.getId());
                thread.setPostTime(String.valueOf(System.currentTimeMillis()));
                thread.setBoardId(boardId);
                if (threadService.postThread(thread) > 0) {
                    result.put("code", 200);
                    result.put("msg", "success");
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
        } else {
            result.put("code", 403);
            result.put("error", "no_such_thread");
        }
        response.setStatus(result.getInteger("code"));
        return result.toJSONString();
    }
}
