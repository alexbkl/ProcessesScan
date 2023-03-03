package com.android.vidrebany.adapters

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.PDFViewer
import com.android.vidrebany.R


class DocumentsAdapter(
    private val context: Context,
    private val documentsNames: ArrayList<String>?,
    private val documentsUrls: ArrayList<String>?
) :
    RecyclerView.Adapter<DocumentsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var documentLayout: LinearLayout = itemView.findViewById(R.id.documentLayout)
        var documentNameTv: TextView = itemView.findViewById(R.id.documentNameTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_document, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val document = documentsNames?.get(position)
        val documentUrl = documentsUrls?.get(position)

        holder.documentNameTv.text = document

        holder.documentLayout.setOnClickListener {
            //if document contains .pdf, open it in the pdf viewer
            if (document?.contains(".pdf") == true) {
                val intent = Intent(context, PDFViewer::class.java)
                intent.putExtra("url", documentUrl)
                context.startActivity(intent)
            } else {
                //download file from url
                val request = DownloadManager.Request(documentUrl?.let { it1 -> android.net.Uri.parse(it1) })
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                request.setTitle(document)
                request.setDescription("Descarregant document...")
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, document)
                val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                manager.enqueue(request)
                Toast.makeText(context, "Descarregant document...", Toast.LENGTH_SHORT).show()

            }


        }
    }

    override fun getItemCount(): Int {
        return documentsNames?.size ?: 0
    }
}