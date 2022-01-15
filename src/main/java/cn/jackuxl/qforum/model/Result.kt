package cn.jackuxl.qforum.model

import cn.jackuxl.qforum.constants.e.Code
import java.io.Serializable

/**
 * @author JackuXL
 */
class Result<T> private constructor() : Serializable {
    private var code = 0
    private var message: String? = null
    private var data: T? = null

    companion object {
        fun <T> ok(): Result<T?> {
            return restResult(null, Code.SUCCESS.value(), null)
        }

        fun <T> ok(message: String?, data: T): Result<T> {
            return restResult(data, Code.SUCCESS.value(), message)
        }

        fun <T> ok(message: String?): Result<T?> {
            return restResult(null, Code.SUCCESS.value(), message)
        }

        fun <T> ok(data: T): Result<T> {
            return restResult(data, Code.SUCCESS.value(), null)
        }

        fun <T> failed(message: String?): Result<T?> {
            return restResult(null, Code.ERROR.value(), message)
        }

        fun <T> failed(code: Code, message: String?): Result<T?> {
            return restResult(null, code.value(), message)
        }

        private fun <T> restResult(data: T, code: Int, message: String?): Result<T> {
            val apiResult = Result<T>()
            apiResult.code = code
            apiResult.data = data
            apiResult.message = message
            return apiResult
        }
    }
}