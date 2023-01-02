package com.android.vidrebany


import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.vidrebany.models.ComandaModel
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfStamper
import se.warting.signatureview.views.SignaturePad
import se.warting.signatureview.views.SignedListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel


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
            val transparentSignatureBitmap: Bitmap = signaturePad.getTransparentSignatureBitmap()
            //ask for write and read files permissions to download from comanda?.pdfUrl

            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val url = URL(comanda?.pdfUrl)
            val rbc: ReadableByteChannel = Channels.newChannel(url.openStream())
            val pdfFile = File(Environment.getExternalStorageDirectory().toString() + "/Download/" + "beaba.pdf")
            val fos = FileOutputStream(pdfFile)
            fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
            fos.close()
            rbc.close()
            //insert signature to pdfFile using itext
            Toast.makeText(this, "PDF saved to " + pdfFile.absolutePath, Toast.LENGTH_SHORT).show()
            val pdfReader = PdfReader(pdfFile.absolutePath)
            //pdfreader not opened with owner password, to solve it:
            PdfReader.unethicalreading = true

            val modifiedPdfFile = File(Environment.getExternalStorageDirectory().toString() + "/Download/" + "modified.pdf")
            val pdfStamper = PdfStamper(pdfReader, FileOutputStream(modifiedPdfFile))
            //transform transparentSignatureBitmap to ByteArray!

            val stream = ByteArrayOutputStream()
            transparentSignatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            //get image from transparentSignatureBitmap
            val signature: Image = Image.getInstance(byteArray)
            val canvas: PdfContentByte = pdfStamper.getOverContent(1)
            signature.setAbsolutePosition(0F, 0F);
            canvas.addImage(signature)
            pdfStamper.close();
            pdfReader.close();
            //Toast the path of the file
            Toast.makeText(this, pdfFile.absolutePath, Toast.LENGTH_LONG).show()





        }
    }


}