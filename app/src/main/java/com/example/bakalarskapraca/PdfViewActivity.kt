package com.example.bakalarskapraca

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.ImageView
import java.io.IOException


class PdfViewActivity : Activity() {

    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var currentPage: PdfRenderer.Page
    private lateinit var pdfView: ImageView
    private var parcelFileDescriptor: ParcelFileDescriptor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pdfView = ImageView(this)
        setContentView(pdfView)

        val assetFileName = intent.getStringExtra("fileName") ?: return

        try {
            openPdfRenderer(assetFileName)
            showPage(0)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun openPdfRenderer(fileName: String) {
        // Open the file from assets using ParcelFileDescriptor
        val assetFileDescriptor = assets.openFd(fileName)
        parcelFileDescriptor = ParcelFileDescriptor.dup(assetFileDescriptor.fileDescriptor)
        pdfRenderer = PdfRenderer(parcelFileDescriptor!!)
    }

    private fun showPage(index: Int) {
        // Make sure to close the current page before opening another one
        if (::currentPage.isInitialized) {
            currentPage.close()
        }
        currentPage = pdfRenderer.openPage(index)

        // Create a bitmap where the PDF page will be rendered
        val bitmap = Bitmap.createBitmap(currentPage.width, currentPage.height, Bitmap.Config.ARGB_8888)

        // Render the page onto the Bitmap
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        pdfView.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::currentPage.isInitialized) {
            currentPage.close()
        }
        pdfRenderer.close()
        parcelFileDescriptor?.close()
    }
}