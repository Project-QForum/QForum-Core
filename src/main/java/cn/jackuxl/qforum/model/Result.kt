package cn.jackuxl.qforum.model


/**
 * @author JackuXL
 */
import cn.jackuxl.qforum.constants.e.Code
import kotlinx.serialization.Serializable

object Result {
    fun <T> ok(): ResultEntity<T?> {
        return restResult(null, Code.SUCCESS.code, null)
    }

    fun <T> ok(message: String, data: T): ResultEntity<T> {
        return restResult(data, Code.SUCCESS.code, message)
    }

    fun <T> ok(message: String): ResultEntity<T?> {
        return restResult(null, Code.SUCCESS.code, message)
    }

    fun <T> ok(data: T): ResultEntity<T> {
        return restResult(data, Code.SUCCESS.code, null)
    }

    fun <T> failed(message: String): ResultEntity<T?> {
        return restResult(null, Code.ERROR.code, message)
    }

    fun <T> failed(code: Code, message: String): ResultEntity<T?> {
        return restResult(null, code.code, message)
    }

    private fun <T> restResult(data: T, code: Int, message: String?): ResultEntity<T> {
        return ResultEntity(code, message, data)
    }
}

@Serializable
data class ResultEntity<T>(var code: Int = 0, var msg: String?, var data: T? = null)