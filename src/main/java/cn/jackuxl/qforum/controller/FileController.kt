package cn.jackuxl.qforum.controller

import cn.jackuxl.qforum.model.Result
import cn.jackuxl.qforum.model.ResultEntity
import cn.jackuxl.qforum.service.serviceimpl.UserServiceImpl
import cn.jackuxl.qforum.util.BasicUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.OutputStream
import java.time.LocalDate
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@CrossOrigin
@RestController
class FileController {
    @Autowired
    lateinit var userService: UserServiceImpl
    @Autowired
    lateinit var response: HttpServletResponse
    @Autowired
    lateinit var request:HttpServletRequest
    @RequestMapping(value = ["/image/upload"], produces = ["application/json;charset=UTF-8"])
    fun uploadImage(@RequestParam(value = "imageFile", required = false) file:MultipartFile): ResultEntity<Map<String, String>> {
        BasicUtil.assertTool(file.isImage(),"suffix_not_allowed")
        val rootPath = File("upload/images/${LocalDate.now().year}/${LocalDate.now().monthValue}/${LocalDate.now().dayOfMonth}")
        rootPath.mkdirs()

        val img = File("${rootPath.absolutePath}/${System.currentTimeMillis()}_${UUID.randomUUID()}.png")
        img.createNewFile()
        file.transferTo(File(img.absolutePath))

        val map = mapOf("url" to "http://${request.serverName}:${request.serverPort}/upload/images/${LocalDate.now().year}/${LocalDate.now().monthValue}/${LocalDate.now().dayOfMonth}/${img.name}")
        return Result.ok("success",map)
    }
    @RequestMapping(value = ["/apk/upload"], produces = ["application/json;charset=UTF-8"])
    fun uploadAPK(file:MultipartFile): ResultEntity<Map<String, String>> {
        BasicUtil.assertTool(file.name.endsWith(".apk"),"suffix_not_allowed")
        val rootPath = File("upload/apks/${LocalDate.now().year}/${LocalDate.now().monthValue}/${LocalDate.now().dayOfMonth}")
        rootPath.mkdirs()

        val img = File("${rootPath.absolutePath}/${System.currentTimeMillis()}_${UUID.randomUUID()}.apk")
        img.createNewFile()
        file.transferTo(File(img.absolutePath))

        val map = mapOf("url" to "http://${request.serverName}:${request.serverPort}/upload/apks/${LocalDate.now().year}/${LocalDate.now().monthValue}/${LocalDate.now().dayOfMonth}/${img.name}")
        return Result.ok("success",map)
    }
    @RequestMapping(value = ["/upload/apks/{year}/{month}/{day}/{fileName}"],produces = ["application/vnd.android.package-archive"])
    fun getAPK(@PathVariable year:String,@PathVariable month:String,@PathVariable day:String,@PathVariable fileName:String){
        val file = File("upload/apks/${year}/${month}/${day}/${fileName}")
        response.contentType = "application/vnd.android.package-archive"
        val out: OutputStream = response.outputStream
        out.write(file.readBytes())
        out.flush()
        out.close()
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
        val suffixes = listOf(".jpg", ".jpeg", ".gif", ".png", ".bmp", ".webp")
        for(i in 0 until 6){
            if(name.endsWith(suffixes[i])){
                return true
            }
        }
        return false
    }
}