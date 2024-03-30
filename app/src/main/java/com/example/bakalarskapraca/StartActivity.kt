package com.example.bakalarskapraca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch
import java.io.File


private lateinit var auth: FirebaseAuth

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        checkAndUpdateFiles()

        auth = Firebase.auth
        val currentUser = auth.currentUser

        if (currentUser != null) {
            User.uid = currentUser.uid
            lifecycleScope.launch {
                User.loadUserFromFireStore()
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }
        } else {
            startActivity(Intent(this@StartActivity, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }

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


    private fun checkAndUpdateFiles() {
        val db = Firebase.firestore

        db.collection("files_metadata").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val fileName = document.getString("fileName") ?: ""
                val filePath = document.getString("filePath") ?: ""
                val lastModified = document.getTimestamp("lastModified") // Timestamp
                val fileType = document.getString("fileType") ?: ""

                checkAndDownloadFile(fileName, filePath, lastModified)
            }
        }
    }

    private fun checkAndDownloadFile(fileName: String, filePath: String, lastModified: Timestamp?) {
        val localFile = File(getExternalFilesDir(null), fileName)

        if (localFile.exists()) {
            val localLastModifiedMs = localFile.lastModified()
            val remoteLastModifiedMs = lastModified?.toDate()?.time ?: 0L

            if (localLastModifiedMs < remoteLastModifiedMs) { // IMPORTANT thing
                downloadFile(filePath, localFile)
            }
        } else {
            downloadFile(filePath, localFile)
        }
    }

    private fun downloadFile(filePath: String, localFile: File) {
        val fileRef = Firebase.storage.reference.child(filePath)
        fileRef.getFile(localFile).addOnSuccessListener {
            Log.d("StartActivity", "File downloaded: $filePath")
        }.addOnFailureListener {
            Log.d("StartActivity", "Failed to download file: $filePath", it)
        }
    }



}

