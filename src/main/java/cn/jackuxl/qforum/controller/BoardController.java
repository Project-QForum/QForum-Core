package cn.jackuxl.qforum.controller;

import cn.jackuxl.qforum.constants.StaticProperty;
import cn.jackuxl.qforum.entity.Board;
import cn.jackuxl.qforum.model.Result;
import cn.jackuxl.qforum.model.ResultEntity;
import cn.jackuxl.qforum.service.serviceimpl.BoardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/board/", produces = "application/json;charset=UTF-8")
public class BoardController {
    @Autowired
    private BoardServiceImpl boardService;

    @RequestMapping(value = "list")
    public ResultEntity<List<Board>> listBoards() {
        return Result.INSTANCE.ok(StaticProperty.SUCCESS, boardService.listBoards());
    }
}
