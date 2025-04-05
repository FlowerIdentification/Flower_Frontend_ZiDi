package com.zidi.flowidentification_demo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class UploadActivity : AppCompatActivity() {

    private lateinit var btnSelectImage: Button
    private lateinit var tempPhotoUri: Uri

    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            previewAndNavigate(tempPhotoUri)
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            previewAndNavigate(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        btnSelectImage = findViewById(R.id.btn_select_image)

        btnSelectImage.setOnClickListener {
            Log.d("UPLOAD_DEBUG", "📌 弹窗打开")
            showImageSourceDialog()
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Image")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> takePhoto()
                1 -> chooseFromGallery()
            }
        }
        builder.show()
    }

    private fun takePhoto() {
        val photoFile = File.createTempFile("temp_photo_", ".jpg", cacheDir)
        tempPhotoUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", photoFile)
        takePhotoLauncher.launch(tempPhotoUri)
    }

    private fun chooseFromGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun previewAndNavigate(uri: Uri) {
        Toast.makeText(this, "跳转预览页", Toast.LENGTH_SHORT).show()
        Log.d("UPLOAD_DEBUG", "跳转 PreviewActivity: $uri")

        val intent = Intent(this, PreviewActivity::class.java)
        intent.putExtra("image_uri", uri.toString())
        startActivity(intent)
    }

}
