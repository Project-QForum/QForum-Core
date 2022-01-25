package cn.jackuxl.qforum.entity

data class Board(
    var id: Int = 0,
    var name: String = "",
    var priorityLevel: Int = 10,
    var description: String? = null,
    var icon: String? = null
)
