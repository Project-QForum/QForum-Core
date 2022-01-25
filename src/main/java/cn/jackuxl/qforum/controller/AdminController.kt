package cn.jackuxl.qforum.controller

import cn.dev33.satoken.stp.StpUtil
import cn.jackuxl.qforum.constants.StaticProperty
import cn.jackuxl.qforum.entity.Board
import cn.jackuxl.qforum.entity.Tag
import cn.jackuxl.qforum.model.Result
import cn.jackuxl.qforum.model.ResultEntity
import cn.jackuxl.qforum.service.serviceimpl.BoardServiceImpl
import cn.jackuxl.qforum.service.serviceimpl.TagServiceImpl
import cn.jackuxl.qforum.util.BasicUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping(value = ["/admin/"], produces = ["application/json;charset=UTF-8"])
class AdminController {
    @Autowired
    lateinit var boardService: BoardServiceImpl

    @Autowired
    lateinit var tagService: TagServiceImpl

    fun checkPermission() {
        BasicUtil.assertTool(StpUtil.isLogin(), StaticProperty.NO_SUCH_USER)
        BasicUtil.assertTool(StpUtil.hasRole("admin"), StaticProperty.PERMISSION_DENIED)
    }

    @RequestMapping(value = ["addBoard"])
    fun addBoard(board: Board?): ResultEntity<String?>? {
        checkPermission()
        BasicUtil.assertTool(boardService.addBoard(board) > 0, StaticProperty.UNKNOWN)
        return Result.ok(StaticProperty.SUCCESS)
    }

    @RequestMapping(value = ["addTag"], produces = ["application/json;charset=UTF-8"])
    fun addBoard(tag: Tag): ResultEntity<String?> {
        checkPermission()
        BasicUtil.assertTool(tagService.addTag(tag) > 0, StaticProperty.UNKNOWN)
        return Result.ok(StaticProperty.SUCCESS)
    }
}