package cn.jackuxl.qforum.service.serviceimpl

import cn.jackuxl.qforum.service.TagService
import org.springframework.beans.factory.annotation.Autowired
import cn.jackuxl.qforum.mapper.TagMapper
import cn.jackuxl.qforum.entity.Tag
import org.springframework.stereotype.Service

@Service
class TagServiceImpl : TagService {
    @Autowired
    lateinit var tagMapper: TagMapper

    override fun addTag(tag: Tag): Int {
        return tagMapper.addTag(tag)
    }

    override fun getTagById(id: Int): Tag? {
        return tagMapper.getTagById(id)
    }

    override fun listTags(): List<Tag> {
        return tagMapper.listTags()
    }
}