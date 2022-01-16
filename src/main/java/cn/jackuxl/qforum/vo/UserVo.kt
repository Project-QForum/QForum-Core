package cn.jackuxl.qforum.vo

data class UserVo(
    var id: Int = 0,
    var userName: String = "",
    var email: String = "",
    var admin: Boolean = false,
    var official: String? = null,
    var introduction: String = "这个人很懒，什么都没留下。",
    var avatarUrl: String? = null
)