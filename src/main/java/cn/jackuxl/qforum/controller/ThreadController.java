package cn.jackuxl.qforum.controller;

import cn.jackuxl.qforum.model.Thread;
import cn.jackuxl.qforum.model.User;
import cn.jackuxl.qforum.serviceimpl.ThreadServiceImpl;
import cn.jackuxl.qforum.serviceimpl.UserServiceImpl;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

public class ThreadController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ThreadServiceImpl threadService;
    @Autowired
    HttpServletResponse response;

    @RequestMapping(value = "/thread/post", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String postThread(@RequestBody String sessionId, @RequestBody Thread thread) {
        JSONObject result = new JSONObject();
        User user = userService.getUserBySessionId(sessionId);
        if (user != null && sessionId != null) {
            thread.setPublisherId(user.getId());
            thread.setPostTime(String.valueOf(System.currentTimeMillis()));
            if (threadService.postThread(thread) > 0) {
                result.put("code", 200);
                result.put("msg", "success");
            } else {
                result.put("code", 403);
                result.put("error", "unknown");
            }
        } else {
            result.put("code", 403);
            result.put("error", "no_such_user");
        }
        response.setStatus(result.getInteger("code"));
        return result.toJSONString();
    }
}
