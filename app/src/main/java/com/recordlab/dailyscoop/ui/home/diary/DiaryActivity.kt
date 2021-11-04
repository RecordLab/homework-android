package com.recordlab.dailyscoop.ui.home.diary

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.recordlab.dailyscoop.databinding.ActivityDiaryBinding

class DiaryActivity: AppCompatActivity(){
    private lateinit var binding: ActivityDiaryBinding
    val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult -> binding.ivDiary.setImageURI(result.data?.data)
    } //ActivityResult에 콜백 등록 ( 결과를 얻어서 선택한 URI로 iv를 세팅한다.)
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        val view = binding.root
        val imageView = binding.ivDiary

        imageView.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = MediaStore.Images.Media.CONTENT_TYPE
                intent.action = Intent.ACTION_GET_CONTENT
                getContent.launch(intent)

            }

        })

        setContentView(view)

    }
}