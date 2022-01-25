package cn.jackuxl.qforum.mapper

import cn.jackuxl.qforum.entity.App
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Component
interface AppMapper {
    @Insert("insert into qf_app(id,name,postTime,up,publisherId,downloadUrl,iconUrl,slogan,version,packageName,description,tagId) values(#{id},#{name},#{postTime},#{up},#{publisherId},#{downloadUrl},#{iconUrl},#{slogan},#{version},#{packageName},#{description},#{tagId})")
    fun postApp(app: App): Int

    @Select("select * from qf_app where id=#{param1};")
    fun getAppById(id: Int): App?

    @Select("select * from qf_app where tagId=#{param1};")
    fun getAppsByTag(tagId:Int): List<App>

    @Select("select * from qf_app where packageName=#{param1};")
    fun getAppByPackageName(packageName:String): App?

    @Select("select * from qf_app order by id asc")
    fun listApps(): List<App>
}