package cn.jackuxl.qforum.controller

import cn.jackuxl.qforum.model.Thread
import cn.jackuxl.qforum.util.InfoUtil
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Autowired
import cn.jackuxl.qforum.serviceimpl.UserServiceImpl
import cn.jackuxl.qforum.serviceimpl.ThreadServiceImpl
import cn.jackuxl.qforum.serviceimpl.BoardServiceImpl
import javax.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.RequestMapping
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.springframework.lang.NonNull

@CrossOrigin
@RestController
class ThreadController {
    @Autowired
    lateinit var userService: UserServiceImpl

    @Autowired
    lateinit var threadService: ThreadServiceImpl

    @Autowired
    lateinit var boardService: BoardServiceImpl

    @Autowired
    lateinit var response: HttpServletResponse
    @RequestMapping(value = ["/thread/post"], produces = ["application/json;charset=UTF-8"])
    fun postThread(sessionId: String?, thread: Thread): String {
        val result = JSONObject()
        val user = userService.getUserBySessionId(sessionId)
        if (user != null && sessionId != null) {
            if (boardService.getBoardById(thread.getBoardId()) == null) {
                result["code"] = 403
                result["error"] = "no_such_board"
            } else if (thread.getTitle().isEmpty()) {
                result["code"] = 403
                result["error"] = "title_cannot_be_empty"
            } else {
                thread.publisherId = user.id
                thread.postTime = System.currentTimeMillis().toString()
                thread.likeList = "[]"
                if (threadService.postThread(thread) > 0) {
                    result["code"] = 200
                    result["msg"] = "success"
                    val threads = threadService.listThreads(thread.getBoardId())
                    result["id"] = threads[threads.size - 1].getId()
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

    @RequestMapping(value = ["/thread/getThreadDetail"], produces = ["application/json;charset=UTF-8"])
    fun getThreadDetail(id: Int): String {
        var result = JSONObject()
        val thread = threadService.getThreadById(id)
        if (thread != null) {
            result = JSON.parseObject(JSON.toJSONString(thread))
            result["code"] = 200
            InfoUtil.init(userService)
            result["publisher"] = InfoUtil.getPublicUserInfo(result.getInteger("publisherId"))
            result.remove("publisherId")
        } else {
            result["code"] = 403
            result["error"] = "no_such_thread"
        }
        response.status = result.getInteger("code")
        return result.toJSONString()
    }

    @RequestMapping(value = ["/thread/list"], produces = ["application/json;charset=UTF-8"])
    fun listThreads(boardId: Int): String {
        val result = JSONObject()
        if (boardService.getBoardById(boardId) != null) {
            result["code"] = 200
            result["msg"] = "success"
            val threads = threadService.listThreads(boardId)
            val tmp = JSON.parseArray(JSON.toJSONString(threads))
            for (i in tmp.indices) {
                InfoUtil.init(userService)
                tmp.getJSONObject(i)["publisher"] = InfoUtil.getPublicUserInfo(tmp.getJSONObject(i).getInteger("publisherId"))
                tmp.getJSONObject(i).remove("publisherId")
                result["likeList"].apply {
                    if(this==null){
                        result["likeList"] = "[]"
                    }
                }
            }
            result["threadList"] = tmp
            result["size"] = threads.size
        } else {
            result["code"] = 403
            result["error"] = "no_such_board"
        }
        response.status = result.getInteger("code")
        return result.toJSONString()
    }

    val LIKE = 0
    val DISLIKE = 1
    @RequestMapping(value = ["/thread/like"], produces = ["application/json;charset=UTF-8"])
    fun likeThread(@NonNull type: Int, @NonNull tid: Int, @NonNull sessionId: String?): String {
        val result = JSONObject()
        val user = userService.getUserBySessionId(sessionId)
        if (user != null) {
            result["code"] = 200
            result["msg"] = "success"
            when (type) {
                LIKE -> {
                    threadService.likeThread(tid, user.getId())
                    result["code"] = 200
                    result["msg"] = "success"
                }
                DISLIKE -> {
                    threadService.disLikeThread(tid, user.getId())
                    result["code"] = 200
                    result["msg"] = "success"
                }
                else -> {
                    result["code"] = 403
                    result["msg"] = "error_type"
                }
            }
        } else {
            result["code"] = 403
            result["error"] = "no_such_user"
        }
        response.status = result.getInteger("code")
        return result.toJSONString()
    }
}