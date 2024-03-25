package com.example.bakalarskapraca

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


// Singelton
object User {
    var uid: String = ""
    var name: String = ""
    var email: String = ""
    var progress: Int = 0
    var teoria: List<Int> = listOf()
    var testy: List<Int> = listOf()
    var isLogged: Boolean = false

    override fun toString(): String {
        return "User(name='$name', email='$email', progress=$progress, uid='$uid', teoria=$teoria, testy=$testy, isLogged=$isLogged)"
    }
    fun logOutUser(){
        uid = ""
        name= ""
        email= ""
        progress = -1
        teoria = emptyList()
        testy = emptyList()
        isLogged= false
    }
    fun setFields(uid:String, email:String, name:String, progress:Int, teoria:List<Int>, testy:List<Int>, isLogged:Boolean){
        this.uid = uid
        this.email = email
        this.name = name
        this.progress = progress
        this.teoria = teoria
        this.testy = testy
        this.isLogged = isLogged
    }
    fun uploadUserToFireStore() {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val userDocumentRef = db.collection("users").document(User.uid)

        userDocumentRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                val newUser = hashMapOf(
                    "uid" to this.uid,
                    "email" to this.email,
                    "name" to this.name,
                    "progress" to this.progress,
                    "teoria" to this.teoria,
                    "testy" to this.testy,
                    "isLogged" to this.isLogged
                )
                userDocumentRef.set(newUser).addOnSuccessListener {
                    println("User data successfully written")
                }.addOnFailureListener { e ->
                    println("Error writing user data: $e")
                }
            }
        }.addOnFailureListener { e ->
            println("Error reading user document: $e")
        }
    }
    suspend fun loadUserFromFireStore() = withContext(Dispatchers.IO) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        try {
            val document = db.collection("users").document(uid).get().await()
            if (document!=null) {
                val localUser = document.toObject(User::class.java)
                if (localUser != null) {
                    setFields(localUser.uid, localUser.email, localUser.name, localUser.progress, localUser.teoria, localUser.testy, true)
                }
            }
        } catch (e: Exception) {
            println("Error reading document $e")
        }
    }

}

