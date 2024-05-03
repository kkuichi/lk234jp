package com.luka_kuzyk.bakalarskapraca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

// Aktivita zodpovedná za registráciu nového používateľa
class RegistrationActivity : AppCompatActivity() {

    lateinit var registrationName: TextInputEditText
    lateinit var registrationEmail: TextInputEditText
    lateinit var registrationPassword: TextInputEditText
    lateinit var registrationRepeatPassword: TextInputEditText
    lateinit var registrationBtn: Button

    private lateinit var auth: FirebaseAuth
    lateinit var progressBar: ProgressBar

    lateinit var registrationGoToLogin: TextView

    lateinit var googleSignUp: ImageButton
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Inicializácia prvkov rozhrania
        registrationName = findViewById(R.id.meno)
        registrationEmail = findViewById(R.id.email)
        registrationPassword = findViewById(R.id.password)
        registrationRepeatPassword = findViewById(R.id.password_repeat)
        registrationBtn = findViewById(R.id.registration_btn)
        progressBar = findViewById(R.id.registration_progress_bar)
        registrationGoToLogin = findViewById(R.id.register_login_now)
        googleSignUp = findViewById(R.id.google_btn)

        // Nastavenie akcie pre tlačidlo pre prechod na prihlásenie
        registrationGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Nastavenie akcie pre tlačidlo pre registráciu
        registrationBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val name: String
            val email: String
            val password: String
            val repeatPassword: String

            name = registrationName.text.toString()
            email = registrationEmail.text.toString()
            password = registrationPassword.text.toString()
            repeatPassword = registrationRepeatPassword.text.toString()

            // Kontrola vstupných údajov
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Uveďte meno", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Uveďte email", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }
            if (!validatePassword(password)) {
                Toast.makeText(this, "Heslo nespĺňa podmienky", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (repeatPassword != password) {
                Toast.makeText(this, "Heslá musia byť rovnaké", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            // Vytvorenie účtu
            auth = Firebase.auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext,
                            "Účet vytvorený.",
                            Toast.LENGTH_SHORT,
                        ).show()

                        val userFirebase = auth.currentUser!!
                        User.setFields(userFirebase.uid, email, name, 0f, mutableListOf(0,0,0,0,0,0,0,0,0,0,0), mutableListOf(0,0,0,0,0,0,0,0,0,0,0), mutableListOf(0,0,0,0,0,0,0,0,0,0,0),mutableListOf(0,0,0,0,0,0,0,0,0,0,0),true)
                        User.uploadUserToFireStore()

                        startActivity(Intent(applicationContext, StartActivity::class.java))
                        finish()
                    } else {
                        // Ak registrácia zlyhá, zobrazí sa správa
                        Toast.makeText(
                            baseContext,
                            task.exception.toString(),
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                }
        }

        // Konfigurácia možností prihlásenia sa cez Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)
        // Nastavenie akcie pre tlačidlo pre prihlásenie cez Google
        googleSignUp.setOnClickListener {
            signInViaGoogle()
        }
    }

    // Overenie platnosti hesla
    fun validatePassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"
        val passwordMatcher = Regex(passwordPattern)
        return passwordMatcher.matches(password)
    }

    // Prihlásenie sa cez účet Google
    fun signInViaGoogle() {
        startActivityForResult(gsc.signInIntent, 1000)
    }

    // Spracovanie výsledku prihlásenia sa cez Google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
                User.name = account.givenName.toString()
            } catch (e: ApiException) {
                Toast.makeText(applicationContext, "Prihlásenie cez Google zlyhalo: $e", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Overenie údajov prihlásenia cez Google a autentifikácia v Firebase
    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userFirebase = auth.currentUser
                    if (userFirebase != null) {
                        val email = userFirebase.email ?: ""
                        val name = userFirebase.displayName ?: ""
                        val uid = userFirebase.uid

                        User.setFields(uid, email, name, 0f, mutableListOf(0,0,0,0,0,0,0,0,0,0,0), mutableListOf(0,0,0,0,0,0,0,0,0,0,0), mutableListOf(0,0,0,0,0,0,0,0,0,0,0),mutableListOf(0,0,0,0,0,0,0,0,0,0,0),true)
                        User.uploadUserToFireStore()
                    }
                    startActivity(Intent(this, StartActivity::class.java))
                } else {
                    Toast.makeText(baseContext, "Autentifikácia zlyhala.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
