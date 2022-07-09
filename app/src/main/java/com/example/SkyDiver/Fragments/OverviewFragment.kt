package com.example.SkyDiver.Fragments


import android.animation.LayoutTransition
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.SkyDiver.R
import com.example.SkyDiver.StartingActivity
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_overview.view.*
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {
    private lateinit var viewOfLayout: View
//    private var clickedonloadtextview =false
    private var init = true //used for the init of Load seekbar progress
    lateinit var shared_preferences_save: SharedPreferences

    //Table of content for the type of actions
    //
    // valueToModify = 1  -weight                increaseValue = false - "-" operation
    // valueToModify = 2  -equipment             increaseValue = true - "+" operation
    // valueToModify = 3  -canopy
    // valueToModify = 4  -load
    var valueToModify = 0
    var increaseValue = true
    var cowntdowninterval:Long = 650
    var load_overflow :Boolean= false
    var load_underflow :Boolean= false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        shared_preferences_save =this.activity!!.getSharedPreferences("save_calculator_values", Context.MODE_PRIVATE)
//        viewOfLayout.layout_main_for_background.setBackgroundResource(R.drawable.clouds11)


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
                var tandem = viewOfLayout.checkBox_tandem.isChecked
                if(unit_KG)
                {
                    setUnitsKG(tandem)
                }
                if(unit_LBS)
                {
                    setUnitsLBS(tandem)
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
                InitIcons ()
                expand_tandem()

            }
            fun setUnitsKG(tandem: Boolean)
            {
                viewOfLayout.radioButton_KG.isChecked=true
                viewOfLayout.textView_weight_units.text=" kg"
                viewOfLayout.textView_equipment_units.text = " kg"

//Values for tandem
                //If tandem = false
                var expand_seekBar_weight_max     = 0
                var expand_seekBar_equipment_min  = 0
                var expand_seekBar_equipment_max  = 0
                var expand_seekBar_canopy_min     = 0
                var expand_seekBar_canopy_max     = 0
                //If tandem = true
                if(tandem)
                {
                     expand_seekBar_weight_max     = 136
                     expand_seekBar_equipment_min  = 10
                     expand_seekBar_equipment_max  = 20
                     expand_seekBar_canopy_min     = 200
                     expand_seekBar_canopy_max     = 100
                }
//*Values for tandem

//Set seekbar limits
                //Create limits value for seekbar
                val defaultSeekBarLimits = SeekBarLimits(
                    45,136 + expand_seekBar_weight_max,
                    5 + expand_seekBar_equipment_min,25 + expand_seekBar_equipment_max,
                    50 + expand_seekBar_canopy_min,350 + expand_seekBar_canopy_max)
                //Call to set the limits for the seek bar
                setSeekBarLimits(defaultSeekBarLimits)
            }

            fun setUnitsLBS(tandem: Boolean)
            {
                viewOfLayout.radioButton_LBS.isChecked=true
                viewOfLayout.textView_weight_units.text=" lbs"
                viewOfLayout.textView_equipment_units.text = " lbs"

//Values for tandem
                //If tandem = false
                var expand_seekBar_weight_max     = 0
                var expand_seekBar_equipment_min  = 0
                var expand_seekBar_equipment_max  = 0
                var expand_seekBar_canopy_min     = 0
                var expand_seekBar_canopy_max     = 0
                //If tandem = true
                if(tandem)
                {
                    expand_seekBar_weight_max     = 300
                    expand_seekBar_equipment_min  = 22
                    expand_seekBar_equipment_max  = 45
                    expand_seekBar_canopy_min     = 200
                    expand_seekBar_canopy_max     = 100
                }
//*Values for tandem
                val defaultSeekBarLimits = SeekBarLimits(
                    99,300 + expand_seekBar_weight_max,
                    11 + expand_seekBar_equipment_min,55 + expand_seekBar_equipment_max,
                    50 + expand_seekBar_canopy_min,350 + expand_seekBar_canopy_max)

                setSeekBarLimits(defaultSeekBarLimits)
            }

            fun setSeekBarLimits(defaultSeekBarLimits:SeekBarLimits)
            {
                viewOfLayout.seekBar_weight.min = defaultSeekBarLimits.seekBar_weight_min
                viewOfLayout.seekBar_weight.max = defaultSeekBarLimits.seekBar_weight_max
                viewOfLayout.seekBar_weight.secondaryProgress = 0
                viewOfLayout.seekBar_equipment.min = defaultSeekBarLimits.seekBar_equipment_min
                viewOfLayout.seekBar_equipment.max= defaultSeekBarLimits.seekBar_equipment_max
                viewOfLayout.seekBar_equipment.secondaryProgress = 0
                viewOfLayout.seekBar_canopy.min = defaultSeekBarLimits.seekBar_canopy_min
                viewOfLayout.seekBar_canopy.max = defaultSeekBarLimits.seekBar_canopy_max
            }
        }
//////////////////////////////////////////////////////////////////////////////////////////////////// I N I T I A L I S I N G ////////////////////////////////////////
             val defaultValues = UserValues(
                shared_preferences_save.getInt("Weight",110),
                shared_preferences_save.getInt("Equipment",10),
                shared_preferences_save.getInt("Canopy",178),
                unit_KG = shared_preferences_save.getBoolean("kg", true),
                unit_LBS = shared_preferences_save.getBoolean("lbs", false)
            )


        //Make viewOfLayout = this fragment
        viewOfLayout =inflater.inflate(R.layout.fragment_overview, container, false)

//Set initial valuse
        //Set "Tandem" checkbox false on 1st run, then get the saved value from last use
        viewOfLayout.checkBox_tandem.isChecked = shared_preferences_save.getBoolean("Tandem_checked",  false)
        //Set default values
        defaultValues.setCalculatorValues()
////////////////////////////////////////////////////////////////////////////////////////////////////*I N I T I A L I S I N G ////////////////////////////////////////
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
            saveData()
        }
//*Radio buttons handling
////////////////////////////////////////////////////////////////////////////////////////////////////
//Tandem checkbox handling
        viewOfLayout.checkBox_tandem.setOnCheckedChangeListener { buttonView, isChecked ->
            defaultValues.setCalculatorValues()

            if(checkBox_tandem.isChecked) {

                Toast.makeText(
                    activity,
                    "TANDEM: ",

                    Toast.LENGTH_SHORT
                ).show()
            }
            expand_tandem()
            saveData()
        }
//*Tandem checkbox handling
////////////////////////////////////////////////////////////////////////////////////////////////////
//Handling of TextView
        //Weight :Text field handling
        viewOfLayout.editNumber_weight.doAfterTextChanged {

            if(viewOfLayout.editNumber_weight.text.toString()!="") {
                var value = Integer.parseInt(viewOfLayout.editNumber_weight.text.toString())

                if(value > viewOfLayout.seekBar_weight.max) {
                    value = viewOfLayout.seekBar_weight.max
                    viewOfLayout.editNumber_weight.setText(value.toString())
                }
                if(value < viewOfLayout.seekBar_weight.min) {
                    value = viewOfLayout.seekBar_weight.min
                    viewOfLayout.editNumber_weight.setText(value.toString())
                }

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
                if(value < viewOfLayout.seekBar_equipment.min) {
                    value = viewOfLayout.seekBar_equipment.min
                    viewOfLayout.editNumber_equipment.setText(value.toString())
                }

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
                if(value < viewOfLayout.seekBar_canopy.min) {
                    value = viewOfLayout.seekBar_canopy.min
                    viewOfLayout.editNumber_canopy.setText(value.toString())
                }

                viewOfLayout.seekBar_canopy.progress=value
                defaultValues.canopy = value
            }

        }

        //Load :Text field handling
        viewOfLayout.editNumber_load.doAfterTextChanged() {

            if(viewOfLayout.editNumber_load.text.toString()!="") {
                var value = ((viewOfLayout.editNumber_load.text.toString()).toDouble()*100).toInt()

                if(value > viewOfLayout.seekBar_load.max) {
                    value = viewOfLayout.seekBar_load.max
                    viewOfLayout.editNumber_load.setText((value.toDouble()/100).toString())
                }
                load_overflow = value == viewOfLayout.seekBar_load.max

                if(value < viewOfLayout.seekBar_load.min) {
                    load_underflow = true
                    value = viewOfLayout.seekBar_load.min
                    viewOfLayout.editNumber_load.setText((value.toDouble()/100).toString())
                }

                handlingOfJumpsConstraintLayout()

                load_underflow = false
            }
        }
//*Handling of TextView
////////////////////////////////////////////////////////////////////////////////////////////////////
//SeekBar handling
        //Function used to update load seekbar
        fun updateLoad(nochange:Boolean, increase:Boolean, decrease:Boolean, onHold:Boolean)
        {
            var progress = viewOfLayout.seekBar_load.progress
            if(nochange) {/*D O  N O T H I N G*/    }
            if(increase)
            {
                if(onHold)
                {
                    viewOfLayout.seekBar_load.progress = progress + onHoldTotalValue(progress,increase = true, decrease = false)
                }
                else
                {
                    viewOfLayout.seekBar_load.progress = progress + 1
                }
            }else if(decrease)
            {
                if(onHold)
                {
                    viewOfLayout.seekBar_load.progress = progress - onHoldTotalValue(progress, increase = false, decrease = true)
                }
                else
                {
                    viewOfLayout.seekBar_load.progress = progress - 1
                }
            }

            progress = viewOfLayout.seekBar_load.progress
            viewOfLayout.editNumber_load.setText((progress.toDouble() / 100).toString())
            viewOfLayout.editNumber_load.setSelection(viewOfLayout.editNumber_load.length())

            val value = ((viewOfLayout.editNumber_load.text.toString()).toDouble() * 100).toInt()

            setCalculatorWingSize(
                defaultValues.weight,
                defaultValues.equipment,
                value,
                defaultValues.unit_KG
            )
            clearFocusFromButtons()
        }

        //Weight seekBar
        viewOfLayout.seekBar_weight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewOfLayout.editNumber_weight.setText(progress.toString())
                    viewOfLayout.editNumber_weight.setSelection(viewOfLayout.editNumber_weight.length())
                    clearFocusFromButtons()
                    setCalculatorWingLoading(defaultValues.weight,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
                }
                HandlerUpdateIcons(0)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //Equipment seekBar
        viewOfLayout.seekBar_equipment.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewOfLayout.editNumber_equipment.setText(progress.toString())
                    viewOfLayout.editNumber_equipment.setSelection(viewOfLayout.editNumber_equipment.length())
                    clearFocusFromButtons()
                    setCalculatorWingLoading(defaultValues.weight,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
                }
                HandlerUpdateIcons(1)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //Canopy seekBar
        viewOfLayout.seekBar_canopy.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewOfLayout.editNumber_canopy.setText(progress.toString())
                    viewOfLayout.editNumber_canopy.setSelection(viewOfLayout.editNumber_canopy.length())
                    clearFocusFromButtons()
                    setCalculatorWingLoading(defaultValues.weight,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
                }
                HandlerUpdateIcons(2)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //Load seekBar
        viewOfLayout.seekBar_load.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    updateLoad( nochange = false, increase = false, decrease = false, onHold = false)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //tresar icoanele de la proggress bar cand dai click pe view
        viewOfLayout.setOnClickListener {
            Toast.makeText(
                    activity,
                    "FRAGMENT CLICKED ",

                    Toast.LENGTH_SHORT
                ).show()
        }
////////////////////////////////////////////////////////////////////////////////////////////////////

//Handling of +/- buttons for all values

        fun updateValues()
        {
            when(valueToModify)
            {
                1->{
                    var size :Int = Integer.parseInt(viewOfLayout.editNumber_weight.text.toString())
                    if(increaseValue)
                    {
                        size += onHoldTotalValue(size, increase = true, decrease = false)
                    }else{
                        size -=onHoldTotalValue(size, increase = false, decrease = true)
                    }
                    viewOfLayout.editNumber_weight.setText(size.toString())
                }
                2->{
                    var size :Int = Integer.parseInt(viewOfLayout.editNumber_equipment.text.toString())
                    if(increaseValue)
                    {
                        size += onHoldTotalValue(size, increase = true, decrease = false)
                    }else{
                        size -= onHoldTotalValue(size, increase = false, decrease = true)
                    }
                    viewOfLayout.editNumber_equipment.setText(size.toString())
                }
                3->{
                    var size :Int = Integer.parseInt(viewOfLayout.editNumber_canopy.text.toString())
                    if(increaseValue)
                    {
                        size += onHoldTotalValue(size, increase = true, decrease = false)
                    }else{
                        size -= onHoldTotalValue(size, increase = false, decrease = true)
                    }
                    viewOfLayout.editNumber_canopy.setText(size.toString())
                    setCalculatorWingLoading(defaultValues.weight,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
                }
                4->{
                    if(increaseValue)
                    {
                        updateLoad(nochange = false, increase = true, decrease = false, onHold = true)
                    }else{
                        updateLoad(nochange = false, increase = false, decrease = true, onHold = true)
                    }
                }

                else -> {
                    //nothing
                }
            }
        }
    /////////////////////////////////C O U N T E R//////////////////////////////////////////
    //counter for holding button
        val weightCounter: CountDownTimer = object : CountDownTimer(Long.MAX_VALUE, cowntdowninterval) {
            override fun onTick(l: Long) {
               updateValues()
            }
            override fun onFinish() {}
        }
    /////////////////////////////////B U T T O N S //////////////////////////////////////////
    //Weight
        //Weight -
        viewOfLayout.button_weight_minus.setOnClickListener {
            var size :Int = Integer.parseInt(viewOfLayout.editNumber_weight.text.toString())
            size -= 1
            viewOfLayout.editNumber_weight.setText(size.toString())
        }
        viewOfLayout.button_weight_minus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.button_weight_minus.setOnLongClickListener{
             valueToModify = 1
             increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }

        //Weight +
        viewOfLayout.button_weight_plus.setOnClickListener {
            var size :Int = Integer.parseInt(viewOfLayout.editNumber_weight.text.toString())
            size += 1
            viewOfLayout.editNumber_weight.setText(size.toString())
        }
        viewOfLayout.button_weight_plus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.button_weight_plus.setOnLongClickListener {
            valueToModify = 1
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }

    //Equipment
        //Equipment -
        viewOfLayout.button_equipment_minus.setOnClickListener {
            var size :Int = Integer.parseInt(viewOfLayout.editNumber_equipment.text.toString())
            size -= 1
            viewOfLayout.editNumber_equipment.setText(size.toString())
        }
        viewOfLayout.button_equipment_minus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.button_equipment_minus.setOnLongClickListener {
            valueToModify = 2
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }
        //Equipment +
        viewOfLayout.button_equipment_plus.setOnClickListener {
            var size :Int = Integer.parseInt(viewOfLayout.editNumber_equipment.text.toString())
            size += 1
            viewOfLayout.editNumber_equipment.setText(size.toString())
        }
        viewOfLayout.button_equipment_plus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.button_equipment_plus.setOnLongClickListener {
            valueToModify = 2
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }

    //Canopy
        //Canopy  -
        viewOfLayout.button_canopy_minus.setOnClickListener {

            var size :Int = Integer.parseInt(viewOfLayout.editNumber_canopy.text.toString())
            size -= 1
            viewOfLayout.editNumber_canopy.setText(size.toString())
            setCalculatorWingLoading(defaultValues.weight,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
        }
        viewOfLayout.button_canopy_minus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.button_canopy_minus.setOnLongClickListener {
            valueToModify = 3
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }

        //Canopy  +
        viewOfLayout.button_canopy_plus.setOnClickListener {
            var size :Int = Integer.parseInt(viewOfLayout.editNumber_canopy.text.toString())
            size += 1
            viewOfLayout.editNumber_canopy.setText(size.toString())
            setCalculatorWingLoading(defaultValues.weight,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
        }
        viewOfLayout.button_canopy_plus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.button_canopy_plus.setOnLongClickListener {
            valueToModify = 3
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }

    //Load
        //Load -
        viewOfLayout.button_load_minus.setOnClickListener {
            updateLoad(nochange = false, increase = false, decrease = true, onHold = false)
        }
        viewOfLayout.button_load_minus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.button_load_minus.setOnLongClickListener {
            valueToModify = 4
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }
        //Load +
        viewOfLayout.button_load_plus.setOnClickListener {
            updateLoad(nochange = false, increase = true, decrease = false, onHold = false)
        }
        viewOfLayout.button_load_plus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.button_load_plus.setOnLongClickListener {
            valueToModify = 4
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }
//*Handling of +/- buttons for all values
////////////////////////////////////////////////////////////////////////////////////////////////////
        //Animation enabled for constraint container of all other constraintLayouts
        //Side Note:   android:animateLayoutChanges="true" needed aswell
        viewOfLayout.Main_constraintLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        clearFocusFromButtons()
        //required to update fragment
        setBackground()

        return viewOfLayout
    }
///////////////////////////////////////////////////////// //////    //    //    ////    //////////////////////////////////////////// /////////////////////////////////////////////////////////
///////////////////////////////////////////////////////// //        ////  //    //  //  //////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////// //////    // // //    //   // / E N D   O F  C L A S S ///////////////// ///////////////////////////////////////////////////////////
///////////////////////////////////////////////////////// //        //  ////    //  //  //////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////// //////    //    //    ////    //////////////////////////////////////////// /////////////////////////////////////////////////////////
//Close onScreen keyboard when user presses something else
    // clear focus from all 4 edit text items
    private fun clearFocusFromButtons()
    {
        viewOfLayout.editNumber_weight.clearFocus()
        viewOfLayout.editNumber_equipment.clearFocus()
        viewOfLayout.editNumber_canopy.clearFocus()
        viewOfLayout.editNumber_load.clearFocus()
    }
//*Close onScreen keyboard when user presses something else
////////////////////////////////////////////////////////////////////////////////////////////////////
//Function used to save current values in the app
    private fun saveData()
    {
        val editor: SharedPreferences.Editor = shared_preferences_save.edit()
        editor.putInt("Weight",    viewOfLayout.editNumber_weight.text.toString().toInt())
        editor.putInt("Equipment", viewOfLayout.editNumber_equipment.text.toString().toInt())
        editor.putInt("Canopy",    viewOfLayout.editNumber_canopy.text.toString().toInt())
        editor.putBoolean("Tandem_checked", viewOfLayout.checkBox_tandem.isChecked)
        editor.putBoolean("kg", viewOfLayout.radioButton_KG.isChecked)
        editor.putBoolean("lbs", viewOfLayout.radioButton_LBS.isChecked)
        editor.putBoolean("SAVED", true)
        editor.apply()
    }
//*Function used to save current values in the app
////////////////////////////////////////////////////////////////////////////////////////////////////
//Calculator values updater
    //Functions used to convert kg/lbs
    private fun convertKGtoLBS(value :Int):Int
    {
        return (value*2.20462).roundToInt()
    }
    private fun convertLBStoKG(value: Int):Int
    {
        return (value/2.20462).roundToInt()
    }
    //*Functions used to convert kg/lbs
    //Updates the values of risk after modification of calculator values
    private fun setCalculatorWingLoading(weight:Int, equipment: Int, canopy:Int, unit_KG:Boolean) {

        val wingLoading:Double
        var totalWeight=weight+equipment

        if(unit_KG){
            totalWeight= convertKGtoLBS(totalWeight)
        }

        wingLoading= totalWeight.toDouble()/canopy.toDouble()
        val result = (wingLoading *100).toInt()
            viewOfLayout.editNumber_load.setText((result.toDouble()/100).toString())
            viewOfLayout.seekBar_load.progress = result

        if(init)//init of load seekbar
        {
        var value = ((viewOfLayout.editNumber_load.text.toString()).toDouble()*100).toInt()

        if(value > viewOfLayout.seekBar_load.max) {
            value = viewOfLayout.seekBar_load.max
            viewOfLayout.editNumber_load.setText((value.toDouble()/100).toString())

        }
        viewOfLayout.seekBar_load.progress=value
        init = false
        }
        saveData()
    }
    //Updates the value of canopy size after the modification of risk value
    private fun setCalculatorWingSize(weight:Int, equipment: Int, wingLoading:Int, unit_KG:Boolean) {
        val canopy:Double
        var totalWeight=weight+equipment

        if(unit_KG){
            totalWeight= convertKGtoLBS(totalWeight)
        }
        canopy= totalWeight.toDouble()/(wingLoading.toDouble())
        val temp = (canopy *100).toInt().toString()

                    viewOfLayout.editNumber_canopy.setText(temp)
        saveData()
    }
//*Calculator values updater
////////////////////////////////////////////////////////////////////////////////////////////////////
//Handling of jump level
    //Update level of jumps
    private fun handlingOfJumpsConstraintLayout()
    {
        val loadValue :Int = ((viewOfLayout.editNumber_load.text.toString()).toDouble()*100).toInt()

        when(updateJumpValue(loadValue)){
            -1 ->{

            }
            0->{
                //Result bar
                viewOfLayout.textView_jumps_level.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.result_bar_shape_and_result_color_0, null)
                viewOfLayout.textView_jumps_level.text ="BASIC: 0-200 JUMPS"
                //Seek bar progress
                viewOfLayout.seekBar_weight.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_0, null)
                viewOfLayout.seekBar_equipment.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_0, null)
                viewOfLayout.seekBar_canopy.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_0, null)
                viewOfLayout.seekBar_load.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_0, null)
               //thumbs icon change
                if (load_underflow){

                    viewOfLayout.seekBar_load.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_load_0, null)
                }else{
                    viewOfLayout.seekBar_load.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_load_0, null)
                }
            }

            1->{
                //Result bar
                viewOfLayout.textView_jumps_level.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.result_bar_shape_and_result_color_1, null)
                viewOfLayout.textView_jumps_level.text ="INTERMEDIATE: 200-600 JUMPS"
                //Seek bar progress
                viewOfLayout.seekBar_weight.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_1, null)
                viewOfLayout.seekBar_equipment.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_1, null)
                viewOfLayout.seekBar_canopy.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_1, null)
                viewOfLayout.seekBar_load.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_1, null)
                //thumbs icon change
                viewOfLayout.seekBar_load.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_load_1, null)
            }

            2->{
                //Result bar
                viewOfLayout.textView_jumps_level.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.result_bar_shape_and_result_color_2, null)
                viewOfLayout.textView_jumps_level.text ="ADVANCED: 600-1500 JUMPS"
                //Seek bar progress
                viewOfLayout.seekBar_weight.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_2, null)
                viewOfLayout.seekBar_equipment.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_2, null)
                viewOfLayout.seekBar_canopy.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_2, null)
                viewOfLayout.seekBar_load.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_2, null)
                //thumbs icon change
                viewOfLayout.seekBar_load.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_load_2, null)
            }

            3->{
                //Result bar
                viewOfLayout.textView_jumps_level.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.result_bar_shape_and_result_color_3, null)
                viewOfLayout.textView_jumps_level.text ="EXPERT: 1500+ JUMPS"
                //Seek bar progress
                viewOfLayout.seekBar_weight.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_3, null)
                viewOfLayout.seekBar_equipment.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_3, null)
                viewOfLayout.seekBar_canopy.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_3, null)
                viewOfLayout.seekBar_load.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_3, null)
                //thumbs icon change
                viewOfLayout.seekBar_load.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_load_3, null)
            }

            4->{
                viewOfLayout.seekBar_load.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_load_alt, null)

            }
            else -> {
                //nothing
            }
        }


    }
    //Get result from load value
    private fun updateJumpValue(loadValue:Int):Int
    {
        var result =4
        when (loadValue) {
            0-> {result = -1}
            in 1..99 -> {
                result =0
            }
            in 100..199 -> {
                result =1
            }
            in 200..299 -> {
                result =2
            }
            in 300..399 -> {
                result =3
            }
            400-> {result = 4}
        }
        return result
    }
//*Handling of jump level

//Handling of on hold + and - buttons
    private fun onHoldTotalValue(size : Int, increase:Boolean, decrease: Boolean):Int
    {
        var result = size%10

        if(result == 0)
        {
            result = 10
        }else
        {
            if(decrease)
            {
                //no handling needed
            }
            if(increase)
            {
                result = 10 - result
            }
        }


        return result
    }
//*Handling of on hold + and - buttons

//Set background
    private fun setBackground()
    {
        when((activity as StartingActivity?)?.getImageSelecter())
        {
            1->viewOfLayout.layout_main_for_background.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds11, null)
            2->viewOfLayout.layout_main_for_background.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds21, null)
            3->viewOfLayout.layout_main_for_background.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds31, null)
            4->viewOfLayout.layout_main_for_background.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds41, null)
            5->viewOfLayout.layout_main_for_background.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds51, null)
            6->viewOfLayout.layout_main_for_background.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds61, null)
            7->viewOfLayout.layout_main_for_background.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds71, null)
            8->viewOfLayout.layout_main_for_background.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds81, null)
            else -> {
                viewOfLayout.layout_main_for_background.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds11, null)
            }
        }
    }

//Handling of seek bar thumbs icons updating on certain progress
    private fun InitIcons ()
    {
        HandlerUpdateIcons(0)
        HandlerUpdateIcons(1)
        HandlerUpdateIcons(2)
    }
    private fun HandlerUpdateIcons(type : Int)
    {
        val progress: Int
        val progress_max :Int
        val progress_min :Int
        when(type)
        {
            0 -> {
                progress_max = viewOfLayout.seekBar_weight.max
                progress_min = viewOfLayout.seekBar_weight.min
                progress     = viewOfLayout.seekBar_weight.progress
                when (progress) {
                    progress_min -> {
                        viewOfLayout.seekBar_weight.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_0, null)
                    }
                    progress_max -> {
                        viewOfLayout.seekBar_weight.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_3, null)
                    }
                    else -> {
                        when(getPercent(progress - progress_min,progress_max - progress_min)) {
                            0->{viewOfLayout.seekBar_weight.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_0, null)}
                            1->{viewOfLayout.seekBar_weight.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_1, null)}
                            2->{viewOfLayout.seekBar_weight.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_2, null)}
                            3->{viewOfLayout.seekBar_weight.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_3, null)}
                            else ->{}
                        }
                    }
                }
            }
            1 -> {
                progress_max = viewOfLayout.seekBar_equipment.max
                progress_min = viewOfLayout.seekBar_equipment.min
                progress     = viewOfLayout.seekBar_equipment.progress
                when (progress) {
                    progress_min -> {
                        viewOfLayout.seekBar_equipment.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_equipment_alt2, null)
                    }
                    progress_max -> {
                        viewOfLayout.seekBar_equipment.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_equipment_3, null)
                    }
                    else -> {
                        when(getPercent(progress - progress_min,progress_max - progress_min)) {
                            0->{viewOfLayout.seekBar_equipment.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_equipment_0, null)}
                            1->{viewOfLayout.seekBar_equipment.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_equipment_1, null)}
                            2->{viewOfLayout.seekBar_equipment.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_equipment_2, null)}
                            3->{viewOfLayout.seekBar_equipment.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_equipment_3, null)}
                            else ->{}
                        }
                    }
                }
            }
            2 -> {
                progress_max = viewOfLayout.seekBar_canopy.max
                progress_min = viewOfLayout.seekBar_canopy.min
                progress     = viewOfLayout.seekBar_canopy.progress
                when (progress) {
                    progress_min -> {
                        viewOfLayout.seekBar_canopy.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_canopee_alt, null)
                    }
                    progress_max -> {
                        viewOfLayout.seekBar_canopy.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_canopee_3, null)
                    }
                    else -> {
                        when(getPercent(progress - progress_min,progress_max - progress_min)) {
                            0->{viewOfLayout.seekBar_canopy.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_canopee_0, null)}
                            1->{viewOfLayout.seekBar_canopy.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_canopee_1, null)}
                            2->{viewOfLayout.seekBar_canopy.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_canopee_2, null)}
                            3->{viewOfLayout.seekBar_canopy.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_canopee_3, null)}
                            else ->{}
                        }
                    }
                }
            }
            else ->{/*Do nothing*/}
        }

    }
    private fun getPercent(progress: Int, progress_max: Int): Int {
        val result : Int
        when((100 * progress) / progress_max) {
            in 0..25 -> {
                result = 0
            }
            in 26..50 -> {
                result = 1
            }
            in 51..80 -> {
                result = 2
            }
            in 81..100 -> {
                result = 3
            }
            else ->{
                result = 0
            }
        }
        return result
    }
//*Handling of seek bar thumbs icons updating on certain progress

//Animation for tandem weigh appearance
    private fun expand_tandem()
    {
        var result : Int
        if(viewOfLayout.checkBox_tandem.isChecked)
        {
            result = View.VISIBLE
        }
        else
        {
            result = View.GONE
        }

        TransitionManager.beginDelayedTransition(viewOfLayout.Weight_tandem_constraintLayout, AutoTransition())
        viewOfLayout.Weight_tandem_constraintLayout.visibility = result
    }

//*Animation for tandem weigh appearance





    //
}
