package com.android.vidrebany.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.ComandaDadesActivity
import com.android.vidrebany.PDFViewer
import com.android.vidrebany.R
import com.android.vidrebany.models.ComandaModel
import com.android.vidrebany.utils.RetrievePDFfromUrl
import com.github.barteksc.pdfviewer.PDFView
import java.util.ArrayList

//create kotlin recyclerview adapter class

class ComandaAdapter(private val comandesList: ArrayList<ComandaModel>) :
    RecyclerView.Adapter<ComandaAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var comandaLayout = itemView.findViewById<LinearLayout>(R.id.comandaLayout);
        var comandaTv = itemView.findViewById<TextView>(R.id.comandaTv);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_comanda, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comanda = comandesList[position]
       holder.comandaTv.text = comanda.address


        //val url = comanda.pdfUrl


        holder.comandaLayout.setOnClickListener {
            //go to comanda dades activity
            val intent = Intent(holder.itemView.context, ComandaDadesActivity::class.java)

            intent.putExtra("comanda", comanda)

            holder.itemView.context.startActivity(intent)

/*
            //open PDF viewer activity intent
            Intent(holder.itemView.context, PDFViewer::class.java).also {
                it.putExtra("url", comanda.pdfUrl)
                holder.itemView.context.startActivity(it)
            }
*/


        }
    }

    override fun getItemCount(): Int {
        return comandesList.size
    }

}
