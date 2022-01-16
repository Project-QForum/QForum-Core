package cn.jackuxl.qforum.util;


import cn.jackuxl.qforum.constants.e.Code;
import cn.jackuxl.qforum.exception.CustomException;

/**
 * @author dsr
 */
public class BasicUtil {

    /**
     * 断言工具
     *
     * @param valid   false 抛出异常
     * @param code    异常结果状态码
     * @param message message
     */
    public static void assertTool(boolean valid, Code code, String message) {
        if (!valid) {
            throw new CustomException(code,message);
        }
    }

    /**
     * 断言工具
     *
     * @param valid   false 抛出异常
     * @param message message
     */
    public static void assertTool(boolean valid, String message) {
        if (!valid) {
            throw new CustomException(Code.ILLEGAL_PARAMETER, message);
        }
    }
}
