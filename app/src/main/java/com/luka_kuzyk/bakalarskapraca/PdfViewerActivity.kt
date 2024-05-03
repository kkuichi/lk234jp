package com.luka_kuzyk.bakalarskapraca

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.github.barteksc.pdfviewer.PDFView
import java.io.File
import java.io.IOException

// Aktivita na zobrazenie PDF súborov
class PdfViewerActivity : AppCompatActivity() {

    var currentProgress: Int = 0
    var currentPage: Int = 0
    lateinit var fileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        val pdfView: PDFView = findViewById(R.id.pdfView)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Získanie a nastavenie názvu PDF
        val pdfTitle = intent.getStringExtra("pdfTitle") ?: "Prehliadač PDF"
        supportActionBar?.title = pdfTitle
        fileName = pdfTitle

        // Povolenie tlačidla Up
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val lastPage = intent.getIntExtra("lastPage", 0)
        val fileName = intent.getStringExtra("fileName") ?: ""

        try {
            val localFile = File(getExternalFilesDir(null), fileName)

            // Nastavenie zobrazenia PDF
            pdfView.fromFile(localFile)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(lastPage)
                .onPageChange { page, pageCount ->
                    val visiblePages = 3 // používateľ zvyčajne vidí až 3 stránky
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
            Log.d("PdfViewerActivity", "Chyba pri načítaní PDF: $fileName")
        }

    }

    // Spracovanie výberu položky v paneli akcií
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Reagovanie na tlačidlo Up/Home
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

    // Spracovanie stlačenia tlačidla Späť
    override fun onBackPressed() {
        val data = Intent().apply {
            putExtra("progress", currentProgress)
            putExtra("fileName", fileName)
            putExtra("lastPage", currentPage)
        }
        setResult(Activity.RESULT_OK, data)
        finish()
        super.onBackPressed()
    }
}

// Dátová trieda reprezentujúca položku PDF
data class PdfItem(
    val id: Int,
    val name: String,
    val fileName: String,
    var progress: Int = 0,
    var lastPage: Int = 0
)

// Adaptér pre RecyclerView zobrazujúci položky PDF
class PdfAdapter(
    private val items: List<PdfItem>,
    private val onItemClick: (PdfItem) -> Unit
) : RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.teoria_item_layout, parent, false)
        return PdfViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    // ViewHolder pre položku PDF
    class PdfViewHolder(
        itemView: View,
        private val onItemClick: (PdfItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.item_text)
        private val progressNum: TextView = itemView.findViewById(R.id.teoria_progress_num)

        // Viazanie údajov o položke PDF na zobrazenie
        fun bind(pdfItem: PdfItem) {
            textView.text = pdfItem.name
            // Ak je pokrok -2, skryjeme text pokroku (výnimka pre aktivitu Priklady)
            if (pdfItem.progress == -2) {
                progressNum.visibility = View.GONE
            } else {
                progressNum.text = "${pdfItem.progress}%"
            }
            // Spracovanie stlačenia položky
            itemView.setOnClickListener {
                onItemClick(pdfItem)
            }
        }
    }
}
