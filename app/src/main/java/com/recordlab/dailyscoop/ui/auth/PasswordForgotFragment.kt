package com.recordlab.dailyscoop.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.ui.profile.ProfileFriendActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PasswordForgotFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordForgotFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var forgotPasswordActivity : ForgotPasswordActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        forgotPasswordActivity = context as ForgotPasswordActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_password_forgot, container, false)
        val view = inflater.inflate(R.layout.fragment_password_forgot, container, false)

        // 새 비밀번호 설정하기 버튼 클릭
        val newPassBtnClicked = view.findViewById<Button>(R.id.forgot_button3)
        newPassBtnClicked.setOnClickListener {
            forgotPasswordActivity?.callFragment()
        }

        // 인증 요청 버튼 클릭
        val certBtnClicked2 = view.findViewById<Button>(R.id.forgot_button1)
        certBtnClicked2.setOnClickListener {
            Toast.makeText(activity?.applicationContext,"요청", Toast.LENGTH_SHORT).show();
        }

        // 인증번호 확인 버튼 클릭
        val checkBtnClicked2 = view.findViewById<Button>(R.id.forgot_button2)
        checkBtnClicked2.setOnClickListener {
            Toast.makeText(activity?.applicationContext,"확인", Toast.LENGTH_SHORT).show();
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PasswordForgotFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PasswordForgotFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}