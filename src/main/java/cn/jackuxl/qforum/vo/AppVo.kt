package cn.jackuxl.qforum.vo

import cn.jackuxl.qforum.entity.Tag

data class AppVo(
    var id: Int = 0,
    var name: String = "",
    var postTime: String = "",
    var up: Boolean = false,
    var publisher: UserVo? = null,
    var downloadUrl: String? = null,
    var iconUrl: String? = null,
    var slogan: String? = null,
    var version: String? = null,
    var packageName: String? = null,
    var description: String? = null,
    var tag: Tag? = null
)
