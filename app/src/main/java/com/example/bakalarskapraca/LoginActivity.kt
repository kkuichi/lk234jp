package com.example.bakalarskapraca

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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    lateinit var loginEmail: TextInputEditText
    lateinit var loginPassword: TextInputEditText
    lateinit var loginBtn: Button

    private lateinit var auth: FirebaseAuth
    lateinit var progressBar: ProgressBar

    lateinit var loginForgotPass: TextView
    lateinit var loginGoToReg: TextView

    lateinit var googleSignIn: ImageButton
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginEmail = findViewById(R.id.email)
        loginPassword = findViewById(R.id.password)
        loginBtn = findViewById(R.id.login_btn)
        progressBar = findViewById(R.id.login_progress_bar)
        loginForgotPass = findViewById(R.id.password_forgot)
        loginGoToReg = findViewById(R.id.login_register_now)
        googleSignIn = findViewById(R.id.google_btn)


        loginGoToReg.setOnClickListener{
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }

        loginForgotPass.setOnClickListener{
            startActivity(Intent(this, ForgotPassword::class.java))
        }


        auth = Firebase.auth
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()


        loginBtn.setOnClickListener{
            progressBar.visibility = View.VISIBLE

            val email:String = loginEmail.text.toString()
            val password:String = loginPassword.text.toString()

            if(TextUtils.isEmpty(email)){
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Login Successfully", Toast.LENGTH_SHORT).show()
                        User.isLogged = true
                        startActivity(Intent(applicationContext, StartActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this,gso)
        googleSignIn.setOnClickListener {
            signInViaGoogle()
        }

    }
    fun signInViaGoogle(){
        startActivityForResult(gsc.signInIntent,1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
                User.name = account.givenName.toString()
            } catch (e: ApiException) {
                Toast.makeText(applicationContext, "Google sign in failed: $e", Toast.LENGTH_LONG).show()
            }
        }
    }

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

                        User.setFields(uid, email, name, 0f, mutableListOf(0,0,0,0,0,0,0,0,0,0,0), mutableListOf(0,0,0,0,0,0,0,0,0,0,0), mutableListOf(0,0,0,0,0,0,0,0,0,0,0),true)
                        User.uploadUserToFireStore()
                    }
                    startActivity(Intent(this, StartActivity::class.java))
                } else {
                    Toast.makeText(baseContext, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}