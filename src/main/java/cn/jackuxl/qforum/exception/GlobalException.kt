package cn.jackuxl.qforum.exception

import cn.jackuxl.qforum.model.Result
import cn.jackuxl.qforum.model.ResultEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView


/*
  全局异常处理类
*/
@ControllerAdvice
class GlobalException{
    /**
     * CustomException Handler
     * @param e 异常
     */
    @ResponseBody
    @ExceptionHandler(value = [CustomException::class])
    fun customExceptionHandler(e: CustomException): ResultEntity<String?> {
        return Result.failed(e.code,e.msg)
    }
}