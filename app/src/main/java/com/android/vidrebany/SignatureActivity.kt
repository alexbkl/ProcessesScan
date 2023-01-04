package com.android.vidrebany


import android.R.string
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.vidrebany.models.ComandaModel
import com.android.vidrebany.utils.ImageUtils
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfStamper
import se.warting.signatureview.views.SignaturePad
import se.warting.signatureview.views.SignedListener
import java.io.*
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel


class SignatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)

        val comanda = intent.getParcelableExtra<ComandaModel>("comanda")




        val signaturePad = findViewById<SignaturePad>(R.id.signature_pad)
        val dni = findViewById<EditText>(R.id.etDni).text
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
            val transSignBitmap: Bitmap = signaturePad.getTransparentSignatureBitmap()

            //make transparentSignatureBitmap smaller to fit in pdf
            val transparentSignatureBitmap = ImageUtils().BITMAP_RESIZER(transSignBitmap, 100, 100)



            //ask for write and read files permissions to download from comanda?.pdfUrl

            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val url = URL(comanda?.pdfUrl)
            val rbc: ReadableByteChannel = Channels.newChannel(url.openStream())
            //pdfFile in Documents folder
            val pdfFile = File(Environment.getExternalStorageDirectory().toString() + "/Documents/faaarjomanda.pdf")

            val fos = FileOutputStream(pdfFile)
            fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
            fos.close()


            rbc.close()
            //insert signature to pdfFile using itext
            Toast.makeText(this, "PDF saved to " + pdfFile.absolutePath, Toast.LENGTH_SHORT).show()
            val pdfReader = PdfReader(pdfFile.absolutePath)
            //pdfreader not opened with owner password, to solve it:
            PdfReader.unethicalreading = true
            val modifiedPdfFile = File(Environment.getExternalStorageDirectory().toString() + "/Documents/faaajrodified.pdf")
            val pdfStamper = PdfStamper(pdfReader, FileOutputStream(modifiedPdfFile))

           //transform transparentSignatureBitmap to ByteArray!

            val stream = ByteArrayOutputStream()
            transparentSignatureBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            //get image from transparentSignatureBitmap
            val signature: Image = Image.getInstance(byteArray)



            val canvas: PdfContentByte? = pdfStamper.getOverContent(1)
            canvas?.addImage(signature, 100f, 0f, 0f, 100f, 100f, 100f)
            //put dni number on top of signature


// the pdf content

// the pdf content

// select the font properties

// select the font properties
            val bf: BaseFont =
                BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED)
            canvas?.setColorFill(BaseColor.DARK_GRAY)
            canvas?.setFontAndSize(bf, 8F)

// write the text in the pdf content

// write the text in the pdf content
            canvas?.beginText()
            var text: String = dni.toString()
// put the alignment and coordinates here
// put the alignment and coordinates here
            canvas?.showTextAligned(1, text, 100f, 100f, 0f)
            canvas?.endText()
            canvas?.beginText()
            text = "Other random blabla..."
// put the alignment and coordinates here
// put the alignment and coordinates here
            canvas?.showTextAligned(2, text, 100f, 110f, 0f)
            canvas?.endText()





            /*
            signature.setAbsolutePosition(0F, 0F)
            canvas?.addImage(signature)
*/
            pdfStamper.close()
            pdfReader.close()



            // Use the FileProvider to generate a content URI for the file
            val modifiedPdfUri = FileProvider.getUriForFile(
                this,
                "com.android.vidrebany.fileprovider",
                modifiedPdfFile
            )

            //send modified file to email
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/pdf"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("asederado@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Comanda")
            intent.putExtra(Intent.EXTRA_TEXT, "Comanda")
            intent.putExtra(Intent.EXTRA_STREAM, modifiedPdfUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)



            //pdfFile.deleteafterdasdasd()
            //modifiedPdfFile.delete()
            //delete tempa.pdf
            // resolver.delete(uri, null, null)

            //Toast the path of the file
            Toast.makeText(this, pdfFile.absolutePath.toString(), Toast.LENGTH_LONG).show()




        }
    }









}