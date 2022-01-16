package cn.jackuxl.qforum.vo

import cn.jackuxl.qforum.entity.Tag

data class AppVo(
    var id: Int,
    var name: String,
    var postTime: String,
    var up: Boolean = false,
    var publisher: UserVo?,
    var downloadUrl: String?,
    var iconUrl: String?,
    var slogan: String?,
    var version: String?,
    var packageName: String?,
    var description: String?,
    var tag: Tag?
) {
    companion object {
        fun empty() = AppVo(0, "", "", false, null, null, null, null, null, null, null, null)
    }
}
