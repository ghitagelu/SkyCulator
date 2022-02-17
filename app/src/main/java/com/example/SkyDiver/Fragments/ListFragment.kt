package com.example.SkyDiver.Fragments


import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.SkyDiver.DataBaseHandler.ListItem
import com.example.SkyDiver.DataBaseHandler.MindOrksDBOpenHelper
import com.example.SkyDiver.Model_for_CustomListView
import com.example.SkyDiver.Model_for_CustomListView_Adapter
import com.example.SkyDiver.R
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {
    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout =inflater.inflate(R.layout.fragment_list, container, false)

//get data from database

        getDataFromDataBase()
        val dbHandler = MindOrksDBOpenHelper(activity!!, null)

        viewOfLayout.listfragment_button_test.setOnClickListener {

//Add TestingUsernam Database text to Database

            val user = ListItem("TestingUsernam Database", 100, 100, 100)
            dbHandler.addName(user)
            Toast.makeText(
                activity,
                "TestingUsernam Database" + "Added to database",
                Toast.LENGTH_LONG
            ).show()
//            Toast.makeText(activity,"Text!",Toast.LENGTH_SHORT).show();


            getDataFromDataBase()



//        val displaySavedItem : TextView = ListFragment.findViewById(fragment_list.textView_listfragment)
//        displaySavedItem.text = temp?.getString("SavedAmount", null)

        }

        viewOfLayout.buttonClearDB.setOnClickListener {

            dbHandler.deleteDB()
            mutableListOf<Model_for_CustomListView>().clear()
            //val user = ListItem(" DATABASE DELETED ")
//            dbHandler.addName(user)

            getDataFromDataBase()
        }

//Create calendar event on item tap@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        val insertCalendarIntent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)

        val RECURRENCE_RULE = "FREQ=WEEKLY;BYDAY=MO"
        val EVENT_BEGIN_TIME_IN_MILLIS = Calendar.getInstance().timeInMillis
        val EVENT_END_TIME_IN_MILLIS = Calendar.getInstance().add(Calendar.HOUR_OF_DAY, 2)//.timeInMillis

        viewOfLayout.ListView_Items.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->

            val textViewCustom1 = view.findViewById(R.id.textView2) as TextView

            insertCalendarIntent.putExtra(CalendarContract.Events.TITLE, textViewCustom1.text)
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,EVENT_BEGIN_TIME_IN_MILLIS) // Only date part is considered when ALL_DAY is true; Same as DTSTART
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,EVENT_BEGIN_TIME_IN_MILLIS) // Only date part is considered when ALL_DAY is true
                .putExtra(CalendarContract.Events.DESCRIPTION, "DESCRIPTION") // Description
            startActivity(insertCalendarIntent)
            //On item tap show toast with textView2 text
//            val textViewCustom1 = view.findViewById(R.id.textView2) as TextView
//            Toast.makeText(
//                activity!!.applicationContext,
//                "Tapped:  " + textViewCustom1.text,
//                Toast.LENGTH_SHORT
//            ).show()
//            getDataFromDataBase()
            //*On item tap show toast with textView2 text


        }

        //On item hold, delete items with that name
        viewOfLayout.ListView_Items.setOnItemLongClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->

            val textViewCustom2 = view?.findViewById(R.id.textView2) as TextView

            dbHandler.deletItemFromDB(textViewCustom2.text.toString())
            Toast.makeText(
                activity!!.applicationContext,
                "DELETED: " + textViewCustom2.text,

                Toast.LENGTH_SHORT
            ).show()

            getDataFromDataBase()
            true
        }

//        viewOfLayout.LitsView_Items.setOnItemLongClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
//
//            val textViewCustom2 = view.findViewById(R.id.textView2) as TextView
//
//        }






        // Inflate the layout for this fragment
        return viewOfLayout
    }

//On resuming to fragment - after adding item-
    override fun onResume() {
        super.onResume()
    //Refresh data from database
        getDataFromDataBase()

    }

    private fun getDataFromDataBase ()
    {
        val dbHandler = MindOrksDBOpenHelper(activity!!, null)
        val cursor = dbHandler.getAllName()

        //For CustomListView
        var listview = viewOfLayout.ListView_Items
        var list = mutableListOf<Model_for_CustomListView>()

        if(cursor!!.count>=0) {
            //https://blog.mindorks.com/android-sqlite-database-in-kotlin
            viewOfLayout.TestingtextView2.text = "" //Deletes previous entries
            listview.adapter = Model_for_CustomListView_Adapter(
                activity!!.applicationContext,
                R.layout.row_for_listview_items,
                list
            ) //Clear entries
//            cursor.moveToFirst()

            //no idea ... If removed - if click add to database after "database empty" nothing happens (1st time only)
//            viewOfLayout.TestingtextView2.append(
//                (cursor.getString(
//                    cursor.getColumnIndex(
//                        MindOrksDBOpenHelper.COLUMN_NAME
//                    )
//                ))
//            )

            viewOfLayout.TestingtextView2.append(System.getProperty("line.separator"))
            while (cursor.moveToNext()) {
                viewOfLayout.TestingtextView2.append(
                    (cursor.getString(
                        cursor.getColumnIndex(
                            MindOrksDBOpenHelper.COLUMN_NAME
                        )
                    ))
                )
                viewOfLayout.TestingtextView2.append(System.getProperty("line.separator"))

                list.add(
                    Model_for_CustomListView(
                        cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_NAME)),
                        R.drawable.ic_pie_chart_selected_24dp
                    )
                )
                listview.adapter = Model_for_CustomListView_Adapter(
                    activity!!.applicationContext,
                    R.layout.row_for_listview_items,
                    list
                )
            }
            cursor.close()

            //For CustomListView
//            var listview = viewOfLayout.LitsView_Items
//            var list = mutableListOf<Model_for_CustomListView>()


//            list.add(Model_for_CustomListView("1st item", "Description for 1st item", R.drawable.ic_pie_chart_selected_24dp))
//            list.add(Model_for_CustomListView("2nd item", "Description for 2nd item", R.drawable.ic_pie_chart_default_24dp))
//            list.add(Model_for_CustomListView("3rd item", "Description for 3rd item", R.drawable.ic_add_circle_outline_selected_24dp))
//            list.add(Model_for_CustomListView("4th item", "Description for 4th item", R.drawable.ic_add_circle_outline_default_24dp))
//        viewOfLayout.LitsView_Items.adapter = ListViewCustomAdaptor(activity!!.applicationContext)
//            listview.adapter = Model_for_CustomListView_Adapter (activity!!.applicationContext, R.layout.row_for_listview_items, list)

//            viewOfLayout.LitsView_Items.setOnItemClickListener { parent:AdapterView<*>, view:View, position:Int, id:Long ->
//                if(position == 0)
//                {
//                    Toast.makeText(activity!!.applicationContext, "you click on 1st", Toast.LENGTH_SHORT).show()
//                }
//                if(position == 1)
//                {
//                    Toast.makeText( activity!!.applicationContext, "you click on 2nd", Toast.LENGTH_SHORT).show()
//                }
//                if(position == 2)
//                {
//                    Toast.makeText( activity!!.applicationContext, "you click on 3rd", Toast.LENGTH_SHORT).show()
//                }
//                if(position == 3)
//                {
//                    Toast.makeText( activity!!.applicationContext, "you click on 4th", Toast.LENGTH_SHORT).show()
//                }
//
//            }





        }
        else
        {
                //Generate default "item" if database is empty
            viewOfLayout.TestingtextView2.text ="DataBase Is Empty"
        }

    }

    fun GetNumberOfItemsInTheList():Int
    {
        val dbHandler = MindOrksDBOpenHelper(activity!!, null)
        val cursor = dbHandler.getAllName()
        return cursor!!.count
    }




}
