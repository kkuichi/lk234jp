package com.example.bakalarskapraca


// Singelton
object User {
    var name: String = ""
    var email: String = ""
    var progress: Int = 0
    var uid: String = ""
    var teoria: List<Int> = listOf()
    var testy: List<Int> = listOf()
    var isLogged: Boolean = false
    override fun toString(): String {
        return "User(name='$name', email='$email', progress=$progress, uid='$uid', teoria=$teoria, testy=$testy, isLogged=$isLogged)"
    }

    fun loadUser(){

    }

    fun logOutUser(){
        name= ""
        email= ""
        progress = -1
        uid = ""
        teoria = emptyList()
        testy = emptyList()
        isLogged= false
    }

}