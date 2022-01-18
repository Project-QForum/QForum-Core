package cn.jackuxl.qforum

import cn.dev33.satoken.stp.StpInterface
import cn.jackuxl.qforum.service.serviceimpl.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class StpInterfaceImpl : StpInterface {
    @Autowired
    lateinit var userService: UserServiceImpl

    override fun getRoleList(loginId: Any?, loginType: String?): MutableList<String> {
        return if (userService.getUserById(loginId.toString().toInt()).admin == true) {
            mutableListOf("admin")
        } else {
            mutableListOf()
        }
    }

    override fun getPermissionList(loginId: Any?, loginType: String?): MutableList<String> {
        return mutableListOf()
    }
}