package com.example.SkyCulator

import PagerViewAdapter
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.SkyCulator.databinding.ActivityStartingBinding

class StartingActivity : AppCompatActivity() {

    //Init navbar buttons and viewPager and pagerAdapter (created class in Adapter Package)
    private lateinit var homeBtn: ImageButton
    private lateinit var listBtn: ImageButton
//    private lateinit var addBtn: ImageButton
    var selectedImage:Int = 0

    private lateinit var binding: ActivityStartingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        setTheme(R.style.AppTheme_NoActionBarWithoutBackground)
        val viewPager: ViewPager2 = binding.viewPager
        val adapter = PagerViewAdapter(this)
        viewPager.adapter = adapter

        selectedImage = (1..8).random()

        // Initially load FragmentOne
        viewPager.currentItem = 0


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

// B U T T O N S   H A N D L I N G
        homeBtn = binding.homeBtn
        homeBtn.setOnClickListener {

           viewPager.currentItem =0
            changinTabs(viewPager.currentItem)
        }

        listBtn = binding.listBtn
        listBtn.setOnClickListener {
            // TODO : FRAGMENT CHANGE - enable next line to allow changing to 2nd fragment when tap on button made
            // TODO : FRAGMENT CHANGE - switch this to "2" - will swipe left to change fragments
        viewPager.currentItem =1
            changinTabs(viewPager.currentItem)
        }
    }

    public fun getImageSelecter():Int
    {
        return selectedImage
    }
    private fun changinTabs(position: Int) {
        //Pie Overview
        if(position==0)
        {
            homeBtn.setImageResource(R.drawable.ic_pie_chart_selected_24dp)
            listBtn.setImageResource(R.drawable.ic_view_list_default_24dp)
//            addBtn.visibility = View.INVISIBLE
        }
        //List view
        if(position==1)
        {
            homeBtn.setImageResource(R.drawable.ic_pie_chart_default_24dp)
            listBtn.setImageResource(R.drawable.ic_view_list_selected_24dp)
//            addBtn.visibility = View.VISIBLE
        }
    }
}
