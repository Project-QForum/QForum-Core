package cn.jackuxl.qforum.model

data class App(var id:Int?,var name:String?,var postTime:String?,var up:Boolean = false,var publisherId:Int?,var downloadUrl:String?,var iconUrl:String?,var slogan:String?,var version:String?,var packageName:String?,var description:String?) {
    constructor() : this(null,null,null,false,null,null,null,null,null,null,null) {
    }
}