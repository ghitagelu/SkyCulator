package com.example.SkyDiver


import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.SkyDiver.DataBaseHandler.ListItem
import com.example.SkyDiver.DataBaseHandler.MindOrksDBOpenHelper
import com.example.SkyDiver.ReminderBroadcast.ReminderBroadcast
import com.example.SkyDiver.SafeClickListener.setSafeOnClickListener
import com.example.SkyDiver.databinding.ActivityStartingBinding


class StartingActivity : AppCompatActivity() {

    //Init navbar buttons and viewPager and pagerAdapter (created class in Adapter Package)
    private lateinit var mViewPager: ViewPager
    private lateinit var mPagerAdapter: PagerAdapter
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



        mViewPager= binding.mViewPager

        selectedImage = (1..8).random()

        mPagerAdapter = PagerViewAdapter(supportFragmentManager)
        mViewPager.adapter= mPagerAdapter
        mViewPager.offscreenPageLimit = 2

        val REQUEST_amount = 0;

        homeBtn = binding.homeBtn
        homeBtn.setOnClickListener {

           mViewPager.currentItem =0
        }

        listBtn = binding.listBtn
        listBtn.setOnClickListener {
            // TODO: FRAGMENT CHANGE - enable next line to allow changing to 2nd fragment when tap on button made
//            mViewPager.currentItem =1

            val text = "Free fall speed calculator available on next update"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        }

        //add page change listener
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {
                changinTabs(position)
            }
        })

        //Default nav bar selection INIT
        mViewPager.currentItem = 0
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
