package cn.jackuxl.qforum.service

import cn.jackuxl.qforum.entity.File
import org.springframework.stereotype.Component

@Component
interface FileService {
    fun addFile(file: File): Int
    fun getFileById(id: Int): File?
    fun listFiles(): List<File>
}