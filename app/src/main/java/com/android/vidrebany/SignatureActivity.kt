package com.android.vidrebany


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import com.google.firebase.database.FirebaseDatabase
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.*
import se.warting.signatureview.views.SignaturePad
import se.warting.signatureview.views.SignedListener
import java.io.*
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel


class SignatureActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION_CODE = 200
    private var pdfFile: File? = null
    private var modifiedPdfFile: File? = null
    private var cleanPdfFile: File? = null
    private var transparentSignatureBitmap: Bitmap? = null
    private var comanda: ComandaModel? = null
    //dni and nom variables CharSequence: can be null
    private var dni: CharSequence? = null
    private var nom: CharSequence? = null
    private var transSignBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)

        comanda = intent.getParcelableExtra("comanda")




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
                Toast.makeText(this, "No hi ha firma", Toast.LENGTH_SHORT).show()
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
            cleanPdfFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() +"/"+comanda?.albaraNum + "net.pdf")


            if (!checkPermissionForReadWriteExtertalStorage()) {
                requestPermissionForReadWriteExtertalStorage()
            } else {
                try {
                    createPdfFile(transSignBitmap!!)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()

                    val parentDirectory = pdfFile?.parentFile


                    if (!pdfFile?.exists()!!) {
                        parentDirectory?.mkdirs()
                        pdfFile?.createNewFile()
                    }

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


        val fos = FileOutputStream(pdfFile)


        fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
        fos.close()


        rbc.close()
        //insert signature to pdfFile using itext

        val pdfReader = PdfReader(pdfFile!!.absolutePath)

        println("c")
        //pdfreader not opened with owner password, to solve it:
        PdfReader.unethicalreading = true
        val pdfStamper = PdfStamper(pdfReader, FileOutputStream(modifiedPdfFile))

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
        val dniText: String = "Identificació: " + dni.toString();
// put the alignment and coordinates here
        canvas?.showTextAligned(1, dniText, 90f, 90f, 0f)
        canvas?.endText()
        canvas?.beginText()
        val nomText: String = "Nom i cognoms: " + nom.toString();
        canvas?.showTextAligned(1, nomText, 100f, 80f, 0f)
        canvas?.endText()







/*
        signature.setAbsolutePosition(0F, 0F)
        canvas?.addImage(signature)
*/
        pdfStamper.close()

        pdfReader.close()



        //if there is a second page on pdf, remove it
        val numberOfPages: Int = pdfReader.numberOfPages
        if (numberOfPages > 1) {
            val document = Document()
            val r = PdfReader(modifiedPdfFile!!.absolutePath)

            val writer = PdfCopy(document, FileOutputStream(cleanPdfFile))
            document.open()

            for (i in 1..numberOfPages) {
                if (i > 1) {
                    continue
                }
                val page = writer.getImportedPage(r, i)
                writer.addPage(page)
            }

            document.close()
            writer.close()
            r.close()

        }



        Toast.makeText(this, "PDF guardat a " + cleanPdfFile!!.absolutePath, Toast.LENGTH_SHORT).show()

        // Use the FileProvider to generate a content URI for the file
        val modifiedPdfUri = FileProvider.getUriForFile(
            this,
            "com.android.vidrebany.fileprovider",
            cleanPdfFile!!
        )





        //set comanda status to "finalizada" inside firebase realtime database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("transport").child(comanda?.id.toString()).child("status")
        myRef.setValue("finalizada")






        //delete pdfFile from Documents folder
        pdfFile?.delete()


        //go to ComandesTransporterActivity
        val comandesIntent = Intent(this, ComandesTransporterActivity::class.java)
        comandesIntent.putExtra("hasPdf", true)
        comandesIntent.putExtra("modifiedPdfUri", modifiedPdfUri)
        comandesIntent.putExtra("comanda", comanda)
        startActivity(comandesIntent)

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