package cn.jackuxl.qforum.service

import cn.jackuxl.qforum.model.App
import cn.jackuxl.qforum.model.Board
import org.springframework.stereotype.Component

@Component
interface AppService {
    fun postApp(app: App): Int
    fun getAppById(id: Int): App
    fun listApps(): List<App>
}