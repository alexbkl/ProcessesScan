package com.android.vidrebany

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.vidrebany.adapters.ServeiTecnicAdapter
import com.android.vidrebany.models.ServeiTecnicModel
import com.android.vidrebany.utils.FileUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.DateFormat
import java.text.SimpleDateFormat

class ComandesTecnicActivity : AppCompatActivity() {
    //create comandesList
    private val serveisList = ArrayList<ServeiTecnicModel>()
    private var documentsTecnicList = ArrayList<String>()
    private var documentsTecnicNamesList = ArrayList<String>()
    private var tecnicUid: String = ""
    private var database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comandes_tecnic)

        val sharedPreferences = getSharedPreferences("tecnicUid", MODE_PRIVATE)
        tecnicUid = sharedPreferences.getString("tecnicUid", "null").toString()

        val intent = intent
        val hasSignature: Boolean = intent.getBooleanExtra("hasSignature", false)

        if (hasSignature) {
            //get mail from mails/destination/mail in firebase database
            val myRef = database.getReference("mails/destination/mail")
            myRef.get().addOnSuccessListener {
                val mail = it.value.toString()


                val signatureUri: Uri? = intent.getParcelableExtra("signatureUri")
                val serveiTecnic = intent.getParcelableExtra<ServeiTecnicModel>("serveiTecnic")
                //get imageUris (ArrayList<Uri>)
                val imageUris = intent.getParcelableArrayListExtra<Uri>("imageUris")

                val arrayImagesUris: ArrayList<Uri> = ArrayList()

                arrayImagesUris.add(signatureUri!!)
                //imageUris to arrayImagesUris
                if (imageUris != null) {
                    for (imageUri in imageUris) {
                        arrayImagesUris.add(imageUri)
                    }
                }
                //send signature to email
                val intent2 = Intent(Intent.ACTION_SEND_MULTIPLE)

                val currentDate = serveiTecnic?.currentDate
                val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yy-HH:mm")
                val currentDateStr: String = dateFormat.format(currentDate)

                val actionDate = serveiTecnic?.actionDate
                val actionDateStr: String = dateFormat.format(actionDate)


                val extraText = "Núm. albarà: " + serveiTecnic!!.albaraNumber +
                        "\nData de creació: " + currentDateStr +
                        "\nData d'acció: " + actionDateStr +
                        "\nObservacions: " + serveiTecnic.description +
                        "\nTècnic: " + serveiTecnic.tecnicName +
                        "\nComentaris tècnic: " + serveiTecnic.comentarisTecnic

                intent2.type = "image/*"
                intent2.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
                intent2.putExtra(Intent.EXTRA_SUBJECT, "Servei tècnic Albarà-" + serveiTecnic.albaraNumber)
                intent2.putExtra(Intent.EXTRA_TEXT, extraText)
                //intent2.putExtra(Intent.EXTRA_STREAM, signatureUri)
                intent2.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayImagesUris)



                startActivity(Intent.createChooser(intent2, "Send Email"))

                //set comanda status to "finalizada" inside firebase realtime database
                val database = FirebaseDatabase.getInstance()
                val stateRef = database.getReference("serveiTecnic").child(serveiTecnic.key).child("stateServei")
                stateRef.setValue("Per revisar")

                //get documentsTecnicList and documentsTecnicNamesList from firebase realtime database
                val serveiTecnicRef = database.getReference("serveiTecnic").child(serveiTecnic.key)
                serveiTecnicRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.child("documentsTecnic").exists() || !snapshot.child("documentsTecnicNames")
                                .exists()
                        ) return
                        val documentsTecnic = snapshot.child("documentsTecnic").value as ArrayList<*>
                        val documentsTecnicNames = snapshot.child("documentsTecnicNames").value as ArrayList<*>
                        if (documentsTecnic.size > 0 && documentsTecnicNames.size > 0) {
                            for (documentTecnic in documentsTecnic) {
                                Toast.makeText(applicationContext, documentTecnic.toString(), Toast.LENGTH_SHORT).show()
                                documentsTecnicList.add(documentTecnic.toString())
                            }
                            for (documentTecnicName in documentsTecnicNames) {
                                documentsTecnicNamesList.add(documentTecnicName.toString())
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, "Error al carregar els documents", Toast.LENGTH_SHORT).show()
                    }

                })

                val commentRef = database.getReference("serveiTecnic").child(serveiTecnic.key).child("comentarisTecnic")
                commentRef.setValue(serveiTecnic.comentarisTecnic)

                //upload images to firebase storage
                val storageRef = Firebase.storage.reference

                val fileUtils = FileUtils()

                if (imageUris != null) {
                    for (imageUri in imageUris) {
                        val sd = fileUtils.getFileName(applicationContext, imageUri!!) ?: return@addOnSuccessListener
                        val uploadTask =
                            storageRef.child("serveiTecnic/" + serveiTecnic.key + "/" + sd).putFile(imageUri)
                        uploadTask.addOnFailureListener {
                            // Handle unsuccessful uploads
                            Toast.makeText(applicationContext, "Error al pujar la imatge", Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener { taskSnapshot ->
                            val imageRef = taskSnapshot.storage
                            imageRef.downloadUrl.addOnSuccessListener {
                                val downloadUrl = it.toString()
                                //add downloadUrl to documentsTecnicList
                                documentsTecnicList.add(downloadUrl)
                                documentsTecnicNamesList.add(sd)

                                //update documentsTecnicList and documentsTecnicNamesList in firebase realtime database
                                val documentsTecnicRef =
                                    database.getReference("serveiTecnic").child(serveiTecnic.key)
                                        .child("documentsTecnic")
                                documentsTecnicRef.setValue(documentsTecnicList)
                                val documentsTecnicNamesRef =
                                    database.getReference("serveiTecnic").child(serveiTecnic.key)
                                        .child("documentsTecnicNames")
                                documentsTecnicNamesRef.setValue(documentsTecnicNamesList)
                            }


                        }

                    }

                }
            }
            //https://stackoverflow.com/questions/69002142/adding-multiple-images-as-an-email-attachment
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        toolbar.title = "Tècnic VidreBany"

        val serveisRv: RecyclerView = findViewById(R.id.serveisRv);

        //poblate transportersRv with data from /transporters/ in firebase database

        //set layout manager to transportersRv
        val layoutManager = LinearLayoutManager(this)
        serveisRv.layoutManager = layoutManager


        //query code:
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("serveiTecnic")
        //if tecnicId is same as tecnicUid, add to serveisList

        myRef.get().addOnSuccessListener {
            for (comanda in it.children) {
                val tecnicId = comanda.child("tecnicId").value.toString()
                val stateServei = comanda.child("stateServei").value.toString()
                if (tecnicId == tecnicUid && (stateServei == "Pendent" || stateServei == "Per revisar")) {
                    val key = comanda.child("key").value.toString()
                    //get actionDate number from firebase
                    val actionDate = comanda.child("actionDate").value.toString().toLong()
                    val albaraFile = comanda.child("albaraFile").value.toString()
                    val albaraFileName = comanda.child("albaraFileName").value.toString()
                    val albaraNumber = comanda.child("albaraNumber").value.toString()
                    val albaraType = comanda.child("albaraType").value.toString()
                    val codeDistributor = comanda.child("codeDistributor").value.toString()
                    val currentDate = comanda.child("currentDate").value.toString().toLong()
                    val description = comanda.child("description").value.toString()
                    //inside comanda.child("documents") there are many documents urls, so we need to iterate
                    val documents = ArrayList<String>()
                    for (document in comanda.child("documents").children) {
                        val documentUrl = document.value.toString()
                        documents.add(documentUrl)
                    }
                    //inside comanda.child("documentsNames") there are many documents names, so we need to iterate
                    val documentsNames = ArrayList<String>()
                    for (documentName in comanda.child("documentsNames").children) {
                        val documentNameUrl = documentName.value.toString()
                        documentsNames.add(documentNameUrl)
                    }
                    val emailDistributor = comanda.child("emailDistributor").value.toString()
                    val finalClientAddress = comanda.child("finalClientAddress").value.toString()
                    val finalClientName = comanda.child("finalClientName").value.toString()
                    val finalClientPhone = comanda.child("finalClientPhone").value.toString()
                    val isMesura: Boolean = comanda.child("isMesura").value.toString().toBoolean()
                    val nameDistributor = comanda.child("nameDistributor").value.toString()
                    val tecnicName = comanda.child("tecnicName").value.toString()
                    val comentarisTecnic = comanda.child("comentarisTecnic").value.toString()
                    val revision = comanda.child("revision").value.toString()
                    val revisionDate = comanda.child("revisionDate").value.toString().toLong()
                    val serveiTecnicModel = ServeiTecnicModel(
                        key,
                        actionDate,
                        albaraFile,
                        albaraFileName,
                        albaraNumber,
                        albaraType,
                        codeDistributor,
                        currentDate,
                        description,
                        documents,
                        documentsNames,
                        emailDistributor,
                        finalClientAddress,
                        finalClientName,
                        finalClientPhone,
                        isMesura,
                        nameDistributor,
                        tecnicName,
                        tecnicId,
                        comentarisTecnic,
                        revision,
                        revisionDate,
                        stateServei
                    )
                    serveisList.add(serveiTecnicModel)
                }
            }
            val adapter = ServeiTecnicAdapter(serveisList)
            serveisRv.adapter = adapter
        }

    }


}