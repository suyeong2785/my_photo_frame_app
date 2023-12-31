package com.example.chapter8

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.chapter8.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val imageLoadLauncher = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()) {
            uriList ->
        updateImages(uriList)
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadImageButton.setOnClickListener {
            checkPermission()
        }
    }
    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadImage()
            }
            shouldShowRequestPermissionRationale(
                Manifest.permission.READ_MEDIA_IMAGES
            ) -> {
                showPermissionInfoDialog()
            }
            else -> {
                requestReadExternalStorage()
            }
        }


    }

    private fun showPermissionInfoDialog(){
        AlertDialog.Builder(this).apply {
            setMessage("이미지를 가져오기 위해서, 외부 저장소 읽기 권한이 필요합니다.")
            setNegativeButton("취소", null)
            setPositiveButton("동의"){_, _ -> requestReadExternalStorage()}
        }.show()
    }

    private fun requestReadExternalStorage(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
            READ_MEDIA_IMAGES)
    }

    private fun loadImage() {
        imageLoadLauncher.launch("image/*")
    }

    private fun updateImages(uriList: List<Uri>) {
        Log.d("updateImages", "$uriList")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            READ_MEDIA_IMAGES -> {
                if(grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                }
            }
        }
    }

    companion object {
        const val READ_MEDIA_IMAGES = 100
    }
}