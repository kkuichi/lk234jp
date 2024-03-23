package com.example.bakalarskapraca

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.github.barteksc.pdfviewer.PDFView
import java.io.IOException
import kotlin.properties.Delegates

class PdfViewerActivity : AppCompatActivity() {

    var currentProgress:Int = 0
    var currentPage: Int = 0
    lateinit var fileName:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        val pdfView: PDFView = findViewById(R.id.pdfView)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Retrieve and set the PDF title
        val pdfTitle = intent.getStringExtra("pdfTitle") ?: "PDF Viewer"
        supportActionBar?.title = pdfTitle
        fileName = pdfTitle

        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val lastPage = intent.getIntExtra("lastPage", 0)
        val fileName = intent.getStringExtra("fileName") ?: ""

        try {
            val bytes = assets.open(fileName).use { inputStream ->
                inputStream.readBytes()
            }
            // Load the PDF from the ByteArray
            pdfView.fromBytes(bytes)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(lastPage)
                .onPageChange { page, pageCount ->
                    val visiblePages = 3 // user typically sees up to 3 pages
                    val adjustedPageCount = pageCount - (visiblePages - 1)
                    val adjustedPage = if (page >= adjustedPageCount) pageCount else page

                    currentProgress = (adjustedPage.toFloat() / pageCount.toFloat() * 100).toInt()
                    currentPage = page

                    if (page >= pageCount - 1) {
                        currentProgress = 100
                    }
                }
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
                // Assume 'currentProgress' holds the last known progress value
                val data = Intent().apply {
                    putExtra("progress", currentProgress)
                    putExtra("fileName", fileName)
                    putExtra("lastPage", currentPage)
                }
                setResult(Activity.RESULT_OK, data)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        val data = Intent().apply {
            putExtra("progress", currentProgress)
            putExtra("fileName", fileName)
        }
        setResult(Activity.RESULT_OK, data)
        finish()
        super.onBackPressed()
    }


}