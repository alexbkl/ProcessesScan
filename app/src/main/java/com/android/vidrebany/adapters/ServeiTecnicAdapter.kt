package com.android.vidrebany.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.models.ServeiTecnicModel
import com.android.vidrebany.R
import android.content.Intent
import android.view.MenuItem
import android.widget.PopupMenu
import com.android.vidrebany.ServeiTecnicDadesActivity


class ServeiTecnicAdapter(private val serveisList: ArrayList<ServeiTecnicModel>) :
    RecyclerView.Adapter<ServeiTecnicAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var serveiTecnicLayout: LinearLayout = itemView.findViewById(R.id.serveiTecnicLayout)
        var albaraNumberTv: TextView = itemView.findViewById(R.id.albaraNumberTv)
        var serveiTypeTv: TextView = itemView.findViewById(R.id.serveiTypeTv)
        var moreBtn: ImageButton = itemView.findViewById(R.id.moreBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_servei_tecnic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val serveiTecnic = serveisList[position]

        val serveiType: String
        serveiType = if (serveiTecnic.isMesura) "Mesura" else "Instal·lació"

        holder.albaraNumberTv.text = serveiTecnic.albaraNumber
        holder.serveiTypeTv.text = serveiType

        holder.moreBtn.setOnClickListener { view ->
            val popup = PopupMenu(holder.itemView.context, view)
            popup.getMenuInflater().inflate(R.menu.servei_tecnic_popup_menu, popup.getMenu())
            popup.setOnMenuItemClickListener { item: MenuItem? ->
                when (item!!.getItemId()) {
                    R.id.mostrar_dades -> {
                        val intent = Intent(holder.itemView.context, ServeiTecnicDadesActivity::class.java)
                        intent.putExtra("serveiTecnic", serveiTecnic)
                        holder.itemView.context.startActivity(intent)
                    }

                    R.id.mostrar_documents -> {


                    }

                    R.id.confirmacio -> {


                    }


                }
                true

            }
            popup.show()
        }

        holder.serveiTecnicLayout.setOnClickListener {
            val intent = Intent(holder.itemView.context, ServeiTecnicDadesActivity::class.java)
            intent.putExtra("serveiTecnic", serveiTecnic)
            holder.itemView.context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return serveisList.size
    }
}