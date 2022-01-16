package cn.jackuxl.qforum.vo
import cn.jackuxl.qforum.entity.Thread
data class CommentVo(
    var id: Int,
    var postTime: String,
    var up: Boolean = false,
    var publisher: UserVo?,
    var content: String?,
    var thread: Thread?,
) {
    companion object {
        fun empty() = CommentVo(0, "", false, null, null, null)
    }
}