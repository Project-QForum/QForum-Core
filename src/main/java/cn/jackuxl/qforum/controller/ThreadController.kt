package cn.jackuxl.qforum.controller

import cn.jackuxl.qforum.entity.Thread
import cn.jackuxl.qforum.entity.Board
import cn.jackuxl.qforum.exception.CustomException
import cn.jackuxl.qforum.model.Result
import cn.jackuxl.qforum.model.ResultEntity
import cn.jackuxl.qforum.service.serviceimpl.BoardServiceImpl
import cn.jackuxl.qforum.service.serviceimpl.ThreadServiceImpl
import cn.jackuxl.qforum.service.serviceimpl.UserServiceImpl
import cn.jackuxl.qforum.util.BasicUtil
import cn.jackuxl.qforum.vo.ThreadVo
import cn.jackuxl.qforum.vo.UserVo
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.lang.NonNull
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class ThreadController {
    @Autowired
    lateinit var userService: UserServiceImpl

    @Autowired
    lateinit var threadService: ThreadServiceImpl

    @Autowired
    lateinit var boardService: BoardServiceImpl

    @RequestMapping(value = ["/thread/post"], produces = ["application/json;charset=UTF-8"])
    fun postThread(sessionId: String?, thread: Thread): ResultEntity<Thread> {
        val user = userService.getUserBySessionId(sessionId)
        BasicUtil.assertTool(user != null && sessionId != null, "no_such_user")
        BasicUtil.assertTool(boardService.getBoardById(thread.boardId) != null, "no_such_board")
        BasicUtil.assertTool(!thread.title.isNullOrBlank(), "title_cannot_be_empty")

        thread.publisherId = user.id
        thread.postTime = System.currentTimeMillis().toString()
        thread.likeList = "[]"

        BasicUtil.assertTool(threadService.postThread(thread) > 0, "unknown")

        val threads = threadService.listThreads(thread.boardId)
        return Result.ok("success", threads[threads.size - 1])
    }

    @RequestMapping(value = ["/thread/getThreadDetail"], produces = ["application/json;charset=UTF-8"])
    fun getThreadDetail(id: Int): ResultEntity<ThreadVo> {
        val thread = threadService.getThreadById(id)
        BasicUtil.assertTool(thread != null, "no_such_thread")

        val publisher = UserVo()
        BeanUtils.copyProperties(userService.getUserById(thread.publisherId), publisher)

        val data = ThreadVo(publisher = publisher, board = boardService.getBoardById(thread.boardId))
        BeanUtils.copyProperties(thread, data);

        if (data.likeList == "null") {
            data.likeList = "[]"
        }
        return Result.ok("success", data)
    }

    @RequestMapping(value = ["/thread/list"], produces = ["application/json;charset=UTF-8"])
    fun listThreads(boardId: Int?): ResultEntity<MutableList<ThreadVo>> {
        BasicUtil.assertTool(boardService.getBoardById(boardId ?: 0) != null, "no_such_board")

        val threads = threadService.listThreads(boardId!!)
        val data = mutableListOf<ThreadVo>()
        for (i in threads.indices) {
            val publisher = UserVo()
            BeanUtils.copyProperties(userService.getUserById(threads[i].publisherId), publisher)

            val thread = ThreadVo(publisher = publisher, board = boardService.getBoardById(threads[i].boardId))
            BeanUtils.copyProperties(threads[i], thread)
            data.add(thread)
        }

        return Result.ok("success", data)
    }

    val LIKE = 0
    val DISLIKE = 1

    @RequestMapping(value = ["/thread/like"], produces = ["application/json;charset=UTF-8"])
    fun likeThread(@NonNull type: Int, @NonNull tid: Int, @NonNull sessionId: String?): ResultEntity<String?> {
        val user = userService.getUserBySessionId(sessionId)

        BasicUtil.assertTool(user != null, "no_such_user")
        when (type) {
            LIKE -> {
                BasicUtil.assertTool(threadService.likeThread(tid, user.id) > 0, "unknown")
            }
            DISLIKE -> {
                BasicUtil.assertTool(threadService.disLikeThread(tid, user.id) > 0, "unknown")
            }
            else -> {
                throw CustomException(msg = "error_type")
            }
        }

        return Result.ok("success")
    }
}