package com.recordlab.dailyscoop.ui.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.MainActivity
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.FragmentProfileBinding
import com.recordlab.dailyscoop.ui.SettingActivity

class ProfileFragment : Fragment() {
    private val REQ_STORAGE_PERMISSION = 9
    private val REQ_GALLERY = 10

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK){
            Glide.with(this).load(it.data?.data).into(binding.profile)
        }
    }

    lateinit var mainActivity: MainActivity

    // 프래그먼트에서 컨텍스트를 사용하기 위해 콜백메서드의 매개변수를 형변환해서 사용
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root = binding.root

        setHasOptionsMenu(true)

        // 프로필 변경(카메라 아이콘) 버튼 클릭
        val profileImageBtnClicked = root.findViewById<View>(R.id.profile2)
        profileImageBtnClicked.setOnClickListener {
            // 갤러리 사용 권한 확인
            val readPermission = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)

            if(readPermission == PackageManager.PERMISSION_DENIED){
                // 권한 없으면 권한 요청
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQ_STORAGE_PERMISSION)
            }else{
                // 사진 선택
                val intent = Intent(Intent.ACTION_PICK)
                intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                intent.type = "image/*"
                startForResult.launch(intent)
            }
        }

        // 친구추가 버튼 클릭
        val friendBtnClicked = binding.bg1
        friendBtnClicked.setOnClickListener {
            val intent = Intent(activity, ProfileFriendActivity::class.java)
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
        val dDayBtnClicked = binding.bg5
        dDayBtnClicked.setOnClickListener {
            val intent = Intent(activity, ProfileDdayActivity::class.java)
            startActivity(intent)
        }

        // 로그아웃 버튼 클릭
        val logoutBtnClicked = binding.bg6
        logoutBtnClicked.setOnClickListener{
            // 커스텀 다이얼로그 출력
            val signOutDialog = SignOutDialog(mainActivity, mainActivity)
            signOutDialog.show()
        }

        return root
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

}