package com.kyawzinlinn.codetest.customview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.kyawzinlinn.codetest.R
import com.kyawzinlinn.codetest.databinding.SliderLayoutBinding
import com.kyawzinlinn.codetest.utils.Gender

class GenderSlider(private val context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    private var binding : SliderLayoutBinding = SliderLayoutBinding.inflate(LayoutInflater.from(context),this,true)

    val boldTypeFace = ResourcesCompat.getFont(context, R.font.bold)
    val normalTypeFace = ResourcesCompat.getFont(context, R.font.regular)

    fun setGender(gender: String){

        when (gender){
            Gender.FEMALE.toString() -> {
                Log.d("TAG", "setGender: FEMALE")
                animateFemaleItem {  }
            }
            Gender.MALE.toString() -> {
                Log.d("TAG", "setGender: MALE")
                animateMaleItem { }
            }
        }
    }

    fun setOnGenderSelectedListener(onGenderSelected: (Gender) -> Unit){
        animateFemaleItem(onGenderSelected)

        animateMaleItem(onGenderSelected)
    }

    private fun animateMaleItem(onGenderSelected: (Gender) -> Unit) {
        binding.tvMale.setOnClickListener {
            binding.selector.animate()
                .x(binding.tvMale.x)
                .y(binding.tvMale.y)
                .setDuration(400)
                .start()

            onGenderSelected(Gender.MALE)

            binding.tvFemale.typeface = normalTypeFace
            binding.tvMale.typeface = boldTypeFace
        }
    }

    private fun animateFemaleItem(onGenderSelected: (Gender) -> Unit) {
        binding.tvFemale.setOnClickListener {
            binding.selector.animate()
                .x(binding.tvFemale.x)
                .y(binding.tvFemale.y)
                .setDuration(400)
                .start()

            onGenderSelected(Gender.FEMALE)

            binding.tvMale.typeface = normalTypeFace
            binding.tvFemale.typeface = boldTypeFace
        }
    }
}