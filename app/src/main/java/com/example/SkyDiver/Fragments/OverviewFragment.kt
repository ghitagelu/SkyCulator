package com.example.SkyDiver.Fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.SkyDiver.R
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_overview.view.*


/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {
    private lateinit var viewOfLayout: View

    private class UserValues(weight:Int ,equipment:Int, canopy: Int, load:Int, kg:Boolean, lbs:Boolean )
    {
        var weight : Int = weight
        var equipment : Int = equipment
        var canopy : Int = canopy
        var load : Int = load
        var unit_KG :Boolean = kg
        var unit_LBS : Boolean = lbs
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


var defaultValues = UserValues(184, 28, 278, 250,false, true )

        //Make viewOfLayout = this fragment
        viewOfLayout =inflater.inflate(R.layout.fragment_overview, container, false)
        setCalculatorValues(defaultValues)

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

//Weight handling

        viewOfLayout.editNumber_weight.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
            {

            }
            else {
                viewOfLayout.editNumber_weight.setText(viewOfLayout.seekBar_weight.progress.toString())
                hideKeyboard(v)
            }
        }
        viewOfLayout.editNumber_equipment.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
            {

            }
            else {
                viewOfLayout.editNumber_equipment.setText(viewOfLayout.seekBar_equipment.progress.toString())
                hideKeyboard(v)
            }
        }
        viewOfLayout.editNumber_canopy.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
            {

            }
            else {
                viewOfLayout.editNumber_canopy.setText(viewOfLayout.seekBar_canopy.progress.toString())
                hideKeyboard(v)
            }
        }
        viewOfLayout.editNumber_load.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
            {

            }
            else {
                viewOfLayout.editNumber_load.setText(viewOfLayout.seekBar_load.progress.toString())
                hideKeyboard(v)
            }
        }


    //Weight :Text field handling
        viewOfLayout.editNumber_weight.doAfterTextChanged {

            if(viewOfLayout.editNumber_weight.text.toString()!="") {
                val value = Integer.parseInt(viewOfLayout.editNumber_weight.text.toString())

                if(value < 99) {
//                    viewOfLayout.editNumber_weight.setText("99")
//                    viewOfLayout.seekBar_weight.progress = 99
                }
                if(value > 265) {
                    viewOfLayout.editNumber_weight.setText("265")
                    viewOfLayout.seekBar_weight.progress = 265
                }
                else
                viewOfLayout.seekBar_weight.progress=value
            }

        }
        viewOfLayout.editNumber_equipment.doAfterTextChanged {

            if(viewOfLayout.editNumber_equipment.text.toString()!="") {
                val value = Integer.parseInt(viewOfLayout.editNumber_equipment.text.toString())

                if(value < 10) {
//                    viewOfLayout.editNumber_weight.setText("99")
//                    viewOfLayout.seekBar_weight.progress = 99
                }
                if(value > 55) {
                    viewOfLayout.editNumber_equipment.setText("55")
                    viewOfLayout.seekBar_equipment.progress = 55
                }
                else
                    viewOfLayout.seekBar_equipment.progress=value
            }

        }

        viewOfLayout.editNumber_canopy.doAfterTextChanged {

            if(viewOfLayout.editNumber_canopy.text.toString()!="") {
                val value = Integer.parseInt(viewOfLayout.editNumber_canopy.text.toString())

                if(value < 50) {
//                    viewOfLayout.editNumber_weight.setText("99")
//                    viewOfLayout.seekBar_weight.progress = 99
                }
                if(value > 350) {
                    viewOfLayout.editNumber_canopy.setText("350")
                    viewOfLayout.seekBar_canopy.progress = 350
                }
                else
                    viewOfLayout.seekBar_canopy.progress=value
            }

        }
        viewOfLayout.editNumber_load.doAfterTextChanged {

            if(viewOfLayout.editNumber_load.text.toString()!="") {
                val value = Integer.parseInt(viewOfLayout.editNumber_load.text.toString())

                if(value < 0) {
//                    viewOfLayout.editNumber_weight.setText("99")
//                    viewOfLayout.seekBar_weight.progress = 99
                }
                if(value > 400) {
                    viewOfLayout.editNumber_load.setText("400")
                    viewOfLayout.seekBar_load.progress = 400
                }
                else
                    viewOfLayout.seekBar_load.progress=value
            }

        }

    //Weight :seek bar handling

        viewOfLayout.seekBar_weight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewOfLayout.editNumber_weight.setText(progress.toString())
                    viewOfLayout.editNumber_weight.setSelection(viewOfLayout.editNumber_weight.length())
                    ClearFocusFromButtons()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        viewOfLayout.seekBar_equipment.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewOfLayout.editNumber_equipment.setText(progress.toString())
                    viewOfLayout.editNumber_equipment.setSelection(viewOfLayout.editNumber_equipment.length())
                    ClearFocusFromButtons()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        viewOfLayout.seekBar_canopy.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewOfLayout.editNumber_canopy.setText(progress.toString())
                    viewOfLayout.editNumber_canopy.setSelection(viewOfLayout.editNumber_canopy.length())
                    ClearFocusFromButtons()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        viewOfLayout.seekBar_load.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewOfLayout.editNumber_load.setText(progress.toString())
                    viewOfLayout.editNumber_load.setSelection(viewOfLayout.editNumber_load.length())
                    ClearFocusFromButtons()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })





        viewOfLayout.setOnClickListener {
            ClearFocusFromButtons()
            Toast.makeText(
                    activity,
                    "FRAGMENT CLICKED ",

                    Toast.LENGTH_SHORT
                ).show()
        }



        ClearFocusFromButtons()
        //required to update fragment
        return viewOfLayout
    }

    private fun ClearFocusFromButtons()
    {
        viewOfLayout.editNumber_weight.clearFocus()
        viewOfLayout.editNumber_equipment.clearFocus()
        viewOfLayout.editNumber_canopy.clearFocus()
        viewOfLayout.editNumber_load.clearFocus()
    }

    private fun hideKeyboard(v: View) {
        val inputManager = v.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
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

    private fun setCalculatorValues(values:UserValues)
    {
//
        viewOfLayout.editNumber_weight.setText(values.weight.toString())
        viewOfLayout.editNumber_equipment.setText(values.equipment.toString())
        viewOfLayout.editNumber_canopy.setText(values.canopy.toString())
        viewOfLayout.editNumber_load.setText(values.load.toString())

        viewOfLayout.seekBar_weight.progress =values.weight
        viewOfLayout.seekBar_equipment.progress =values.equipment
        viewOfLayout.seekBar_canopy.progress =values.canopy
        viewOfLayout.seekBar_load.progress =values.load

        if(values.unit_KG)
        {
            setUnitsKG()
        }
        if(values.unit_LBS)
        {
            setUnitsLBS()
        }
    }
    //
}
