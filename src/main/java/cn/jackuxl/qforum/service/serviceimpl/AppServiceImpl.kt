package cn.jackuxl.qforum.service.serviceimpl

import cn.jackuxl.qforum.entity.App
import cn.jackuxl.qforum.mapper.AppMapper
import cn.jackuxl.qforum.service.AppService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AppServiceImpl : AppService {
    @Autowired
    lateinit var appMapper: AppMapper
    override fun postApp(app: App): Int {
        return appMapper.postApp(app)
    }

    override fun getAppById(id: Int): App? {
        return appMapper.getAppById(id)
    }

    override fun getAppByPackageName(packageName: String): App? {
        return appMapper.getAppByPackageName(packageName)
    }

    override fun getAppsByTag(tagId: Int): List<App> {
        return appMapper.getAppsByTag(tagId)
    }

    override fun listApps(): List<App> {
        return appMapper.listApps()
    }
}