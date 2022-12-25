package com.android.vidrebany.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.R
import com.android.vidrebany.models.TransporterModel
import java.util.ArrayList

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
            Toast.makeText(holder.itemView.context, transporter.name+transporter.id, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return transportersList.size
    }

    }
