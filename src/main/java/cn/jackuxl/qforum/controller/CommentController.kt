package cn.jackuxl.qforum.controller

import cn.dev33.satoken.stp.StpUtil
import cn.jackuxl.qforum.constants.StaticProperty
import cn.jackuxl.qforum.entity.Comment
import cn.jackuxl.qforum.model.Result
import cn.jackuxl.qforum.model.ResultEntity
import cn.jackuxl.qforum.service.serviceimpl.CommentServiceImpl
import cn.jackuxl.qforum.service.serviceimpl.ThreadServiceImpl
import cn.jackuxl.qforum.service.serviceimpl.UserServiceImpl
import cn.jackuxl.qforum.util.BasicUtil
import cn.jackuxl.qforum.vo.CommentVo
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
    lateinit var commentService: CommentServiceImpl

    @RequestMapping(value = ["post"], produces = ["application/json;charset=UTF-8"])
    fun postComment(comment: Comment): ResultEntity<Comment> {
        BasicUtil.assertTool(StpUtil.isLogin(), StaticProperty.NO_SUCH_USER)
        BasicUtil.assertTool(
            threadService.getThreadById(comment.threadId as Int) != null,
            StaticProperty.NO_SUCH_THREAD
        )
        BasicUtil.assertTool(comment.content?.isEmpty() == false, "content_cannot_be_empty")

        comment.publisherId = StpUtil.getLoginIdAsInt()
        comment.postTime = System.currentTimeMillis().toString()
        comment.up = false

        BasicUtil.assertTool(commentService.postComment(comment) > 0, StaticProperty.UNKNOWN)

        val comments = commentService.listComments(comment.threadId as Int)

        return Result.ok(StaticProperty.SUCCESS, comments[comments.size - 1])
    }

    @RequestMapping(value = ["list"], produces = ["application/json;charset=UTF-8"])
    fun listComments(threadId: Int): ResultEntity<MutableList<CommentVo>> {
        BasicUtil.assertTool(threadService.getThreadById(threadId) != null, StaticProperty.NO_SUCH_THREAD)

        val comments = commentService.listComments(threadId)
        val data = mutableListOf<CommentVo>()
        for (i in comments.indices) {
            val publisher = UserVo()
            BeanUtils.copyProperties(userService.getUserById(comments[i].publisherId ?: 0), publisher)

            val comment =
                CommentVo(publisher = publisher, thread = threadService.getThreadById(comments[i].threadId ?: 0))
            BeanUtils.copyProperties(comments[i], comment)

            data.add(comment)
        }
        return Result.ok(StaticProperty.SUCCESS, data)
    }

}