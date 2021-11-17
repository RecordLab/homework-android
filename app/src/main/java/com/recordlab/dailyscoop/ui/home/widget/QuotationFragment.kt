package com.recordlab.dailyscoop.ui.home.widget

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.recordlab.dailyscoop.databinding.ItemMainWidgetQuotationBinding
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.ui.dashboard.DialogYearMonth

class QuotationFragment : Fragment(), View.OnClickListener {
    private var _binding: ItemMainWidgetQuotationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemMainWidgetQuotationBinding.inflate(inflater, container, false)
        val view = binding.root

        val quotation = binding.tvMainWidgetQuotation
        quotation.setMovementMethod(ScrollingMovementMethod())
        service.requestQuotation("guest").enqueue(
            onSuccess = {
//                Log.d("asd", it.body()!![1].respond)
                quotation.text = it.body()!![1].respond.toString()
            },
            onError = {

            },
            onFail = {

            }
        )

        val bookmarkBtn = binding.ivWidgetQuotationBookmark
        bookmarkBtn.setOnClickListener {
            bookmarkBtn.isSelected = !bookmarkBtn.isSelected
//            if (!bookmarkBtn.isSelected) {
//
//            } else {
//
//            }
        }

        return view
    }


    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

}