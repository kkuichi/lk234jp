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

        registrationName = findViewById(R.id.meno)
        registrationEmail = findViewById(R.id.email)
        registrationPassword = findViewById(R.id.password)
        registrationRepeatPassword = findViewById(R.id.password_repeat)
        registrationBtn = findViewById(R.id.registration_btn)
        progressBar = findViewById(R.id.registration_progress_bar)
        registrationGoToLogin = findViewById(R.id.register_login_now)
        googleSignUp = findViewById(R.id.google_btn)

        registrationGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


        registrationBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val name: String
            val email: String
            val password: String
            val reapet_password: String

            name = registrationName.text.toString()
            email = registrationEmail.text.toString()
            password = registrationPassword.text.toString()
            reapet_password = registrationRepeatPassword.text.toString()

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(reapet_password)) {
                Toast.makeText(this, "Repeat password", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }
            if (!reapet_password.equals(password)) {
                Toast.makeText(this, "Passwords must be same", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }
            auth = Firebase.auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext,
                            "Account created.",
                            Toast.LENGTH_SHORT,
                        ).show()


                        val userFirebase = auth.currentUser!!
                        User.setFields(userFirebase.uid, email, name, 0, mutableListOf(0,0,0,0,0,0,0,0,0,0,0), mutableListOf(0,0,0,0,0,0,0,0,0,0,0), mutableListOf(0,0,0,0,0,0,0,0,0,0,0),true)
                        User.uploadUserToFireStore()

                        startActivity(Intent(applicationContext, StartActivity::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext,
                            task.exception.toString(),
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                }
        }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this,gso)
        googleSignUp.setOnClickListener {
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

                        User.setFields(uid, email, name, 0, mutableListOf(0,0,0,0,0,0,0,0,0,0,0), mutableListOf(0,0,0,0,0,0,0,0,0,0,0), mutableListOf(0,0,0,0,0,0,0,0,0,0,0),true)
                        User.uploadUserToFireStore()
                    }
                    startActivity(Intent(this, StartActivity::class.java))
                } else {
                    Toast.makeText(baseContext, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}