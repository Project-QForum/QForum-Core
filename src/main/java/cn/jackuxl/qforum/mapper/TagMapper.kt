package cn.jackuxl.qforum.mapper

import cn.jackuxl.qforum.entity.Tag
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Component
interface TagMapper {
    @Insert("insert into qf_tag(id,name,priorityLevel,type) values(#{id},#{name},#{priorityLevel},#{type})")
    fun addTag(tag: Tag): Int

    @Select("select * from qf_tag where id=#{param1};")
    fun getTagById(id: Int): Tag?

    @Select("select * from qf_tag order by id asc")
    fun listTags(): List<Tag>
}