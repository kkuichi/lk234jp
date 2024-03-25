package com.example.bakalarskapraca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar :ProgressBar
    private lateinit var progressInfo :TextView
    private lateinit var progressPercentage :TextView
    private lateinit var progressTotal :TextView

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if(User.email == ""){
            User.email = currentUser?.email.toString()
        }
        if (!User.isLogged) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settingsButton: ImageButton = findViewById(R.id.settings_btn)
        val teoriaButton: Button = findViewById(R.id.teoria_btn)
        val testyButton: Button = findViewById(R.id.testy_btn)
        val ulohyButton: Button = findViewById(R.id.ulohy_btn)
        progressBar = findViewById(R.id.progressBar)
        progressInfo = findViewById(R.id.progressInfo)
        progressPercentage = findViewById(R.id.progressPercentage)
        progressTotal = findViewById(R.id.progressTotal)


        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        teoriaButton.setOnClickListener {
            startActivity(Intent(this, TeoriaActivity::class.java))
        }
        testyButton.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }


        setProgress(User.progress, progressBar, progressPercentage)
        progressInfo.text = "Prebrali ste takmer polovicu"
        progressTotal.text = "6 z 10"

        progressInfo.text = User.email
        progressTotal.text = User.name
    }

    private fun setProgress(progress: Int, progressBar: ProgressBar, progressPercentage :TextView){
        progressBar.progress = progress;
        progressPercentage.text = "$progress%"
    }
}