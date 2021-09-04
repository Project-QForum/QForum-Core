package cn.jackuxl.qforum.service

import cn.jackuxl.qforum.model.Comment
import org.springframework.stereotype.Component

@Component
interface CommentService {
    fun postComment(comment: Comment):Int
    fun listComments(threadId:Int):List<Comment>
}