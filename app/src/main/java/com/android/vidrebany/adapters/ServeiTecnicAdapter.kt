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
import android.graphics.Color
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import com.android.vidrebany.ConfirmarServeiTecnicActivity
import com.android.vidrebany.ServeiTecnicDadesActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ServeiTecnicAdapter(private val serveisList: ArrayList<ServeiTecnicModel>) :
    RecyclerView.Adapter<ServeiTecnicAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var serveiTecnicLayout: LinearLayout = itemView.findViewById(R.id.serveiTecnicLayout)
        var serveiStatusTv: TextView = itemView.findViewById(R.id.serveiStatusTv)
        var albaraNumberTv: TextView = itemView.findViewById(R.id.albaraNumberTv)
        var actionDateTv: TextView = itemView.findViewById(R.id.actionDateTv)
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

        holder.serveiStatusTv.text = serveiTecnic.stateServei
        if (serveiTecnic.stateServei == "Per revisar") {
            //set serveiStatusTv textColor to yellow
            holder.serveiStatusTv.setTextColor(Color.parseColor("#FFFF00"))
        }

        holder.albaraNumberTv.text = serveiTecnic.albaraNumber
        val actionDateStr = "Acció: " + serveiTecnic.actionDate.let { convertTimestampToDateTime(it) }
        holder.actionDateTv.text = actionDateStr
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
                    R.id.confirmacio -> {
                        val intent = Intent(
                            holder.itemView.context,
                            ConfirmarServeiTecnicActivity::class.java
                        )
                        intent.putExtra("serveiTecnic", serveiTecnic)
                        holder.itemView.context.startActivity(intent)
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
    private fun convertTimestampToDateTime(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
    override fun getItemCount(): Int {
        return serveisList.size
    }

}