package com.example.SkyDiver.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getColorStateList

import com.example.SkyDiver.R
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_overview.view.*


/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {
    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout =inflater.inflate(R.layout.fragment_overview, container, false)
        // Inflate the layout for this fragment

        viewOfLayout.radioButton_KG.setOnClickListener {


        }
        viewOfLayout.radioButton_LBS.setOnClickListener {
            Toast.makeText(
                activity!!.applicationContext,
                "DELETED: ",

                Toast.LENGTH_SHORT
            ).show()

        }
        viewOfLayout.checkBox_tandem.setOnCheckedChangeListener { buttonView, isChecked ->

            Toast.makeText(
                activity,
                "TANDEM: ",

                Toast.LENGTH_SHORT
            ).show()
        }
        viewOfLayout.button_test.setOnClickListener {
            Toast.makeText(
                activity!!.applicationContext,
                "BUTTONN: ",

                Toast.LENGTH_SHORT
            ).show()
        }
//        viewOfLayout.radioGroup.setOnCheckedChangeListener { buttonView, isChecked ->
//            if(isChecked == radioButton_KG.)
//            Toast.makeText(activity,
//                "KG",
//                Toast.LENGTH_LONG
//            ).show()
//            if(isChecked == radioButton_LBS.id)
//                Toast.makeText(activity,
//                    "LBS",
//                    Toast.LENGTH_LONG
//                ).show()
//        }
//        val view: View = inflater!!.inflate(R.layout.fragment_overview, container, false)

//        val radio_KG : RadioButton = findViewById(R.id.radioButton_KG)
//        viewOfLayout.radioGroup_units.setOnCheckedChangeListener { group, checkedId ->
//            Toast.makeText(activity,
//                "CHANGE",
//                Toast.LENGTH_LONG
//            ).show()
//
//            if(checkedId == R.id.radioButton_KG)
//                Toast.makeText(activity,
//                    "KG",
//                    Toast.LENGTH_LONG
//                ).show()
//            if(checkedId ==R.id.radioButton_LBS)
//                Toast.makeText(activity,
//                    "LBS",
//                    Toast.LENGTH_LONG
//                ).show()
//        }






        return viewOfLayout
    }



}
