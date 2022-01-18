package cn.jackuxl.qforum.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("qf_app")
data class App(
    @TableId(type = IdType.AUTO)
    var id: Int?,
    var name: String?,
    var postTime: String?,
    var up: Boolean = false,
    var publisherId: Int?,
    var downloadUrl: String?,
    var iconUrl: String?,
    var slogan: String?,
    var version: String?,
    var packageName: String?,
    var description: String?,
    var tagId: Int = 0
)