package com.recordlab.dailyscoop.ui.profile.account

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.ui.profile.notice.ProfileNoticeDialogInterface

class ProfileWithdrawDialog(context: Context,
                            profileWithdrawDialogInterface: ProfileWithdrawDialogInterface
                            ): Dialog(context), View.OnClickListener {

    private var profileWithdrawDialogInterface: ProfileWithdrawDialogInterface? = null

    init {
        this.profileWithdrawDialogInterface = profileWithdrawDialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 레이아웃 가져오기
        setContentView(R.layout.custom_dialog_withdraw)

        // 배경 투명
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 확인 버튼 클릭
        val okBtnClicked = findViewById<AppCompatButton>(R.id.btn_withdraw_dlg_ok)
        okBtnClicked.setOnClickListener(this)

        // 취소 버튼 클릭
        val cancelBtnClicked = findViewById<AppCompatButton>(R.id.btn_withdraw_dlg_cancel)
        cancelBtnClicked.setOnClickListener {
            dismiss()
        }
    }

    override fun onClick(view: View?) {
        when(view) {
            findViewById<AppCompatButton>(R.id.btn_withdraw_dlg_ok) -> {
                this.profileWithdrawDialogInterface?.dialogOkBtnClicked()
                dismiss()
            }
        }
    }
}