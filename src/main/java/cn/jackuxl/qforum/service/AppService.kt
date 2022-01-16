package cn.jackuxl.qforum.service

import cn.jackuxl.qforum.entity.App
import org.springframework.stereotype.Component

@Component
interface AppService {
    fun postApp(app: App): Int
    fun getAppById(id: Int): App?
    fun getAppByPackageName(packageName: String): App?
    fun listApps(): List<App>
    fun getAppsByTag(tagId: Int): List<App>
}