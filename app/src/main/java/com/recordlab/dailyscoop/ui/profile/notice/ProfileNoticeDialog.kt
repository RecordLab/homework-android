package com.recordlab.dailyscoop.ui.profile.notice

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.recordlab.dailyscoop.R

class ProfileNoticeDialog(context: Context,
                          profileNoticeDialogInterface: ProfileNoticeDialogInterface
                        ): Dialog(context), View.OnClickListener {

    private var profileNoticeDialogInterface: ProfileNoticeDialogInterface? = null

    init {
        this.profileNoticeDialogInterface = profileNoticeDialogInterface
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 레이아웃 가져오기
        setContentView(R.layout.custom_dialog_notice_timepicker)

        // 배경 투명
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 확인 버튼 클릭
        val okBtnClicked = findViewById<AppCompatButton>(R.id.btn_timepicker_dlg_ok)
        okBtnClicked.setOnClickListener(this)

        // 취소 버튼 클릭
        val cancelBtnClicked = findViewById<AppCompatButton>(R.id.btn_timepicker_dlg_cancel)
        cancelBtnClicked.setOnClickListener {
            dismiss()
        }
    }

    override fun onClick(view: View?) {
        when(view){
            findViewById<AppCompatButton>(R.id.btn_timepicker_dlg_ok) -> {
                val hourPicker = findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.np_hour_picker)
                val minutePicker = findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.np_minutes_picker)
                this.profileNoticeDialogInterface?.dialogOkBtnClicked(hourPicker.value, minutePicker.value)
                dismiss()
            }
        }
    }
}