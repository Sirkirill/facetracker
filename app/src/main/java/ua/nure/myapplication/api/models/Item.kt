package ua.nure.myapplication.api.models

class Item(
    val pk: Int,
    val name: String,
    val description: String,
    val units: Int,
    val start_date: String,
    val end_date: String,
    val last_change: String,
    val assigned_user: User,
    val creator: User,
    val backlog:Int,
    val list:ItemList
)

class ItemList(val pk:Int,val name:String)