package com.example.SkyDiver.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged

import com.example.SkyDiver.R
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_overview.view.*


/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {
    private lateinit var viewOfLayout: View

    private class UserValues(a:Int ,b:Int, c: Int )
    {
        var weight : Int = a
        var equipment : Int = b
        var canopy : Int = c

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


var valori = UserValues(100, 20, 100)

        //Make viewOfLayout = this fragment
        viewOfLayout =inflater.inflate(R.layout.fragment_overview, container, false)
        setDefaultValues(valori)

        //Radio buttons handling
        viewOfLayout.radioGroup.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked == radioButton_KG.id) {
            //Kilograms

                setUnitsKG()
                Toast.makeText(
                    activity,
                    "KG",
                    Toast.LENGTH_LONG
                ).show()


            }
            if(isChecked == radioButton_LBS.id) {
            //LBS
                setUnitsLBS()
                Toast.makeText(
                    activity,
                    "LBS",
                    Toast.LENGTH_LONG
                ).show()


            }
        }

        //Tandem checkbox handling
        viewOfLayout.checkBox_tandem.setOnCheckedChangeListener { buttonView, isChecked ->

            //Listeners work if you change radio buttons through code
//            viewOfLayout.radioButton_LBS.isChecked = true

            if(checkBox_tandem.isChecked) {


                Toast.makeText(
                    activity,
                    "TANDEM: ",

                    Toast.LENGTH_SHORT
                ).show()
            }
        }
//        viewOfLayout.editNumber_weight.doAfterTextChanged {
//
//        }
        //required to update fragment
        return viewOfLayout
    }

    //Handling of changing the units types
    private fun setUnitsKG()
    {
        viewOfLayout.textView_weight_units.text=" kg"
        viewOfLayout.textView_equipment_units.text = " kg"
    }
    private fun setUnitsLBS()
    {
        viewOfLayout.textView_weight_units.text=" lbs"
        viewOfLayout.textView_equipment_units.text = " lbs"
    }

    private fun setDefaultValues(valori:UserValues)
    {

        viewOfLayout.editNumber_weight.setText(valori.weight.toString())
        viewOfLayout.editNumber_equipment.setText(valori.equipment.toString())
        viewOfLayout.editNumber_canopy.setText(valori.canopy.toString())
    }
    //
}
