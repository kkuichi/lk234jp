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
            PdfItem(1,getString(R.string.Teoria_1), "Teoria1.pdf"),
            PdfItem(2,getString(R.string.Teoria_2), "Teoria2.pdf"),
            PdfItem(3,getString(R.string.Teoria_3), "Teoria3.pdf"),
            PdfItem(4,getString(R.string.Teoria_4), "Teoria4.pdf"),
            PdfItem(5,getString(R.string.Teoria_5), "Teoria5.pdf"),
            PdfItem(6,getString(R.string.Teoria_6), "Teoria6.pdf"),
            PdfItem(7,getString(R.string.Teoria_7), "Teoria7.pdf"),
            PdfItem(8,getString(R.string.Teoria_8), "Teoria8.pdf"),
            PdfItem(9,getString(R.string.Teoria_9), "Teoria9.pdf"),
            PdfItem(10,getString(R.string.Teoria_10), "Teoria10.pdf"),
            PdfItem(11,getString(R.string.Teoria_11), "Teoria11.pdf"),
        )

        startPdfViewerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val progress = data?.getIntExtra("progress", 0) ?: 0
                val fileName = data?.getStringExtra("fileName") ?: ""
                val lastPage = data?.getIntExtra("lastPage", 0) ?: 0


                // Update the progress in your list and refresh the RecyclerView
                items.find { it.name == fileName }?.let { item ->
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

data class PdfItem(val id: Int, val name: String, val fileName: String, var progress: Int = 0, var lastPage: Int = 0)

class PdfAdapter(
    private val items: List<PdfItem>,
    private val onItemClick: (PdfItem) -> Unit
) : RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.teoria_item_layout, parent, false)
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
        private val progressBar: ProgressBar = itemView.findViewById(R.id.item_progress)
        private val progressNum: TextView = itemView.findViewById(R.id.teoria_progress_num)

        fun bind(pdfItem: PdfItem) {
            textView.text = pdfItem.name
//            progressBar.progress = pdfItem.progress
            progressNum.text = "${pdfItem.progress}%"
            itemView.setOnClickListener {
                onItemClick(pdfItem)
            }
        }
    }
}

