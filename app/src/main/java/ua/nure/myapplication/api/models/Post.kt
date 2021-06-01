package ua.nure.myapplication.api.models

class FlagUpdate(
    var is_important: Boolean,
    var is_reacted: Boolean,
    var note: String
)

class Post(
    val pk: Int,
    val room: Room,
    val photo: Photo,
    val is_important: Boolean,
    val is_reacted: Boolean,
    val note: String
)

class ItemList(val pk:Int,val name:String)