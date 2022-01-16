package cn.jackuxl.qforum.constants.e

enum class Code(var code:Int) {
    ERROR(500),
    ILLEGAL_PARAMETER(400),
    NOT_LOGIN(403),
    SUCCESS(200)
}