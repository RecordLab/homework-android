package com.recordlab.dailyscoop.ui.dashboard

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import com.recordlab.dailyscoop.R

class DialogYearMonth(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var btnOk: Button
    private lateinit var btnCancel: Button
    private lateinit var listener: DialogOKClickedListener

    fun init() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 다이얼로그 타이틀바 제거
        dialog.setContentView(R.layout.custom_dialog_datepicker) //뷰 설정
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnOk = dialog.findViewById(R.id.btn_datepicker_dlg_ok)
        btnOk.setOnClickListener {
            listener.onOKClicked(true)
            dialog.dismiss()
        }
        btnCancel = dialog.findViewById(R.id.btn_datepicker_dlg_cancel)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun setOnOKClickedListener(listener: (Boolean) -> Unit) {
        this.listener = object : DialogOKClickedListener {
            override fun onOKClicked(content: Boolean) {
                listener(content)
            }
        }
    }

    interface DialogOKClickedListener {
        fun onOKClicked(content: Boolean)
    }

}