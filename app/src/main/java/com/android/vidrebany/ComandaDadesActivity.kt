package com.android.vidrebany

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

        //make textviews uneditable but selectable so that the user can copy the text
        addressTv.isFocusable = false
        addressTv.isFocusableInTouchMode = false
        addressTv.isClickable = true
        etTelOne.isFocusable = false
        etTelOne.isFocusableInTouchMode = false
        etTelOne.isClickable = true
        etTelTwo.isFocusable = false
        etTelTwo.isFocusableInTouchMode = false
        etTelTwo.isClickable = true
        etObservations.isFocusable = false
        etObservations.isFocusableInTouchMode = false
        etObservations.isClickable = true

        //textviews on click listener to copy text
        addressTv.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("address", addressTv.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Direcció copiada", Toast.LENGTH_SHORT).show()
        }
        etTelOne.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("telOne", etTelOne.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Telèfon copiat", Toast.LENGTH_SHORT).show()
        }
        etTelTwo.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("telTwo", etTelTwo.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Telèfon copiat", Toast.LENGTH_SHORT).show()
        }
        etObservations.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("observations", etObservations.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Observacions copiades", Toast.LENGTH_SHORT).show()
        }




        btAcceptar.setOnClickListener {
            //go to SignatureActivity
            val intent = Intent(this, SignatureActivity::class.java)
            intent.putExtra("comanda", comanda)
            startActivity(intent)
        }
    }
}