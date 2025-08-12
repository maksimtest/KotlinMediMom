package com.pilltracker.data

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.io.FileOutputStream

class ImageHolder(val context: Context) {

    private val _imageUri = MutableLiveData<String?>()
    val imageUri: LiveData<String?> get() = _imageUri

    fun init(){
        _imageUri.value = null
    }
    fun setImageUri(uri: Uri?) {
        if (uri == null) return
        val newUri = copyImageToInternalStorage(uri)
        Log.d("MyTag", "ImageHolder.setImageUri(), oldUri=$uri")
        Log.d("MyTag", "ImageHolder.setImageUri(), newUri=$newUri")
        if (newUri != null) _imageUri.value = newUri.toString()
    }

    fun copyImageToInternalStorage(uri: Uri): Uri? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "child_photo_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}