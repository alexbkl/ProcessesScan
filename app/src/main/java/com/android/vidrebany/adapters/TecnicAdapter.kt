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
import com.android.vidrebany.ComandesTecnicActivity
import com.android.vidrebany.models.TecnicModel
import com.android.vidrebany.R
import com.android.vidrebany.ServeiTecnicActivity

class TecnicAdapter(private val tecnicsList: ArrayList<TecnicModel>) :
        RecyclerView.Adapter<TecnicAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTv = itemView.findViewById<TextView>(R.id.nameTv);
        var userLayout = itemView.findViewById<LinearLayout>(R.id.userLayout);

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_transporter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tecnic = tecnicsList[position]
        holder.nameTv.text = tecnic.name
        holder.userLayout.setOnClickListener {
            ServeiTecnicActivity.tecnicUid = tecnic.id

            val sharedPref = holder.itemView.context.getSharedPreferences("tecnicUid", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("tecnicUid", tecnic.id)
            editor.apply()


            //handle click
            //go to ComandesTecnicActivity from intent
            val intent = Intent(holder.itemView.context, ComandesTecnicActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return tecnicsList.size
    }

}