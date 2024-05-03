package com.luka_kuzyk.bakalarskapraca

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// Singleton objekt reprezentujúci používateľa
object User {
    var uid: String = "" // Identifikátor používateľa
    var name: String = "" // Meno používateľa
    var email: String = "" // Email používateľa
    var progress: Float = 0.0f // Celkový pokrok používateľa (v percentách)
    var teoria_progress: MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0) // Pokrok v teoretických témach
    var teoria_lastPage: MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0) // Posledná prezeraná stránka v teoretických témach
    var testy: MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0) // Výsledky testov (v percentách)
    var priklady_lastPage: MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0) // Posledná prezeraná stránka v príkladoch
    var isLogged: Boolean = false // Indikátor prihlásenia používateľa

    // Metóda na odhlásenie používateľa (resetuje všetky údaje)
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

    // Metóda na nastavenie údajov používateľa
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

    // Metóda na výpočet celkového pokroku používateľa
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

    // Metóda na nahranie údajov používateľa do Firestore
    fun uploadUserToFireStore() {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val userDocumentRef = db.collection("users").document(uid)

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
                    println("Údaje používateľa úspešne uložené")
                }.addOnFailureListener { e ->
                    println("Chyba pri ukladaní údajov používateľa: $e")
                }
            }
        }.addOnFailureListener { e ->
            println("Chyba pri čítaní dokumentu používateľa: $e")
        }
    }

    // Metóda na aktualizáciu údajov používateľa v Firestore
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
                println("Údaje používateľa úspešne aktualizované")
            }
            .addOnFailureListener { e ->
                println("Chyba pri zápise údajov používateľa: $e")
            }

    }

    // Asynchrónna metóda na načítanie údajov používateľa z Firestore
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
            println("Chyba pri čítaní dokumentu: $e")
        }
    }

}
