package com.android.vidrebany

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.adapters.TransporterAdapter
import com.android.vidrebany.models.TransporterModel
import com.google.firebase.database.FirebaseDatabase

class TransporterActivity : AppCompatActivity() {
    //create transportersList
    private val transportersList = ArrayList<TransporterModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transporter)
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        toolbar.title = "Transports"

        //get "transporterUid" from shared preferences
        val sharedPreferences = getSharedPreferences("transporterUid", MODE_PRIVATE)
        transporterUid = sharedPreferences.getString("transporterUid", "null").toString()


        val transportersRv: RecyclerView = findViewById(R.id.transportersRv) as RecyclerView;

        //poblate transportersRv with data from /transporters/ in firebase database

        //set layout manager to transportersRv
        val layoutManager = LinearLayoutManager(this)
        transportersRv.layoutManager = layoutManager


        //query code:
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("transporters")
        myRef.get().addOnSuccessListener {
            for (transporter in it.children) {
                val id = transporter.child("id").value.toString()
                val name = transporter.child("name").value.toString()
                val transporterModel = TransporterModel(id, name)
                transportersList.add(transporterModel)
            }
            val adapter = TransporterAdapter(transportersList)
            transportersRv.adapter = adapter
        }


    }

    companion object {
        lateinit var transporterUid: String
    }
}