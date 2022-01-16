package cn.jackuxl.qforum.controller;

import cn.jackuxl.qforum.entity.Board;
import cn.jackuxl.qforum.entity.User;
import cn.jackuxl.qforum.service.serviceimpl.BoardServiceImpl;
import cn.jackuxl.qforum.service.serviceimpl.UserServiceImpl;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
public class BoardController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private BoardServiceImpl boardService;

    @RequestMapping(value = "/admin/addBoard", produces = "application/json;charset=UTF-8")
    public String addBoard(String sessionId, Board board) {
        JSONObject result = new JSONObject();
        User user = userService.getUserBySessionId(sessionId);
        if (user != null && sessionId != null && user.isAdmin()) {
            if (boardService.addBoard(board) > 0) {
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
        
        return result.toJSONString();
    }

    @RequestMapping(value = "/board/list", produces = "application/json;charset=UTF-8")
    public String listBoards() {
        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("msg", "success");
        List<Board> boards = boardService.listBoards();
        result.put("boardList",boards);
        result.put("size",boards.size());
        
        return result.toJSONString();
    }
}
