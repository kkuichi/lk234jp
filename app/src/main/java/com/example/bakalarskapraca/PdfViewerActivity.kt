package com.example.bakalarskapraca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.github.barteksc.pdfviewer.PDFView
import java.io.IOException

class PdfViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        val pdfView: PDFView = findViewById(R.id.pdfView)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Retrieve and set the PDF title
        val pdfTitle = intent.getStringExtra("pdfTitle") ?: "PDF Viewer"
        supportActionBar?.title = pdfTitle

        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // Get the file name from the intent
        val fileName = intent.getStringExtra("fileName") ?: ""
        Log.d("PdfViewerActivity", "Received file name: $fileName")

        try {
            val bytes = assets.open(fileName).use { inputStream ->
                inputStream.readBytes()
            }
            // Load the PDF from the ByteArray
            pdfView.fromBytes(bytes)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .load()
            } catch (e: IOException) {
            e.printStackTrace()
            Log.d("PdfViewerActivity", "Failed to load PDF: $fileName")
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Respond to the action bar's Up/Home button
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}