package cn.jackuxl.qforum.mapper

import cn.jackuxl.qforum.model.Board
import cn.jackuxl.qforum.model.Tag
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Component
interface TagMapper {
    @Insert("insert into qf_tag(id,name,type) values(#{id},#{name},#{type})")
    fun addTag(tag: Tag): Int

    @Select("select * from qf_tag where id=#{param1};")
    fun getTagById(id: Int): Tag?

    @Select("select * from qf_tag order by id desc")
    fun listTags(): List<Tag>
}