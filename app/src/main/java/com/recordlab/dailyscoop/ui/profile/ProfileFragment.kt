package com.recordlab.dailyscoop.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.recordlab.dailyscoop.R

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

        val friendBtnClicked = root.findViewById<View>(R.id.bg1)
        friendBtnClicked.setOnClickListener {
            val intent = Intent(getActivity(), ProfileFriendActivity::class.java)
            startActivity(intent)
        }
        return root
    }
}