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

// Aktivita pre zobrazenie príkladov
class PrikladyActivity : AppCompatActivity() {

    private lateinit var startPdfViewerResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_priklady)

        // Nájdenie tlačidla pre návrat na hlavnú aktivitu
        val backToMain: ImageButton = findViewById(R.id.backToMainBtn)

        // Nastavenie onClickListenera pre tlačidlo návratu
        backToMain.setOnClickListener {
            finish()
        }

        // Inicializácia zoznamu RecyclerView
        val pdfListView: RecyclerView = findViewById(R.id.pdfListView)

        // Zoznam príkladov
        val items = listOf(
            PdfItem(1,getString(R.string.Priklady_1), "Priklady_1.pdf", -2, User.priklady_lastPage[0]),
            PdfItem(2,getString(R.string.Priklady_2), "Priklady_2.pdf", -2, User.priklady_lastPage[1]),
            PdfItem(3,getString(R.string.Priklady_3), "Priklady_3.pdf", -2, User.priklady_lastPage[2]),
            PdfItem(4,getString(R.string.Priklady_4), "Priklady_4.pdf", -2, User.priklady_lastPage[3]),
            PdfItem(5,getString(R.string.Priklady_5), "Priklady_5.pdf", -2, User.priklady_lastPage[4]),
            PdfItem(6,getString(R.string.Priklady_6), "Priklady_6.pdf", -2, User.priklady_lastPage[5]),
            PdfItem(7,getString(R.string.Priklady_7), "Priklady_6.pdf", -2, User.priklady_lastPage[6]),
        )

        // Inicializácia listenera na výsledky spustenia aktivity
        startPdfViewerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val fileName = data?.getStringExtra("fileName") ?: ""
                val lastPage = data?.getIntExtra("lastPage", 0) ?: 0

                // Aktualizácia pokroku pre používateľa a v Firebase a obnovenie zoznamu RecyclerView
                items.find { it.name == fileName }?.let { item ->
                    val itemId = item.id - 1
                    User.priklady_lastPage[itemId] = lastPage
                    User.uploadUserProgressToFireStore()
                    item.lastPage = lastPage
                    pdfListView.adapter?.notifyDataSetChanged()
                }
            }
        }

        // Nastavenie manažéra rozloženia a adaptéra pre zoznam RecyclerView
        pdfListView.layoutManager = LinearLayoutManager(this)
        pdfListView.adapter = PdfAdapter(items) { pdfItem ->
            // Po kliknutí na položku spustite aktivitu PdfViewerActivity s menom PDF súboru
            val intent = Intent(this, PdfViewerActivity::class.java).apply {
                putExtra("fileName", pdfItem.fileName)
                putExtra("pdfTitle", pdfItem.name) // Pre odovzdanie názvu na nastavenie v PdfViewerActivity
                putExtra("lastPage", pdfItem.lastPage) // Pre odovzdanie poslednej strany na nastavenie v PdfViewerActivity
            }
            startPdfViewerResult.launch(intent)
        }
    }
}
