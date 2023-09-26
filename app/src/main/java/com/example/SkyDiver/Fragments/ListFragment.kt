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
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.SkyDiver.FreeFallCalculations.FreeFallCalculator
import com.example.SkyDiver.FreeFallCalculations.calculateAcceleration
import com.example.SkyDiver.R
import com.example.SkyDiver.StartingActivity
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_overview.view.*
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {
    private lateinit var viewOfLayout: View

    lateinit var shared_preferences_save2: SharedPreferences
    var valueToModify = 0
    var increaseValue = true
    var cowntdowninterval:Long = 650

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        shared_preferences_save2 =this.activity!!.getSharedPreferences("save_calculator_values", Context.MODE_PRIVATE)
        class SeekBarLimits(
            var seekBar_weight_min: Int,        var seekBar_weight_max: Int,
        )
        class UsersValues(
            var userTop_weight: Int,
            var userTop_height: Int,
            var userTop_suit: Int,
            var userBottom_weight: Int,
            var userBottom_height: Int,
            var userBottom_suit: Int,
            var unit_KG: Boolean,
            var unit_LBS: Boolean
        ) {
            //Handling of changing the units types
            fun setCalculatorValues() {
                if(unit_KG)
                {
                    setUnitsKG()
                }
                if(unit_LBS)
                {
                    setUnitsLBS()
                }
                init()
                viewOfLayout.userTop_editNumber_weight.setText(userTop_weight.toString())
                viewOfLayout.userTop_editNumber_height.setText(userTop_height.toString())
                viewOfLayout.userBottom_editNumber_weight.setText(userBottom_weight.toString())
                viewOfLayout.userBottom_editNumber_height.setText(userBottom_height.toString())

                viewOfLayout.userTop_seekBar_weight.progress =userTop_weight
                viewOfLayout.userTop_seekBar_height.progress =userTop_height
                viewOfLayout.userBottom_seekBar_weight.progress =userBottom_weight
                viewOfLayout.userBottom_seekBar_height.progress =userBottom_height

            }
            fun setUnitsKG()
            {
                viewOfLayout.radioButton_KG2.isChecked=true
                viewOfLayout.userTop_textView_weight_units.text=" kg"
                viewOfLayout.userBottom_textView_weight_units.text=" kg"


//Set seekbar limits
                //Create limits value for seekbar
                val defaultSeekBarLimits = SeekBarLimits(45,136 )
                //Call to set the limits for the seek bar
                setSeekBarLimits(defaultSeekBarLimits)
            }

            fun setUnitsLBS()
            {
                viewOfLayout.radioButton_LBS2.isChecked=true
                viewOfLayout.userTop_textView_weight_units.text=" lbs"
                viewOfLayout.userBottom_textView_weight_units.text= " lbs"

                val defaultSeekBarLimits = SeekBarLimits(99,300)

                setSeekBarLimits(defaultSeekBarLimits)

            }
            fun setSeekBarLimits(defaultSeekBarLimits:SeekBarLimits)
            {
                viewOfLayout.userTop_seekBar_weight.min = defaultSeekBarLimits.seekBar_weight_min
                viewOfLayout.userTop_seekBar_weight.max = defaultSeekBarLimits.seekBar_weight_max
                viewOfLayout.userBottom_seekBar_weight.min = defaultSeekBarLimits.seekBar_weight_min
                viewOfLayout.userBottom_seekBar_weight.max = defaultSeekBarLimits.seekBar_weight_max
                viewOfLayout.userTop_seekBar_weight.secondaryProgress = 0
                viewOfLayout.userBottom_seekBar_weight.secondaryProgress = 0

            }
        }


        val defaultValues = UsersValues(
            shared_preferences_save2.getInt("userTop_weight",200),
            shared_preferences_save2.getInt("userTop_height",150),
            1,//todo save in shared prefferences the value of the suit
            shared_preferences_save2.getInt("userBottom_weight",200),
            shared_preferences_save2.getInt("userBottom_height",150),
            1,//todo save in shared prefferences the value of the suit
            unit_KG = shared_preferences_save2.getBoolean("kg", false),
            unit_LBS = shared_preferences_save2.getBoolean("lbs", true)
        )

        viewOfLayout =inflater.inflate(R.layout.fragment_list, container, false)

        //Set "Tandem" checkbox false on 1st run, then get the saved value from last use

        defaultValues.setCalculatorValues()


//Radio buttons handling
        viewOfLayout.radioGroup2.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked == radioButton_KG2.id) {
                //Kilograms
                defaultValues.userTop_weight = convertLBStoKG(defaultValues.userTop_weight)
                defaultValues.userBottom_weight = convertLBStoKG(defaultValues.userBottom_weight)

                defaultValues.unit_KG = true
                defaultValues.unit_LBS= false
                defaultValues.setCalculatorValues()
//                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)

            }

            if(isChecked == radioButton_LBS2.id) {
                //LBS
                defaultValues.userTop_weight = convertKGtoLBS(defaultValues.userTop_weight)
                defaultValues.userBottom_weight = convertKGtoLBS(defaultValues.userBottom_weight)

                defaultValues.unit_KG = false
                defaultValues.unit_LBS= true
                defaultValues.setCalculatorValues()
//                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
            }
            saveData()
        }


//UserTop
    //UserTop- Weight
        //UserTop- Weight- Textview
        viewOfLayout.userTop_editNumber_weight.doAfterTextChanged {

            if(viewOfLayout.userTop_editNumber_weight.text.toString()!="") {
                var value = Integer.parseInt(viewOfLayout.userTop_editNumber_weight.text.toString())

                if(value > viewOfLayout.userTop_seekBar_weight.max) {
                    value = viewOfLayout.userTop_seekBar_weight.max
                    viewOfLayout.userTop_editNumber_weight.setText(value.toString())
                }
                if(value < viewOfLayout.userTop_seekBar_weight.min) {
                    value = viewOfLayout.userTop_seekBar_weight.min
                    viewOfLayout.userTop_editNumber_weight.setText(value.toString())
                }

                viewOfLayout.userTop_seekBar_weight.progress=value
                defaultValues.userTop_weight = value

//                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
            }
        }
        viewOfLayout.userTop_editNumber_height.doAfterTextChanged {

            if(viewOfLayout.userTop_editNumber_height.text.toString()!="") {
                var value = Integer.parseInt(viewOfLayout.userTop_editNumber_height.text.toString())

                if(value > viewOfLayout.userTop_seekBar_height.max) {
                    value = viewOfLayout.userTop_seekBar_height.max
                    viewOfLayout.userTop_editNumber_height.setText(value.toString())
                }
                if(value < viewOfLayout.userTop_seekBar_height.min) {
                    value = viewOfLayout.userTop_seekBar_height.min
                    viewOfLayout.userTop_editNumber_height.setText(value.toString())
                }

                viewOfLayout.userTop_seekBar_height.progress=value
                defaultValues.userTop_height = value

//                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
            }
        }
        viewOfLayout.userBottom_editNumber_weight.doAfterTextChanged {

            if(viewOfLayout.userBottom_editNumber_weight.text.toString()!="") {
                var value = Integer.parseInt(viewOfLayout.userBottom_editNumber_weight.text.toString())

                if(value > viewOfLayout.userBottom_seekBar_weight.max) {
                    value = viewOfLayout.userBottom_seekBar_weight.max
                    viewOfLayout.userBottom_editNumber_weight.setText(value.toString())
                }
                if(value < viewOfLayout.userBottom_seekBar_weight.min) {
                    value = viewOfLayout.userBottom_seekBar_weight.min
                    viewOfLayout.userBottom_editNumber_weight.setText(value.toString())
                }

                viewOfLayout.userBottom_seekBar_weight.progress=value
                defaultValues.userBottom_weight = value

//                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
            }
        }
        viewOfLayout.userBottom_editNumber_height.doAfterTextChanged {

            if(viewOfLayout.userBottom_editNumber_height.text.toString()!="") {
                var value = Integer.parseInt(viewOfLayout.userBottom_editNumber_height.text.toString())

                if(value > viewOfLayout.userBottom_seekBar_height.max) {
                    value = viewOfLayout.userBottom_seekBar_height.max
                    viewOfLayout.userBottom_editNumber_height.setText(value.toString())
                }
                if(value < viewOfLayout.userBottom_seekBar_height.min) {
                    value = viewOfLayout.userBottom_seekBar_height.min
                    viewOfLayout.userBottom_editNumber_height.setText(value.toString())
                }

                viewOfLayout.userBottom_seekBar_height.progress=value
                defaultValues.userBottom_height = value

//                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
            }
        }


        viewOfLayout.userTop_seekBar_weight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewOfLayout.userTop_editNumber_weight.setText(progress.toString())
                    viewOfLayout.userTop_editNumber_weight.setSelection(viewOfLayout.userTop_editNumber_weight.length())
//                    clearFocusFromButtons()
//                    setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
                }
//                HandlerUpdateIcons(0)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        viewOfLayout.userTop_seekBar_height.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewOfLayout.userTop_editNumber_height.setText(progress.toString())
                    viewOfLayout.userTop_editNumber_height.setSelection(viewOfLayout.userTop_editNumber_height.length())
//                    clearFocusFromButtons()
//                    setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
                }
//                HandlerUpdateIcons(0)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        viewOfLayout.userBottom_seekBar_weight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewOfLayout.userBottom_editNumber_weight.setText(progress.toString())
                    viewOfLayout.userBottom_editNumber_weight.setSelection(viewOfLayout.userBottom_editNumber_weight.length())
//                    clearFocusFromButtons()
//                    setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
                }
//                HandlerUpdateIcons(0)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        viewOfLayout.userBottom_seekBar_height.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewOfLayout.userBottom_editNumber_height.setText(progress.toString())
                    viewOfLayout.userBottom_editNumber_height.setSelection(viewOfLayout.userBottom_editNumber_height.length())
//                    clearFocusFromButtons()
//                    setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
                }
//                HandlerUpdateIcons(0)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

//Handling of +/- buttons for all values

        fun updateValues()
        {
            when(valueToModify)
            {
                1->{
                    var size :Int = Integer.parseInt(viewOfLayout.userTop_editNumber_weight.text.toString())
                    if(increaseValue)
                    {
                        size += onHoldTotalValue(size, increase = true, decrease = false)
                    }else{
                        size -=onHoldTotalValue(size, increase = false, decrease = true)
                    }
                    viewOfLayout.userTop_editNumber_weight.setText(size.toString())
                }
                2->{
                    var size :Int = Integer.parseInt(viewOfLayout.userTop_editNumber_height.text.toString())
                    if(increaseValue)
                    {
                        size += onHoldTotalValue(size, increase = true, decrease = false)
                    }else{
                        size -= onHoldTotalValue(size, increase = false, decrease = true)
                    }
                    viewOfLayout.userTop_editNumber_height.setText(size.toString())
                }
                3->{
                    var size :Int = Integer.parseInt(viewOfLayout.userBottom_editNumber_weight.text.toString())
                    if(increaseValue)
                    {
                        size += onHoldTotalValue(size, increase = true, decrease = false)
                    }else{
                        size -= onHoldTotalValue(size, increase = false, decrease = true)
                    }
                    viewOfLayout.userBottom_editNumber_weight.setText(size.toString())
//                    setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
                }
                4->{
                    var size :Int = Integer.parseInt(viewOfLayout.userBottom_editNumber_height.text.toString())
                    if(increaseValue)
                    {
                        size += onHoldTotalValue(size, increase = true, decrease = false)
                    }else{
                        size -= onHoldTotalValue(size, increase = false, decrease = true)
                    }
                    viewOfLayout.userBottom_editNumber_height.setText(size.toString())
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
//User Top
    //UserTop
        //Weight -
        viewOfLayout.userTop_button_weight_minus.setOnClickListener {
            var size :Int = Integer.parseInt(viewOfLayout.userTop_editNumber_weight.text.toString())
            size -= 1
            viewOfLayout.userTop_editNumber_weight.setText(size.toString())
        }
        viewOfLayout.userTop_button_weight_minus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.userTop_button_weight_minus.setOnLongClickListener{
            valueToModify = 1
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }

        //Weight +
        viewOfLayout.userTop_button_weight_plus.setOnClickListener {
            var size :Int = Integer.parseInt(viewOfLayout.userTop_editNumber_weight.text.toString())
            size += 1
            viewOfLayout.userTop_editNumber_weight.setText(size.toString())
        }
        viewOfLayout.userTop_button_weight_plus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.userTop_button_weight_plus.setOnLongClickListener {
            valueToModify = 1
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }
//User Top
        //UserTop
        //Height_tandem -
        viewOfLayout.userTop_button_height_minus.setOnClickListener {
            var size :Int = Integer.parseInt(viewOfLayout.userTop_editNumber_height.text.toString())
            size -= 1
            viewOfLayout.userTop_editNumber_height.setText(size.toString())
        }
        viewOfLayout.userTop_button_height_minus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.userTop_button_height_minus.setOnLongClickListener{
            valueToModify = 2
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }

        //Weight_tandem +
        viewOfLayout.userTop_button_height_plus.setOnClickListener {
            var size :Int = Integer.parseInt(viewOfLayout.userTop_editNumber_height.text.toString())
            size += 1
            viewOfLayout.userTop_editNumber_height.setText(size.toString())
        }
        viewOfLayout.userTop_button_height_plus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.userTop_button_height_plus.setOnLongClickListener {
            valueToModify = 2
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }

//User Bottom
        //Equipment -
        viewOfLayout.userBottom_button_weight_minus.setOnClickListener {
            var size :Int = Integer.parseInt(viewOfLayout.userBottom_editNumber_weight.text.toString())
            size -= 1
            viewOfLayout.userBottom_editNumber_weight.setText(size.toString())
        }
        viewOfLayout.userBottom_button_weight_minus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.userBottom_button_weight_minus.setOnLongClickListener {
            valueToModify = 3
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }
        //Equipment +
        viewOfLayout.userBottom_button_weight_plus.setOnClickListener {
            var size :Int = Integer.parseInt(viewOfLayout.userBottom_editNumber_weight.text.toString())
            size += 1
            viewOfLayout.userBottom_editNumber_weight.setText(size.toString())
        }
        viewOfLayout.userBottom_button_weight_plus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.userBottom_button_weight_plus.setOnLongClickListener {
            valueToModify = 3
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }

        //Canopy
        //Canopy  -
        viewOfLayout.userBottom_button_height_minus.setOnClickListener {

            var size :Int = Integer.parseInt(viewOfLayout.userBottom_editNumber_height.text.toString())
            size -= 1
            viewOfLayout.userBottom_editNumber_height.setText(size.toString())
//            setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
        }
        viewOfLayout.userBottom_button_height_minus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.userBottom_button_height_minus.setOnLongClickListener {
            valueToModify = 4
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }

        //Canopy  +
        viewOfLayout.userBottom_button_height_plus.setOnClickListener {
            var size :Int = Integer.parseInt(viewOfLayout.userBottom_editNumber_height.text.toString())
            size += 1
            viewOfLayout.userBottom_editNumber_height.setText(size.toString())
//            setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
        }
        viewOfLayout.userBottom_button_height_plus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        viewOfLayout.userBottom_button_height_plus.setOnLongClickListener {
            valueToModify = 4
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }

//*Handling of +/- buttons for all values
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////


        viewOfLayout.button3_result.setOnClickListener() {

            val result = calculateAcceleration(1.0,1.1,1.3,1.3,12.3,1.2).toString()
            viewOfLayout.text_result.setText(result.toString())

        }


        //Animation enabled for constraint container of all other constraintLayouts
        //Side Note:   android:animateLayoutChanges="true" needed aswell
        viewOfLayout.Main_constraintLayout2.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        setBackground()
        // Inflate the layout for this fragment
        return viewOfLayout
    }

    private fun init()
    {
        TransitionManager.beginDelayedTransition(viewOfLayout.userTop_Weight_constraintLayout, AutoTransition())
        TransitionManager.beginDelayedTransition(viewOfLayout.userBottom_Weight_constraintLayout, AutoTransition())
    }


    //Set background
    private fun setBackground()
    {
        when((activity as StartingActivity?)?.getImageSelecter())
        {
            1->viewOfLayout.layout_main_for_background2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds12, null)
            2->viewOfLayout.layout_main_for_background2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds22, null)
            3->viewOfLayout.layout_main_for_background2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds32, null)
            4->viewOfLayout.layout_main_for_background2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds42, null)
            5->viewOfLayout.layout_main_for_background2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds52, null)
            6->viewOfLayout.layout_main_for_background2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds62, null)
            7->viewOfLayout.layout_main_for_background2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds72, null)
            8->viewOfLayout.layout_main_for_background2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds82, null)
            else -> {
                viewOfLayout.layout_main_for_background2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds12, null)
            }
        }
    }
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


    ////////////////////////////////////////////////////////////////////////////////////////////////////
//Function used to save current values in the app
    private fun saveData()
    {
        val editor: SharedPreferences.Editor = shared_preferences_save2.edit()
        editor.putInt("userTop_weight",    viewOfLayout.userTop_editNumber_weight.text.toString().toInt())
        editor.putInt("userTop_height", viewOfLayout.userTop_editNumber_height.text.toString().toInt())
//        editor.putInt("userTop_suit", viewOfLayout.userTop_editNumber_weight.text.toString().toInt())
        editor.putInt("userBottom_weight",    viewOfLayout.userBottom_editNumber_weight.text.toString().toInt())
        editor.putInt("userBottom_height", viewOfLayout.userBottom_editNumber_height.text.toString().toInt())
//        editor.putInt("userBottom_suit", viewOfLayout.editNumber_equipment.text.toString().toInt())
        editor.putBoolean("kg", viewOfLayout.radioButton_KG2.isChecked)
        editor.putBoolean("lbs", viewOfLayout.radioButton_LBS2.isChecked)
        editor.putBoolean("SAVED", true)
        editor.apply()

    }
//*Function used to save current values in the app
////////////////////////////////////////////////////////////////////////////////////////////////////


}
