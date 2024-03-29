package com.example.bakalarskapraca

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

private lateinit var auth: FirebaseAuth

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        User.isLogged = true
//        if(isNetworkAvailable(this)) {
            if (currentUser != null) {
                User.uid = currentUser.uid
                lifecycleScope.launch {
                    User.loadUserFromFireStore()
                    startActivity(Intent(this@StartActivity, MainActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                }
            } else { // Якщо користувач не ввійшов, просто переходимо далі
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }
//        }else{
//            startActivity(Intent(this@StartActivity, MainActivity::class.java))
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//            finish()
//        }

        loadNightModeSettings()

    }
    fun loadNightModeSettings() {
        val sharedPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE)

        val isNightModeEnabled = sharedPreferences.getBoolean("NightMode", false) // false - значення за замовчуванням

        setNightMode(isNightModeEnabled)
    }
    fun setNightMode(isEnabled: Boolean) {
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}



