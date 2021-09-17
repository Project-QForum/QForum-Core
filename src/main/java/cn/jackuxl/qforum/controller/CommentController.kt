package cn.jackuxl.qforum.controller

import cn.jackuxl.qforum.model.Comment
import cn.jackuxl.qforum.model.User
import cn.jackuxl.qforum.serviceimpl.CommentServiceImpl
import cn.jackuxl.qforum.serviceimpl.ThreadServiceImpl
import cn.jackuxl.qforum.serviceimpl.UserServiceImpl
import cn.jackuxl.qforum.util.InfoUtil
import cn.jackuxl.qforum.util.InfoUtil.init
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@CrossOrigin
@RestController
class CommentController {
    @Autowired
    lateinit var userService: UserServiceImpl
    @Autowired
    lateinit var threadService: ThreadServiceImpl
    @Autowired
    lateinit var commentService:CommentServiceImpl
    @Autowired
    lateinit var response: HttpServletResponse

    @RequestMapping(value = ["/comment/post"], produces = ["application/json;charset=UTF-8"])
    fun postComment(sessionId: String?, comment: Comment): String? {
        val result = JSONObject()
        val user: User? = userService.getUserBySessionId(sessionId)
        if (user!=null && sessionId != null) {
            if (threadService.getThreadById(comment.threadId as Int) == null) {
                result["code"] = 403
                result["error"] = "no_such_thread"
            } else if (comment.content?.isEmpty() == true) {
                result["code"] = 403
                result["error"] = "content_cannot_be_empty"
            } else {
                comment.publisherId = user.getId()
                comment.postTime = System.currentTimeMillis().toString()
                comment.up = false
                val log = commentService.postComment(comment)
                if (log > 0) {
                    result["code"] = 200
                    result["msg"] = "success"
                    val comments = commentService.listComments(comment.threadId as Int)
                    result["comment"] = comments[comments.size-1]
                } else {
                    result["code"] = 403
                    result["error"] = "unknown"
                }
            }
        } else {
            result["code"] = 403
            result["error"] = "no_such_user"
        }
        response.status = result.getInteger("code")
        return result.toJSONString()
    }

    @RequestMapping(value = ["/comment/list"], produces = ["application/json;charset=UTF-8"])
    fun listComments(threadId: Int): String? {
        val result = JSONObject()
        if (threadService.getThreadById(threadId) != null) {
            result["code"] = 200
            result["msg"] = "success"
            val comments = commentService.listComments(threadId)
            val tmp = JSON.parseArray(JSON.toJSONString(comments))
            init(userService)
            for (i in tmp.indices) {
                tmp.getJSONObject(i)["publisher"] = InfoUtil.getPublicUserInfo(tmp.getJSONObject(i).getInteger("publisherId"))
                tmp.getJSONObject(i).remove("publisherId")
            }
            result["commentList"] = tmp
            result["size"] = comments.size
        } else {
            result["code"] = 403
            result["error"] = "no_such_thread"
        }
        response.status = result.getInteger("code")
        return result.toJSONString()
    }

}