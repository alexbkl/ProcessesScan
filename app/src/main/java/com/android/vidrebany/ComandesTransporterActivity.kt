package com.android.vidrebany

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.adapters.ComandaAdapter
import com.android.vidrebany.adapters.TransporterAdapter
import com.android.vidrebany.models.ComandaModel
import com.android.vidrebany.models.TransporterModel
import com.google.firebase.database.FirebaseDatabase

class ComandesTransporterActivity : AppCompatActivity() {
    //create comandesList
    private val comandesList = ArrayList<ComandaModel>()
    private val transporterUid = TransporterActivity.transporterUid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comandes_transporter)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        toolbar.title = "Transportista VidreBany"

        val comandesRv: RecyclerView = findViewById(R.id.comandesRv) as RecyclerView;

        //poblate transportersRv with data from /transporters/ in firebase database

        //set layout manager to transportersRv
        val layoutManager = LinearLayoutManager(this)
        comandesRv.layoutManager = layoutManager


        //query code:
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("transport")
        //if transId is same as transporterUid, add to comandesList
        myRef.get().addOnSuccessListener {
            for (comanda in it.children) {
                val transId = comanda.child("transId").value.toString()
                if (transId == transporterUid) {
                    val id = comanda.child("id").value.toString()
                    val address = comanda.child("address").value.toString()
                    val clientNum = comanda.child("clientNum").value.toString()
                    val date = comanda.child("date").value.toString()
                    val firstTel = comanda.child("firstTel").value.toString()
                    val secondTel = comanda.child("secondTel").value.toString()
                    val observations = comanda.child("observations").value.toString()
                    val pdfUrl = comanda.child("pdfUrl").value.toString()
                    val status = comanda.child("status").value.toString()
                    val time = comanda.child("time").value.toString()
                    val transporterUid = comanda.child("transporterUid").value.toString()
                    val transName = comanda.child("transName").value.toString()

                    val comandaModel = ComandaModel(id, address, clientNum, date, firstTel, secondTel, observations, pdfUrl, status, time, transporterUid, transName)
                    comandesList.add(comandaModel)
                }
            }
            val adapter = ComandaAdapter(comandesList)
            comandesRv.adapter = adapter
        }


    }
}