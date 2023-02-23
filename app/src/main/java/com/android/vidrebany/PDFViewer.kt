package com.android.vidrebany

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.vidrebany.utils.RetrievePDFfromUrl
import com.github.barteksc.pdfviewer.PDFView

class PDFViewer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfviewer)

        //get url from intent
        val url = intent.getStringExtra("url")

        //init pdfView
        val pdfView = findViewById<PDFView>(R.id.pdfView)

        //get pdf from url
        RetrievePDFfromUrl(pdfView).execute(url)

    }
}