package cn.jackuxl.qforum.mapper

import cn.jackuxl.qforum.entity.File
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Component
interface FileMapper {
    @Insert("insert into qf_file(id,path,type,publisherId) values(#{id},#{path},#{type},#{publisherId})")
    fun addFile(file: File): Int

    @Select("select * from qf_file where id=#{param1};")
    fun getFileById(id: Int): File?

    @Select("select * from qf_file order by id asc")
    fun listFiles(): List<File>
}