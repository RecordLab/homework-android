package com.recordlab.dailyscoop.ui.home.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.recordlab.dailyscoop.databinding.ItemMainWidgetDiaryBinding

class QuickDiaryFragment : Fragment() {
    private var _binding: ItemMainWidgetDiaryBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemMainWidgetDiaryBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }
}