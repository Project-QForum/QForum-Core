package cn.jackuxl.qforum.service.serviceimpl

import cn.jackuxl.qforum.entity.Comment
import cn.jackuxl.qforum.mapper.CommentMapper
import cn.jackuxl.qforum.service.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl : CommentService {
    @Autowired
    lateinit var commentMapper: CommentMapper

    override fun postComment(comment: Comment): Int {
        return commentMapper.postComment(comment)
    }

    override fun getCommentById(id: Int): Comment? {
        return commentMapper.getCommentById(id)
    }

    override fun upComment(id: Int, up: Int): Int {
        return commentMapper.upComment(id, up)
    }

    override fun listComments(threadId: Int): List<Comment> {
        return commentMapper.listComments(threadId)
    }
}