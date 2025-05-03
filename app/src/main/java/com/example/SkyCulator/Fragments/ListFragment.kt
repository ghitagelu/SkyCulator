package com.example.SkyCulator.Fragments

import android.animation.LayoutTransition
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.SkyCulator.FreeFallCalculations.FreeFallCalculator
import com.example.SkyCulator.FreeFallCalculations.calculateAcceleration
import com.example.SkyCulator.R
import com.example.SkyCulator.StartingActivity
import com.example.SkyCulator.databinding.FragmentListBinding
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {
//    private lateinit var binding: View

    lateinit var shared_preferences_save2: SharedPreferences
    var valueToModify = 0
    var increaseValue = true
    var cowntdowninterval:Long = 650


    private var _binding: FragmentListBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("App stages", "Fragment List starting")
        shared_preferences_save2 =this.activity!!.getSharedPreferences("save_calculator_values", Context.MODE_PRIVATE)

        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)

        class SeekBarLimits(
            var seekBarWeight_min: Int,        var seekBarWeight_max: Int,
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

                binding.userTopEditNumberWeight.setText(userTop_weight.toString())
                binding.userTopEditNumberHeight.setText(userTop_height.toString())
                binding.userBottomEditNumberWeight.setText(userBottom_weight.toString())
                binding.userBottomEditNumberHeight.setText(userBottom_height.toString())

                binding.userTopSeekBarWeight.progress =userTop_weight
                binding.userTopSeekBarHeight.progress =userTop_height
                binding.userBottomSeekBarWeight.progress =userBottom_weight
                binding.userBottomSeekBarHeight.progress =userBottom_height

            }
            fun setUnitsKG()
            {
                binding.radioButtonKG2.isChecked=true
                binding.userTopTextViewWeightWnits.text=" kg"
                binding.userBottomTextViewWeightUnits.text=" kg"


//Set seekbar limits
                //Create limits value for seekbar
                val defaultSeekBarLimits = SeekBarLimits(45,136 )
                //Call to set the limits for the seek bar
                setSeekBarLimits(defaultSeekBarLimits)
            }

            fun setUnitsLBS()
            {
                binding.radioButtonLBS2.isChecked=true
                binding.userTopTextViewWeightWnits.text=" lbs"
                binding.userBottomTextViewWeightUnits.text= " lbs"

                val defaultSeekBarLimits = SeekBarLimits(99,300)

                setSeekBarLimits(defaultSeekBarLimits)

            }
            fun setSeekBarLimits(defaultSeekBarLimits:SeekBarLimits)
            {
                binding.userTopSeekBarWeight.min = defaultSeekBarLimits.seekBarWeight_min                
                binding.userTopSeekBarWeight.max = defaultSeekBarLimits.seekBarWeight_max
                binding.userBottomSeekBarWeight.min = defaultSeekBarLimits.seekBarWeight_min
                binding.userBottomSeekBarWeight.max = defaultSeekBarLimits.seekBarWeight_max
                binding.userTopSeekBarWeight.secondaryProgress = 0
                binding.userBottomSeekBarWeight.secondaryProgress = 0

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

//        binding =inflater.inflate(R.layout.fragment_list, container, false)

        //Set "Tandem" checkbox false on 1st run, then get the saved value from last use

        defaultValues.setCalculatorValues()


//Radio buttons handling
        binding.radioGroup2.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked == binding.radioButtonKG2.id) {
                //Kilograms
                defaultValues.userTop_weight = convertLBStoKG(defaultValues.userTop_weight)
                defaultValues.userBottom_weight = convertLBStoKG(defaultValues.userBottom_weight)

                defaultValues.unit_KG = true
                defaultValues.unit_LBS= false
                defaultValues.setCalculatorValues()
//                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)

            }

            if(isChecked == binding.radioButtonLBS2.id) {
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
        binding.userTopEditNumberWeight.doAfterTextChanged {

            if(binding.userTopEditNumberWeight.text.toString()!="") {
                var value = Integer.parseInt(binding.userTopEditNumberWeight.text.toString())

                if(value > binding.userTopSeekBarWeight.max) {
                    value = binding.userTopSeekBarWeight.max
                    binding.userTopEditNumberWeight.setText(value.toString())
                }
                if(value < binding.userTopSeekBarWeight.min) {
                    value = binding.userTopSeekBarWeight.min
                    binding.userTopEditNumberWeight.setText(value.toString())
                }

                binding.userTopSeekBarWeight.progress=value
                defaultValues.userTop_weight = value

//                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
            }
        }
        binding.userTopEditNumberHeight.doAfterTextChanged {

            if(binding.userTopEditNumberHeight.text.toString()!="") {
                var value = Integer.parseInt(binding.userTopEditNumberHeight.text.toString())

                if(value > binding.userTopSeekBarHeight.max) {
                    value = binding.userTopSeekBarHeight.max
                    binding.userTopEditNumberHeight.setText(value.toString())
                }
                if(value < binding.userTopSeekBarHeight.min) {
                    value = binding.userTopSeekBarHeight.min
                    binding.userTopEditNumberHeight.setText(value.toString())
                }

                binding.userTopSeekBarHeight.progress=value
                defaultValues.userTop_height = value

//                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
            }
        }
        binding.userBottomEditNumberWeight.doAfterTextChanged {

            if(binding.userBottomEditNumberWeight.text.toString()!="") {
                var value = Integer.parseInt(binding.userBottomEditNumberWeight.text.toString())

                if(value > binding.userBottomSeekBarWeight.max) {
                    value = binding.userBottomSeekBarWeight.max
                    binding.userBottomEditNumberWeight.setText(value.toString())
                }
                if(value < binding.userBottomSeekBarWeight.min) {
                    value = binding.userBottomSeekBarWeight.min
                    binding.userBottomEditNumberWeight.setText(value.toString())
                }

                binding.userBottomSeekBarWeight.progress=value
                defaultValues.userBottom_weight = value

//                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
            }
        }
        binding.userBottomEditNumberHeight.doAfterTextChanged {

            if(binding.userBottomEditNumberHeight.text.toString()!="") {
                var value = Integer.parseInt(binding.userBottomEditNumberHeight.text.toString())

                if(value > binding.userBottomSeekBarHeight.max) {
                    value = binding.userBottomSeekBarHeight.max
                    binding.userBottomEditNumberHeight.setText(value.toString())
                }
                if(value < binding.userBottomSeekBarHeight.min) {
                    value = binding.userBottomSeekBarHeight.min
                    binding.userBottomEditNumberHeight.setText(value.toString())
                }

                binding.userBottomSeekBarHeight.progress=value
                defaultValues.userBottom_height = value

//                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
            }
        }


        binding.userTopSeekBarWeight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    binding.userTopEditNumberWeight.setText(progress.toString())
                    binding.userTopEditNumberWeight.setSelection(binding.userTopEditNumberWeight.length())
//                    clearFocusFromButtons()
//                    setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
                }
//                HandlerUpdateIcons(0)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.userTopSeekBarHeight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    binding.userTopEditNumberHeight.setText(progress.toString())
                    binding.userTopEditNumberHeight.setSelection(binding.userTopEditNumberHeight.length())
//                    clearFocusFromButtons()
//                    setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
                }
//                HandlerUpdateIcons(0)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.userBottomSeekBarWeight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    binding.userBottomEditNumberWeight.setText(progress.toString())
                    binding.userBottomEditNumberWeight.setSelection(binding.userBottomEditNumberWeight.length())
//                    clearFocusFromButtons()
//                    setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
                }
//                HandlerUpdateIcons(0)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.userBottomSeekBarHeight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    binding.userBottomEditNumberHeight.setText(progress.toString())
                    binding.userBottomEditNumberHeight.setSelection(binding.userBottomEditNumberHeight.length())
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
                    var size :Int = Integer.parseInt(binding.userTopEditNumberWeight.text.toString())
                    if(increaseValue)
                    {
                        size += onHoldTotalValue(size, increase = true, decrease = false)
                    }else{
                        size -=onHoldTotalValue(size, increase = false, decrease = true)
                    }
                    binding.userTopEditNumberWeight.setText(size.toString())
                }
                2->{
                    var size :Int = Integer.parseInt(binding.userTopEditNumberHeight.text.toString())
                    if(increaseValue)
                    {
                        size += onHoldTotalValue(size, increase = true, decrease = false)
                    }else{
                        size -= onHoldTotalValue(size, increase = false, decrease = true)
                    }
                    binding.userTopEditNumberHeight.setText(size.toString())
                }
                3->{
                    var size :Int = Integer.parseInt(binding.userBottomEditNumberWeight.text.toString())
                    if(increaseValue)
                    {
                        size += onHoldTotalValue(size, increase = true, decrease = false)
                    }else{
                        size -= onHoldTotalValue(size, increase = false, decrease = true)
                    }
                    binding.userBottomEditNumberWeight.setText(size.toString())
//                    setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
                }
                4->{
                    var size :Int = Integer.parseInt(binding.userBottomEditNumberHeight.text.toString())
                    if(increaseValue)
                    {
                        size += onHoldTotalValue(size, increase = true, decrease = false)
                    }else{
                        size -= onHoldTotalValue(size, increase = false, decrease = true)
                    }
                    binding.userBottomEditNumberHeight.setText(size.toString())
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
        binding.userTopButtonWeightMinus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.userTopEditNumberWeight.text.toString())
            size -= 1
            binding.userTopEditNumberWeight.setText(size.toString())
        }
        binding.userTopButtonWeightMinus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.userTopButtonWeightMinus.setOnLongClickListener{
            valueToModify = 1
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }

        //Weight +
        binding.userTopButtonWeightPlus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.userTopEditNumberWeight.text.toString())
            size += 1
            binding.userTopEditNumberWeight.setText(size.toString())
        }
        binding.userTopButtonWeightPlus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.userTopButtonWeightPlus.setOnLongClickListener {
            valueToModify = 1
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }
//User Top
        //UserTop
        //Height_tandem -
        binding.userTopButtonHeightMinus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.userTopEditNumberHeight.text.toString())
            size -= 1
            binding.userTopEditNumberHeight.setText(size.toString())
        }
        binding.userTopButtonHeightMinus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.userTopButtonHeightMinus.setOnLongClickListener{
            valueToModify = 2
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }

        //Weight_tandem +
        binding.userTopButtonHeightPlus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.userTopEditNumberHeight.text.toString())
            size += 1
            binding.userTopEditNumberHeight.setText(size.toString())
        }
        binding.userTopButtonHeightPlus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.userTopButtonHeightPlus.setOnLongClickListener {
            valueToModify = 2
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }

//User Bottom
        //Equipment -
        binding.userBottomButtonWeightMinus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.userBottomEditNumberWeight.text.toString())
            size -= 1
            binding.userBottomEditNumberWeight.setText(size.toString())
        }
        binding.userBottomButtonWeightMinus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.userBottomButtonWeightMinus.setOnLongClickListener {
            valueToModify = 3
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }
        //Equipment +
        binding.userBottomButtonWeightPlus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.userBottomEditNumberWeight.text.toString())
            size += 1
            binding.userBottomEditNumberWeight.setText(size.toString())
        }
        binding.userBottomButtonWeightPlus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.userBottomButtonWeightPlus.setOnLongClickListener {
            valueToModify = 3
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }

        //Canopy
        //Canopy  -
        binding.userBottomButtonHeightMinus.setOnClickListener {

            var size :Int = Integer.parseInt(binding.userBottomEditNumberHeight.text.toString())
            size -= 1
            binding.userBottomEditNumberHeight.setText(size.toString())
//            setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
        }
        binding.userBottomButtonHeightMinus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.userBottomButtonHeightMinus.setOnLongClickListener {
            valueToModify = 4
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }

        //Canopy  +
        binding.userBottomButtonHeightPlus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.userBottomEditNumberHeight.text.toString())
            size += 1
            binding.userBottomEditNumberHeight.setText(size.toString())
//            setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.canopy,defaultValues.unit_KG)
        }
        binding.userBottomButtonHeightPlus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.userBottomButtonHeightPlus.setOnLongClickListener {
            valueToModify = 4
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }

//*Handling of +/- buttons for all values
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////


        binding.button3Result.setOnClickListener() {

            //    Tight-Fitting Athletic Clothing (e.g., Spandex, Lycra): 0.1
            //    Regular Casual Clothing (e.g., Jeans and T-Shirt):0.7
            //    Wingsuit: 1 - 1.5
            //    Winter Coat and Heavier Clothing: 1 - 1.2
            val result = calculateAcceleration(
                binding.userTopEditNumberHeight.text.toString().toDouble()/100,
                binding.userBottomEditNumberHeight.text.toString().toDouble()/100,
                binding.userTopEditNumberWeight.text.toString().toDouble(),
                binding.userBottomEditNumberWeight.text.toString().toDouble(),
                1.0,
                1.0
            )
            binding.textResult.setText(result.toString())

        }


        //Animation enabled for constraint container of all other constraintLayouts
        //Side Note:   android:animateLayoutChanges="true" needed aswell
        binding.MainConstraintLayout2.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

//        setBackground()



        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init()
    {
        TransitionManager.beginDelayedTransition(binding.userTopWeightConstraintLayout, AutoTransition())
        TransitionManager.beginDelayedTransition(binding.userBottomWeightConstraintLayout, AutoTransition())
    }


    //Set background
    private fun setBackground()
    {
        when((activity as StartingActivity?)?.getImageSelecter())
        {
            1->binding.layoutMainForBackground2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds12, null)
            2->binding.layoutMainForBackground2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds22, null)
            3->binding.layoutMainForBackground2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds32, null)
            4->binding.layoutMainForBackground2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds42, null)
            5->binding.layoutMainForBackground2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds52, null)
            6->binding.layoutMainForBackground2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds62, null)
            7->binding.layoutMainForBackground2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds72, null)
            8->binding.layoutMainForBackground2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds82, null)
            else -> {
                binding.layoutMainForBackground2.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds12, null)
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
        editor.putInt("userTop_weight",    binding.userTopEditNumberWeight.text.toString().toInt())
        editor.putInt("userTop_height", binding.userTopEditNumberHeight.text.toString().toInt())
//        editor.putInt("userTop_suit", binding.userTopEditNumberWeight.text.toString().toInt())
        editor.putInt("userBottom_weight",    binding.userBottomEditNumberWeight.text.toString().toInt())
        editor.putInt("userBottom_height", binding.userBottomEditNumberHeight.text.toString().toInt())
//        editor.putInt("userBottom_suit", binding.editNumberEquipment.text.toString().toInt())
        editor.putBoolean("kg", binding.radioButtonKG2.isChecked)
        editor.putBoolean("lbs", binding.radioButtonLBS2.isChecked)
        editor.putBoolean("SAVED", true)
        editor.apply()

    }
//*Function used to save current values in the app
////////////////////////////////////////////////////////////////////////////////////////////////////


}
