package com.recordlab.dailyscoop.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.MainActivity
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.ImagePath
import com.recordlab.dailyscoop.databinding.FragmentProfileBinding
import com.recordlab.dailyscoop.network.RetrofitClient
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.network.request.RequestProfileImage
import com.recordlab.dailyscoop.ui.SettingActivity
import com.recordlab.dailyscoop.ui.profile.account.ProfileAccountActivity
import com.recordlab.dailyscoop.ui.profile.day.ProfileDdayActivity
import com.recordlab.dailyscoop.ui.profile.lock.ProfileLockActivity
import com.recordlab.dailyscoop.ui.profile.notice.ProfileNoticeActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileFragment : Fragment() {
    private val reqStoragePermission = 9

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainActivity: MainActivity
    private lateinit var sharedPref: SharedPreferences
    private var profileUrl: String? = null
    val header = mutableMapOf<String, String?>()
    var service = RetrofitClient.service
    private val profileViewModel: ProfileViewModel by viewModels()

    // 프래그먼트에서 컨텍스트를 사용하기 위해 콜백메서드의 매개변수를 형변환해서 사용
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if (it.data == null) {
                val message = R.string.no_selected_image
                Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
            } else {
                Glide.with(this).load(it.data?.data).into(binding.profile)

                val uri = it.data?.data
                val imgPath = uri?.let { ImagePath().getPath(requireContext(), it) }

                // 해당 경로로 파일 생성
                val file = File(imgPath)
                // requestBody로 만들기
                val requestFile = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
                // multipartBody.Part로 만들기
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                service.requestImageUrl(file = body).enqueue(
                    onSuccess = {
                        when (it.code()) {
                            in 200..206 -> {
                                profileUrl = it.body()?.data
//                                Log.d("프로필 확인", "${profileViewModel.profileImage}")
                                updateInfo(it.body()?.data!!)
                            }
                        }
                    }, onError = {

                    }, onFail = {

                    }
                )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root = binding.root

        sharedPref = requireActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        // 헤더설정하기
        header["Content-type"] = "application/json; charset=UTF-8"
        header["Authorization"] = sharedPref.getString("token", "token")

        if (header["Authorization"] != "token") {
            profileViewModel.profileHeader = header
        }

        setHasOptionsMenu(true)

        // 저장된 프로필 이미지 불러오기
        loadProfileInfo()
        profileViewModel.apply {
            profileImage.observe(viewLifecycleOwner, Observer { url ->
//                profileUrl = url
                Glide.with(this@ProfileFragment)
                    .load(url)
                    .error(R.drawable.icon_dailyscoop)
                    .into(binding.profile)
            })
            nickname.observe(viewLifecycleOwner, Observer {
                binding.tvMypageNickname.text = it
            })
            loadingLiveData.observe(viewLifecycleOwner, Observer { isLoading ->
                binding.progressBarProfile.visibility = if (isLoading) View.VISIBLE else View.GONE
            })
        }

        // 프로필 변경(카메라 아이콘) 버튼 클릭
        val profileImageBtnClicked = root.findViewById<View>(R.id.profile2)
        profileImageBtnClicked.setOnClickListener {
            // 갤러리 사용 권한 확인
            val readPermission = ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            if (readPermission == PackageManager.PERMISSION_DENIED) {
                // 권한 없으면 권한 요청
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    reqStoragePermission
                )
            } else {
                // 사진 선택
                val intent = Intent(Intent.ACTION_PICK)
//                intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                intent.type = MediaStore.Images.Media.CONTENT_TYPE
                intent.type = "image/*"
                startForResult.launch(intent)
            }
        }

        // 일기 폰트 버튼 클릭
        val friendBtnClicked = binding.bg1
        friendBtnClicked.setOnClickListener {
            val intent = Intent(activity, ProfileFontActivity::class.java)
            startActivity(intent)
        }

        // 계정 버튼 클릭
        val accountBtnClicked = binding.bg2
        accountBtnClicked.setOnClickListener {
            val intent = Intent(activity, ProfileAccountActivity::class.java)
            startActivity(intent)
        }

        // 알림 버튼 클릭
        val noticeBtnClicked = binding.bg3
        noticeBtnClicked.setOnClickListener {
            val intent = Intent(activity, ProfileNoticeActivity::class.java)
            startActivity(intent)
        }

        // 잠금 버튼 클릭
        val lockBtnClicked = binding.bg4
        lockBtnClicked.setOnClickListener {
            val intent = Intent(activity, ProfileLockActivity::class.java)
            startActivity(intent)
        }

        // 디데이 버튼 클릭
//        val dDayBtnClicked = binding.bg5
//        dDayBtnClicked.setOnClickListener {
//            val intent = Intent(activity, ProfileDdayActivity::class.java)
//            startActivity(intent)
//        }

        // 북마크 버튼 클릭
        val bookmarkClicked = binding.bg7
        bookmarkClicked.setOnClickListener {
            val intent = Intent(activity, ProfileBookmarkActivity::class.java)
            startActivity(intent)
        }

        // 로그아웃 버튼 클릭
        val logoutBtnClicked = binding.bg6
        logoutBtnClicked.setOnClickListener {
            // 커스텀 다이얼로그 출력
            val signOutDialog = SignOutDialog(mainActivity, mainActivity)
            signOutDialog.show()
        }

        return root
    }

    // 저장된 프로필 이미지 불러오기
    private fun loadProfileInfo() {
        profileViewModel.requestUserInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_settings, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(activity, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateInfo(profileImage: String) {
        service.requestUserImageChange(header = header, image = RequestProfileImage(profileImage))
            .enqueue(
                onSuccess = {
                    when (it.code()) {
                        in 200..206 -> {
                            Toast.makeText(context, it.body()?.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, onError = {

                }
            )
    }
}