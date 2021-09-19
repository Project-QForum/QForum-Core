package cn.jackuxl.qforum.controller

import cn.jackuxl.qforum.serviceimpl.UserServiceImpl
import com.alibaba.fastjson.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.time.LocalDate
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@CrossOrigin
@RestController
class ImageController {
    @Autowired
    lateinit var userService: UserServiceImpl
    @Autowired
    lateinit var response: HttpServletResponse
    @Autowired
    lateinit var request:HttpServletRequest

    @RequestMapping(value = ["/image/upload"], produces = ["application/json;charset=UTF-8"])
    fun uploadImage(@RequestParam(value = "editormd-image-file", required = false) file:MultipartFile): String? {
        val result = JSONObject()
        try{
            if(file.isImage()){
                result["code"] = 403
                result["error"] = "suffix_not_allowed"
                response.status = 403
            }
            else {
                val rootPath = File("upload/images/${LocalDate.now().year}/${LocalDate.now().monthValue}/${LocalDate.now().dayOfMonth}")
                rootPath.mkdirs()

                val img = File("${rootPath.absolutePath}/${System.currentTimeMillis()}_${UUID.randomUUID()}.png")
                img.createNewFile()
                file.transferTo(File(img.absolutePath))

                result["success"] = 1
                result["message"] = "上传成功"
                result["url"] = "http://${request.serverName}:${request.serverPort}/upload/images/${LocalDate.now().year}/${LocalDate.now().monthValue}/${LocalDate.now().dayOfMonth}/${img.name}"
            }
        }
        catch (e:Exception){
            result["success"] = 0
            result["error"] = e.message
            e.printStackTrace()
        }

        return result.toJSONString()
    }
    @RequestMapping(value = ["/upload/images/{year}/{month}/{day}/{fileName}"],produces = ["image/png"])
    fun getImage(@PathVariable year:String,@PathVariable month:String,@PathVariable day:String,@PathVariable fileName:String){
        val file = File("upload/images/${year}/${month}/${day}/${fileName}")
        response.contentType = "image/png"
        val out: OutputStream = response.outputStream
        out.write(file.readBytes())
        out.flush()
        out.close()
    }
    fun MultipartFile.isImage():Boolean{
        val suffixs = listOf(".jpg", ".jpeg", ".gif", ".png", ".bmp", ".webp");
        for(i in 0 until 6){
            if(name.endsWith(suffixs[i])){
                return true
            }
        }
        return false
    }
}