package com.recordlab.dailyscoop.ui.profile

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.recordlab.dailyscoop.R

class SignOutDialog(context: Context,
                    signOutDialogInterface: SignOutDialogInterface)
    :Dialog(context), View.OnClickListener {

    private var signOutDialogInterface: SignOutDialogInterface? = null

    // 인터페이스 연결
    init {
        this.signOutDialogInterface = signOutDialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog_signout)

        // 배경 투명
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 로그아웃 다이얼로그 취소 버튼
        val cancelBtnClicked = findViewById<AppCompatButton>(R.id.btn_sign_out_dlg_cancel)
        cancelBtnClicked.setOnClickListener {
            dismiss()
        }

        // 로그아웃 다이얼로그 확인 버튼
        val okBtnClicked = findViewById<AppCompatButton>(R.id.btn_sign_out_dlg_ok)
        okBtnClicked.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view){
            findViewById<AppCompatButton>(R.id.btn_sign_out_dlg_ok) ->{
                signOutDialogInterface?.okBtnClicked()
            }
        }
    }
}