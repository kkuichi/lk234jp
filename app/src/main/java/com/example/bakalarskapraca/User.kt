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
    var teoria_progress: List<Int> = listOf()
    var teoria_lastPage: List<Int> = listOf()
    var testy: List<Int> = listOf()
    var isLogged: Boolean = false

    fun logOutUser(){
        uid = ""
        name= ""
        email= ""
        progress = -1
        teoria_progress = emptyList()
        teoria_lastPage = emptyList()
        testy = emptyList()
        isLogged= false
    }
    fun setFields(uid:String, email:String, name:String, progress:Int, teoria_progress:List<Int>,teoria_lastPage:List<Int>, testy:List<Int>, isLogged:Boolean){
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

