package com.recordlab.dailyscoop.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.ui.SettingActivity

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val profileViewModel : ProfileViewModel by viewModels()
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val textView: TextView = root.findViewById(R.id.tv_profile_test)
        profileViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        // 프로필 변경(카메라 아이콘) 버튼 클릭
        val profileImageBtnClicked = root.findViewById<View>(R.id.profile2)
        profileImageBtnClicked.setOnClickListener {

        }

        val friendBtnClicked = root.findViewById<View>(R.id.bg1)
        friendBtnClicked.setOnClickListener {
            val intent = Intent(activity, ProfileFriendActivity::class.java)
            startActivity(intent)
        }

        val accountBtnClicked = root.findViewById<View>(R.id.bg2)
        accountBtnClicked.setOnClickListener {
            val intent = Intent(activity, ProfileAccountActivity::class.java)
            startActivity(intent)
        }

        val noticeBtnClicked = root.findViewById<View>(R.id.bg3)
        noticeBtnClicked.setOnClickListener {
            val intent = Intent(activity, ProfileNoticeActivity::class.java)
            startActivity(intent)
        }

        val lockBtnClicked = root.findViewById<View>(R.id.bg4)
        lockBtnClicked.setOnClickListener {
            val intent = Intent(activity, ProfileLockActivity::class.java)
            startActivity(intent)
        }

        val ddayBtnClicked = root.findViewById<View>(R.id.bg5)
        ddayBtnClicked.setOnClickListener {
            val intent = Intent(activity, ProfileDdayActivity::class.java)
            startActivity(intent)
        }

        val logoutBtnClicked = root.findViewById<View>(R.id.bg6)
        logoutBtnClicked.setOnClickListener{
            //Toast.makeText(getActivity()?.getApplicationContext(),"이것은 log out.", Toast.LENGTH_SHORT).show();

            val builder = AlertDialog.Builder(activity)
            val dialogView = layoutInflater.inflate(R.layout.custom_dialog_withdraw, null)

            builder.setView(dialogView)
                .show()
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
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

}