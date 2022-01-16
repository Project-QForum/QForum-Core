package cn.jackuxl.qforum.exception

import cn.jackuxl.qforum.constants.e.Code

data class CustomException(var code:Code = Code.ILLEGAL_PARAMETER, var msg:String) : RuntimeException(msg)