package com.example.SkyCulator

import PagerViewAdapter
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.SkyCulator.databinding.ActivityStartingBinding
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class StartingActivity : AppCompatActivity() {

    private lateinit var homeBtn: ImageButton
    private lateinit var listBtn: ImageButton
    var selectedImage: Int = 0

    private lateinit var binding: ActivityStartingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // FULLSCREEN IMMERSIVE MODE (Modern API)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val viewPager: ViewPager2 = binding.viewPager
        val adapter = PagerViewAdapter(this)
        viewPager.adapter = adapter

        selectedImage = (1..8).random()
        viewPager.currentItem = 0

        //Fragment swiping ( false = disabled )
        viewPager.isUserInputEnabled = true


        // ViewPager handling icon change in Menu bar when different fragment is opened
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Action to perform when a page is selected
                changinTabs(viewPager.currentItem)
                // You can perform other actions here
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                // Handle scroll state if needed
            }
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                // Handle changes in scroll state if needed
            }
        })




        homeBtn = binding.homeBtn
        homeBtn.setOnClickListener {
            viewPager.currentItem = 0
            changinTabs(viewPager.currentItem)
        }

        listBtn = binding.listBtn
        listBtn.setOnClickListener {
            viewPager.currentItem = 1
            changinTabs(viewPager.currentItem)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun getImageSelecter(): Int {
        return selectedImage
    }

    private fun changinTabs(position: Int) {
        if (position == 0) {
            homeBtn.setImageResource(R.drawable.ic_pie_chart_selected_24dp)
            listBtn.setImageResource(R.drawable.ic_view_list_default_24dp)
        }
        if (position == 1) {
            homeBtn.setImageResource(R.drawable.ic_pie_chart_default_24dp)
            listBtn.setImageResource(R.drawable.ic_view_list_selected_24dp)
        }
    }
    fun setViewPagerUserInputEnabled(enabled: Boolean) {
        binding.viewPager.isUserInputEnabled = enabled
    }
}
