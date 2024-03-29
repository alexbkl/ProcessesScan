package com.android.vidrebany.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.ComandaDadesActivity
import com.android.vidrebany.PDFViewer
import com.android.vidrebany.R
import com.android.vidrebany.SignatureActivity
import com.android.vidrebany.models.ComandaModel


//create kotlin recyclerview adapter class

class ComandaAdapter(private val comandesList: ArrayList<ComandaModel>) :
    RecyclerView.Adapter<ComandaAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var comandaLayout: LinearLayout = itemView.findViewById(R.id.comandaLayout)
        var comandaTv: TextView = itemView.findViewById(R.id.comandaTv)
        var moreBtn: ImageButton = itemView.findViewById(R.id.moreBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_comanda, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comanda = comandesList[position]


        val comandaText = comanda.albaraNum + "\n" + comanda.date + "-" + comanda.time + "\n" + comanda.address
       holder.comandaTv.text = comandaText


        holder.moreBtn.setOnClickListener { view ->
            val popup = PopupMenu(holder.itemView.context, view)
            popup.menuInflater.inflate(R.menu.comanda_popup_menu, popup.menu)
            popup.setOnMenuItemClickListener { item: MenuItem? ->
                when (item!!.itemId) {
                    R.id.mostrar_pdf -> {
                        val pdfUrl = comanda.pdfUrl

                        //open PDF viewer activity intent
                        Intent(holder.itemView.context, PDFViewer::class.java).also {
                            it.putExtra("url", pdfUrl)
                            holder.itemView.context.startActivity(it)
                        }
                    }

                    R.id.mostrar_dades -> {
                        //go to comanda dades activity
                        val intent = Intent(holder.itemView.context, ComandaDadesActivity::class.java)

                        intent.putExtra("comanda", comanda)

                        holder.itemView.context.startActivity(intent)
                    }

                    R.id.generar_firma -> {
                        //go to SignatureActivity
                        val intent = Intent(holder.itemView.context, SignatureActivity::class.java)
                        intent.putExtra("comanda", comanda)
                        holder.itemView.context.startActivity(intent)
                    }
                }
                true
            }
            popup.show()

        }



        holder.comandaLayout.setOnClickListener {
            //go to comanda dades activity
            val intent = Intent(holder.itemView.context, ComandaDadesActivity::class.java)

            intent.putExtra("comanda", comanda)

            holder.itemView.context.startActivity(intent)

/*

*/


        }
    }

    override fun getItemCount(): Int {
        return comandesList.size
    }

}
