package cn.jackuxl.qforum.util

import cn.jackuxl.qforum.model.User
import cn.jackuxl.qforum.serviceimpl.UserServiceImpl
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject

object InfoUtil {
    var userService: UserServiceImpl? = null
    fun init(userService:UserServiceImpl){
        this.userService = userService
    }
    fun getPublicUserInfo(uid:Int):JSONObject{
        return JSON.parseObject(JSON.toJSONString(removeConfidentialInfo(userService?.getUserById(uid) as User)))
    }
    private fun removeConfidentialInfo(user: User): User {
        user.apply {
            password = null
            lastLoginIp = null
            salt = null
            sessionId = null
        }
        return user
    }
}