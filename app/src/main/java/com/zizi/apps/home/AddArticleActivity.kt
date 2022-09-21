package com.zizi.apps.home

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.zizi.apps.DBKey.Companion.DB_ARTICLES
import com.zizi.apps.R

class AddArticleActivity: AppCompatActivity() {

    private var selectedUri: Uri? =null
    private val auth: FirebaseAuth by lazy{
        Firebase.auth
    }

    private val storage: FirebaseStorage by lazy{
        Firebase.storage
    }

    private val articleDB: DatabaseReference by lazy{
        Firebase.database.reference.child(DB_ARTICLES)
    }

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        findViewById<Button>(R.id.imageAddButton).setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED ->{
                    startContentProvider()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) ->{
                    showPermissionContextPopup()
                }
                else -> {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)

                }
            }

        }

        findViewById<Button>(R.id.submitButton).setOnClickListener {
            val title = findViewById<EditText>(R.id.titleEditText).text.toString()
            val passage = findViewById<EditText>(R.id.passageEditText).text.toString()
            val Id = auth.currentUser?.uid.orEmpty()


            if (selectedUri != null){
                val photoUri = selectedUri ?: return@setOnClickListener
                uploadPhoto(photoUri,
                    successHandler = { uri ->
                        upload(Id,title,passage,uri)
                    },
                    errorHandler = {
                        Toast.makeText(this,"사진 업로드를 실패했습니다",Toast.LENGTH_SHORT).show()
                    }

                )
            } else{
                upload(Id,title,passage,"")
            }


        }
    }

    private fun uploadPhoto(uri: Uri,successHandler: (String)->Unit, errorHandler:() -> Unit){
        val fileName = "${System.currentTimeMillis()}.png"
        storage.reference.child("article/photo").child(fileName)
            .putFile(uri)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    storage.reference.child("article/photo").child(fileName)
                        .downloadUrl
                        .addOnSuccessListener {
                            uri ->
                            successHandler(uri.toString())
                        }.addOnFailureListener{
                            errorHandler()
                        }
                } else{
                    errorHandler()
                }
            }
    }

    private fun upload(Id:String,title:String,passage:String,imageUrl:String){
        val model = ArticleModel(Id,title,System.currentTimeMillis(),passage,imageUrl)
        articleDB.push().setValue(model)
        finish()
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode){
            1000 ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startContentProvider()
                } else{
                    Toast.makeText(this, "권한을 거부하셨습니다.",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun startContentProvider(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,2020)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK){
            return
        }

        when (requestCode) {
            2020 -> {
                val uri = data?.data
                if (uri != null){
                    findViewById<ImageView>(R.id.photoImageView).setImageURI(uri)
                    selectedUri = uri

                }else{
                    Toast.makeText(this,"사진을 가져오지 못했습니다.",Toast.LENGTH_SHORT).show()

                }
            }
            else -> {
                Toast.makeText(this,"사진을 가져오지 못했습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionContextPopup(){
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의"){_,_->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1010)
            }
            .create()
            .show()


    }
}