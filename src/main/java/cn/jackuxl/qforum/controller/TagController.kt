package cn.jackuxl.qforum.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Autowired
import cn.jackuxl.qforum.serviceimpl.UserServiceImpl
import javax.servlet.http.HttpServletResponse
import cn.jackuxl.qforum.serviceimpl.BoardServiceImpl
import org.springframework.web.bind.annotation.RequestMapping
import cn.jackuxl.qforum.model.Board
import cn.jackuxl.qforum.model.Tag
import cn.jackuxl.qforum.serviceimpl.TagServiceImpl
import com.alibaba.fastjson.JSONObject

@CrossOrigin
@RestController
class TagController {
    @Autowired
    lateinit var userService: UserServiceImpl

    @Autowired
    lateinit var response: HttpServletResponse

    @Autowired
    lateinit var tagService:TagServiceImpl
    @RequestMapping(value = ["/admin/addTag"], produces = ["application/json;charset=UTF-8"])
    fun addBoard(sessionId: String?, tag: Tag): String {
        val result = JSONObject()
        val user = userService.getUserBySessionId(sessionId)
        if (user != null && sessionId != null && user.isAdmin) {
            if (tagService.addTag(tag) > 0) {
                result["code"] = 200
                result["msg"] = "success"
            } else {
                result["code"] = 403
                result["error"] = "unknown"
            }
        } else {
            result["code"] = 403
            result["error"] = "no_such_user"
        }
        response.status = result.getInteger("code")
        return result.toJSONString()
    }

    @RequestMapping(value = ["/tag/list"], produces = ["application/json;charset=UTF-8"])
    fun listBoards(): String {
        val result = JSONObject()
        result["code"] = 200
        result["msg"] = "success"
        val tags = tagService.listTags()
        result["tagList"] = tags
        result["size"] = tags.size
        response.status = result.getInteger("code")
        return result.toJSONString()
    }
}