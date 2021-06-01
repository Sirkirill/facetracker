package ua.nure.myapplication.api.models

class Room(
    val pk: Int,
    val company: Int,
    val name: String,
    val info: String,
    val is_whitelisted: Boolean
)
