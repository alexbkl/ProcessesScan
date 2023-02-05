package com.android.vidrebany.adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.ComandesTransporterActivity
import com.android.vidrebany.R
import com.android.vidrebany.TransporterActivity
import com.android.vidrebany.models.TransporterModel


//create kotlin recyclerview adapter class

class TransporterAdapter(private val transportersList: ArrayList<TransporterModel>) :
        RecyclerView.Adapter<TransporterAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      var nameTv = itemView.findViewById<TextView>(R.id.nameTv);
        var userLayout = itemView.findViewById<LinearLayout>(R.id.userLayout);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_transporter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transporter = transportersList[position]
        holder.nameTv.text = transporter.name
        holder.userLayout.setOnClickListener {
            //handle click
            TransporterActivity.transporterUid = transporter.id


            val sharedPref = holder.itemView.context.getSharedPreferences("transporterUid", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("transporterUid", transporter.id)
            editor.apply()


            //go to ComandesTransporterActivity from intent
            val intent = Intent(holder.itemView.context, ComandesTransporterActivity::class.java)
            holder.itemView.context.startActivity(intent)
            Toast.makeText(holder.itemView.context, transporter.name, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return transportersList.size
    }

    }
