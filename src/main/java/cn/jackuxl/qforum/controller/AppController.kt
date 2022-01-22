package cn.jackuxl.qforum.controller

import cn.dev33.satoken.stp.StpUtil
import cn.jackuxl.qforum.constants.StaticProperty
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
@RequestMapping(value = ["/app/"], produces = ["application/json;charset=UTF-8"])
class AppController {
    @Autowired
    lateinit var userService: UserServiceImpl
    @Autowired
    lateinit var appService: AppServiceImpl
    @Autowired
    lateinit var tagService: TagServiceImpl

    @RequestMapping(value = ["post"], produces = ["application/json;charset=UTF-8"])
    fun postApp(app: App): ResultEntity<AppVo> {
        BasicUtil.assertTool(StpUtil.isLogin(), StaticProperty.NO_SUCH_USER)
        app.postTime = System.currentTimeMillis().toString()
        app.publisherId = StpUtil.getLoginIdAsInt()

        // Check the data
        BasicUtil.assertTool(!app.name.isNullOrBlank(), StaticProperty.NAME_CANNOT_BE_EMPTY)
        BasicUtil.assertTool(app.packageName != null, StaticProperty.PACKAGE_NAME_CANNOT_BE_EMPTY)
        BasicUtil.assertTool(tagService.getTagById(app.tagId) != null, StaticProperty.NO_SUCH_TAG)
        BasicUtil.assertTool(
            appService.getAppByPackageName(app.packageName as String) == null,
            StaticProperty.PACKAGE_NAME_ALREADY_EXISTS
        )

        // Operate and return the result
        BasicUtil.assertTool(appService.postApp(app) > 0, StaticProperty.UNKNOWN)
        val apps = appService.listApps().reversed()

        val publisher = UserVo()
        BeanUtils.copyProperties(userService.getUserById(app.publisherId ?: 0), publisher)

        val data = AppVo(publisher = publisher, tag = tagService.getTagById(app.tagId))
        BeanUtils.copyProperties(apps.last(), data)

        return Result.ok(StaticProperty.SUCCESS, data)
    }

    @RequestMapping(value = ["list"], produces = ["application/json;charset=UTF-8"])
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
        return Result.ok(StaticProperty.SUCCESS, data)
    }

    @RequestMapping(value = ["getAppDetail"], produces = ["application/json;charset=UTF-8"])
    fun getThreadDetail(packageName: String): ResultEntity<AppVo> {

        val app = appService.getAppByPackageName(packageName)
        BasicUtil.assertTool(app!=null,"no_such_app")

        val appVo = AppVo()
        BeanUtils.copyProperties(app!!,appVo)
        val publisher = UserVo()
        BeanUtils.copyProperties(userService.getUserById(app.publisherId?:0),publisher)

        appVo.publisher = publisher
        appVo.tag = tagService.getTagById(app.tagId)

        return Result.ok(StaticProperty.SUCCESS, appVo)
    }
}