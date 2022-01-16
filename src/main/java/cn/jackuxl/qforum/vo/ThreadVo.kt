package cn.jackuxl.qforum.vo

import cn.jackuxl.qforum.entity.Board

data class ThreadVo(
    var id: Int = 0,
    var title: String? = null,
    var type: Int = 0,
    var publisher: UserVo? = null,
    var postTime: String? = null,
    var content: String? = null,
    var board: Board? = null,
    var likeList: String = "[]"
)