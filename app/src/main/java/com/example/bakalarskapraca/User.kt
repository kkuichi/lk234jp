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
    var teoria_progress: MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)
    var teoria_lastPage: MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)
    var testy: MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)
    var isLogged: Boolean = false

    fun logOutUser(){
        uid = ""
        name= ""
        email= ""
        progress = -1
        teoria_progress = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)
        teoria_lastPage = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)
        testy = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)
        isLogged= false
    }
    fun setFields(uid:String, email:String, name:String, progress:Int, teoria_progress:MutableList<Int>,teoria_lastPage:MutableList<Int>, testy:MutableList<Int>, isLogged:Boolean){
        this.uid = uid
        this.email = email
        this.name = name
        this.progress = progress
        this.teoria_progress = teoria_progress
        this.teoria_lastPage = teoria_lastPage
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
                    "teoria_progress" to this.teoria_progress,
                    "teoria_lastPage" to this.teoria_lastPage,
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
    fun uploadUserTeoriaToFireStore() {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val userDocumentRef = db.collection("users").document(uid)

        val updatedUser = hashMapOf(
            "progress" to this.progress,
            "teoria_progress" to this.teoria_progress,
            "teoria_lastPage" to this.teoria_lastPage,
        )
        userDocumentRef.update(updatedUser)
            .addOnSuccessListener {
                println("User data successfully updated")
            }
            .addOnFailureListener { e ->
            println("Error writing user data: $e")
            }

    }
    suspend fun loadUserFromFireStore() = withContext(Dispatchers.IO) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        try {
            val document = db.collection("users").document(uid).get().await()
            if (document!=null) {
                val localUser = document.toObject(User::class.java)
                if (localUser != null) {
                    setFields(localUser.uid, localUser.email, localUser.name, localUser.progress, localUser.teoria_progress, localUser.teoria_lastPage, localUser.testy, true)
                }
            }
        } catch (e: Exception) {
            println("Error reading document $e")
        }
    }
}

