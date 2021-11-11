package com.recordlab.dailyscoop.ui.home.diary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.core.view.get
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.TimeToString
import com.recordlab.dailyscoop.databinding.ActivityDiaryWriteBinding
import com.recordlab.dailyscoop.network.RetrofitClient
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.network.request.RequestWriteDiary
import java.util.*

class DiaryWriteActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDiaryWriteBinding
    private lateinit var backgroundLayout: CoordinatorLayout
    private lateinit var backgroundImage: ImageView
    private lateinit var contentText: EditText
    val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            binding.ivWriteDiary.setImageURI(result.data?.data)
            Glide.with(backgroundLayout).load(result.data?.data).into(binding.ivWriteDiary)
        }
    private val DW_DEBUG_TAG = "DiaryWrite_DEBUG>>"
//    private lateinit var upload: MenuItem

    private val service = RetrofitClient.service
    private val header = mutableMapOf<String, String?>()

    private lateinit var sharedPref: SharedPreferences

    var writeDate: String? = null
    var diaryContent: String? = null
    var emotions: List<String>? = null
    var theme: String? = "paper_white"
    var emotionCnt: Int = 0
    private lateinit var emotionType: List<RadioButton>

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
        sharedPref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        header["Content-type"] = "application/json; charset=UTF-8"
        header["Authorization"] = sharedPref.getString("token", "token")
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

        /*binding.emotionAngry.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.emotionAngry.isChecked = false
            } else {
                if (buttonView != null) {
                    if (buttonView.isChecked == true) {
                        Log.d(DW_DEBUG_TAG, "$emotionCnt")
                        emotionCnt++;
                    } else {
                        emotionCnt--;
                    }
                }
                if (emotionCnt == 3) {
                    deactivateEmotionButton()
                    emotionWarning()
                } else {
                    activateEmotionButton()
                }
            }
        }

        binding.emotionRelax.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Log.d(DW_DEBUG_TAG, "$emotionCnt")
                emotionCnt++;
            } else {
                emotionCnt--;
                binding.emotionRelax.isChecked = false
            }

                if (buttonView != null) {

                }
                if (emotionCnt == 3) {
                    deactivateEmotionButton()
                    emotionWarning()
                } else {
                    activateEmotionButton()
                }
        }*/

        binding.emotionAngry.setOnClickListener {
            if( binding.emotionAngry.isSelected){
                emotionCnt--;
                (!binding.emotionAngry.isSelected).also {
                    binding.emotionAngry.isSelected = it
                }
            }else {
                if(binding.emotionAngry.isEnabled && emotionCnt < 3){
                    emotionCnt++;
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
                emotionCnt--;
                (!binding.emotionRelax.isSelected).also { binding.emotionRelax.isSelected = it }
            } else { // 새로 선택하기.
                if (binding.emotionRelax.isEnabled && emotionCnt < 3) {
                    emotionCnt++;
                    (!binding.emotionRelax.isSelected).also { binding.emotionRelax.isSelected = it }
                } else {
                    emotionWarning()
                }
            }

            saveButtonCheck()
        }

        binding.emotionFun.setOnClickListener {
            if (binding.emotionFun.isSelected) {
                emotionCnt--;
                (!binding.emotionFun.isSelected).also { binding.emotionFun.isSelected = it }
            } else {
                if (emotionCnt < 3) {
                    emotionCnt++;
                    (!binding.emotionFun.isSelected).also { binding.emotionFun.isSelected = it }
                } else {
                    emotionWarning()
                }
            }
            saveButtonCheck()
        }

        binding.emotionJoy.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (binding.emotionJoy.isSelected) {
                    emotionCnt--
                    (!binding.emotionJoy.isSelected).also { binding.emotionJoy.isSelected = it }
                } else {
                    if (emotionCnt < 3){
                        emotionCnt++;
                        (!binding.emotionJoy.isSelected).also { binding.emotionJoy.isSelected = it }
                    }else {
                        emotionWarning()
                    }
                }

            }
        })

        binding.emotionSound.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (binding.emotionSound.isSelected) {
                    emotionCnt--
                    (!binding.emotionSound.isSelected).also { binding.emotionSound.isSelected = it }
                } else {
                    if (emotionCnt < 3){
                        emotionCnt++
                        (!binding.emotionSound.isSelected).also { binding.emotionSound.isSelected = it }
                    }else {
                        emotionWarning()
                    }
                }
            }
        })

        binding.emotionExcitement.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (binding.emotionExcitement.isSelected) {
                    emotionCnt--
                    (!binding.emotionExcitement.isSelected).also { binding.emotionExcitement.isSelected = it }
                } else {
                    if (emotionCnt < 3){
                        emotionCnt++
                        (!binding.emotionExcitement.isSelected).also { binding.emotionExcitement.isSelected = it }
                    }else {
                        emotionWarning()
                    }
                }
            }
        })

        binding.emotionBored.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (binding.emotionBored.isSelected) {
                    emotionCnt--
                    (!binding.emotionBored.isSelected).also { binding.emotionBored.isSelected = it }
                } else {
                    if (emotionCnt < 3){
                        emotionCnt++
                        (!binding.emotionBored.isSelected).also { binding.emotionBored.isSelected = it }
                    }else {
                        emotionWarning()
                    }
                }

            }
        })

        binding.emotionSad.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (binding.emotionSad.isSelected) {
                    emotionCnt--
                    (!binding.emotionSad.isSelected).also { binding.emotionSad.isSelected = it }
                } else {
                    if (emotionCnt < 3){
                        emotionCnt++
                        (!binding.emotionSad.isSelected).also { binding.emotionSad.isSelected = it }
                    }else {
                        emotionWarning()
                    }
                }

            }
        })

        binding.emotionNervous.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (binding.emotionNervous.isSelected) {
                    emotionCnt--
                    (!binding.emotionNervous.isSelected).also { binding.emotionNervous.isSelected = it }
                } else {
                    if (emotionCnt < 3){
                        emotionCnt++
                        (!binding.emotionNervous.isSelected).also { binding.emotionNervous.isSelected = it }
                    }else {
                        emotionWarning()
                    }
                }

            }
        })

        binding.emotionTired.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (binding.emotionTired.isSelected) {
                    emotionCnt--
                    (!binding.emotionTired.isSelected).also { binding.emotionTired.isSelected = it }
                } else {
                    if (emotionCnt < 3){
                        emotionCnt++
                        (!binding.emotionTired.isSelected).also { binding.emotionTired.isSelected = it }
                    }else {
                        emotionWarning()
                    }
                }
            }
        })

        binding.emotionAnxious.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (binding.emotionAnxious.isSelected) {
                    emotionCnt--
                    (!binding.emotionAnxious.isSelected).also { binding.emotionAnxious.isSelected = it }
                } else {
                    if (emotionCnt < 3){
                        emotionCnt++
                        (!binding.emotionAnxious.isSelected).also { binding.emotionAnxious.isSelected = it }
                    }else {
                        emotionWarning()
                    }
                }

            }
        })

        binding.emotionHappy.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (binding.emotionHappy.isSelected) {
                    emotionCnt--
                    (!binding.emotionHappy.isSelected).also { binding.emotionHappy.isSelected = it }
                } else {
                    if (emotionCnt < 3){
                        emotionCnt++
                        (!binding.emotionHappy.isSelected).also { binding.emotionHappy.isSelected = it }
                    }else {
                        emotionWarning()
                    }
                }
            }
        })

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

        Log.d(DW_DEBUG_TAG, "액티비티 생성")

        postDiary()

    }

    private fun saveButtonCheck() {
        if (emotionCnt > 0 && binding.etWriteDiary.length() > 0){
            // 작성 버튼 활성화하기.
        } else {
            // 작성 버튼 비활성화
        }
    }

    private fun deactivateEmotionButton() {
        binding.emotionAngry.isActivated = false
        binding.emotionAnxious.isActivated = false
        binding.emotionBored.isActivated = false
        binding.emotionFun.isActivated = false
        binding.emotionSad.isActivated = false
        binding.emotionHappy.isActivated = false
        binding.emotionJoy.isActivated = false
        binding.emotionTired.isActivated = false
        binding.emotionSad.isActivated = false
        binding.emotionSound.isActivated = false
        binding.emotionRelax.isActivated = false
        binding.emotionNervous.isActivated = false
    }

    private fun activateEmotionButton() {
        binding.emotionAngry.isActivated = true
        binding.emotionAnxious.isActivated = true
        binding.emotionBored.isActivated = true
        binding.emotionFun.isActivated = true
        binding.emotionSad.isActivated = true
        binding.emotionHappy.isActivated = true
        binding.emotionJoy.isActivated = true
        binding.emotionTired.isActivated = true
        binding.emotionSad.isActivated = true
        binding.emotionSound.isActivated = true
        binding.emotionRelax.isActivated = true
        binding.emotionNervous.isActivated = true
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_write_diary -> {
                val intent =
                    Intent(Intent.ACTION_PICK) // Intent.ACTION_PICK 부터 CONTENT_TYPE, image/*까지 설정하면 갤러리 열림.
                intent.type = MediaStore.Images.Media.CONTENT_TYPE
                intent.type = "image/*"
//                intent.action = Intent.ACTION_GET_CONTENT // 이 설정을 풀면 파일 폴더에서 사진 선택하는 방식.
                getContent.launch(intent)
            }
            R.id.chip_paper_white -> {
                Glide.with(backgroundLayout).load(R.drawable.paper_texture_1).into(backgroundImage)
                theme = "paper_white"
                setTextColor(0)
            }
            R.id.chip_paper_ivory -> {
                Glide.with(backgroundLayout).load(R.drawable.paper_texture_2).into(backgroundImage)
                theme = "paper_ivory"
                setTextColor(0)
            }
            R.id.chip_paper_black -> {
                Glide.with(backgroundLayout).load(R.drawable.annie_unsplash_black_paper)
                    .into(backgroundImage)
                Log.d(DW_DEBUG_TAG, "paper black chosen")
                theme = "paper_dark"
                setTextColor(1)
            }
            R.id.chip_window -> {
                Glide.with(backgroundLayout).load(R.drawable.kevin_unsplash_window)
                    .into(backgroundImage)
                setTextColor(1)
                theme = "window"
                Log.d(DW_DEBUG_TAG, "window chosen")
            }
            R.id.chip_sky_day -> {
                Glide.with(backgroundLayout).load(R.drawable.sky_with_moon).into(backgroundImage)
                setTextColor(0)
                theme = "sky_day"
                Log.d(DW_DEBUG_TAG, "sky day chosen")
            }
            R.id.chip_sky_night -> {
                Glide.with(backgroundLayout).load(R.drawable.night_sky).into(backgroundImage)
                setTextColor(1)
                theme = "sky_night"
                Log.d(DW_DEBUG_TAG, "sky night chosen")
            }
        }
    }

    fun postDiary() {
        binding.etWriteDiary.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (emotionCnt > 0 && s.toString().isNotEmpty()) {

                } else {

                }
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
                val st = StringTokenizer(emo.id.toString(), "_")
                st.nextToken()
                selectedEmo.add(st.nextToken())
            }
        }
        return selectedEmo
    }


    fun setTextColor(mode: Int) {
        when (mode) {
            0 -> {
                binding.tbDiaryWrite.toolbarId.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                contentText.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                binding.emotionAngry.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                binding.emotionAnxious.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                binding.emotionBored.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                binding.emotionFun.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                binding.emotionSad.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                binding.emotionHappy.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                binding.emotionJoy.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                binding.emotionTired.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                binding.emotionSad.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                binding.emotionSound.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                binding.emotionRelax.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                binding.emotionNervous.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
            }
            1 -> { // 밝은 텍스트
                binding.tbDiaryWrite.toolbarId.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
//                binding.tvDiaryEmotion.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                contentText.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                binding.emotionAngry.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                binding.emotionAnxious.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                binding.emotionBored.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                binding.emotionFun.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                binding.emotionSad.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                binding.emotionHappy.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                binding.emotionJoy.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                binding.emotionTired.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                binding.emotionSad.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                binding.emotionSound.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                binding.emotionRelax.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                binding.emotionNervous.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_save -> {
                // 여기에서 통신 붙이기
                if (binding.btnSave.isActivated && theme != null) { // 이미지?, 텍스트 최소 한 글자, 감정 최소 한개, 테마 (기본 paper_white)
                    service.requestWriteDiary(
                        header = header,
                        RequestWriteDiary(
                            content = binding.etWriteDiary.text.toString(),
                            emotions = getEmotionAsList(),
                            theme = theme!!,
                            date = writeDate,
                            image = ""
                        )
                    ).enqueue(
                        onSuccess = {
                            when (it.code()) {
                                in 200..206 -> {
                                    Log.d(DW_DEBUG_TAG, "작성완료 ${it.code()}")
                                    // 작성 완료 되면 완료 액티비티로 보내기.
                                    val intent = Intent(this, DiaryActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
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
                        Toast.makeText(applicationContext, "감정을 선택해주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "내용을 작성해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }
        return super.onOptionsItemSelected(item)
    }
}