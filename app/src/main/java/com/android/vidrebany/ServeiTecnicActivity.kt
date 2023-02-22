package com.android.vidrebany

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.adapters.TecnicAdapter
import com.android.vidrebany.models.TecnicModel
import com.google.firebase.database.FirebaseDatabase

class ServeiTecnicActivity : AppCompatActivity() {
    //create tecnicsList
    private val tecnicsList = ArrayList<TecnicModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servei_tecnic)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        toolbar.title = "Servei Tècnic"

        //get "tecnicUid" from shared preferences
        val sharedPreferences = getSharedPreferences("tecnicUid", MODE_PRIVATE)
        tecnicUid = sharedPreferences.getString("tecnicUid", "null").toString()

        val tecnicsRv: RecyclerView = findViewById(R.id.tecnicsRv) as RecyclerView;

        //poblate tecnicsRv with data from /tecnics/ in firebase database

        //set layout manager to tecnicsRv
        val layoutManager = LinearLayoutManager(this)
        tecnicsRv.layoutManager = layoutManager

        //query code:
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("tecnics")
        myRef.get().addOnSuccessListener {
            for (tecnic in it.children) {
                val id = tecnic.child("id").value.toString()
                val name = tecnic.child("name").value.toString()
                val tecnicModel = TecnicModel(id, name)
                tecnicsList.add(tecnicModel)
            }
            val adapter = TecnicAdapter(tecnicsList)
            tecnicsRv.adapter = adapter
        }

    }

    companion object {
        lateinit var tecnicUid: String
    }
}