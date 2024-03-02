package com.example.bakalarskapraca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView


class TeoriaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teoria)

        val backToMain: ImageButton = findViewById(R.id.backToMainBtn)

        backToMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


        val pdfListView: ListView = findViewById(R.id.pdfListView)
        val pdfFiles = listOf("file1.pdf", "file2.pdf")
//        val pdfFiles = listOf("file1.pdf", "file2.pdf", "RaL3.pdf", "RaL4.pdf", "RaL5.pdf", "RaL6.pdf", "RaL7.pdf", "RaL8.pdf", "RaL9.pdf", "RaL10.pdf", "RaL11.pdf")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, pdfFiles)
        pdfListView.adapter = adapter
//
        pdfListView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, PdfViewActivity::class.java).apply {
                putExtra("fileName", pdfFiles[position])
            }
            startActivity(intent)
        }




    }


}



