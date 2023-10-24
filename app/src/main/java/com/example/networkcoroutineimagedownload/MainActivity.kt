package com.example.networkcoroutineimagedownload

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var urlEditText: EditText
    private lateinit var downloadButton: Button
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        urlEditText = findViewById(R.id.editTextText)
        downloadButton = findViewById(R.id.downloadButton)
        imageView = findViewById(R.id.imageView)

        downloadButton.setOnClickListener {
            val url = urlEditText.text.toString()
            GlobalScope.launch(Dispatchers.IO) {
                val bitmap = downloadImage(url)
                launch(Dispatchers.Main) {
                    imageView.setImageBitmap(bitmap)
                }
                launch(Dispatchers.IO) {
                    saveImage(bitmap)
                }
            }
        }

    }
    private fun downloadImage(url: String): Bitmap {
        val inputStream = URL(url).openStream()
        return BitmapFactory.decodeStream(inputStream)
    }
    private fun saveImage(bitmap: Bitmap) {
        val directory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File(directory, "image.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    }
}