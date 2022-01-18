package cn.jackuxl.qforum.controller

import cn.dev33.satoken.stp.StpUtil
import cn.jackuxl.qforum.entity.Tag
import cn.jackuxl.qforum.model.Result
import cn.jackuxl.qforum.model.ResultEntity
import cn.jackuxl.qforum.service.serviceimpl.TagServiceImpl
import cn.jackuxl.qforum.util.BasicUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class TagController {
    @Autowired
    lateinit var tagService: TagServiceImpl

    @RequestMapping(value = ["/admin/addTag"], produces = ["application/json;charset=UTF-8"])
    fun addBoard(tag: Tag): ResultEntity<String?> {
        BasicUtil.assertTool(StpUtil.isLogin() && StpUtil.getLoginId() != null, "no_such_user")
        BasicUtil.assertTool(StpUtil.hasRole("admin"), "no_such_admin")
        BasicUtil.assertTool(tagService.addTag(tag) > 0, "unknown")

        return Result.ok("success")
    }

    @RequestMapping(value = ["/tag/list"], produces = ["application/json;charset=UTF-8"])
    fun listTags(): ResultEntity<List<Tag>> {
        return Result.ok("success", tagService.listTags())
    }
}