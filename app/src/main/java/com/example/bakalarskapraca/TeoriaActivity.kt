package com.example.bakalarskapraca

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TeoriaActivity : AppCompatActivity() {

    private lateinit var startPdfViewerResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teoria)

        val backToMain: ImageButton = findViewById(R.id.backToMainBtn)

        backToMain.setOnClickListener {
            finish()
        }

        val pdfListView: RecyclerView = findViewById(R.id.pdfListView)

        val items = listOf(
            PdfItem(1,getString(R.string.Teoria_1), "Teoria1.pdf", User.teoria_progress[0], User.teoria_lastPage[0]),
            PdfItem(2,getString(R.string.Teoria_2), "Teoria2.pdf", User.teoria_progress[1], User.teoria_lastPage[1]),
            PdfItem(3,getString(R.string.Teoria_3), "Teoria3.pdf", User.teoria_progress[2], User.teoria_lastPage[2]),
            PdfItem(4,getString(R.string.Teoria_4), "Teoria4.pdf", User.teoria_progress[3], User.teoria_lastPage[3]),
            PdfItem(5,getString(R.string.Teoria_5), "Teoria5.pdf", User.teoria_progress[4], User.teoria_lastPage[4]),
            PdfItem(6,getString(R.string.Teoria_6), "Teoria6.pdf", User.teoria_progress[5], User.teoria_lastPage[5]),
            PdfItem(7,getString(R.string.Teoria_7), "Teoria7.pdf", User.teoria_progress[6], User.teoria_lastPage[6]),
            PdfItem(8,getString(R.string.Teoria_8), "Teoria8.pdf", User.teoria_progress[7], User.teoria_lastPage[7]),
            PdfItem(9,getString(R.string.Teoria_9), "Teoria9.pdf", User.teoria_progress[8], User.teoria_lastPage[8]),
            PdfItem(10, getString(R.string.Teoria_10), "Teoria10.pdf", User.teoria_progress[9], User.teoria_lastPage[9]),
            PdfItem(11,getString(R.string.Teoria_11), "Teoria11.pdf", User.teoria_progress[10], User.teoria_lastPage[10]),
        )

        startPdfViewerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val progress = data?.getIntExtra("progress", 0) ?: 0
                val fileName = data?.getStringExtra("fileName") ?: ""
                val lastPage = data?.getIntExtra("lastPage", 0) ?: 0

                // Update the progress in your list and refresh the RecyclerView
                items.find { it.name == fileName }?.let { item ->
                    var itemId = item.id-1
                    User.teoria_progress[itemId] = progress
                    User.teoria_lastPage[itemId] = lastPage
                    User.uploadUserProgressToFireStore()
                    item.progress = progress
                    item.lastPage = lastPage
                    pdfListView.adapter?.notifyDataSetChanged()
                }
            }
        }
        pdfListView.layoutManager = LinearLayoutManager(this)
        pdfListView.adapter = PdfAdapter(items) { pdfItem ->
            // When an item is clicked, start PdfViewerActivity with the PDF file name
            val intent = Intent(this, PdfViewerActivity::class.java).apply {
                putExtra("fileName", pdfItem.fileName)
                putExtra("pdfTitle", pdfItem.name) // Pass the title for setting in PdfViewerActivity
                putExtra("lastPage", pdfItem.lastPage) // Pass the title for setting in PdfViewerActivity
            }
//            startActivity(intent)
            startPdfViewerResult.launch(intent)
        }

    }

}

