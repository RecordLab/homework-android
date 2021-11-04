package com.recordlab.dailyscoop.ui.home.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.recordlab.dailyscoop.databinding.ItemMainWidgetQuotationBinding
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
        val btnView = binding.ivWidgetQuotationBookmark
        btnView.setOnClickListener(this)
//        val view = inflater.inflate(R.layout.item_main_widget_quotation, container, false)
        val bookmarkBtn = binding.ivWidgetQuotationBookmark
        bookmarkBtn.setOnClickListener {
            if (!bookmarkBtn.isSelected) {
                with(Toast(context)) {

                }
            } else {
                val customDialog =
                    DialogYearMonth(requireContext())//context?.let { it1 -> DialogYearMonth(it1) }

                customDialog.init()
            }
        }

        return view
    }


    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }


}