package com.recordlab.dailyscoop.ui.home.widget

import android.content.Context
import android.content.SharedPreferences
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
import com.recordlab.dailyscoop.network.request.RequestQuotation
import com.recordlab.dailyscoop.ui.dashboard.DialogYearMonth

class QuotationFragment : Fragment(), View.OnClickListener {
    private var _binding: ItemMainWidgetQuotationBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemMainWidgetQuotationBinding.inflate(inflater, container, false)
        val view = binding.root

        sharedPref = requireActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE)

        val quotation = binding.tvMainWidgetQuotation
        quotation.setMovementMethod(ScrollingMovementMethod())
        service.requestQuotation("guest").enqueue(
            onSuccess = {
                quotation.text = it.body()!![1].respond.toString()
            },
            onError = {

            },
            onFail = {

            }
        )

        val bookmarkBtn = binding.ivWidgetQuotationBookmark
        bookmarkBtn.setOnClickListener {
            val data = RequestQuotation(quotation.text.toString())

            if (!bookmarkBtn.isSelected) {
                addQuotation(data)
            } else {
                delQuotation(data)
            }
            bookmarkBtn.isSelected = !bookmarkBtn.isSelected
        }

        return view
    }


    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    private fun addQuotation(data: RequestQuotation) {
        val header = mutableMapOf<String, String?>()

        header["Content-type"] = "application/json; charset=UTF-8"
        header["Authorization"] = sharedPref.getString("token", "token")

        service.requestAddQuotation(header = header, quote = data).enqueue(
            onSuccess = {
                when (it.code()) {
                    in 200..206 -> {
                        //Log.d("message", it.body()!!.message)
                    }

                }
            },
            onError = {

            },
            onFail = {

            }
        )
    }

    private fun delQuotation(data: RequestQuotation) {
        val header = mutableMapOf<String, String?>()

        header["Content-type"] = "application/json; charset=UTF-8"
        header["Authorization"] = sharedPref.getString("token", "token")

        service.requestDelQuotation(header = header, quote = data).enqueue(
            onSuccess = {
//                Log.d("message", it.body()!!.message)
            },
            onError = {

            },
            onFail = {

            }
        )
    }

}