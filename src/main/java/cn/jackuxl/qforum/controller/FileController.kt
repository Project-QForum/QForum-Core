package cn.jackuxl.qforum.controller

import cn.dev33.satoken.stp.StpUtil
import cn.jackuxl.qforum.constants.StaticProperty
import cn.jackuxl.qforum.model.Result
import cn.jackuxl.qforum.model.ResultEntity
import cn.jackuxl.qforum.service.serviceimpl.FileServiceImpl
import cn.jackuxl.qforum.util.BasicUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.OutputStream
import java.time.LocalDate
import java.util.*
import javax.servlet.http.HttpServletResponse
import cn.jackuxl.qforum.entity.File as FileEntity


@CrossOrigin
@RestController
class FileController {

    @Autowired
    lateinit var fileService: FileServiceImpl

    @Autowired
    lateinit var response: HttpServletResponse

    @RequestMapping(value = ["/file/upload"], produces = ["application/json;charset=UTF-8"])
    fun uploadFile(file: MultipartFile): ResultEntity<Map<String, Int>> {
        BasicUtil.assertTool(StpUtil.isLogin(), "no_such_user")
        BasicUtil.assertTool(file.isImage() || file.isApk(), "suffix_not_allowed")

        val now = LocalDate.now()
        val type = if (file.isImage()) "image" else "apk"
        val fileName = "${System.currentTimeMillis()}_${UUID.randomUUID()}.${type}"
        val dirName = "upload/${type}s/${now.year}/${now.monthValue}/${now.dayOfMonth}"

        val rootPath = File(dirName)
        rootPath.mkdirs()

        val img = File("${rootPath.absolutePath}/${fileName}")
        img.createNewFile()
        file.transferTo(File(img.absolutePath))

        fileService.addFile(
            FileEntity(
                path = "${dirName}/${fileName}",
                publisherId = StpUtil.getLoginIdAsInt(),
                type = type
            )
        )

        val map =
            mapOf("id" to fileService.listFiles().last().id)
        return Result.ok(StaticProperty.SUCCESS, map)
    }

    @RequestMapping(value = ["/file/get"])
    fun getFile(id: Int) {
        BasicUtil.assertTool(fileService.getFileById(id) != null, "no_such_file")
        val file = File(fileService.getFileById(id)!!.path)
        response.contentType = if (file.isImage()) "image/png" else "application/vnd.android.package-archive"
        val out: OutputStream = response.outputStream
        out.write(file.readBytes())
        out.flush()
        out.close()
    }

    @RequestMapping(value = ["/file/get/type"])
    fun getFileType(id: Int): ResultEntity<Map<String, String?>> {
        BasicUtil.assertTool(fileService.getFileById(id) != null, "no_such_file")
        val file = fileService.getFileById(id)
        return Result.ok("success", mapOf("type" to file?.type))
    }

    val imageExtensions = listOf("jpg", "jpeg", "gif", "png", "bmp", "webp")

    fun MultipartFile.isImage(): Boolean {
        imageExtensions.forEach {
            if (getExtension() == it) {
                return true
            }
        }
        return false
    }

    fun MultipartFile.isApk(): Boolean {
        return getExtension() == "apk"
    }

    fun MultipartFile.getExtension(): String? {
        return originalFilename?.substringAfterLast(".", "")
    }

    fun File.isApk(): Boolean {
        return extension == "apk"
    }

    fun File.isImage(): Boolean {
        imageExtensions.forEach {
            if (extension == it) {
                return true
            }
        }
        return false
    }
}