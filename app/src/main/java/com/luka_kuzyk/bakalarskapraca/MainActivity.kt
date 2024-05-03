package com.luka_kuzyk.bakalarskapraca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    // Deklarácia premenných pre zobrazenie pokroku
    private lateinit var progressBar :ProgressBar
    private lateinit var progressInfo :TextView
    private lateinit var progressPercentage :TextView
    private lateinit var progressTotal :TextView

    // Premenná pre autentifikáciu
    private lateinit var auth: FirebaseAuth

    // Metóda onStart pre kontrolu prihlásenia pri spustení aktivity
    public override fun onStart() {
        super.onStart()
        // Inicializácia autentifikačnej inštancie
        auth = Firebase.auth
        // Získanie aktuálneho používateľa
        val currentUser = auth.currentUser
        // Ak nie je používateľ prihlásený, presmerujeme na prihlásenie
        if(User.email == ""){
            User.email = currentUser?.email.toString()
        }
        if (!User.isLogged || currentUser == null) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    // Metóda onResume pre aktualizáciu zobrazenia po návrate do aktivity
    override fun onResume() {
        super.onResume()
        // Výpočet pokroku používateľa
        User.calculateProgress()
        // Inicializácia prvkov používateľského rozhrania pre zobrazenie pokroku
        progressBar = findViewById(R.id.progressBar)
        progressInfo = findViewById(R.id.progressInfo)
        // Nastavenie zobrazenia pokroku
        setProgress(User.progress, progressBar, progressPercentage)
    }

    // Metóda onCreate pre inicializáciu aktivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializácia tlačidiel pre navigáciu
        val settingsButton: ImageButton = findViewById(R.id.settings_btn)
        val ratingButton: ImageButton = findViewById(R.id.rating_btn)
        val teoriaButton: Button = findViewById(R.id.teoria_btn)
        val testyButton: Button = findViewById(R.id.testy_btn)
        val ulohyButton: Button = findViewById(R.id.ulohy_btn)
        // Inicializácia prvkov používateľského rozhrania pre zobrazenie pokroku
        progressBar = findViewById(R.id.progressBar)
        progressInfo = findViewById(R.id.progressInfo)
        progressPercentage = findViewById(R.id.progressPercentage)
        progressTotal = findViewById(R.id.progressTotal)

        // Nastavenie onClickListenerov pre tlačidlá
        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        ratingButton.setOnClickListener {
            startActivity(Intent(this, RatingActivity::class.java))
        }
        teoriaButton.setOnClickListener {
            startActivity(Intent(this, TeoriaActivity::class.java))
        }
        testyButton.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }
        ulohyButton.setOnClickListener {
            startActivity(Intent(this, PrikladyActivity::class.java))
        }

        // Výpočet a nastavenie zobrazenia pokroku používateľa
        User.calculateProgress()
        setProgress(User.progress, progressBar, progressPercentage)
        progressInfo.text = User.name
        progressTotal.text = User.email
        // Ak nie je k dispozícii internetové pripojenie, zobrazíme správu
        if(User.name == ""){
            progressInfo.text = getString(R.string.no_internet_connection)
            progressTotal.text = getString(R.string.no_internet_connection2)
        }
    }

    // Metóda na nastavenie zobrazenia pokroku
    fun setProgress(progress: Float, progressBar: ProgressBar, progressPercentage :TextView){
        progressBar.progress = progress.toInt()
        progressPercentage.text = "${progress.toInt()}%"
    }
}
