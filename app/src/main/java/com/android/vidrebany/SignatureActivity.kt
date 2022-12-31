package com.android.vidrebany

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.vidrebany.models.ComandaModel
import se.warting.signatureview.utils.SignaturePadBindingAdapter
import se.warting.signatureview.views.SignaturePad
import se.warting.signatureview.views.SignedListener

class SignatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)

        val comanda = intent.getParcelableExtra<ComandaModel>("comanda")


        val signaturePad = findViewById<SignaturePad>(R.id.signature_pad)

        val btnSign = findViewById<Button>(R.id.btnSign)
        val btnClear = findViewById<Button>(R.id.btnClear)

        signaturePad.setOnSignedListener(object : SignedListener {
            override fun onStartSigning() {
                // Event triggered when the pad is touched
            }

            override fun onSigned() {
                // Event triggered when the pad is signed
                btnSign.isEnabled = true
                btnClear.isEnabled = true
            }

            override fun onClear() {
                // Event triggered when the pad is cleared
                btnSign.isEnabled = false
                btnClear.isEnabled = false
            }
        })

        btnClear.setOnClickListener {
            if (signaturePad.isEmpty) {
                Toast.makeText(this, "Please sign first", Toast.LENGTH_SHORT).show()
            } else {
                //save signature
                signaturePad.clear()
                //send signature to server
            }
        }

        btnSign.setOnClickListener {
            val transparentSignatureBitmap = signaturePad.getTransparentSignatureBitmap()
            Toast.makeText(this, "Signature saved"+transparentSignatureBitmap.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}