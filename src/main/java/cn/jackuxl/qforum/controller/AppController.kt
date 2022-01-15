package cn.jackuxl.qforum.controller

import cn.jackuxl.qforum.entity.App
import cn.jackuxl.qforum.model.ResultEntity
import cn.jackuxl.qforum.serviceimpl.AppServiceImpl
import cn.jackuxl.qforum.serviceimpl.TagServiceImpl
import cn.jackuxl.qforum.serviceimpl.UserServiceImpl
import cn.jackuxl.qforum.util.InfoUtil
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@CrossOrigin
@RestController
class AppController {
    @Autowired
    lateinit var userService: UserServiceImpl
    @Autowired
    lateinit var response: HttpServletResponse
    @Autowired
    lateinit var appService: AppServiceImpl
    @Autowired
    lateinit var tagService:TagServiceImpl
    @RequestMapping(value = ["/app/post"], produces = ["application/json;charset=UTF-8"])
    fun postApp(sessionId: String?, app: App): String {
        val result = JSONObject()
        val user = userService.getUserBySessionId(sessionId)
        if (user != null && sessionId != null) {
            app.postTime = System.currentTimeMillis().toString()
            app.publisherId = user.id
            if (app.name.isNullOrBlank()) {
                result["code"] = 403
                result["error"] = "name_cannot_be_empty"
            }
            else if(app.packageName==null) {
                result["code"] = 403
                result["error"] = "packageName_cannot_be_empty"
            } else if(tagService.getTagById(app.tagId)==null){
                result["code"] = 403
                result["error"] = "no_such_tag"
            } else if(appService.getAppByPackageName(app.packageName as String)!=null){
                result["code"] = 403
                result["error"] = "packageName_already_exists"
            } else if (appService.postApp(app) > 0) {
                result["code"] = 200
                result["msg"] = "success"
                val apps = appService.listApps().reversed()
                result["packageName"] = apps[apps.size - 1].packageName
            } else {
                result["code"] = 403
                result["error"] = "unknown"
            }
        } else {
            result["code"] = 403
            result["error"] = "no_such_user"
        }
        return result.toJSONString()
    }
    @RequestMapping(value = ["/app/test"], produces = ["application/json;charset=UTF-8"])
    fun test():ResultEntity<String>{
        System.out.println(ResultEntity.ok("success","success").toString())
        return ResultEntity.ok("success","success")
    }
    @RequestMapping(value = ["/app/list"], produces = ["application/json;charset=UTF-8"])
    fun listApp(tagId:Int?): String {
        val result = JSONObject()
        result["code"] = 200
        result["msg"] = "success"

        val apps:List<App> = if(tagId==null){
            appService.listApps()
        } else{
            appService.getAppsByTag(tagId)
        }

        val tmp = JSON.parseArray(JSON.toJSONString(apps))
        InfoUtil.init(userService)
        for (i in tmp.indices) {
            tmp.getJSONObject(i)["publisher"] = InfoUtil.getPublicUserInfo(tmp.getJSONObject(i).getInteger("publisherId"))
            tmp.getJSONObject(i).remove("publisherId")
            tmp.getJSONObject(i)["tag"] =  tagService.getTagById(tmp.getJSONObject(i).getInteger("tagId"))
            tmp.getJSONObject(i).remove("tagId")
        }
        result["appList"] = tmp
        result["size"] = apps.size
       
        return result.toJSONString()
    }

    @RequestMapping(value = ["/app/getAppDetail"], produces = ["application/json;charset=UTF-8"])
    fun getThreadDetail(packageName: String): String {
        var result = JSONObject()
        val app = appService.getAppByPackageName(packageName)
        if (app != null) {
            result = JSON.parseObject(JSON.toJSONString(app))
            result["code"] = 200
            InfoUtil.init(userService)
            result["publisher"] = InfoUtil.getPublicUserInfo(result.getInteger("publisherId"))
            result.remove("publisherId")
            result["tag"] = tagService.getTagById(result.getInteger("tagId"))
            result.remove("tagId")
        } else {
            result["code"] = 403
            result["error"] = "no_such_app"
        }
       
        return result.toJSONString()
    }
}