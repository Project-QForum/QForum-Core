package cn.jackuxl.qforum.entity

data class Comment(var id:Int?,var postTime:String?,var up:Boolean = false,var publisherId:Int?,var content:String?,var threadId:Int?,var description:String?) {
    constructor() : this(null,null,false,null,null,null,null)
}