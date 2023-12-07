package com.example.ecommercei.data

data class User(
  val firstName:String,
  val lastName:String,
  val email:String,
  val imagePath:String? = null,

){
    constructor():this("","","","")
}
