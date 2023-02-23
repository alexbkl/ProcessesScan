package com.android.vidrebany.adapters

import android.content.Context
import android.content.Intent
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
        Toast.makeText(context, "suka", Toast.LENGTH_SHORT).show()
        println("suchka")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("position: $position")
        println("piska")

        val document = documentsNames?.get(position)
        val documentUrl = documentsUrls?.get(position)
        println("documentsName: $document")
        Toast.makeText(context, document, Toast.LENGTH_SHORT).show()
        holder.documentNameTv.text = document

        holder.documentLayout.setOnClickListener {
            val intent = Intent(context, PDFViewer::class.java)
            intent.putExtra("url", documentUrl)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return documentsNames?.size ?: 0
    }
}