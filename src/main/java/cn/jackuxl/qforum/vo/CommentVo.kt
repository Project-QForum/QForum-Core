package cn.jackuxl.qforum.vo

import cn.jackuxl.qforum.entity.Thread

data class CommentVo(
    var id: Int = 0,
    var postTime: String = "",
    var up: Boolean = false,
    var publisher: UserVo? = null,
    var content: String? = null,
    var thread: Thread? = null,
)