package com.example.bakalarskapraca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

private lateinit var auth: FirebaseAuth

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        if(currentUser!=null) {
            User.email = currentUser.email.toString()
            User.isLogged = true
        }

        val mSplashThread = Thread(){
            run {
                try {
                    synchronized(this){
                        Thread.sleep(1500)
                    }
                }catch (_:InterruptedException){
                }finally {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                }
            }
        }

        mSplashThread.start()
    }
}