package com.recordlab.dailyscoop.ui.dashboard

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import com.recordlab.dailyscoop.R
import com.shawnlin.numberpicker.NumberPicker

class DialogYearMonth(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var btnOk: Button
    private lateinit var btnCancel: Button
    private lateinit var numberPickerYear: NumberPicker
    private lateinit var numberPickerMonth: NumberPicker
    private lateinit var listener: DialogOKClickedListener

    fun init() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 다이얼로그 타이틀바 제거
        dialog.setContentView(R.layout.custom_dialog_datepicker) //뷰 설정
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        numberPickerYear = dialog.findViewById(R.id.np_datepicker_year_picker)
        numberPickerMonth = dialog.findViewById(R.id.np_datepicker_month_picker)

        btnOk = dialog.findViewById(R.id.btn_datepicker_dlg_ok)
        btnOk.setOnClickListener {
            listener.onOKClicked(true, numberPickerYear.value, numberPickerMonth.value)
            dialog.dismiss()
        }
        btnCancel = dialog.findViewById(R.id.btn_datepicker_dlg_cancel)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun setOnOKClickedListener(listener: (Boolean, Int, Int) -> Unit) {
        this.listener = object : DialogOKClickedListener {
            override fun onOKClicked(content: Boolean, year: Int, month: Int) {
                listener(content, year, month)
            }
        }
    }

    interface DialogOKClickedListener {
        fun onOKClicked(content: Boolean, year: Int, month: Int)
    }

}