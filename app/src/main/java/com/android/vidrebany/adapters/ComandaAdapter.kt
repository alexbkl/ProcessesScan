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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.ComandaDadesActivity
import com.android.vidrebany.PDFViewer
import com.android.vidrebany.R
import com.android.vidrebany.models.ComandaModel


//create kotlin recyclerview adapter class

class ComandaAdapter(private val comandesList: ArrayList<ComandaModel>) :
    RecyclerView.Adapter<ComandaAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var comandaLayout = itemView.findViewById<LinearLayout>(R.id.comandaLayout);
        var comandaTv = itemView.findViewById<TextView>(R.id.comandaTv);
        var moreBtn = itemView.findViewById<ImageButton>(R.id.moreBtn);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_comanda, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comanda = comandesList[position]



       holder.comandaTv.text = comanda.address


        holder.moreBtn.setOnClickListener(View.OnClickListener { view ->
            val popup = PopupMenu(holder.itemView.context, view)
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu())
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
                when (item!!.getItemId()) {
                    R.id.mostrar_pdf -> {
                        val pdfUrl = comanda.pdfUrl

                        //open PDF viewer activity intent
                        Intent(holder.itemView.context, PDFViewer::class.java).also {
                            it.putExtra("url", pdfUrl)
                            holder.itemView.context.startActivity(it)
                        }
                    }
                    R.id.menu_item_2 -> {
                        Toast.makeText(holder.itemView.context, "Item 2", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            })
            popup.show()

        })



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
