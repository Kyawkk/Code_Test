package com.kyawzinlinn.ui_design

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.kyawzinlinn.ui_design.adapter.ItemAdapter
import com.kyawzinlinn.ui_design.adapter.SliderAdapter
import com.kyawzinlinn.ui_design.databinding.ActivityMainBinding
import com.kyawzinlinn.ui_design.utils.OptionType
import java.lang.Math.abs

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var handler: Handler
    private val runnable = Runnable {
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler(Looper.myLooper()!!)

        setUpImageSlider()
        setUpRoomRecyclerview()
        setUpClickListeners()
    }

    private fun setUpImageSlider() {
        val adapter = SliderAdapter()
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.clipToPadding = false
        binding.viewPager.clipChildren = false
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        binding.tvSliderCount.text = "See All ${binding.viewPager.currentItem + 1}/${adapter.itemCount}"

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r + 0.14f
        }

        binding.viewPager.setPageTransformer(transformer)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                binding.tvSliderCount.text = "See All ${position + 1}/${adapter.itemCount}"
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable , 2000)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                binding.tvSliderCount.text = "See All ${position + 1}/${adapter.itemCount}"
            }
        })
    }

    private fun setUpClickListeners() {
        binding.apply {
            tvByRates.setOnClickListener {
                tvByRates.background= resources.getDrawable(R.drawable.option_one_bg)
                tvByRoom.background = resources.getDrawable(R.drawable.clear_bg)
                setUpRateRecyclerView()
            }
            tvByRoom.setOnClickListener {
                tvByRoom.background= resources.getDrawable(R.drawable.option_one_bg)
                tvByRates.background = resources.getDrawable(R.drawable.clear_bg)
                setUpRoomRecyclerview()
            }
        }
    }

    private fun setUpRateRecyclerView() {
        binding.rvByRoom.setHasFixedSize(true)
        binding.rvByRoom.adapter = ItemAdapter(OptionType.RATE)
    }

    private fun setUpRoomRecyclerview() {
        binding.rvByRoom.setHasFixedSize(true)
        binding.rvByRoom.adapter = ItemAdapter(OptionType.ROOM)
    }

    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable , 2000)
    }
}