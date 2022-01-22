package cn.jackuxl.qforum.service.serviceimpl

import cn.jackuxl.qforum.entity.File
import cn.jackuxl.qforum.mapper.FileMapper
import cn.jackuxl.qforum.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FileServiceImpl : FileService {
    @Autowired
    lateinit var fileMapper: FileMapper

    override fun addFile(file: File): Int {
        return fileMapper.addFile(file)
    }

    override fun getFileById(id: Int): File? {
        return fileMapper.getFileById(id)
    }

    override fun listFiles(): List<File> {
        return fileMapper.listFiles()
    }
}