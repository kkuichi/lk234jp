package com.example.bakalarskapraca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ForgotPassword : AppCompatActivity() {

    lateinit var fpEmail: TextInputEditText
    lateinit var fpBtn: Button
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        fpBtn = findViewById(R.id.forgot_password_btn)
        fpEmail = findViewById(R.id.email)

        auth = Firebase.auth

        fpBtn.setOnClickListener {
            val email = fpEmail.text.toString()

            if(email.isEmpty()){
                Toast.makeText(applicationContext, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Password succesfully reseted", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

        }

    }
}