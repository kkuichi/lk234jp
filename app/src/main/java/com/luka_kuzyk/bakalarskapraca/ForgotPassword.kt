package com.luka_kuzyk.bakalarskapraca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ForgotPassword : AppCompatActivity() {

    // Deklarácia premenných pre ovládacie prvky UI
    lateinit var fpEmail: TextInputEditText
    lateinit var fpBtn: Button
    private lateinit var auth: FirebaseAuth // Firebase authentification

    // Metóda onCreate sa volá pri vytváraní aktivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Pripojenie ovládacích prvkov z rozhrania k objektom v kóde
        fpBtn = findViewById(R.id.forgot_password_btn)
        fpEmail = findViewById(R.id.email)

        // Inicializácia Firebase autentifikácie
        auth = Firebase.auth

        // Nastavenie onClickListener pre tlačidlo fpBtn
        fpBtn.setOnClickListener {
            val email = fpEmail.text.toString();  // Získanie emailu z textového poľa

            // Kontrola, či bolo pole emailu vyplnené
            if(email.isEmpty()){
                Toast.makeText(applicationContext, "Zadajte email", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;  // Skorý návrat z listenera, ak nie je email zadaný
            }

            // Posielanie žiadosti o reset hesla
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    // Úspešné resetovanie hesla
                    Toast.makeText(applicationContext, "Heslo bolo úspešne resetované", Toast.LENGTH_SHORT).show();
                    finish()  // Ukončenie aktivity
                }.addOnFailureListener {
                    // Chyba pri resetovaní hesla
                    Toast.makeText(applicationContext, "Nastala chyba", Toast.LENGTH_SHORT).show();
                }
        }

    }
}