package cn.jackuxl.qforum.entity

data class User(
    var userName: String? = null,
    var password: String? = null,
    val id:Int = 0,
    val email: String? = null,
    var sessionId: String? = null,
    var salt: String? = null,
    var lastLoginIp: String? = null,
    var admin: Boolean? = false,
    var official: String? = null,
    var introduction:String? = "这个人很懒，什么都没留下。",
    var avatarUrl: String? = null,
)
