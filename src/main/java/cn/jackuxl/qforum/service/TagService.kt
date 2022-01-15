package cn.jackuxl.qforum.service

import cn.jackuxl.qforum.entity.Tag
import org.springframework.stereotype.Component

@Component
interface TagService {
    fun addTag(tag: Tag): Int
    fun getTagById(id: Int): Tag?
    fun listTags(): List<Tag>
}