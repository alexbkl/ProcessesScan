package com.android.vidrebany

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.adapters.DocumentsAdapter
import com.android.vidrebany.models.ServeiTecnicModel
import java.text.SimpleDateFormat
import java.util.*

class ServeiTecnicDadesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comanda_tecnic_dades)

        val serveiTecnic = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("serveiTecnic", ServeiTecnicModel::class.java)
        } else {
            intent.getParcelableExtra("serveiTecnic")
        }

        val documentsNames = serveiTecnic?.documentsNames
        val documentsUrls = serveiTecnic?.documents

        val typeTv = findViewById<TextView>(R.id.typeTv)
        val creationDateTv = findViewById<TextView>(R.id.creationDateTv)
        val actionDateTv = findViewById<TextView>(R.id.actionDateTv)
        val distributorCodeTv = findViewById<TextView>(R.id.distributorCodeTv)
        val distributorNameTv = findViewById<TextView>(R.id.distributorNameTv)
        val distributorEmailTv = findViewById<TextView>(R.id.distributorEmailTv)
        val finalClientNameTv = findViewById<TextView>(R.id.finalClientNameTv)
        val finalClientPhoneTv = findViewById<TextView>(R.id.finalClientPhoneTv)
        val finalClientAdress = findViewById<TextView>(R.id.finalClientAdress)
        val albaraTypeTv = findViewById<TextView>(R.id.albaraTypeTv)
        val albaraNumberTv = findViewById<TextView>(R.id.albaraNumberTv)
        val descriptionTv = findViewById<TextView>(R.id.descriptionTv)
        val albaraDocumentTv = findViewById<TextView>(R.id.albaraDocumentTv)
        val documentsRv = findViewById<RecyclerView>(R.id.documentsRv)
        val revisionLt = findViewById<LinearLayout>(R.id.revisionLt)
        val revisionTv = findViewById<TextView>(R.id.revisionTv)
        val revisionDateTv = findViewById<TextView>(R.id.revisionDateTv)
        val confirmBtn = findViewById<TextView>(R.id.confirmBtn)

        typeTv.text = if (serveiTecnic?.isMesura == true) "Mesura" else "Instal·lació"

        //serveiTecnic?.currentDate is a timestamp, so we need to convert it to a date
        creationDateTv.text = serveiTecnic?.currentDate?.let { convertTimestampToDateTime(it) }
        actionDateTv.text = serveiTecnic?.actionDate?.let { convertTimestampToDateTime(it) }

        distributorCodeTv.text = serveiTecnic?.codeDistributor
        distributorNameTv.text = serveiTecnic?.nameDistributor
        distributorEmailTv.text = serveiTecnic?.emailDistributor
        finalClientNameTv.text = serveiTecnic?.finalClientName
        finalClientPhoneTv.text = serveiTecnic?.finalClientPhone
        finalClientAdress.text = serveiTecnic?.finalClientAddress
        albaraTypeTv.text = serveiTecnic?.albaraType
        albaraNumberTv.text = serveiTecnic?.albaraNumber
        descriptionTv.text = serveiTecnic?.description
        albaraDocumentTv.text = serveiTecnic?.albaraFileName

        //albaraDocumentTv on click, opens the file in the url of albaraFile
        albaraDocumentTv.setOnClickListener {
            val intent = Intent(this, PDFViewer::class.java)
            intent.putExtra("url", serveiTecnic?.albaraFile)
            startActivity(intent)
        }

        //populate documentsRv with documentsNames and documentsUrls
        val layoutManager = LinearLayoutManager(this)
        documentsRv.layoutManager = layoutManager

        val adapter = DocumentsAdapter(this, documentsNames, documentsUrls)
        documentsRv.adapter = adapter

        if (serveiTecnic?.revision != null && serveiTecnic.revision != "") {
            revisionLt.visibility = LinearLayout.VISIBLE
        } else {
            revisionLt.visibility = LinearLayout.GONE
        }
        revisionTv.text = serveiTecnic?.revision
        revisionDateTv.text = serveiTecnic?.revisionDate?.let { convertTimestampToDateTime(it) }

        //confirmBtn on click, sends the serveiTecnic to confirmarServeiTecnicActivity
        confirmBtn.setOnClickListener {
            //if isMesura is false:
            //if (!serveiTecnic?.isMesura!!) {
                val intent = Intent(this, ConfirmarServeiTecnicActivity::class.java)
                intent.putExtra("serveiTecnic", serveiTecnic)
                startActivity(intent)
            //} else {
            //    Toast.makeText(this, "Mesura encara no implementat", Toast.LENGTH_SHORT).show()
            //}
        }
    }

    fun convertTimestampToDateTime(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
}