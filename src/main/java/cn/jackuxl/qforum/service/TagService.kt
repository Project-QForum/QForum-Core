package cn.jackuxl.qforum.service

import cn.jackuxl.qforum.model.Board
import cn.jackuxl.qforum.model.Tag
import org.springframework.stereotype.Component

@Component
interface TagService {
    fun addTag(tag: Tag): Int
    fun getTagById(id: Int): Tag?
    fun listTags(): List<Tag>
}