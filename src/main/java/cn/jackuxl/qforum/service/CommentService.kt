package cn.jackuxl.qforum.service

import cn.jackuxl.qforum.entity.Comment
import org.springframework.stereotype.Component

@Component
interface CommentService {
    fun postComment(comment: Comment): Int
    fun getCommentById(id: Int): Comment?
    fun upComment(id: Int, up: Int): Int
    fun listComments(threadId: Int): List<Comment>
}