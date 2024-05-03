package com.luka_kuzyk.bakalarskapraca

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Aktivita zodpovedná za zobrazenie teoretických materiálov
class TeoriaActivity : AppCompatActivity() {

    private lateinit var startPdfViewerResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teoria)

        // Návrat na hlavnú obrazovku
        val backToMain: ImageButton = findViewById(R.id.backToMainBtn)
        backToMain.setOnClickListener {
            finish()
        }

        // Inicializácia RecyclerView pre zobrazenie teoretických materiálov
        val pdfListView: RecyclerView = findViewById(R.id.pdfListView)

        // Zoznam teoretických materiálov
        val items = listOf(
            PdfItem(1, getString(R.string.Teoria_1), "RaL1.pdf", User.teoria_progress[0], User.teoria_lastPage[0]),
            PdfItem(2, getString(R.string.Teoria_2), "RaL2.pdf", User.teoria_progress[1], User.teoria_lastPage[1]),
            PdfItem(3, getString(R.string.Teoria_3), "RaL3.pdf", User.teoria_progress[2], User.teoria_lastPage[2]),
            PdfItem(4, getString(R.string.Teoria_4), "RaL4.pdf", User.teoria_progress[3], User.teoria_lastPage[3]),
            PdfItem(5, getString(R.string.Teoria_5), "RaL5.pdf", User.teoria_progress[4], User.teoria_lastPage[4]),
            PdfItem(6, getString(R.string.Teoria_6), "RaL6.pdf", User.teoria_progress[5], User.teoria_lastPage[5]),
            PdfItem(7, getString(R.string.Teoria_7), "RaL7.pdf", User.teoria_progress[6], User.teoria_lastPage[6]),
            PdfItem(8, getString(R.string.Teoria_8), "RaL8.pdf", User.teoria_progress[7], User.teoria_lastPage[7]),
            PdfItem(9, getString(R.string.Teoria_9), "RaL9.pdf", User.teoria_progress[8], User.teoria_lastPage[8]),
            PdfItem(10, getString(R.string.Teoria_10), "RaL10.pdf", User.teoria_progress[9], User.teoria_lastPage[9]),
            PdfItem(11, getString(R.string.Teoria_11), "RaL11.pdf", User.teoria_progress[10], User.teoria_lastPage[10]),
        )

        // Spustenie aktivity pre zobrazenie PDF
        startPdfViewerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val progress = data?.getIntExtra("progress", 0) ?: 0
                val fileName = data?.getStringExtra("fileName") ?: ""
                val lastPage = data?.getIntExtra("lastPage", 0) ?: 0

                // Aktualizácia pokroku v zozname a obnovenie RecyclerView
                items.find { it.name == fileName }?.let { item ->
                    val itemId = item.id - 1
                    User.teoria_progress[itemId] = progress
                    User.teoria_lastPage[itemId] = lastPage
                    User.uploadUserProgressToFireStore()
                    item.progress = progress
                    item.lastPage = lastPage
                    pdfListView.adapter?.notifyDataSetChanged()
                }
            }
        }

        // Nastavenie layout managera a adaptéra pre RecyclerView
        pdfListView.layoutManager = LinearLayoutManager(this)
        pdfListView.adapter = PdfAdapter(items) { pdfItem ->
            // Pri kliknutí na položku spustite aktivitu PdfViewerActivity s názvom PDF súboru
            val intent = Intent(this, PdfViewerActivity::class.java).apply {
                putExtra("fileName", pdfItem.fileName)
                putExtra("pdfTitle", pdfItem.name) // Poslať názov pre nastavenie v PdfViewerActivity
                putExtra("lastPage", pdfItem.lastPage) // Poslať poslednú stránku pre nastavenie v PdfViewerActivity
            }
            startPdfViewerResult.launch(intent)
        }
    }
}
