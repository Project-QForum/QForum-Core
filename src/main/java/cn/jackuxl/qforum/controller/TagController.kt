package cn.jackuxl.qforum.controller

import cn.jackuxl.qforum.constants.StaticProperty
import cn.jackuxl.qforum.entity.Tag
import cn.jackuxl.qforum.model.Result
import cn.jackuxl.qforum.model.ResultEntity
import cn.jackuxl.qforum.service.serviceimpl.TagServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping(value = ["/tag/"], produces = ["application/json;charset=UTF-8"])
class TagController {
    @Autowired
    lateinit var tagService: TagServiceImpl

    @RequestMapping(value = ["list"])
    fun listTags(): ResultEntity<List<Tag>> {
        return Result.ok(StaticProperty.SUCCESS, tagService.listTags())
    }
}