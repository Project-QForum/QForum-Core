package cn.jackuxl.qforum.controller

import cn.dev33.satoken.stp.StpUtil
import cn.jackuxl.qforum.constants.StaticProperty
import cn.jackuxl.qforum.constants.e.Code
import cn.jackuxl.qforum.entity.Comment
import cn.jackuxl.qforum.model.Result
import cn.jackuxl.qforum.model.ResultEntity
import cn.jackuxl.qforum.service.serviceimpl.BoardServiceImpl
import cn.jackuxl.qforum.service.serviceimpl.CommentServiceImpl
import cn.jackuxl.qforum.service.serviceimpl.ThreadServiceImpl
import cn.jackuxl.qforum.service.serviceimpl.UserServiceImpl
import cn.jackuxl.qforum.util.BasicUtil
import cn.jackuxl.qforum.vo.CommentVo
import cn.jackuxl.qforum.vo.ThreadVo
import cn.jackuxl.qforum.vo.UserVo
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping(value = ["/comment/"], produces = ["application/json;charset=UTF-8"])
class CommentController {
    @Autowired
    lateinit var userService: UserServiceImpl

    @Autowired
    lateinit var threadService: ThreadServiceImpl

    @Autowired
    lateinit var boardService: BoardServiceImpl

    @Autowired
    lateinit var commentService: CommentServiceImpl

    @RequestMapping(value = ["post"], produces = ["application/json;charset=UTF-8"])
    fun postComment(comment: Comment): ResultEntity<CommentVo> {
        BasicUtil.assertTool(StpUtil.isLogin(), StaticProperty.NO_SUCH_USER)
        BasicUtil.assertTool(
            threadService.getThreadById(comment.threadId as Int) != null,
            StaticProperty.NO_SUCH_THREAD
        )
        BasicUtil.assertTool(!comment.content.isNullOrBlank(), "content_cannot_be_empty")

        comment.publisherId = StpUtil.getLoginIdAsInt()
        comment.postTime = System.currentTimeMillis().toString()
        comment.up = false

        BasicUtil.assertTool(commentService.postComment(comment) > 0, StaticProperty.UNKNOWN)

        val comments = commentService.listComments(comment.threadId as Int)

        val publisher = UserVo()
        BeanUtils.copyProperties(userService.getUserById(comment.publisherId ?: 0), publisher)

        val threadPublisher = UserVo()
        BeanUtils.copyProperties(
            userService.getUserById(
                threadService.getThreadById(
                    comment.threadId ?: 0
                ).publisherId
            ), threadPublisher
        )

        val thread = ThreadVo(
            publisher = threadPublisher,
            board = boardService.getBoardById(threadService.getThreadById(comment.threadId ?: 0).boardId)
        )
        BeanUtils.copyProperties(threadService.getThreadById(comment.threadId ?: 0), thread)

        val data = CommentVo(publisher = publisher, thread = thread)
        BeanUtils.copyProperties(comments.last(), data)

        return Result.ok(StaticProperty.SUCCESS, data)
    }

    @RequestMapping(value = ["list"], produces = ["application/json;charset=UTF-8"])
    fun listComments(threadId: Int): ResultEntity<MutableList<CommentVo>> {
        BasicUtil.assertTool(threadService.getThreadById(threadId) != null, StaticProperty.NO_SUCH_THREAD)

        val comments = commentService.listComments(threadId)
        val data = mutableListOf<CommentVo>()
        for (i in comments.indices) {
            val publisher = UserVo()
            BeanUtils.copyProperties(userService.getUserById(comments[i].publisherId ?: 0), publisher)

            val threadPublisher = UserVo()
            BeanUtils.copyProperties(
                userService.getUserById(
                    threadService.getThreadById(
                        comments[i].threadId ?: 0
                    ).publisherId
                ), threadPublisher
            )

            val thread = ThreadVo(
                publisher = threadPublisher,
                board = boardService.getBoardById(threadService.getThreadById(comments[i].threadId ?: 0).boardId)
            )
            BeanUtils.copyProperties(threadService.getThreadById(comments[i].threadId ?: 0), thread)

            val comment =
                CommentVo(publisher = publisher, thread = thread)
            BeanUtils.copyProperties(comments[i], comment)

            data.add(comment)
        }
        return Result.ok(StaticProperty.SUCCESS, data)
    }

    @RequestMapping(value = ["up"], produces = ["application/json;charset=UTF-8"])
    fun upComment(commentId: Int, up: Int): ResultEntity<String?> {
        BasicUtil.assertTool(StpUtil.isLogin(), StaticProperty.NO_SUCH_USER)
        BasicUtil.assertTool(
            commentService.getCommentById(commentId) != null,
            StaticProperty.NO_SUCH_COMMENT
        )
        val comment = commentService.getCommentById(commentId)
        BasicUtil.assertTool(
            comment?.threadId?.let { threadService.getThreadById(it)?.publisherId } == StpUtil.getLoginIdAsInt() || StpUtil.hasRole(
                "admin"
            ),
            StaticProperty.PERMISSION_DENIED
        )
        when (up) {
            0 -> BasicUtil.assertTool(commentService.upComment(commentId, 0) > 0, StaticProperty.UNKNOWN)
            1 -> BasicUtil.assertTool(commentService.upComment(commentId, 1) > 0, StaticProperty.UNKNOWN)
            else -> return Result.failed(Code.ILLEGAL_PARAMETER, StaticProperty.ILLEGAL_PARAMETER)
        }
        return Result.ok(StaticProperty.SUCCESS)
    }


}