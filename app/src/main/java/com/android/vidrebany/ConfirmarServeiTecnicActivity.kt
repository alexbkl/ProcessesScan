package com.android.vidrebany

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.vidrebany.models.ServeiTecnicModel
import se.warting.signatureview.views.SignaturePad
import se.warting.signatureview.views.SignedListener
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class ConfirmarServeiTecnicActivity : AppCompatActivity() {
    private val REQUEST_PERMISSION_CODE = 200
    private var transparentSignatureBitmap: Bitmap? = null
    private var signatureUri: Uri? = null
    private var serveiTecnic: ServeiTecnicModel? = null
    private var dni: String? = null
    private var nom: String? = null
    private var imageUris: ArrayList<Uri> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmar_servei_tecnic)

        serveiTecnic = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("serveiTecnic", ServeiTecnicModel::class.java)
        } else {
            intent.getParcelableExtra("serveiTecnic")
        }


        val signaturePad = findViewById<SignaturePad>(R.id.signature_pad)
        dni = findViewById<EditText>(R.id.etDni).text.toString()
        nom = findViewById<EditText>(R.id.etNom).text.toString()
        val btnSign = findViewById<android.widget.Button>(R.id.btnSign)
        val btnClear = findViewById<android.widget.Button>(R.id.btnClear)

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
            transparentSignatureBitmap = signaturePad.getSignatureBitmap()
            if (!checkPermissionForReadWriteExtertalStorage()) {
                requestPermissionForReadWriteExtertalStorage()
            } else {
                createSignatureContent()
            }
        }


    }

    private fun createSignatureContent() {
        // Create the file directory
        val signatureDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/firmes"
        )
        signatureDir.mkdirs()
        val signFileName: String = serveiTecnic!!.key + "_" + dni + "_" + nom + ".jpg"
        val signFile = File(signatureDir, signFileName)

        try {
            val signFileCreated: Boolean = signFile.createNewFile()
            if (signFileCreated) {
                // write the bitmap to that file
                val outputStream = FileOutputStream(signFile)
                transparentSignatureBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            }
        } catch (ex: IOException) {
            Toast.makeText(this, "Error al guardar la firma: " + ex, Toast.LENGTH_SHORT).show()
        }
        signatureUri = Uri.fromFile(signFile)

        //go to ComandesTransporterActivity
        val comandesIntentTecnic = Intent(this, ComandesTecnicActivity::class.java)
        comandesIntentTecnic.putExtra("hasSignature", true)
        comandesIntentTecnic.putExtra("signatureUri", signatureUri)
        comandesIntentTecnic.putExtra("serveiTecnic", serveiTecnic)
        startActivity(comandesIntentTecnic)
    }

    private fun checkPermissionForReadWriteExtertalStorage(): Boolean {
        //check for read and write permissions
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionForReadWriteExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_CODE
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    try {
                        createSignatureContent()
                    } catch (e: FileNotFoundException) {
                        Toast.makeText(this, "Error al guardar la firma: " + e, Toast.LENGTH_SHORT).show()
                    }

                } else {
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