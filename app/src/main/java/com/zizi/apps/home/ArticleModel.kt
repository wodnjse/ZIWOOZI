package com.zizi.apps.home

data class ArticleModel(
    val Id:String,
    val title: String,
    val createdAt: Long,
    val passage: String,
    val imageUrl:String

)  {
    constructor(): this("","",0,"","")
}