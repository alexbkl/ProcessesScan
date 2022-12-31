package com.android.vidrebany

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.android.vidrebany.models.ComandaModel

class ComandaDadesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comanda_dades)

        //get data from intent
        val comanda = intent.getParcelableExtra<ComandaModel>("comanda")

        //init views
        val addressTv = findViewById<EditText>(R.id.etDireccio)
        val etTelOne = findViewById<EditText>(R.id.etTelOne)
        val etTelTwo = findViewById<EditText>(R.id.etTelTwo)
        val etObservations = findViewById<EditText>(R.id.etObservations)
        val btAcceptar = findViewById<Button>(R.id.btAcceptar)

        //set data to views
        addressTv.setText(comanda?.address)
        etTelOne.setText(comanda?.firstTel)
        etTelTwo.setText(comanda?.secondTel)
        etObservations.setText(comanda?.observations)


        btAcceptar.setOnClickListener {
            //go to SignatureActivity
            val intent = Intent(this, SignatureActivity::class.java)
            intent.putExtra("comanda", comanda)
            startActivity(intent)
        }
    }
}