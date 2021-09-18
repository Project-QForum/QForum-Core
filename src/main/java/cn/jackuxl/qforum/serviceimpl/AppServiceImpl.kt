package cn.jackuxl.qforum.serviceimpl

import cn.jackuxl.qforum.mapper.AppMapper
import cn.jackuxl.qforum.model.App
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

    override fun getAppById(id: Int): App {
        return appMapper.getAppById(id)
    }

    override fun listApps(): List<App> {
        return appMapper.listApps()
    }
}