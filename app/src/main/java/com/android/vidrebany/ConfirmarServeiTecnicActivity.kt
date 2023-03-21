package com.android.vidrebany

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
    private var dniEt: EditText? = null
    private var nomEt: EditText? = null
    private var nom: String? = null
    private var dni: String? = null
    private var comentaris: String? = null
    private var imageUris: ArrayList<Uri> = ArrayList()
    private var cameraImageUri: Uri? = null
    private var CAMERA_REQUEST_CODE: Int = 100
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var cameraResultLauncher: ActivityResultLauncher<Intent>? = null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmar_servei_tecnic)
        val comentarisEt: EditText? = findViewById(R.id.etComment)

        serveiTecnic = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("serveiTecnic", ServeiTecnicModel::class.java)
        } else {
            intent.getParcelableExtra("serveiTecnic")
        }


        val signaturePad = findViewById<SignaturePad>(R.id.signature_pad)
        val selectImages = findViewById<Button>(R.id.selectImages)
        val selectedImagesTv = findViewById<TextView>(R.id.selectedImagesTv)

        dniEt = findViewById(R.id.etDni)
        nomEt = findViewById(R.id.etNom)

        comentarisEt?.setText(serveiTecnic?.comentarisTecnic)


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
            dni = dniEt?.text.toString()
            nom = nomEt?.text.toString()
            comentaris = comentarisEt?.text.toString()
            serveiTecnic?.comentarisTecnic = comentaris as String

            transparentSignatureBitmap = signaturePad.getSignatureBitmap()
            if (!checkPermissionForReadWriteExtertalStorage()) {
                requestPermissionForReadWriteExtertalStorage()
            } else {
                if (dni != "" && nom != "") {
                    createSignatureContent()
                } else {
                    Toast.makeText(this, "Has d'introduir el DNI i el nom.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                    if (data.clipData != null) {
                        val count: Int = data.clipData!!.itemCount
                        for (i in 0 until count) {
                            val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                            imageUris.add(imageUri)
                            //set number of imageUris
                            val imagesText = imageUris.size.toString() + " imatges seleccionades"
                            selectedImagesTv.text = imagesText

                        }
                    } else if (data.data != null) {
                        //Toast type of data
                        val imageUri: Uri = data.data!!
                        imageUris.add(imageUri)
                        val imagesText = imageUris.size.toString() + " imatges seleccionades"
                        selectedImagesTv.text = imagesText
                    }
                }


                //Toast the names of the selected images
                Toast.makeText(this, "Imatges sel·leccionades: " + imageUris.size, Toast.LENGTH_SHORT).show()
            }
        }

        cameraResultLauncher = registerForActivityResult(
            StartActivityForResult()
        ) { result ->
            if (result.getResultCode().equals(RESULT_OK)) {
                // There are no request codes
                imageUris.add(cameraImageUri!!)
                val imagesText = imageUris.size.toString() + " imatges seleccionades"
                selectedImagesTv.text = imagesText
            }
        }

        selectImages.setOnClickListener {
            //create a dialog to choose if pick from camera or gallery
            AlertDialog.Builder(this)
                .setTitle("Selecciona una opció")
                .setPositiveButton("Galeria") { dialog, which ->
                    //pick from gallery
                    pickFromGallery(resultLauncher!!)
                }
                .setNegativeButton("Càmera") { dialog, which ->
                    //pick from camera
                    pickFromCamera()
                }
                .show()
/*
            // initialising intent
            val intent = Intent()

            // setting type to select to be image
            intent.type = "image/ *"


            // allowing multiple image to be selected
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            resultLauncher.launch(intent)*/
        }



    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun pickFromCamera() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {

                // You can use the API that requires the permission.
                val contentValues = ContentValues()
                contentValues.put(MediaStore.Images.Media.TITLE, "Imatge Tècnic Transportista" + serveiTecnic!!.albaraNumber)
                contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Imatge Tècnic Revisió")
                cameraImageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!


                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)

                cameraResultLauncher?.launch(intent)
            }
            else -> {
                // You can directly ask for the permission.
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            }
        }




    }

    private fun pickFromGallery(resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)

    }

    private fun createSignatureContent() {
        // Create the file directory
        val signatureDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/firmes"
        )
        signatureDir.mkdirs()
        val signFileName: String = "firma_"+serveiTecnic!!.albaraNumber + "_" + dni + "_" + nom + ".jpg"
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

        // Use the FileProvider to generate a content URI for the file
        signatureUri = FileProvider.getUriForFile(
            this,
            "com.android.vidrebany.fileprovider",
            signFile
        )



        //go to ComandesTransporterActivity
        val comandesIntentTecnic = Intent(this, ComandesTecnicActivity::class.java)
        comandesIntentTecnic.putExtra("hasSignature", true)
        comandesIntentTecnic.putExtra("signatureUri", signatureUri)
        comandesIntentTecnic.putExtra("serveiTecnic", serveiTecnic)
        comandesIntentTecnic.putParcelableArrayListExtra("imageUris", imageUris)
        if (signatureUri != null) {
            startActivity(comandesIntentTecnic)
        } else {
            Toast.makeText(this, "Error al guardar la firma", Toast.LENGTH_SHORT).show()
        }
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    try {
                        if (dni != null && nom != null) {
                            createSignatureContent()
                        } else {
                            Toast.makeText(this, "Has d'introduir el DNI i el nom.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: FileNotFoundException) {
                        Toast.makeText(this, "Error al guardar la firma: " + e, Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this, "Has de concedir el permís d'emmagatzemament.", Toast.LENGTH_SHORT).show()
                }
                return
            }

            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    pickFromCamera()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(this, "Has de concedir el permís de càmera.", Toast.LENGTH_SHORT).show()
                }
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}