package com.recordlab.dailyscoop.ui.profile.day

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.recordlab.dailyscoop.R

class ProfileDdayDialog(context: Context,
                        profileDdayDialogInterface: ProfileDdayDialogInterface
                        ): Dialog(context), View.OnClickListener {
    private var profileDdayDialogInterface: ProfileDdayDialogInterface? = null

    init {
        this.profileDdayDialogInterface = profileDdayDialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 레이아웃 가져오기
        setContentView(R.layout.custom_dialog_day_datepicker)

        // 배경 투명
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 확인 버튼 클릭
        val okBtnClicked = findViewById<AppCompatButton>(R.id.btn_datepicker_dlg_ok)
        okBtnClicked.setOnClickListener(this)

        // 취소 버튼 클릭
        val cancelBtnClicked = findViewById<AppCompatButton>(R.id.btn_datepicker_dlg_cancel)
        cancelBtnClicked.setOnClickListener {
            dismiss()
        }

    }

    override fun onClick(view: View?) {
        when(view){
            findViewById<AppCompatButton>(R.id.btn_datepicker_dlg_ok) -> {
                val yearPicker = findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.np_datepicker_year_picker)
                val monthPicker = findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.np_datepicker_month_picker)
                val dayPicker = findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.np_datepicker_day_picker)
                this.profileDdayDialogInterface?.dialogDdayOkBtnClicked(yearPicker.value, monthPicker.value, dayPicker.value)
                dismiss()
            }
        }
    }

}