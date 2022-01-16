package cn.jackuxl.qforum.vo

data class UserVo(var id:Int,
                  var userName:String,
                  var email:String,
                  var admin:Boolean = false,
                  var official:String?,
                  var introduction:String = "这个人很懒，什么都没留下。",
                  var avatarUrl:String?){
    companion object{
        fun empty() = UserVo(0,"","",false,null,"这个人很懒，什么都没留下。",null)
    }
}
