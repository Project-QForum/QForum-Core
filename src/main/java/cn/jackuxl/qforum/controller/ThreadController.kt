package cn.jackuxl.qforum.controller

import cn.dev33.satoken.stp.StpUtil
import cn.jackuxl.qforum.constants.StaticProperty
import cn.jackuxl.qforum.entity.Thread
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
@RequestMapping(value = ["/thread/"], produces = ["application/json;charset=UTF-8"])
class ThreadController {
    @Autowired
    lateinit var userService: UserServiceImpl

    @Autowired
    lateinit var threadService: ThreadServiceImpl

    @Autowired
    lateinit var boardService: BoardServiceImpl

    @RequestMapping(value = ["post"], produces = ["application/json;charset=UTF-8"])
    fun postThread(thread: Thread): ResultEntity<Thread> {
        BasicUtil.assertTool(StpUtil.isLogin() && StpUtil.getLoginId() != null, StaticProperty.NO_SUCH_USER)

        BasicUtil.assertTool(boardService.getBoardById(thread.boardId) != null, StaticProperty.NO_SUCH_THREAD)
        BasicUtil.assertTool(!thread.title.isNullOrBlank(), "title_cannot_be_empty")

        thread.publisherId = StpUtil.getLoginIdAsInt()
        thread.postTime = System.currentTimeMillis().toString()
        thread.likeList = "[]"

        BasicUtil.assertTool(threadService.postThread(thread) > 0, StaticProperty.UNKNOWN)

        val threads = threadService.listThreads(thread.boardId)
        return Result.ok(StaticProperty.SUCCESS, threads[threads.size - 1])
    }

    @RequestMapping(value = ["getThreadDetail"], produces = ["application/json;charset=UTF-8"])
    fun getThreadDetail(id: Int): ResultEntity<ThreadVo> {
        val thread = threadService.getThreadById(id)
        BasicUtil.assertTool(thread != null, StaticProperty.NO_SUCH_THREAD)

        val publisher = UserVo()
        BeanUtils.copyProperties(userService.getUserById(thread.publisherId), publisher)

        val data = ThreadVo(publisher = publisher, board = boardService.getBoardById(thread.boardId))
        BeanUtils.copyProperties(thread, data)

        if (data.likeList == "null") {
            data.likeList = "[]"
        }
        return Result.ok(StaticProperty.SUCCESS, data)
    }

    @RequestMapping(value = ["list"], produces = ["application/json;charset=UTF-8"])
    fun listThreads(boardId: Int?): ResultEntity<MutableList<ThreadVo>> {
        BasicUtil.assertTool(boardService.getBoardById(boardId ?: 0) != null, StaticProperty.NO_SUCH_BOARD)

        val threads = threadService.listThreads(boardId!!)
        val data = mutableListOf<ThreadVo>()
        for (i in threads.indices) {
            val publisher = UserVo()
            BeanUtils.copyProperties(userService.getUserById(threads[i].publisherId), publisher)

            val thread = ThreadVo(publisher = publisher, board = boardService.getBoardById(threads[i].boardId))
            BeanUtils.copyProperties(threads[i], thread)
            data.add(thread)
        }

        return Result.ok(StaticProperty.SUCCESS, data)
    }

    val LIKE = 0
    val DISLIKE = 1

    @RequestMapping(value = ["like"], produces = ["application/json;charset=UTF-8"])
    fun likeThread(@NonNull type: Int, @NonNull tid: Int): ResultEntity<String?> {
        BasicUtil.assertTool(StpUtil.isLogin() && StpUtil.getLoginId() != null, StaticProperty.NO_SUCH_USER)

        when (type) {
            LIKE -> {
                BasicUtil.assertTool(
                    threadService.likeThread(tid, StpUtil.getLoginIdAsInt()) > 0,
                    StaticProperty.UNKNOWN
                )
            }
            DISLIKE -> {
                BasicUtil.assertTool(
                    threadService.disLikeThread(tid, StpUtil.getLoginIdAsInt()) > 0,
                    StaticProperty.UNKNOWN
                )
            }
            else -> {
                throw CustomException(msg = StaticProperty.ERROR_TYPE)
            }
        }

        return Result.ok(StaticProperty.SUCCESS)
    }
}