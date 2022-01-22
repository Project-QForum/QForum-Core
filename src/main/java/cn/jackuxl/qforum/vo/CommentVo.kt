package cn.jackuxl.qforum.vo

data class CommentVo(
    var id: Int = 0,
    var postTime: String = "",
    var up: Boolean = false,
    var publisher: UserVo? = null,
    var content: String? = null,
    var thread: ThreadVo? = null,
)