package com.android.vidrebany

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.adapters.ComandaAdapter
import com.android.vidrebany.models.ComandaModel
import com.google.firebase.database.FirebaseDatabase


class ComandesTransporterActivity : AppCompatActivity() {
    //create comandesList
    private val comandesList = ArrayList<ComandaModel>()

    private var transporterUid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comandes_transporter)

        val sharedPreferences = getSharedPreferences("transporterUid", MODE_PRIVATE)
        transporterUid = sharedPreferences.getString("transporterUid", "null").toString()

        val intent = intent
        val hasPdf: Boolean = intent.getBooleanExtra("hasPdf", false)

        if (hasPdf) {
            val modifiedPdfUri: Uri? = intent.getParcelableExtra("modifiedPdfUri")
            val comanda = intent.getParcelableExtra<ComandaModel>("comanda")

            //send modified file to email
            val intent2 = Intent(Intent.ACTION_SEND)

            val extraText = "Núm. albarà: " + comanda!!.albaraNum + "\nNúm. client: " + comanda.clientNum +
                    "\nData: " + comanda.date + "-" +comanda.time +
                    "\nPrimer tel.: " + comanda.firstTel +
                    "\nSegon tel.: " + comanda.secondTel +
                    "\nAdreça: " + comanda.address +
                    "\nObservacions: " + comanda.observations +
                    "\nTransportista: " + comanda.transName;

            intent2.type = "application/pdf"
            intent2.putExtra(Intent.EXTRA_EMAIL, arrayOf("asederado@gmail.com"))
            intent2.putExtra(Intent.EXTRA_SUBJECT, "Comanda")
            intent2.putExtra(Intent.EXTRA_TEXT, extraText)
            intent2.putExtra(Intent.EXTRA_STREAM, modifiedPdfUri)
            intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent2)
        }

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
                val status = comanda.child("status").value.toString()

                if (transId == transporterUid && status == "pendiente") {
                    val id = comanda.child("id").value.toString()
                    val address = comanda.child("address").value.toString()
                    val albaraNum = comanda.child("albaraNum").value.toString()
                    val clientNum = comanda.child("clientNum").value.toString()
                    val date = comanda.child("date").value.toString()
                    val firstTel = comanda.child("firstTel").value.toString()
                    val secondTel = comanda.child("secondTel").value.toString()
                    val observations = comanda.child("observations").value.toString()
                    val pdfUrl = comanda.child("pdfUrl").value.toString()
                    val time = comanda.child("time").value.toString()
                    val transporterUid = comanda.child("transporterUid").value.toString()
                    val transName = comanda.child("transName").value.toString()

                    val comandaModel = ComandaModel(id, address, albaraNum, clientNum, date, firstTel, secondTel, observations, pdfUrl, status, time, transporterUid, transName)
                    comandesList.add(comandaModel)
                }
            }
            val adapter = ComandaAdapter(comandesList)
            comandesRv.adapter = adapter
        }


    }
}