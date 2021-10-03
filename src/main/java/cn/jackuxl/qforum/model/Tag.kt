package cn.jackuxl.qforum.model

data class Tag(var id:Int?, var name:String?, var type:Int = 0) {
    constructor() : this(null,null,0) {
    }
}