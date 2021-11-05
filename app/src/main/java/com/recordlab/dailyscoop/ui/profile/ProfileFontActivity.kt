package com.recordlab.dailyscoop.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityDiaryWriteBinding
import com.recordlab.dailyscoop.databinding.ActivityProfileFontBinding

class ProfileFontActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileFontBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileFontBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            val setfont = binding.testTxt

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_1 ->
                    if (checked) {
                        setfont.typeface = resources.getFont(R.font.spoqa_han_sans_neo_bold)
                    }
                R.id.radio_2 ->
                    if (checked) {
                        // Ninjas rule
                        setfont.typeface = resources.getFont(R.font.spoqa_han_sans_neo_medium)
                    }
                R.id.radio_3 ->
                    if (checked) {
                        // Ninjas rule
                        setfont.typeface = resources.getFont(R.font.spoqa_han_sans_neo_thin)
                    }
            }
        }
    }
}