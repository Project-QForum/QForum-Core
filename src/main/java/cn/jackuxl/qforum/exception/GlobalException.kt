package cn.jackuxl.qforum.exception

import cn.jackuxl.qforum.model.Result
import cn.jackuxl.qforum.model.ResultEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletResponse


/*
  全局异常处理类
*/
@ControllerAdvice
class GlobalException {
    @Autowired
    lateinit var response: HttpServletResponse

    /**
     * CustomException Handler
     * @param e 异常
     */
    @ResponseBody
    @ExceptionHandler(value = [CustomException::class])
    fun customExceptionHandler(e: CustomException): ResultEntity<String?> {
        response.status = e.code.code
        return Result.failed(e.code, e.msg)
    }
}