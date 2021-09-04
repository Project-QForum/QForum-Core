package cn.jackuxl.qforum.mapper

import cn.jackuxl.qforum.model.Comment
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Component
interface CommentMapper {
    @Insert("insert into qf_comment(postTime,up,publisherId,content,threadId) values(#{postTime},#{up},#{publisherId},#{content},#{threadId})")
    fun postComment(comment: Comment): Int

    @Select("select * from qf_comment where threadId=#{param1};")
    fun listComments(threadId: Int): List<Comment>
}