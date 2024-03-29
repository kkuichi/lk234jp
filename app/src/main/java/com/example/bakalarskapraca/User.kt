package com.example.bakalarskapraca

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


// Singelton
object User {
    var uid: String = ""
    var name: String = ""
    var email: String = ""
    var progress: Float = 0.0f
    var teoria_progress: MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0) // Range 0-100 each = 11 topics
    var teoria_lastPage: MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)
    var testy: MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)           // Range 0-100 each = 11 tests
    var priklady_lastPage: MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)
    var isLogged: Boolean = false

    fun logOutUser(){
        uid = ""
        name= ""
        email= ""
        progress = -1f
        teoria_progress = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)
        teoria_lastPage = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)
        testy = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)
        priklady_lastPage = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)
        isLogged= false
    }
    fun setFields(uid:String, email:String, name:String, progress:Float,
                  teoria_progress:MutableList<Int>,teoria_lastPage:MutableList<Int>, testy:MutableList<Int>, priklady_lastPage:MutableList<Int>,
                  isLogged:Boolean){
        this.uid = uid
        this.email = email
        this.name = name
        this.progress = progress
        this.teoria_progress = teoria_progress
        this.teoria_lastPage = teoria_lastPage
        this.testy = testy
        this.priklady_lastPage = priklady_lastPage
        this.isLogged = isLogged
    }
    fun calculateProgress(){
        progress = 0f
        for (i in teoria_progress){
            val local = (i.toFloat()/100)* (100.0/22.0)
            progress += local.toFloat()
        }
        for (i in testy){
            val local = (i.toFloat()/100)* (100.0/22.0)
            progress += local.toFloat()
        }
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
                    "priklady_lastPage" to this.priklady_lastPage,
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
    fun uploadUserProgressToFireStore() {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val userDocumentRef = db.collection("users").document(uid)
        calculateProgress()

        val updatedUser = hashMapOf(
            "progress" to this.progress,
            "teoria_progress" to this.teoria_progress,
            "teoria_lastPage" to this.teoria_lastPage,
            "testy" to this.testy,
            "priklady_lastPage" to this.priklady_lastPage
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
                    setFields(localUser.uid, localUser.email, localUser.name, localUser.progress, localUser.teoria_progress, localUser.teoria_lastPage, localUser.testy, localUser.priklady_lastPage,true)
                }
            }
        } catch (e: Exception) {
            println("Error reading document $e")
        }
    }

}

