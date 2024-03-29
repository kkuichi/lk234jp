package com.example.bakalarskapraca

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PrikladyActivity : AppCompatActivity() {

    private lateinit var startPdfViewerResult: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_priklady)

        val backToMain: ImageButton = findViewById(R.id.backToMainBtn)

        backToMain.setOnClickListener {
            finish()
        }

        val pdfListView: RecyclerView = findViewById(R.id.pdfListView)

        val items = listOf(
            PdfItem(1,getString(R.string.Priklady_1), "Priklady_1.pdf", 0, User.priklady_lastPage[0]),
            PdfItem(2,getString(R.string.Priklady_2), "Priklady_2.pdf", 0, User.priklady_lastPage[1]),
            PdfItem(3,getString(R.string.Priklady_3), "Priklady_3.pdf", 0, User.priklady_lastPage[2]),
            PdfItem(4,getString(R.string.Priklady_4), "Priklady_4.pdf", 0, User.priklady_lastPage[3]),
            PdfItem(5,getString(R.string.Priklady_5), "Priklady_5.pdf", 0, User.priklady_lastPage[4]),
            PdfItem(6,getString(R.string.Priklady_6), "Priklady_6.pdf", 0, User.priklady_lastPage[5]),
            PdfItem(7,getString(R.string.Priklady_7), "Priklady_6.pdf", 0, User.priklady_lastPage[6]),

        )

        startPdfViewerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val progress = data?.getIntExtra("progress", 0) ?: 0
                val fileName = data?.getStringExtra("fileName") ?: ""
                val lastPage = data?.getIntExtra("lastPage", 0) ?: 0

                // Update the progress in your list and refresh the RecyclerView
                items.find { it.name == fileName }?.let { item ->
                    val itemId = item.id-1
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