package cn.jackuxl.qforum.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.jackuxl.qforum.entity.Board;
import cn.jackuxl.qforum.model.Result;
import cn.jackuxl.qforum.model.ResultEntity;
import cn.jackuxl.qforum.service.serviceimpl.BoardServiceImpl;
import cn.jackuxl.qforum.util.BasicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class BoardController {
    @Autowired
    private BoardServiceImpl boardService;

    @RequestMapping(value = "/admin/addBoard", produces = "application/json;charset=UTF-8")
    public ResultEntity<String> addBoard(Board board) {
        BasicUtil.assertTool(StpUtil.isLogin() && StpUtil.getLoginId() != null, "no_such_user");
        BasicUtil.assertTool(StpUtil.hasRole("admin"), "no_such_admin");
        BasicUtil.assertTool(boardService.addBoard(board) > 0, "unknown");
        return Result.INSTANCE.ok("success");
    }

    @RequestMapping(value = "/board/list", produces = "application/json;charset=UTF-8")
    public ResultEntity<List<Board>> listBoards() {
        return Result.INSTANCE.ok("success",boardService.listBoards());
    }
}
