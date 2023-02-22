package com.android.vidrebany

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.adapters.ServeiTecnicAdapter
import com.android.vidrebany.models.ServeiTecnicModel
import com.google.firebase.database.FirebaseDatabase

class ComandesTecnicActivity : AppCompatActivity() {
    //create comandesList
    private val serveisList = ArrayList<ServeiTecnicModel>()

    private var tecnicUid: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comandes_tecnic)

        val sharedPreferences = getSharedPreferences("tecnicUid", MODE_PRIVATE)
        tecnicUid = sharedPreferences.getString("tecnicUid", "null").toString()

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        toolbar.title = "Tècnic VidreBany"

        val serveisRv: RecyclerView = findViewById(R.id.serveisRv) as RecyclerView;

        //poblate transportersRv with data from /transporters/ in firebase database

        //set layout manager to transportersRv
        val layoutManager = LinearLayoutManager(this)
        serveisRv.layoutManager = layoutManager


        //query code:
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("serveiTecnic")
        //if tecnicId is same as tecnicUid, add to serveisList

        myRef.get().addOnSuccessListener {
            for (comanda in it.children) {
                val tecnicId = comanda.child("tecnicId").value.toString()
                val stateServei = comanda.child("stateServei").value.toString()
                if (tecnicId == tecnicUid && (stateServei == "Pendent" || stateServei == "Per revisar")) {
                    val key = comanda.child("key").value.toString()
                    //get actionDate number from firebase
                    val actionDate = comanda.child("actionDate").value.toString().toLong()
                    val albaraFile = comanda.child("albaraFile").value.toString()
                    val albaraFileName = comanda.child("albaraFileName").value.toString()
                    val albaraNumber = comanda.child("albaraNumber").value.toString()
                    val albaraType = comanda.child("albaraType").value.toString()
                    val codeDistributor = comanda.child("codeDistributor").value.toString()
                    val currentDate = comanda.child("currentDate").value.toString().toLong()
                    val description = comanda.child("description").value.toString()
                    //inside comanda.child("documents") there are many documents urls, so we need to iterate
                    val documents = ArrayList<String>()
                    for (document in comanda.child("documents").children) {
                        val documentUrl = document.value.toString()
                        documents.add(documentUrl)
                    }
                    //inside comanda.child("documentsNames") there are many documents names, so we need to iterate
                    val documentsNames = ArrayList<String>()
                    for (documentName in comanda.child("documentsNames").children) {
                        val documentNameUrl = documentName.value.toString()
                        documentsNames.add(documentNameUrl)
                    }
                    val emailDistributor = comanda.child("emailDistributor").value.toString()
                    val finalClientAddress = comanda.child("finalClientAddress").value.toString()
                    val finalClientName = comanda.child("finalClientName").value.toString()
                    val finalClientPhone = comanda.child("finalClientPhone").value.toString()
                    val isMesura: Boolean = comanda.child("isMesura").value.toString().toBoolean()
                    val nameDistributor = comanda.child("nameDistributor").value.toString()
                    val tecnicName = comanda.child("tecnicName").value.toString()



                    val serveiTecnicModel = ServeiTecnicModel(key, actionDate, albaraFile, albaraFileName, albaraNumber, albaraType, codeDistributor, currentDate, description, documents, documentsNames, emailDistributor, finalClientAddress, finalClientName, finalClientPhone, isMesura, nameDistributor, tecnicName, tecnicId, stateServei)
                    serveisList.add(serveiTecnicModel)
                }
            }
            val adapter = ServeiTecnicAdapter(serveisList)
            serveisRv.adapter = adapter
        }

    }
}