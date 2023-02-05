package com.android.vidrebany


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.android.vidrebany.models.ComandaModel
import com.android.vidrebany.utils.ImageUtils
import com.google.firebase.database.FirebaseDatabase
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

    val REQUEST_PERMISSION_CODE = 200
    var pdfFile: File? = null
    var modifiedPdfFile: File? = null
    var transparentSignatureBitmap: Bitmap? = null
    var comanda: ComandaModel? = null
    //dni and nom variables CharSequence: can be null
    var dni: CharSequence? = null
    var nom: CharSequence? = null
    var transSignBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)

        comanda = intent.getParcelableExtra<ComandaModel>("comanda")




        val signaturePad = findViewById<SignaturePad>(R.id.signature_pad)
        dni = findViewById<EditText>(R.id.etDni).text
        nom = findViewById<EditText>(R.id.etNom).text
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

            transSignBitmap = signaturePad.getTransparentSignatureBitmap()
            //pdfFile in Documents folder
            pdfFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() +"/"+comanda?.albaraNum + ".pdf")
            modifiedPdfFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() +"/"+comanda?.albaraNum + "firmat.pdf")


            if (!checkPermissionForReadWriteExtertalStorage()) {
                requestPermissionForReadWriteExtertalStorage()
                println("0")
            } else {
                try {
                    println("a")
                    createPdfFile(transSignBitmap!!)
                    println("1")
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    println("2")

                    val parentDirectory = pdfFile?.parentFile

                    println("e"+parentDirectory)

                    if (!pdfFile?.exists()!!) {
                        println("5")
                        parentDirectory?.mkdirs()
                        pdfFile?.createNewFile()
                        println("6")
                    } else {
                        println("d")

                    };

                    createPdfFile(transSignBitmap!!)
                }
            }


        }
    }

    private fun createPdfFile(transSignBitmap: Bitmap) {

        //if pdfFile and modifiedPdfFile are not null, delete them
        if (pdfFile != null) {
            pdfFile!!.delete()
        }
        if (modifiedPdfFile != null) {
            modifiedPdfFile!!.delete()
        }


        //make transparentSignatureBitmap smaller to fit in pdf
        //transparentSignatureBitmap = ImageUtils().BITMAP_RESIZER(transSignBitmap, 100, 100)
        transparentSignatureBitmap = transSignBitmap
        //transform transparentSignatureBitmap to ByteArray!
        val stream = ByteArrayOutputStream()
        transparentSignatureBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        //get image from transparentSignatureBitmap
        val signature: Image = Image.getInstance(byteArray)



        //ask for write and read files permissions to download from comanda?.pdfUrl

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val url = URL(comanda?.pdfUrl)
        val rbc: ReadableByteChannel = Channels.newChannel(url.openStream())


        println("3")
        println(pdfFile)
        val fos = FileOutputStream(pdfFile)
        println("4")


        fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
        fos.close()

        println("b")

        rbc.close()
        //insert signature to pdfFile using itext

        val pdfReader = PdfReader(pdfFile!!.absolutePath)
        println("c")
        //pdfreader not opened with owner password, to solve it:
        PdfReader.unethicalreading = true
        val pdfStamper = PdfStamper(pdfReader, FileOutputStream(modifiedPdfFile))
        //print pdfStamper
        println("PDF STAMPER: ")
        println(pdfStamper)




        println("g")

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
        println("h")

// write the text in the pdf content

// write the text in the pdf content
        canvas?.beginText()
        val dniText: String = "Identificació: " + dni.toString();
        println("dniText: "+dniText)
// put the alignment and coordinates here
        canvas?.showTextAligned(1, dniText, 90f, 90f, 0f)
        canvas?.endText()
        canvas?.beginText()
        println("i")
        val nomText: String = "Nom i cognoms: " + nom.toString();
        println("nomtext: "+nomText)
        canvas?.showTextAligned(1, nomText, 100f, 80f, 0f)
        canvas?.endText()


        println("j")



/*
        signature.setAbsolutePosition(0F, 0F)
        canvas?.addImage(signature)
*/
        pdfStamper.close()
        println("k")

        pdfReader.close()

        Toast.makeText(this, "PDF guardat a " + modifiedPdfFile!!.absolutePath, Toast.LENGTH_SHORT).show()

        println("l")
        // Use the FileProvider to generate a content URI for the file
        val modifiedPdfUri = FileProvider.getUriForFile(
            this,
            "com.android.vidrebany.fileprovider",
            modifiedPdfFile!!
        )

        println("m")





        //pdfFile.deleteafterdasdasd()
        //modifiedPdfFile.delete()
        //delete tempa.pdf
        // resolver.delete(uri, null, null)



        //set comanda status to "finalizada" inside firebase realtime database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("transport").child(comanda?.id.toString()).child("status")
        myRef.setValue("finalizada")



        println("n")



        //delete pdfFile from Documents folder
        pdfFile?.delete()


        //go to ComandesTransporterActivity
        val comandesIntent = Intent(this, ComandesTransporterActivity::class.java)
        comandesIntent.putExtra("hasPdf", true)
        comandesIntent.putExtra("modifiedPdfUri", modifiedPdfUri)
        comandesIntent.putExtra("comanda", comanda)
        startActivity(comandesIntent)
        println("o")

    }

    private fun requestPermissionForReadWriteExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun checkPermissionForReadWriteExtertalStorage(): Boolean {
        //check for read and write permissions
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.

                    println("requespermission")
                    try {
                        //create pdf file
                        transSignBitmap?.let { createPdfFile(it) }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()

                        val parentDirectory = pdfFile?.parentFile

                        if (!pdfFile?.exists()!!) {
                            parentDirectory?.mkdirs()
                            pdfFile?.createNewFile()
                        };

                        transSignBitmap?.let { createPdfFile(it) }
                    }

                } else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(this, "Has de concedir el permís d'emmagatzemament.", Toast.LENGTH_SHORT).show()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


}