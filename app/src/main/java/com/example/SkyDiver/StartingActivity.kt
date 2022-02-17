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


class StartingActivity : AppCompatActivity() {

    //Init navbar buttons and viewPager and pagerAdapter (created class in Adapter Package)
    private lateinit var mViewPager: ViewPager
    private lateinit var mPagerAdapter: PagerAdapter
    private lateinit var homeBtn: ImageButton
    private lateinit var listBtn: ImageButton
    private lateinit var addBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)


        mViewPager= findViewById(R.id.mViewPager)
        //init imageButtons

//Create notification
        createNotificationChannel();



        mPagerAdapter = PagerViewAdapter(supportFragmentManager)
        mViewPager.adapter= mPagerAdapter
        mViewPager.offscreenPageLimit = 2

        val REQUEST_amount = 0;




//        val displaySavedItem : TextView = ListFragment.findViewById(fragment_list.textView_listfragment)
//        displaySavedItem.text = temp?.getString("SavedAmount", null)

        homeBtn = findViewById(R.id.homeBtn)
        homeBtn.setOnClickListener {

           mViewPager.currentItem =0
        }

        listBtn = findViewById(R.id.listBtn)
        listBtn.setOnClickListener {
            mViewPager.currentItem =1
        }


        addBtn = findViewById(R.id.addBtn)
        // https://stackoverflow.com/questions/48323793/how-to-set-setonclicklistener-for-a-button-in-a-fragment-of-navigationdraweracti
        addBtn.setSafeOnClickListener {
            val text = "ADD ITEM "
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()

            val intent = Intent(this, Add_IncomeOrExpense::class.java)
            startActivityForResult(intent,REQUEST_amount)


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
        homeBtn.setImageResource(R.drawable.ic_pie_chart_selected_24dp)
        addBtn.visibility = View.INVISIBLE




    }



    private fun changinTabs(position: Int) {
        //Pie Overview
        if(position==0)
        {
            homeBtn.setImageResource(R.drawable.ic_pie_chart_selected_24dp)
            listBtn.setImageResource(R.drawable.ic_view_list_default_24dp)
            addBtn.visibility = View.INVISIBLE
        }
        //List view
        if(position==1)
        {
            homeBtn.setImageResource(R.drawable.ic_pie_chart_default_24dp)
            listBtn.setImageResource(R.drawable.ic_view_list_selected_24dp)
            addBtn.visibility = View.VISIBLE
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val name = "ReminderChannel";
            val description = "Channel fo reminder";
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            var channel = NotificationChannel("notify", name, importance)
            channel.description = description

            val notificationManager = getSystemService(NotificationManager::class.java);
            notificationManager.createNotificationChannel(channel);

        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK )
        {
            //---------get the data from the received data
            val tempAmount = (data?.getSerializableExtra( "amount")).toString()
            val tempDate = data?.getSerializableExtra( "date")
            val tempTitle = (data?.getSerializableExtra( "title")).toString()
            val tempExtraValue = data?.getSerializableExtra("sometingExtra")
            val tempType = data?.getSerializableExtra("Type")
            val weight = (data?.getSerializableExtra( "weight")).toString()
            val equipment = (data?.getSerializableExtra( "equipment")).toString()
            val canopy = (data?.getSerializableExtra( "canopy")).toString()

            val text = "Amount ITEM :"
            val duration = Toast.LENGTH_LONG

            val toast = Toast.makeText(applicationContext, text + tempTitle +"Amount "+ tempAmount +" Date issued" +tempDate, duration)
            toast.show()



            val dbHandler = MindOrksDBOpenHelper(this, null)
            val user = ListItem(tempTitle,Integer.parseInt(weight), Integer.parseInt(equipment), Integer.parseInt(canopy)
               // tempTitle.plus(" ").plus(tempAmount)
            )
            dbHandler.addName(user)

            //REMINDER
Toast.makeText(this, "reminder", Toast.LENGTH_SHORT).show()
            val intent = Intent (this, ReminderBroadcast::class.java);
            val pendingIntent =  PendingIntent.getBroadcast(this,0, intent, 0);
//Use with getSystemService to retriee a ALrarmAmanager for receiving intents at a time of your choosing
            val alarmManager = getSystemService(ALARM_SERVICE) as? AlarmManager

            val timeAtButtonClick = System.currentTimeMillis();
            val tenSecondsInMillis = 1000 * 10 ;

            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSecondsInMillis, pendingIntent)
            }

        }
        else
        {
            //ignore
        }
    }












}
