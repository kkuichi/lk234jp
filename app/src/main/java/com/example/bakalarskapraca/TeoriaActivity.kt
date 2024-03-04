package com.example.bakalarskapraca

import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import com.github.barteksc.pdfviewer.PDFView
import java.io.IOException

data class PdfItem(val id: Int, val name: String, val fileName: String)
class TeoriaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teoria)

        val backToMain: ImageButton = findViewById(R.id.backToMainBtn)

        backToMain.setOnClickListener {
            finish()
        }

        val pdfListView: RecyclerView = findViewById(R.id.pdfListView)

        val items = listOf(
            PdfItem(1,"Item 1", "Teoria1.pdf"),
            PdfItem(2,"Item 2", "Teoria2.pdf"),
            PdfItem(3,"Item 3", "Teoria3.pdf"),
            PdfItem(4,"Item 4", "Teoria4.pdf"),
            PdfItem(5,"Item 5", "Teoria5.pdf"),
            PdfItem(6,"Item 6", "Teoria6.pdf"),
            PdfItem(7,"Item 7", "Teoria7.pdf"),
            PdfItem(8,"Item 8", "Teoria8.pdf"),
            PdfItem(9,"Item 9", "Teoria9.pdf"),
            PdfItem(10,"Item 10", "Teoria10.pdf"),
            PdfItem(11,"Item 11", "Teoria11.pdf"),
        )

        pdfListView.layoutManager = LinearLayoutManager(this)
        pdfListView.adapter = PdfAdapter(items) { pdfItem ->
            // When an item is clicked, start PdfViewerActivity with the PDF file name
            val intent = Intent(this, PdfViewerActivity::class.java).apply {
                putExtra("fileName", pdfItem.fileName)
                putExtra("pdfTitle", pdfItem.name) // Pass the title for setting in PdfViewerActivity
            }
            startActivity(intent)
        }

    }

}


class PdfAdapter(
    private val items: List<PdfItem>,
    private val onItemClick: (PdfItem) -> Unit
) : RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return PdfViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    class PdfViewHolder(
        itemView: View,
        private val onItemClick: (PdfItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.item_text)

        fun bind(pdfItem: PdfItem) {
            textView.text = pdfItem.name
            itemView.setOnClickListener {
                onItemClick(pdfItem)
            }
        }
    }
}

