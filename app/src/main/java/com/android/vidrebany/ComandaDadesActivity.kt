package com.android.vidrebany

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.vidrebany.models.ComandaModel

class ComandaDadesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comanda_dades)

        //get data from intent and put to ComandaModel
        val comanda = ComandaModel(
            intent.getStringExtra("address")!!,
            intent.getStringExtra("clientNum")!!,
            intent.getStringExtra("date")!!,
            intent.getStringExtra("firstTel")!!,
            intent.getStringExtra("secondTel")!!,
            intent.getStringExtra("id")!!,
            intent.getStringExtra("pdfUrl")!!,
            intent.getStringExtra("observations")!!,
            intent.getStringExtra("transporterId")!!,
            intent.getStringExtra("transName")!!,
            intent.getStringExtra("status")!!,
            intent.getStringExtra("time")!!
        )
    }
}