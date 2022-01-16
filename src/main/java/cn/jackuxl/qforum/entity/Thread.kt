package cn.jackuxl.qforum.entity

data class Thread(
    var id: Int = 0,
    var title: String? = null,
    var type: Int = 0,
    var publisherId: Int = 0,
    var postTime: String? = null,
    var content: String? = null,
    var boardId: Int = 0,
    var likeList: String = "[]"
) {

    constructor() : this(0, null, 0, 0, null, null, 0, "[]")


}