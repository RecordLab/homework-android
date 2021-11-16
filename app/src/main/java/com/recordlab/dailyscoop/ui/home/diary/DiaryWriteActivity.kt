package com.recordlab.dailyscoop.ui.home.diary

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.ImagePath
import com.recordlab.dailyscoop.databinding.ActivityDiaryWriteBinding
import com.recordlab.dailyscoop.network.RetrofitClient
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.network.request.RequestWriteDiary
import com.recordlab.dailyscoop.ui.diary.DiaryDetailActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.util.*
import kotlin.collections.set


class DiaryWriteActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDiaryWriteBinding
    private lateinit var backgroundLayout: CoordinatorLayout
    private lateinit var backgroundImage: ImageView
    private lateinit var contentText: EditText
    val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            binding.ivWriteDiary.setImageURI(result.data?.data)
            if (result.data == null) {
                val message = R.string.no_selected_image
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                binding.ivWriteDiary.visibility = ImageView.GONE
            } else {
                Glide.with(backgroundLayout).load(result.data?.data).into(binding.ivWriteDiary)
                val uri = result.data?.data
                val imgPath = uri?.let { ImagePath().getPath(applicationContext, it) }
//                Log.d(DW_DEBUG_TAG, "가져온 파일 경로 $imgPath")

                val file = File(imgPath)
//                Log.d(DW_DEBUG_TAG, "파일 : $file")

                val requestFile = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                service.requestImageUrl( file = body).enqueue(

                    onSuccess = {
                        when (it.code()) {
                            in 200..206 ->  {
                                imageUrl = it.body()?.data
                                Log.d(DW_DEBUG_TAG, "return image url -> ${imageUrl.toString()}")
                            }
                            in 400..499 -> {
                                Log.d(DW_DEBUG_TAG, "${it.code()} : ${it.message()}" )
                            }
                        }
                    }, onError = {
                        Log.d(DW_DEBUG_TAG, "통신 에러 발생~ ")
                    }, onFail = {
                        Log.d(DW_DEBUG_TAG, "에궁, 실패!")
                    }
                )


            }

        }
    private val DW_DEBUG_TAG = "DiaryWrite_DEBUG>>"

    private val service = RetrofitClient.service
    private val header = mutableMapOf<String, String?>()

    private lateinit var sharedPref: SharedPreferences

    var writeDate: String? = null
    var diaryContent: String? = null
    var emotions: List<String>? = null
    var theme: String? = "paper_white"
    private var imageUrl: String? = null
    var emotionCnt: Int = 0
    private lateinit var emotionType: List<RadioButton>

    private val content_permission_code = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (intent.hasExtra("date")) {
            writeDate = intent.getStringExtra("date")
        } else {
            writeDate = intent.getStringExtra("date")
        }
        Log.d("넘어온 작성 날짜", "$writeDate")
        sharedPref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        header["Content-type"] = "application/json"
        header["Authorization"] = sharedPref.getString("token", "token")
        Log.d(DW_DEBUG_TAG, "토큰 값 : ${header["Authorization"]}")
        val image = findViewById<ImageView>(R.id.iv_write_diary)
        val toolbar = binding.tbDiaryWrite.toolbar
        toolbar.background.alpha = 0
        // 토큰은 이걸로 저장해서 보내주면 됨.
        // val token = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)

        backgroundLayout = binding.clDiaryWrite
        backgroundImage = binding.ivBackground
        contentText = binding.etWriteDiary

        image.setOnClickListener(this)
        binding.chipPaperWhite.setOnClickListener(this)
        binding.chipPaperIvory.setOnClickListener(this)
        binding.chipPaperBlack.setOnClickListener(this)
        binding.chipWindow.setOnClickListener(this)
        binding.chipSkyDay.setOnClickListener(this)
        binding.chipSkyNight.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)

        emotionType = mutableListOf(
            binding.emotionAngry,
            binding.emotionRelax,
            binding.emotionFun,
            binding.emotionAnxious,
            binding.emotionJoy,
            binding.emotionSound,
            binding.emotionExcitement,
            binding.emotionBored,
            binding.emotionSad,
            binding.emotionTired,
            binding.emotionNervous,
            binding.emotionHappy
        )

        binding.emotionAngry.setOnClickListener {
            if( binding.emotionAngry.isSelected){
                emotionCnt--
                (!binding.emotionAngry.isSelected).also {
                    binding.emotionAngry.isSelected = it
                }
            }else {
                if(binding.emotionAngry.isEnabled && emotionCnt < 3){
                    emotionCnt++
                    (!binding.emotionAngry.isSelected).also {
                        binding.emotionAngry.isSelected = it
                    }
                } else {
                    emotionWarning()
                }
            }
            saveButtonCheck()
        }

        binding.emotionRelax.setOnClickListener {
            if (binding.emotionRelax.isSelected) { // 선택 해제.
                emotionCnt--
                (!binding.emotionRelax.isSelected).also { binding.emotionRelax.isSelected = it }
            } else { // 새로 선택하기.
                if (binding.emotionRelax.isEnabled && emotionCnt < 3) {
                    emotionCnt++
                    (!binding.emotionRelax.isSelected).also { binding.emotionRelax.isSelected = it }
                } else {
                    emotionWarning()
                }
            }

            saveButtonCheck()
        }

        binding.emotionFun.setOnClickListener {
            if (binding.emotionFun.isSelected) {
                emotionCnt--
                (!binding.emotionFun.isSelected).also { binding.emotionFun.isSelected = it }
            } else {
                if (emotionCnt < 3) {
                    emotionCnt++
                    (!binding.emotionFun.isSelected).also { binding.emotionFun.isSelected = it }
                } else {
                    emotionWarning()
                }
            }
            saveButtonCheck()
        }

        binding.emotionJoy.setOnClickListener {
            if (binding.emotionJoy.isSelected) {
                emotionCnt--
                (!binding.emotionJoy.isSelected).also { binding.emotionJoy.isSelected = it }
            } else {
                if (emotionCnt < 3) {
                    emotionCnt++
                    (!binding.emotionJoy.isSelected).also { binding.emotionJoy.isSelected = it }
                } else {
                    emotionWarning()
                }
            }
            saveButtonCheck()
        }

        binding.emotionSound.setOnClickListener {
            if (binding.emotionSound.isSelected) {
                emotionCnt--
                (!binding.emotionSound.isSelected).also { binding.emotionSound.isSelected = it }
            } else {
                if (emotionCnt < 3) {
                    emotionCnt++
                    (!binding.emotionSound.isSelected).also { binding.emotionSound.isSelected = it }
                } else {
                    emotionWarning()
                }
            }
            saveButtonCheck()
        }

        binding.emotionExcitement.setOnClickListener {
            if (binding.emotionExcitement.isSelected) {
                emotionCnt--
                (!binding.emotionExcitement.isSelected).also {
                    binding.emotionExcitement.isSelected = it
                }
            } else {
                if (emotionCnt < 3) {
                    emotionCnt++
                    (!binding.emotionExcitement.isSelected).also {
                        binding.emotionExcitement.isSelected = it
                    }
                } else {
                    emotionWarning()
                }
            }
            saveButtonCheck()
        }

        binding.emotionBored.setOnClickListener {
            if (binding.emotionBored.isSelected) {
                emotionCnt--
                (!binding.emotionBored.isSelected).also { binding.emotionBored.isSelected = it }
            } else {
                if (emotionCnt < 3) {
                    emotionCnt++
                    (!binding.emotionBored.isSelected).also { binding.emotionBored.isSelected = it }
                } else {
                    emotionWarning()
                }
            }
            saveButtonCheck()
        }

        binding.emotionSad.setOnClickListener {
            if (binding.emotionSad.isSelected) {
                emotionCnt--
                (!binding.emotionSad.isSelected).also { binding.emotionSad.isSelected = it }
            } else {
                if (emotionCnt < 3) {
                    emotionCnt++
                    (!binding.emotionSad.isSelected).also { binding.emotionSad.isSelected = it }
                } else {
                    emotionWarning()
                }
            }
            saveButtonCheck()
        }

        binding.emotionNervous.setOnClickListener {
            if (binding.emotionNervous.isSelected) {
                emotionCnt--
                (!binding.emotionNervous.isSelected).also { binding.emotionNervous.isSelected = it }
            } else {
                if (emotionCnt < 3) {
                    emotionCnt++
                    (!binding.emotionNervous.isSelected).also {
                        binding.emotionNervous.isSelected = it
                    }
                } else {
                    emotionWarning()
                }
            }
            saveButtonCheck()
        }

        binding.emotionTired.setOnClickListener {
            if (binding.emotionTired.isSelected) {
                emotionCnt--
                (!binding.emotionTired.isSelected).also { binding.emotionTired.isSelected = it }
            } else {
                if (emotionCnt < 3) {
                    emotionCnt++
                    (!binding.emotionTired.isSelected).also { binding.emotionTired.isSelected = it }
                } else {
                    emotionWarning()
                }
            }
            saveButtonCheck()
        }

        binding.emotionAnxious.setOnClickListener {
            if (binding.emotionAnxious.isSelected) {
                emotionCnt--
                (!binding.emotionAnxious.isSelected).also { binding.emotionAnxious.isSelected = it }
            } else {
                if (emotionCnt < 3) {
                    emotionCnt++
                    (!binding.emotionAnxious.isSelected).also {
                        binding.emotionAnxious.isSelected = it
                    }
                } else {
                    emotionWarning()
                }
            }
            saveButtonCheck()
        }

        binding.emotionHappy.setOnClickListener {
            if (binding.emotionHappy.isSelected) {
                emotionCnt--
                (!binding.emotionHappy.isSelected).also { binding.emotionHappy.isSelected = it }
            } else {
                if (emotionCnt < 3) {
                    emotionCnt++
                    (!binding.emotionHappy.isSelected).also { binding.emotionHappy.isSelected = it }
                } else {
                    emotionWarning()
                }
            }
            saveButtonCheck()
        }

        setSupportActionBar(toolbar)

        val getActionBar = supportActionBar
        if (getActionBar != null) {
            getActionBar.setDisplayShowCustomEnabled(true)  // custom하기 위해
            getActionBar.setDisplayShowTitleEnabled(false)
            getActionBar.setDisplayHomeAsUpEnabled(true)
            getActionBar.setDisplayShowHomeEnabled(true)
            getActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        //액티비티 시작 시 캘린더에서 클릭한 날짜로 넘겨받기.임의로 현재날짜 지정.

        binding.tbDiaryWrite.toolbarId.text = writeDate
        binding.tbDiaryWrite.toolbarId.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))

        binding.btnSave.isEnabled = false

        binding.ivButtonGallery.setOnClickListener(this)

        postDiary()

    }

    private fun saveButtonCheck() {
        binding.btnSave.isClickable = true
        if (emotionCnt > 0 && binding.etWriteDiary.length() > 0){
            // 작성 버튼 활성화하기.
            binding.btnSave.isEnabled = true

        } else {
            // 작성 버튼 비활성화
            binding.btnSave.isEnabled = false
        }
    }

    private fun activateEmotionButton( status :Boolean) {
        binding.emotionAngry.isActivated = status
        binding.emotionAnxious.isActivated = status
        binding.emotionBored.isActivated = status
        binding.emotionFun.isActivated = status
        binding.emotionHappy.isActivated = status
        binding.emotionJoy.isActivated = status
        binding.emotionSad.isActivated = status
        binding.emotionSad.isActivated = status
        binding.emotionSound.isActivated = status
        binding.emotionTired.isActivated = status
        binding.emotionRelax.isActivated = status
        binding.emotionNervous.isActivated = status
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_button_gallery -> {
                val readPermission = ContextCompat.checkSelfPermission(
                    this@DiaryWriteActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (readPermission == PackageManager.PERMISSION_GRANTED) { // 접근 권한 있는 경우,
                   selectPhoto()
                } else {
                    ActivityCompat.requestPermissions(
                        this@DiaryWriteActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        content_permission_code
                    )

                }
            }
            R.id.iv_write_diary -> {

            }
            R.id.chip_paper_white -> {
                Glide.with(backgroundLayout).load(R.drawable.theme_paper_white).into(backgroundImage)
                theme = "paper_white"
                setTextColor(0)
            }
            R.id.chip_paper_ivory -> {
                Glide.with(backgroundLayout).load(R.drawable.theme_paper_ivory).into(backgroundImage)
                theme = "paper_ivory"
                setTextColor(0)
            }
            R.id.chip_paper_black -> {
                Glide.with(backgroundLayout).load(R.drawable.theme_paper_dark).into(backgroundImage)
                theme = "paper_dark"
                setTextColor(1)
            }
            R.id.chip_window -> {
                Glide.with(backgroundLayout).load(R.drawable.theme_window).into(backgroundImage)
                setTextColor(1)
                theme = "window"
            }
            R.id.chip_sky_day -> {
                Glide.with(backgroundLayout).load(R.drawable.theme_sky_day_bright).into(backgroundImage)
                setTextColor(0)
                theme = "sky_day"
            }
            R.id.chip_sky_night -> {
                Glide.with(backgroundLayout).load(R.drawable.theme_sky_night).into(backgroundImage)
                setTextColor(1)
                theme = "sky_night"
            }
            R.id.btn_save -> {
                // 여기에서 통신 붙이기
                if (binding.btnSave.isEnabled) { // 이미지?, 텍스트 최소 한 글자, 감정 최소 한개, 테마 (기본 paper_white)
                    service.requestWriteDiary(
                        header = header,
                        diary = RequestWriteDiary(
                            content = binding.etWriteDiary.text.toString(),
                            image = imageUrl?: "default",
                            emotions = getEmotionAsList(),
                            theme = theme!!,
                            date = writeDate
                        )
                    ).enqueue(
                        onSuccess = {
                            when (it.code()) {
                                in 200..206 -> {
                                    Log.d(DW_DEBUG_TAG, "작성완료 ${it.code()}")
                                    // 작성 완료 되면 완료 액티비티로 보내기.
                                    val intent = Intent(this, DiaryDetailActivity::class.java)
                                    intent.putExtra("diaryDate", writeDate)
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(intent)
                                    finish()
                                }
                                401 -> {
                                    Log.d(DW_DEBUG_TAG, "권한 없음.")
                                }
                                in 400..499 -> {
                                    Log.d(DW_DEBUG_TAG, "요청 오류 : 코드 ${it.code()} 메시지 : ${it.message()}")
                                }
                                in 500.. 599 -> {
                                    Log.d(DW_DEBUG_TAG, "서버 오류")
                                }
                            }

                        }, onFail = {

                        }, onError = {

                        }
                    )

                } else {
                    if (emotionCnt > 0){
                        Toast.makeText(applicationContext, "내용을 작성해주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "감정을 선택해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // 권한 허용됐을 때
            content_permission_code -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectPhoto()
                } else {
                    val message = R.string.require_permission_message
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun selectPhoto(){
        val intent =
            Intent(Intent.ACTION_PICK) // Intent.ACTION_PICK 부터 CONTENT_TYPE, image/*까지 설정하면 갤러리 열림.
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
//                intent.action = Intent.ACTION_GET_CONTENT // 이 설정을 풀면 파일 폴더에서 사진 선택하는 방식.
        getContent.launch(intent)
        binding.ivWriteDiary.visibility = ImageView.VISIBLE
    }

    fun postDiary() {
        binding.etWriteDiary.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                saveButtonCheck()
            }
        })
    }

    fun emotionWarning() {
        val message = getString(R.string.toast_emotion_limit_3)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun getEmotionAsList(): List<String> {
        val selectedEmo = arrayListOf<String>()
        for (emo in emotionType) {
            if (emo.isSelected) {
                val st = StringTokenizer(emo.contentDescription.toString(), "_")
                Log.d(DW_DEBUG_TAG, "id >> ${emo.id.toString()}")
                st.nextToken()
                selectedEmo.add(st.nextToken())
            }
        }
        return selectedEmo
    }


    fun setTextColor(mode: Int) {
        when (mode) {
            0 -> {
                val darkColor: Int = Color.argb(0xCC, 0x30, 0x30, 0x30)
                colorMode(darkColor)
            }
            1 -> { // 밝은 텍스트
                val brightColor = Color.argb(0xCC, 0xDB, 0xDB, 0xDB)
                colorMode(brightColor)
            }
        }
    }

    fun colorMode(color: Int) {
        binding.tbDiaryWrite.toolbarId.setTextColor(color)
        contentText.setTextColor(color)
        binding.emotionAngry.setTextColor(color)
        binding.emotionAnxious.setTextColor(color)
        binding.emotionBored.setTextColor(color)
        binding.emotionExcitement.setTextColor(color)
        binding.emotionFun.setTextColor(color)
        binding.emotionSad.setTextColor(color)
        binding.emotionHappy.setTextColor(color)
        binding.emotionJoy.setTextColor(color)
        binding.emotionTired.setTextColor(color)
        binding.emotionSad.setTextColor(color)
        binding.emotionSound.setTextColor(color)
        binding.emotionRelax.setTextColor(color)
        binding.emotionNervous.setTextColor(color)
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_save, menu)
        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}