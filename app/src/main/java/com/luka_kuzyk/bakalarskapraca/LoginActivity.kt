package com.luka_kuzyk.bakalarskapraca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
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

class LoginActivity : AppCompatActivity() {

    // Deklarácia premenných pre používateľské rozhranie
    lateinit var loginEmail: TextInputEditText
    lateinit var loginPassword: TextInputEditText
    lateinit var loginBtn: Button
    lateinit var loginForgotPass: TextView
    lateinit var loginGoToReg: TextView
    lateinit var googleSignIn: ImageButton

    // Premenná pre autentifikáciu
    private lateinit var auth: FirebaseAuth

    // Premenné pre Google prihlásenie
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializácia prvkov používateľského rozhrania
        loginEmail = findViewById(R.id.email)
        loginPassword = findViewById(R.id.password)
        loginBtn = findViewById(R.id.login_btn)
        loginForgotPass = findViewById(R.id.password_forgot)
        loginGoToReg = findViewById(R.id.login_register_now)
        googleSignIn = findViewById(R.id.google_btn)

        // Nastavenie onClickListenera pre prechod na registráciu
        loginGoToReg.setOnClickListener{
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }

        // Nastavenie onClickListenera pre zabudnuté heslo
        loginForgotPass.setOnClickListener{
            startActivity(Intent(this, ForgotPassword::class.java))
        }

        // Inicializácia autentifikačnej inštancie
        auth = Firebase.auth

        // Nastavenie onClickListenera pre prihlásenie
        loginBtn.setOnClickListener{

            val email:String = loginEmail.text.toString()
            val password:String = loginPassword.text.toString()

            if(TextUtils.isEmpty(email)){
                Toast.makeText(this, "Zadajte e-mail", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(this, "Zadajte heslo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Prihlásenie pomocou e-mailu a hesla
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Úspešné prihlásenie", Toast.LENGTH_SHORT).show()
                        User.isLogged = true
                        startActivity(Intent(applicationContext, StartActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Autentifikácia zlyhala.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

        // Konfigurácia možnosti prihlásenia pomocou účtu Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Inicializácia klienta pre prihlásenie pomocou Google
        gsc = GoogleSignIn.getClient(this,gso)
        googleSignIn.setOnClickListener {
            signInViaGoogle()
        }

    }

    // Metóda na prihlásenie pomocou účtu Google
    fun signInViaGoogle(){
        startActivityForResult(gsc.signInIntent,1000)
    }

    // Spracovanie výsledku po prihlásení pomocou účtu Google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnSuccessListener { account ->
                // Ak úspešne, spracujeme získaný účet
                firebaseAuthWithGoogle(account.idToken!!)
                User.name = account.givenName ?: "N/A"
            }.addOnFailureListener { exception ->
                // Spracovanie chyby
                if (exception is ApiException) {
                    Log.e("GoogleSignIn", "signInResult:failed code=" + exception.statusCode)
                    Toast.makeText(applicationContext, "Prihlásenie pomocou Google zlyhalo: ${exception.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Autentifikácia pomocou účtu Google
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