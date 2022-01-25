package cn.jackuxl.qforum.mapper

import cn.jackuxl.qforum.entity.Comment
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.springframework.stereotype.Component

@Component
interface CommentMapper {
    @Insert("insert into qf_comment(postTime,up,publisherId,content,threadId) values(#{postTime},#{up},#{publisherId},#{content},#{threadId})")
    fun postComment(comment: Comment): Int

    @Select("select * from qf_comment where id=#{param1};")
    fun getCommentById(id: Int): Comment?

    @Update("update qf_comment set up=#{param2} where id=#{param1};")
    fun upComment(id: Int, up: Int): Int

    @Select("select * from qf_comment where threadId=#{param1};")
    fun listComments(threadId: Int): List<Comment>
}