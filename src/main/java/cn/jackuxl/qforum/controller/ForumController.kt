package cn.jackuxl.qforum.controller

import cn.jackuxl.qforum.model.App
import cn.jackuxl.qforum.util.InfoUtil
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

@CrossOrigin
@RestController
class ForumController {
    @RequestMapping(value = ["/forum/info"], produces = ["application/json;charset=UTF-8"])
    fun getForumInfo(tagId:Int?): String {
        val info = JSON.parseObject(File("config.json").readText())
        return info.toJSONString()
    }
}