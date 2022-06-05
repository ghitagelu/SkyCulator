package com.example.SkyDiver.Fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.SkyDiver.R
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_overview.view.*
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {
    private lateinit var viewOfLayout: View
    private var clickedonloadtextview =false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        class SeekBarLimits(
            var seekBar_weight_min: Int,    var seekBar_weight_max: Int,
            var seekBar_equipment_min :Int, var seekBar_equipment_max :Int,
            var seekBar_canopy_min :Int,    var seekBar_canopy_max :Int
        )
         class UserValues(
            var weight: Int,
            var equipment: Int,
            var canopy: Int,
            var unit_KG: Boolean,
            var unit_LBS: Boolean
        )
        {
            //Handling of changing the units types
            fun setCalculatorValues()
            {
                if(unit_KG)
                {
                    setUnitsKG()
                }
                if(unit_LBS)
                {
                    setUnitsLBS()
                }
//
                viewOfLayout.editNumber_weight.setText(weight.toString())
                viewOfLayout.editNumber_equipment.setText(equipment.toString())
                viewOfLayout.editNumber_canopy.setText(canopy.toString())
                setCalculatorWingLoading(weight,equipment,canopy,unit_KG)

                viewOfLayout.seekBar_weight.progress =weight
                viewOfLayout.seekBar_equipment.progress =equipment
                viewOfLayout.seekBar_canopy.progress =canopy

                handlingOfJumpsConstraintLayout()

            }
            fun setUnitsKG()
            {
                viewOfLayout.radioButton_KG.isChecked=true
                viewOfLayout.textView_weight_units.text=" kg"
                viewOfLayout.textView_equipment_units.text = " kg"

                val defaultSeekBarLimits = SeekBarLimits(
                    45,120,
                    5,25,
                    50,350)

                setSeekBarLimits(defaultSeekBarLimits)
            }

            fun setUnitsLBS()
            {
                viewOfLayout.radioButton_LBS.isChecked=true
                viewOfLayout.textView_weight_units.text=" lbs"
                viewOfLayout.textView_equipment_units.text = " lbs"

                val defaultSeekBarLimits = SeekBarLimits(
                    99,265,
                    10,55,
                    50,350)

                setSeekBarLimits(defaultSeekBarLimits)
            }

            fun setSeekBarLimits(defaultSeekBarLimits:SeekBarLimits)
            {
                viewOfLayout.seekBar_weight.min = defaultSeekBarLimits.seekBar_weight_min
                viewOfLayout.seekBar_weight.max = defaultSeekBarLimits.seekBar_weight_max

                viewOfLayout.seekBar_equipment.min = defaultSeekBarLimits.seekBar_equipment_min
                viewOfLayout.seekBar_equipment.max= defaultSeekBarLimits.seekBar_equipment_max

                viewOfLayout.seekBar_canopy.min = defaultSeekBarLimits.seekBar_canopy_min
                viewOfLayout.seekBar_canopy.max = defaultSeekBarLimits.seekBar_canopy_max
            }


        }

        val defaultValues = UserValues(
            100,
            10,
            278,
            unit_KG = true,
            unit_LBS = false
        )


        //Make viewOfLayout = this fragment
        viewOfLayout =inflater.inflate(R.layout.fragment_overview, container, false)

        //Set default values
        defaultValues.setCalculatorValues()

//Radio buttons handling
        viewOfLayout.radioGroup.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked == radioButton_KG.id) {
            //Kilograms
                defaultValues.weight = convertLBStoKG(defaultValues.weight)
                defaultValues.equipment = convertLBStoKG(defaultValues.equipment)
                defaultValues.unit_KG = true
                defaultValues.unit_LBS= false
                defaultValues.setCalculatorValues()
                setCalculatorWingLoading(defaultValues.weight,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)

            }

            if(isChecked == radioButton_LBS.id) {
            //LBS
                defaultValues.weight = convertKGtoLBS(defaultValues.weight)
                defaultValues.equipment = convertKGtoLBS(defaultValues.equipment)
                defaultValues.unit_KG = false
                defaultValues.unit_LBS= true
                defaultValues.setCalculatorValues()
                setCalculatorWingLoading(defaultValues.weight,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
            }

        }
//*Radio buttons handling

//Tandem checkbox handling
        viewOfLayout.checkBox_tandem.setOnCheckedChangeListener { buttonView, isChecked ->

            if(checkBox_tandem.isChecked) {

                Toast.makeText(
                    activity,
                    "TANDEM: ",

                    Toast.LENGTH_SHORT
                ).show()
            }

        }
//*Tandem checkbox handling

        viewOfLayout.editNumber_weight.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus)
            {
                viewOfLayout.editNumber_weight.setText(viewOfLayout.seekBar_weight.progress.toString())
                hideKeyboard(v)
            }
        }
        viewOfLayout.editNumber_equipment.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus)
            {
                viewOfLayout.editNumber_equipment.setText(viewOfLayout.seekBar_equipment.progress.toString())
                hideKeyboard(v)
            }
        }
        viewOfLayout.editNumber_canopy.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus)
            {
                viewOfLayout.editNumber_canopy.setText(viewOfLayout.seekBar_canopy.progress.toString())
                hideKeyboard(v)
            }
        }
        viewOfLayout.editNumber_load.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus)
            {
                viewOfLayout.editNumber_load.setText(((viewOfLayout.seekBar_load.progress.toDouble())/100).toString())
                hideKeyboard(v)
            }
        }

//Weight handling
        //Weight :Text field handling
        viewOfLayout.editNumber_weight.doAfterTextChanged {

            if(viewOfLayout.editNumber_weight.text.toString()!="") {
                var value = Integer.parseInt(viewOfLayout.editNumber_weight.text.toString())

                if(value > viewOfLayout.seekBar_weight.max) {
                    value = viewOfLayout.seekBar_weight.max
                    viewOfLayout.editNumber_weight.setText(value.toString())
                }
//                if(value < viewOfLayout.seekBar_weight.min) {
//                    value = viewOfLayout.seekBar_weight.min
//                    viewOfLayout.editNumber_weight.setText(value.toString())
//                }

                viewOfLayout.seekBar_weight.progress=value
                defaultValues.weight = value

                setCalculatorWingLoading(defaultValues.weight,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
            }

        }

        //Equipment :Text field handling
        viewOfLayout.editNumber_equipment.doAfterTextChanged {

            if(viewOfLayout.editNumber_equipment.text.toString()!="") {
                var value = Integer.parseInt(viewOfLayout.editNumber_equipment.text.toString())

                if(value > viewOfLayout.seekBar_equipment.max) {
                    value = viewOfLayout.seekBar_equipment.max
                    viewOfLayout.editNumber_equipment.setText(value.toString())
                }
//                if(value < viewOfLayout.seekBar_equipment.min) {
//                    value = viewOfLayout.seekBar_equipment.min
//                    viewOfLayout.editNumber_equipment.setText(value.toString())
//                }

                viewOfLayout.seekBar_equipment.progress=value
                defaultValues.equipment = value

                setCalculatorWingLoading(defaultValues.weight,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
            }

        }

        //Canopy :Text field handling
        viewOfLayout.editNumber_canopy.doAfterTextChanged {

            if(viewOfLayout.editNumber_canopy.text.toString()!="") {
                var value = Integer.parseInt(viewOfLayout.editNumber_canopy.text.toString())

                if(value > viewOfLayout.seekBar_canopy.max) {
                    value = viewOfLayout.seekBar_canopy.max

                    viewOfLayout.editNumber_canopy.setText(value.toString())

                }
//                if(value < viewOfLayout.seekBar_canopy.min) {
//                    value = viewOfLayout.seekBar_canopy.min
//
//                    viewOfLayout.editNumber_canopy.setText(value.toString())
//
//                }

                viewOfLayout.seekBar_canopy.progress=value
                defaultValues.canopy = value

                setCalculatorWingLoading(defaultValues.weight,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)

            }

        }

        viewOfLayout.editNumber_load.setOnClickListener {
            clickedonloadtextview = true
        }
        //Load :Text field handling
        viewOfLayout.editNumber_load.doAfterTextChanged() {


            if(viewOfLayout.editNumber_load.text.toString()!="") {
                var value = ((viewOfLayout.editNumber_load.text.toString()).toDouble()*100).toInt()

                if(value > viewOfLayout.seekBar_load.max) {
                    value = viewOfLayout.seekBar_load.max
                    viewOfLayout.editNumber_load.setText((value.toDouble()/100).toString())

                }
//                if(value < viewOfLayout.seekBar_load.min) {
//                    value = viewOfLayout.seekBar_load.min
//                    viewOfLayout.editNumber_load.setText((value.toDouble()/100).toString())
//
//                }

                viewOfLayout.seekBar_load.progress=value

                if (clickedonloadtextview)
                {
                    clickedonloadtextview=false
                    var value = ((viewOfLayout.editNumber_load.text.toString()).toDouble()*100).toInt()
                    setCalculatorWingSize(defaultValues.weight,defaultValues.equipment,value,defaultValues.unit_KG)
                }
                handlingOfJumpsConstraintLayout()
            }

        }
//*Weight handling

//SeekBar handling

        viewOfLayout.seekBar_weight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewOfLayout.editNumber_weight.setText(progress.toString())
                    viewOfLayout.editNumber_weight.setSelection(viewOfLayout.editNumber_weight.length())
                    clearFocusFromButtons()
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
                    clearFocusFromButtons()
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
                    clearFocusFromButtons()
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
                    viewOfLayout.editNumber_load.setText((progress.toDouble()/100).toString())
                    viewOfLayout.editNumber_load.setSelection(viewOfLayout.editNumber_load.length())

                    var value = ((viewOfLayout.editNumber_load.text.toString()).toDouble()*100).toInt()
                        setCalculatorWingSize(defaultValues.weight,defaultValues.equipment,value,defaultValues.unit_KG)

                    clearFocusFromButtons()

                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
//        viewOfLayout.editNumber_load.addTextChangedListener {
//
//
//        }




        viewOfLayout.setOnClickListener {
//            clearFocusFromButtons()
//            Toast.makeText(
//                    activity,
//                    "FRAGMENT CLICKED ",
//
//                    Toast.LENGTH_SHORT
//                ).show()
        }



        clearFocusFromButtons()
        //required to update fragment
        return viewOfLayout
    }
//Close onScreen keyboard when user presses something else
    // clear focus from all 4 edit text items
    private fun clearFocusFromButtons()
    {
        viewOfLayout.editNumber_weight.clearFocus()
        viewOfLayout.editNumber_equipment.clearFocus()
        viewOfLayout.editNumber_canopy.clearFocus()
        viewOfLayout.editNumber_load.clearFocus()
        clickedonloadtextview = false
    }
    //
    private fun hideKeyboard(v: View) {
        val inputManager = v.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
    }
//*Close onScreen keyboard when user presses something else






    private fun handlingOfJumpsConstraintLayout()
    {
        val loadValue :Int = ((viewOfLayout.editNumber_load.text.toString()).toDouble()*100).toInt()

        when(updateJumpValue(loadValue)){
            0->{
                viewOfLayout.textView_jumps_level.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.result_bar_shape_and_result_color_0, null)
                viewOfLayout.textView_jumps_level.text ="BASIC: 0-200 JUMPS"
            }

            1->{
                viewOfLayout.textView_jumps_level.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.result_bar_shape_and_result_color_1, null)
                viewOfLayout.textView_jumps_level.text ="INTERMEDIATE: 200-600 JUMPS"
            }

            2->{
                viewOfLayout.textView_jumps_level.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.result_bar_shape_and_result_color_2, null)
                viewOfLayout.textView_jumps_level.text ="ADVANCED: 600-1500 JUMPS"
            }

            3->{
                viewOfLayout.textView_jumps_level.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.result_bar_shape_and_result_color_3, null)
                viewOfLayout.textView_jumps_level.text ="EXPERT: 1500+ JUMPS"
            }

            else -> {
                //nothing
            }
        }


    }
     private fun updateJumpValue(loadValue:Int):Int
    {
        var result =4
        when (loadValue) {
            in 1..99 -> {
                result =0
            }
            in 100..199 -> {
                result =1
            }
            in 200..299 -> {
                result =2
            }
            in 300..400 -> {
                result =3
            }
        }
       return result
    }


    private fun convertKGtoLBS(value :Int):Int
    {
        return (value*2.20462).roundToInt()
    }
    private fun convertLBStoKG(value: Int):Int
    {
        return (value/2.20462).roundToInt()
    }


//Calculator values updater
    //Updates the values of risk after modification of calculator values
    private fun setCalculatorWingLoading(weight:Int, equipment: Int, canopy:Int, unit_KG:Boolean) {

        val wingLoading:Double
        var totalWeight=weight+equipment

        if(unit_KG){
            totalWeight= convertKGtoLBS(totalWeight)
        }
        wingLoading= totalWeight.toDouble()/canopy.toDouble()
        var result = ((((wingLoading *100).toInt()).toDouble())/100).toString()
            viewOfLayout.editNumber_load.setText(result)
    }
    //Updates the value of canopy size after the modification of risk value
    private fun setCalculatorWingSize(weight:Int, equipment: Int, wingLoading:Int, unit_KG:Boolean) {
        val canopy:Double
        var totalWeight=weight+equipment

        if(unit_KG){
            totalWeight= convertKGtoLBS(totalWeight)
        }
        canopy= totalWeight.toDouble()/(wingLoading.toDouble())
        var temp = (canopy *100).toInt().toString()
//                if(temp !=  viewOfLayout.editNumber_canopy.text.toString()) {
                    viewOfLayout.editNumber_canopy.setText(temp)
//                }

//            setCalculatorWingSize(defaultValues.weight,defaultValues.equipment,value,defaultValues.unit_KG)
//            var value = ((viewOfLayout.editNumber_load.text.toString()).toDouble()*100).toInt()






//        Toast.makeText(
//            activity,
//            "TANDEM: $temp",
//
//            Toast.LENGTH_SHORT
//        ).show()
    }
//*Calculator values updater


    //
}
