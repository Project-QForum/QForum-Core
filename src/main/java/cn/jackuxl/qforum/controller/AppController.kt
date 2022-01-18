package cn.jackuxl.qforum.controller

import cn.dev33.satoken.stp.StpUtil
import cn.jackuxl.qforum.entity.App
import cn.jackuxl.qforum.model.Result
import cn.jackuxl.qforum.model.ResultEntity
import cn.jackuxl.qforum.service.serviceimpl.AppServiceImpl
import cn.jackuxl.qforum.service.serviceimpl.TagServiceImpl
import cn.jackuxl.qforum.service.serviceimpl.UserServiceImpl
import cn.jackuxl.qforum.util.BasicUtil
import cn.jackuxl.qforum.vo.AppVo
import cn.jackuxl.qforum.vo.UserVo
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class AppController {
    @Autowired
    lateinit var userService: UserServiceImpl
    @Autowired
    lateinit var appService: AppServiceImpl
    @Autowired
    lateinit var tagService: TagServiceImpl

    @RequestMapping(value = ["/app/post"], produces = ["application/json;charset=UTF-8"])
    fun postApp(app: App): ResultEntity<App> {
        BasicUtil.assertTool(StpUtil.isLogin() && StpUtil.getLoginId() != null, "no_such_user")
        app.postTime = System.currentTimeMillis().toString()
        app.publisherId = StpUtil.getLoginIdAsInt()

        // Check the data
        BasicUtil.assertTool(!app.name.isNullOrBlank(), "name_cannot_be_empty")
        BasicUtil.assertTool(app.packageName != null, "packageName_cannot_be_empty")
        BasicUtil.assertTool(tagService.getTagById(app.tagId) != null, "no_such_tag")
        BasicUtil.assertTool(
            appService.getAppByPackageName(app.packageName as String) == null,
            "packageName_already_exists"
        )
        BasicUtil.assertTool(appService.postApp(app) > 0, "unknown")

        // Operate and return the result
        val apps = appService.listApps().reversed()
        return Result.ok("success", apps[apps.size - 1])
    }

    @RequestMapping(value = ["/app/list"], produces = ["application/json;charset=UTF-8"])
    fun listApp(tagId:Int?): ResultEntity<MutableList<AppVo>> {
        val apps:List<App> = if(tagId==null){
            appService.listApps()
        } else{
            appService.getAppsByTag(tagId)
        }

        val data = mutableListOf<AppVo>()

        for (i in apps.indices) {
            val app = AppVo()
            BeanUtils.copyProperties(apps[i],app)

            val publisher = UserVo()
            BeanUtils.copyProperties(userService.getUserById(apps[i].publisherId?:0),publisher)

            app.publisher = publisher
            app.tag = tagService.getTagById(apps[i].tagId)

            data.add(app)
        }
        return Result.ok("success",data)
    }

    @RequestMapping(value = ["/app/getAppDetail"], produces = ["application/json;charset=UTF-8"])
    fun getThreadDetail(packageName: String): ResultEntity<AppVo> {

        val app = appService.getAppByPackageName(packageName)
        BasicUtil.assertTool(app!=null,"no_such_app")

        val appVo = AppVo()
        BeanUtils.copyProperties(app!!,appVo)
        val publisher = UserVo()
        BeanUtils.copyProperties(userService.getUserById(app.publisherId?:0),publisher)

        appVo.publisher = publisher
        appVo.tag = tagService.getTagById(app.tagId)

        return Result.ok("success",appVo)
    }
}