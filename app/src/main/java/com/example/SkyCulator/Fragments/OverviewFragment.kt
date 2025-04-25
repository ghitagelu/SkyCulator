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
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.SkyCulator.R
import com.example.SkyCulator.StartingActivity
import com.example.SkyCulator.databinding.FragmentOverviewBinding
import kotlin.math.roundToInt
import android.widget.EditText

/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {

//    private lateinit var binding: View
//    private var clickedonloadtextview =false
    private var init = true //used for the init of Load seekbar progress
    lateinit var shared_preferences_save: SharedPreferences

    private var _binding: FragmentOverviewBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!



    //Table of content for the type of actions
    //
    // valueToModify = 1  -weight                increaseValue = false - "-" operation
    // valueToModify = 2  -equipment             increaseValue = true - "+" operation
    // valueToModify = 3  -canopy
    // valueToModify = 4  -load
    // valueToModify = 5  -weight_tandem

    var valueToModify = 0
    var increaseValue = true
    var cowntdowninterval:Long = 650
    var load_overflow :Boolean= false
    var load_underflow :Boolean= false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        Log.d("App stages", "Fragment Overview starting")
        shared_preferences_save =this.activity!!.getSharedPreferences("save_calculator_values", Context.MODE_PRIVATE)
//        binding.layoutMainForBackground.setBackgroundResource(R.drawable.clouds11)


        class SeekBarLimits(
            var seekBarWeight_min: Int,        var seekBarWeight_max: Int,
            var seekBarWeightTandem_min: Int, var seekBarWeightTandem_max: Int,
            var seekBarEquipment_min :Int,     var seekBarEquipment_max :Int,
            var seekBarCanopy_min :Int,        var seekBarCanopy_max :Int
        )
         class UserValues(
            var weight: Int,
            var weight_tandem: Int,
            var equipment: Int,
            var equipment_tandem: Int,
            var canopy: Int,
            var canopy_tandem: Int,
            var unit_KG: Boolean,
            var unit_LBS: Boolean
        )
        {
            //Handling of changing the units types
            fun setCalculatorValues()
            {
                var tandem = binding.checkBoxTandem.isChecked
                if(unit_KG)
                {
                    setUnitsKG(tandem)
                }
                if(unit_LBS)
                {
                    setUnitsLBS(tandem)
                }
//
                binding.editNumberWeight.setText(weight.toString())
                binding.editNumberWeightTandem.setText(weight_tandem.toString())
                if(tandem){
                    binding.editNumberEquipment.setText(equipment_tandem.toString())
                    binding.editNumberCanopy.setText(canopy_tandem.toString())
                }else{
                    binding.editNumberEquipment.setText(equipment.toString())
                    binding.editNumberCanopy.setText(canopy.toString())
                }

                setCalculatorWingLoading(weight,weight_tandem,equipment,equipment_tandem,canopy,canopy_tandem,unit_KG)

                binding.seekBarWeight.progress =weight
                binding.seekBarWeightTandem.progress =weight_tandem

                if(tandem){
                    binding.seekBarEquipment.progress =equipment_tandem
                    binding.seekBarCanopy.progress =canopy_tandem
                }else{
                    binding.seekBarEquipment.progress =equipment
                    binding.seekBarCanopy.progress =canopy
                }

                handlingOfJumpsConstraintLayout()
                InitIcons ()
                expand_tandem()

            }
            fun setUnitsKG(tandem: Boolean)
            {
                binding.radioButtonKG.isChecked=true
                binding.textViewWeightUnits.text=" kg"
                binding.textViewWeightTandemUnits.text=" kg"
                binding.textViewEquipmentUnits.text = " kg"

//Values for tandem
                //If tandem = false
                var expand_seekBarEquipment_min  = 0
                var expand_seekBarEquipment_max  = 0
                var expand_seekBarCanopy_min     = 0
                var expand_seekBarCanopy_max     = 0
                //If tandem = true
                if(tandem)
                {
                     expand_seekBarEquipment_min  = 10
                     expand_seekBarEquipment_max  = 20
                     expand_seekBarCanopy_min     = 200
                     expand_seekBarCanopy_max     = 100
                }
//*Values for tandem

//Set seekbar limits
                //Create limits value for seekbar
                val defaultSeekBarLimits = SeekBarLimits(
                    45,136 ,
                    45,136 ,
                    5 + expand_seekBarEquipment_min,25 + expand_seekBarEquipment_max,
                    50 + expand_seekBarCanopy_min,350 + expand_seekBarCanopy_max)
                //Call to set the limits for the seek bar
                setSeekBarLimits(defaultSeekBarLimits)
            }

            fun setUnitsLBS(tandem: Boolean)
            {
                binding.radioButtonLBS.isChecked=true
                binding.textViewWeightUnits.text=" lbs"
                binding.textViewWeightTandemUnits.text=" lbs"
                binding.textViewEquipmentUnits.text = " lbs"

//Values for tandem
                //If tandem = false
                var expand_seekBarEquipment_min  = 0
                var expand_seekBarEquipment_max  = 0
                var expand_seekBarCanopy_min     = 0
                var expand_seekBarCanopy_max     = 0
                //If tandem = true
                if(tandem)
                {
                    expand_seekBarEquipment_min  = 22
                    expand_seekBarEquipment_max  = 45
                    expand_seekBarCanopy_min     = 200
                    expand_seekBarCanopy_max     = 100
                }
//*Values for tandem
                val defaultSeekBarLimits = SeekBarLimits(
                    99,300,
                    99,300,
                    11 + expand_seekBarEquipment_min,55 + expand_seekBarEquipment_max,
                    50 + expand_seekBarCanopy_min,350 + expand_seekBarCanopy_max)

                setSeekBarLimits(defaultSeekBarLimits)
            }

            fun setSeekBarLimits(defaultSeekBarLimits:SeekBarLimits)
            {
                binding.seekBarWeight.min = defaultSeekBarLimits.seekBarWeight_min
                binding.seekBarWeight.max = defaultSeekBarLimits.seekBarWeight_max
                binding.seekBarWeight.secondaryProgress = 0
                binding.seekBarWeightTandem.min = defaultSeekBarLimits.seekBarWeightTandem_min
                binding.seekBarWeightTandem.max = defaultSeekBarLimits.seekBarWeightTandem_max
                binding.seekBarWeightTandem.secondaryProgress = 0
                binding.seekBarEquipment.min = defaultSeekBarLimits.seekBarEquipment_min
                binding.seekBarEquipment.max= defaultSeekBarLimits.seekBarEquipment_max
                binding.seekBarEquipment.secondaryProgress = 0
                binding.seekBarCanopy.min = defaultSeekBarLimits.seekBarCanopy_min
                binding.seekBarCanopy.max = defaultSeekBarLimits.seekBarCanopy_max
            }
        }
//////////////////////////////////////////////////////////////////////////////////////////////////// I N I T I A L I S I N G ////////////////////////////////////////
             val defaultValues = UserValues(
                shared_preferences_save.getInt("Weight",110),
                shared_preferences_save.getInt("Weight_tandem",110),
                shared_preferences_save.getInt("Equipment",10),
                shared_preferences_save.getInt("Equipment_tandem",30),
                shared_preferences_save.getInt("Canopy",178),
                shared_preferences_save.getInt("Canopy_tandem",278),
                unit_KG = shared_preferences_save.getBoolean("kg", true),
                unit_LBS = shared_preferences_save.getBoolean("lbs", false)
            )


        //Make binding = this fragment
//        binding =inflater.inflate(R.layout.fragment_overview, container, false)

//Set initial valuse
        //Set "Tandem" checkbox false on 1st run, then get the saved value from last use
        binding.checkBoxTandem.isChecked = shared_preferences_save.getBoolean("Tandem_checked",  false)
        //Set default values
        defaultValues.setCalculatorValues()
////////////////////////////////////////////////////////////////////////////////////////////////////*I N I T I A L I S I N G ////////////////////////////////////////
//Radio buttons handling
        binding.radioGroup.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked == binding.radioButtonKG.id) {
            //Kilograms
                defaultValues.weight = convertLBStoKG(defaultValues.weight)
                defaultValues.weight_tandem = convertLBStoKG(defaultValues.weight_tandem)

                    defaultValues.equipment_tandem = convertLBStoKG(defaultValues.equipment_tandem)
                    defaultValues.equipment = convertLBStoKG(defaultValues.equipment)

                defaultValues.unit_KG = true
                defaultValues.unit_LBS= false
                defaultValues.setCalculatorValues()
                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.equipment_tandem,defaultValues.canopy,defaultValues.canopy_tandem,defaultValues.unit_KG)

            }

            if(isChecked == binding.radioButtonLBS.id) {
            //LBS
                defaultValues.weight = convertKGtoLBS(defaultValues.weight)
                defaultValues.weight_tandem = convertKGtoLBS(defaultValues.weight_tandem)

                    defaultValues.equipment_tandem = convertKGtoLBS(defaultValues.equipment_tandem)

                    defaultValues.equipment = convertKGtoLBS(defaultValues.equipment)

                defaultValues.unit_KG = false
                defaultValues.unit_LBS= true
                defaultValues.setCalculatorValues()
                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.equipment_tandem,defaultValues.canopy,defaultValues.canopy_tandem,defaultValues.unit_KG)
            }
            saveData()
        }
//*Radio buttons handling
////////////////////////////////////////////////////////////////////////////////////////////////////
//Tandem checkbox handling
        binding.checkBoxTandem.setOnCheckedChangeListener { buttonView, isChecked ->
            defaultValues.setCalculatorValues()

            if(binding.checkBoxTandem.isChecked) {

                Toast.makeText(
                    activity,
                    "TANDEM: ",

                    Toast.LENGTH_SHORT
                ).show()

            }
            //show the weight_tandem constraintLayout
            expand_tandem()

            saveData()
        }
//*Tandem checkbox handling
////////////////////////////////////////////////////////////////////////////////////////////////////
//Handling of TextView
        //Weight :Text field handling
        binding.editNumberWeight.doAfterTextChanged {

            if(binding.editNumberWeight.text.toString()!="") {
                var value = Integer.parseInt(binding.editNumberWeight.text.toString())

                if(value > binding.seekBarWeight.max) {
                    value = binding.seekBarWeight.max
                    binding.editNumberWeight.setText(value.toString())
                }
                if(value < binding.seekBarWeight.min) {
                    value = binding.seekBarWeight.min
                    binding.editNumberWeight.setText(value.toString())
                }

                binding.seekBarWeight.progress=value
                defaultValues.weight = value

                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.equipment_tandem,defaultValues.canopy,defaultValues.canopy_tandem,defaultValues.unit_KG)
            }
        }
        //Weight_tandem :Text field handling
        binding.editNumberWeightTandem.doAfterTextChanged {

            if(binding.editNumberWeightTandem.text.toString()!="") {
                var value = Integer.parseInt(binding.editNumberWeightTandem.text.toString())

                if(value > binding.seekBarWeightTandem.max) {
                    value = binding.seekBarWeightTandem.max
                    binding.editNumberWeightTandem.setText(value.toString())
                }
                if(value < binding.seekBarWeightTandem.min) {
                    value = binding.seekBarWeightTandem.min
                    binding.editNumberWeightTandem.setText(value.toString())
                }

                binding.seekBarWeightTandem.progress=value
                defaultValues.weight_tandem = value

                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.equipment_tandem,defaultValues.canopy,defaultValues.canopy_tandem,defaultValues.unit_KG)
            }
        }

        //Equipment :Text field handling
        binding.editNumberEquipment.doAfterTextChanged {

            if(binding.editNumberEquipment.text.toString()!="") {
                var value = Integer.parseInt(binding.editNumberEquipment.text.toString())

                if(value > binding.seekBarEquipment.max) {
                    value = binding.seekBarEquipment.max
                    binding.editNumberEquipment.setText(value.toString())
                }
                if(value < binding.seekBarEquipment.min) {
                    value = binding.seekBarEquipment.min
                    binding.editNumberEquipment.setText(value.toString())
                }

                binding.seekBarEquipment.progress=value

                if(binding.checkBoxTandem.isChecked)
                {
                    defaultValues.equipment_tandem = value
                }else{
                    defaultValues.equipment = value
                }


                setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.equipment_tandem,defaultValues.canopy,defaultValues.canopy_tandem,defaultValues.unit_KG)
            }
        }

        //Canopy :Text field handling
        binding.editNumberCanopy.doAfterTextChanged {

            if(binding.editNumberCanopy.text.toString()!="") {
                var value = Integer.parseInt(binding.editNumberCanopy.text.toString())

                if(value > binding.seekBarCanopy.max) {
                    value = binding.seekBarCanopy.max
                    binding.editNumberCanopy.setText(value.toString())
                }
                if(value < binding.seekBarCanopy.min) {
                    value = binding.seekBarCanopy.min
                    binding.editNumberCanopy.setText(value.toString())
                }

                binding.seekBarCanopy.progress=value
                if(binding.checkBoxTandem.isChecked)
                {
                    defaultValues.canopy_tandem = value
                }else{
                    defaultValues.canopy = value
                }
            }

        }

        //Load :Text field handling
        binding.editNumberLoad.doAfterTextChanged() {

            if(binding.editNumberLoad.text.toString()!="") {
                var value = ((binding.editNumberLoad.text.toString()).toDouble()*100).toInt()

                if(value > binding.seekBarLoad.max) {
                    value = binding.seekBarLoad.max
                    binding.editNumberLoad.setText((value.toDouble()/100).toString())
                }
                load_overflow = value == binding.seekBarLoad.max

                if(value < binding.seekBarLoad.min) {
                    load_underflow = true
                    value = binding.seekBarLoad.min
                    binding.editNumberLoad.setText((value.toDouble()/100).toString())
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
            var progress = binding.seekBarLoad.progress
            if(nochange) {/*D O  N O T H I N G*/    }
            if(increase)
            {
                if(onHold)
                {
                    binding.seekBarLoad.progress = progress + onHoldTotalValue(progress,increase = true, decrease = false)
                }
                else
                {
                    binding.seekBarLoad.progress = progress + 1
                }
            }else if(decrease)
            {
                if(onHold)
                {
                    binding.seekBarLoad.progress = progress - onHoldTotalValue(progress, increase = false, decrease = true)
                }
                else
                {
                    binding.seekBarLoad.progress = progress - 1
                }
            }

            progress = binding.seekBarLoad.progress
            binding.editNumberLoad.setText((progress.toDouble() / 100).toString())
            binding.editNumberLoad.setSelection(binding.editNumberLoad.length())

            val value = ((binding.editNumberLoad.text.toString()).toDouble() * 100).toInt()

            setCalculatorWingSize(
                defaultValues.weight,
                defaultValues.weight_tandem,
                defaultValues.equipment,
                defaultValues.equipment_tandem,
                value,
                defaultValues.unit_KG
            )
            clearFocusFromButtons()
        }

        //Weight seekBar
        binding.seekBarWeight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    binding.editNumberWeight.setText(progress.toString())
                    binding.editNumberWeight.setSelection(binding.editNumberWeight.length())
                    clearFocusFromButtons()
                    setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.equipment_tandem,defaultValues.canopy,defaultValues.canopy_tandem,defaultValues.unit_KG)
                }
                HandlerUpdateIcons(0)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        //Weight_tandem seekBar
        binding.seekBarWeightTandem.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    binding.editNumberWeightTandem.setText(progress.toString())
                    binding.editNumberWeightTandem.setSelection(binding.editNumberWeightTandem.length())
                    clearFocusFromButtons()
                    setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.equipment_tandem,defaultValues.canopy,defaultValues.canopy_tandem,defaultValues.unit_KG)
                }
                HandlerUpdateIcons(4)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //Equipment seekBar
        binding.seekBarEquipment.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    binding.editNumberEquipment.setText(progress.toString())
                    binding.editNumberEquipment.setSelection(binding.editNumberEquipment.length())
                    clearFocusFromButtons()
                    setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.equipment_tandem,defaultValues.canopy,defaultValues.canopy_tandem,defaultValues.unit_KG)
                }
                HandlerUpdateIcons(1)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //Canopy seekBar
        binding.seekBarCanopy.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    binding.editNumberCanopy.setText(progress.toString())
                    binding.editNumberCanopy.setSelection(binding.editNumberCanopy.length())
                    clearFocusFromButtons()
                    setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.equipment_tandem,defaultValues.canopy,defaultValues.canopy_tandem,defaultValues.unit_KG)
                }
                HandlerUpdateIcons(2)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //Load seekBar
        binding.seekBarLoad.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    updateLoad( nochange = false, increase = false, decrease = false, onHold = false)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


////////////////////////////////////////////////////////////////////////////////////////////////////

//Handling of +/- buttons for all values

        fun updateValues() {
            fun updateField(editText: EditText, defaultValue: Int? = null, isCanopy: Boolean = false) {
                val currentValue = editText.text.toString().toIntOrNull() ?: return
                val modifier = onHoldTotalValue(currentValue, increase = increaseValue, decrease = !increaseValue)
                val newValue = if (increaseValue) currentValue + modifier else currentValue - modifier
                editText.setText(newValue.toString())

                if (isCanopy && defaultValue != null) {
                    setCalculatorWingLoading(
                        defaultValues.weight,
                        defaultValues.weight_tandem,
                        defaultValues.equipment,
                        defaultValues.equipment_tandem,
                        defaultValues.canopy,
                        defaultValues.canopy_tandem,
                        defaultValues.unit_KG
                    )
                }
            }

            when (valueToModify) {
                1 -> updateField(binding.editNumberWeight)
                2 -> updateField(binding.editNumberEquipment)
                3 -> updateField(binding.editNumberCanopy, defaultValue = defaultValues.canopy, isCanopy = true)
                4 -> updateLoad(nochange = false, increase = increaseValue, decrease = !increaseValue, onHold = true)
                5 -> updateField(binding.editNumberWeightTandem)
                else -> {
                    // No action
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
        binding.buttonWeightMinus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.editNumberWeight.text.toString())
            size -= 1
            binding.editNumberWeight.setText(size.toString())
        }
        binding.buttonWeightMinus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.buttonWeightMinus.setOnLongClickListener{
             valueToModify = 1
             increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }

        //Weight +
        binding.buttonWeightPlus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.editNumberWeight.text.toString())
            size += 1
            binding.editNumberWeight.setText(size.toString())
        }
        binding.buttonWeightPlus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.buttonWeightPlus.setOnLongClickListener {
            valueToModify = 1
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }

    //Weight_tandem
        //Weight_tandem -
        binding.buttonWeightTandemMinus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.editNumberWeightTandem.text.toString())
            size -= 1
            binding.editNumberWeightTandem.setText(size.toString())
        }
        binding.buttonWeightTandemMinus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.buttonWeightTandemMinus.setOnLongClickListener{
             valueToModify = 5
             increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }

        //Weight_tandem +
        binding.buttonWeightTandemPlus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.editNumberWeightTandem.text.toString())
            size += 1
            binding.editNumberWeightTandem.setText(size.toString())
        }
        binding.buttonWeightTandemPlus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.buttonWeightTandemPlus.setOnLongClickListener {
            valueToModify = 5
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }

    //Equipment
        //Equipment -
        binding.buttonEquipmentMinus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.editNumberEquipment.text.toString())
            size -= 1
            binding.editNumberEquipment.setText(size.toString())
        }
        binding.buttonEquipmentMinus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.buttonEquipmentMinus.setOnLongClickListener {
            valueToModify = 2
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }
        //Equipment +
        binding.buttonEquipmentPlus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.editNumberEquipment.text.toString())
            size += 1
            binding.editNumberEquipment.setText(size.toString())
        }
        binding.buttonEquipmentPlus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.buttonEquipmentPlus.setOnLongClickListener {
            valueToModify = 2
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }

    //Canopy
        //Canopy  -
        binding.buttonCanopyMinus.setOnClickListener {

            var size :Int = Integer.parseInt(binding.editNumberCanopy.text.toString())
            size -= 1
            binding.editNumberCanopy.setText(size.toString())
            setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.equipment_tandem,defaultValues.canopy,defaultValues.canopy_tandem,defaultValues.unit_KG)
        }
        binding.buttonCanopyMinus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.buttonCanopyMinus.setOnLongClickListener {
            valueToModify = 3
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }

        //Canopy  +
        binding.buttonCanopyPlus.setOnClickListener {
            var size :Int = Integer.parseInt(binding.editNumberCanopy.text.toString())
            size += 1
            binding.editNumberCanopy.setText(size.toString())
            setCalculatorWingLoading(defaultValues.weight,defaultValues.weight_tandem,defaultValues.equipment,defaultValues.equipment_tandem,defaultValues.canopy,defaultValues.canopy_tandem,defaultValues.unit_KG)
        }
        binding.buttonCanopyPlus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.buttonCanopyPlus.setOnLongClickListener {
            valueToModify = 3
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }

    //Load
        //Load -
        binding.buttonLoadMinus.setOnClickListener {
            updateLoad(nochange = false, increase = false, decrease = true, onHold = false)
        }
        binding.buttonLoadMinus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.buttonLoadMinus.setOnLongClickListener {
            valueToModify = 4
            increaseValue = false
            weightCounter.start()
            return@setOnLongClickListener true
        }
        //Load +
        binding.buttonLoadPlus.setOnClickListener {
            updateLoad(nochange = false, increase = true, decrease = false, onHold = false)
        }
        binding.buttonLoadPlus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                weightCounter.cancel()
            }
            false
        }
        binding.buttonLoadPlus.setOnLongClickListener {
            valueToModify = 4
            increaseValue = true
            weightCounter.start()
            return@setOnLongClickListener true
        }
//*Handling of +/- buttons for all values
////////////////////////////////////////////////////////////////////////////////////////////////////
        //Animation enabled for constraint container of all other constraintLayouts
        //Side Note:   android:animateLayoutChanges="true" needed aswell
        binding.MainConstraintLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        
        
        clearFocusFromButtons()
        //required to update fragment
        setBackground()



        val view = binding.root
        return view
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
        binding.editNumberWeight.clearFocus()
        binding.editNumberWeightTandem.clearFocus()
        binding.editNumberEquipment.clearFocus()
        binding.editNumberCanopy.clearFocus()
        binding.editNumberLoad.clearFocus()
    }
//*Close onScreen keyboard when user presses something else
////////////////////////////////////////////////////////////////////////////////////////////////////
//Function used to save current values in the app
    private fun saveData()
    {
        val editor: SharedPreferences.Editor = shared_preferences_save.edit()
        editor.putInt("Weight",    binding.editNumberWeight.text.toString().toInt())
        editor.putInt("Weight_tandem", binding.editNumberWeightTandem.text.toString().toInt())

        if(binding.checkBoxTandem.isChecked){
            editor.putInt("Equipment_tandem", binding.editNumberEquipment.text.toString().toInt())
            editor.putInt("Canopy_tandem",    binding.editNumberCanopy.text.toString().toInt())
        }
        else{
            editor.putInt("Equipment", binding.editNumberEquipment.text.toString().toInt())
            editor.putInt("Canopy",    binding.editNumberCanopy.text.toString().toInt())
        }

        editor.putBoolean("Tandem_checked", binding.checkBoxTandem.isChecked)
        editor.putBoolean("kg", binding.radioButtonKG.isChecked)
        editor.putBoolean("lbs", binding.radioButtonLBS.isChecked)
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
    private fun setCalculatorWingLoading(weight:Int, weightTandem:Int, equipment: Int,equipmentTandem: Int, canopy:Int,canopyTandem:Int, unit_KG:Boolean) {

        val wingLoading:Double
        var totalWeight=weight

        if(binding.checkBoxTandem.isChecked)
        {
            totalWeight += weightTandem + equipmentTandem
            if(unit_KG){
                totalWeight= convertKGtoLBS(totalWeight)
            }
            wingLoading= totalWeight.toDouble()/canopyTandem.toDouble()
        }else
        {
            totalWeight += equipment
            if(unit_KG){
                totalWeight= convertKGtoLBS(totalWeight)
            }
            wingLoading= totalWeight.toDouble()/canopy.toDouble()

        }

        val result = (wingLoading *100).toInt()
        binding.editNumberLoad.setText((result.toDouble()/100).toString())
        binding.seekBarLoad.progress = result

        if(init)//init of load seekbar
        {
        var value = ((binding.editNumberLoad.text.toString()).toDouble()*100).toInt()

        if(value > binding.seekBarLoad.max) {
            value = binding.seekBarLoad.max
            binding.editNumberLoad.setText((value.toDouble()/100).toString())

        }
        binding.seekBarLoad.progress=value
        init = false
        }
        saveData()
    }
    //Updates the value of canopy size after the modification of risk value
    private fun setCalculatorWingSize(weight:Int, weight_tandem:Int, equipment: Int,equipmentTandem: Int, wingLoading:Int, unit_KG:Boolean) {
        val canopy:Double

        var totalWeight=weight

        if(binding.checkBoxTandem.isChecked)
        {
            totalWeight += weight_tandem + equipmentTandem
        }else{
            totalWeight += equipment
        }

        if(unit_KG){
            totalWeight= convertKGtoLBS(totalWeight)
        }
        canopy= totalWeight.toDouble()/(wingLoading.toDouble())
        val temp = (canopy *100).toInt().toString()

        binding.editNumberCanopy.setText(temp)
        saveData()
    }
//*Calculator values updater
////////////////////////////////////////////////////////////////////////////////////////////////////
//Handling of jump level
    //Update level of jumps
    private fun handlingOfJumpsConstraintLayout()
    {
        val loadValue :Int = ((binding.editNumberLoad.text.toString()).toDouble()*100).toInt()

        when(updateJumpValue(loadValue)){
            -1 ->{

            }
            0->{
                //Result bar
                binding.textViewJumpsLevel.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.result_bar_shape_and_result_color_0, null)
                binding.textViewJumpsLevel.text ="BASIC: 0-200 JUMPS"
                //Seek bar progress
                binding.seekBarWeight.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_0, null)
                binding.seekBarWeightTandem.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_0, null)
                binding.seekBarEquipment.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_0, null)
                binding.seekBarCanopy.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_0, null)
                binding.seekBarLoad.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_0, null)
               //thumbs icon change
                if (load_underflow){

                    binding.seekBarLoad.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_load_0, null)
                }else{
                    binding.seekBarLoad.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_load_0, null)
                }
            }

            1->{
                //Result bar
                binding.textViewJumpsLevel.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.result_bar_shape_and_result_color_1, null)
                binding.textViewJumpsLevel.text ="INTERMEDIATE: 200-600 JUMPS"
                //Seek bar progress
                binding.seekBarWeight.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_1, null)
                binding.seekBarWeightTandem.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_1, null)
                binding.seekBarEquipment.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_1, null)
                binding.seekBarCanopy.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_1, null)
                binding.seekBarLoad.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_1, null)
                //thumbs icon change
                binding.seekBarLoad.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_load_1, null)
            }

            2->{
                //Result bar
                binding.textViewJumpsLevel.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.result_bar_shape_and_result_color_2, null)
                binding.textViewJumpsLevel.text ="ADVANCED: 600-1500 JUMPS"
                //Seek bar progress
                binding.seekBarWeight.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_2, null)
                binding.seekBarWeightTandem.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_2, null)
                binding.seekBarEquipment.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_2, null)
                binding.seekBarCanopy.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_2, null)
                binding.seekBarLoad.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_2, null)
                //thumbs icon change
                binding.seekBarLoad.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_load_2, null)
            }

            3->{
                //Result bar
                binding.textViewJumpsLevel.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.result_bar_shape_and_result_color_3, null)
                binding.textViewJumpsLevel.text ="EXPERT: 1500+ JUMPS"
                //Seek bar progress
                binding.seekBarWeight.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_3, null)
                binding.seekBarWeightTandem.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_3, null)
                binding.seekBarEquipment.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_3, null)
                binding.seekBarCanopy.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_3, null)
                binding.seekBarLoad.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_3, null)
                //thumbs icon change
                binding.seekBarLoad.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_load_3, null)
            }

            4->{
                //Result bar
                binding.textViewJumpsLevel.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.result_bar_shape_and_result_color_4, null)
                binding.textViewJumpsLevel.text ="EXPERT: 1500+ JUMPS"
                //Seek bar progress
                binding.seekBarWeight.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_4, null)
                binding.seekBarWeightTandem.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_4, null)
                binding.seekBarEquipment.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_4, null)
                binding.seekBarCanopy.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_4, null)
                binding.seekBarLoad.progressDrawable = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.seek_bar_calculator_fragment_color_4, null)
                //thumbs icon change
                binding.seekBarLoad.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_load_alt, null)

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
            1->binding.layoutMainForBackground.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds11, null)
            2->binding.layoutMainForBackground.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds21, null)
            3->binding.layoutMainForBackground.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds31, null)
            4->binding.layoutMainForBackground.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds41, null)
            5->binding.layoutMainForBackground.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds51, null)
            6->binding.layoutMainForBackground.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds61, null)
            7->binding.layoutMainForBackground.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds71, null)
            8->binding.layoutMainForBackground.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds81, null)
            else -> {
                binding.layoutMainForBackground.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.clouds11, null)
            }
        }
    }

//Handling of seek bar thumbs icons updating on certain progress
    private fun InitIcons ()
    {
        //Init animation for constraintLayouts //enable animation transition from kg to lbs and vice-versa
        TransitionManager.beginDelayedTransition(binding.WeightConstraintLayout, AutoTransition())
        TransitionManager.beginDelayedTransition(binding.WeightTandemConstraintLayout, AutoTransition())
        TransitionManager.beginDelayedTransition(binding.EquipmentConstraintLayout, AutoTransition())

        //weight
        HandlerUpdateIcons(0)
        //weight_tandem
        HandlerUpdateIcons(4)
        //equipment
        HandlerUpdateIcons(1)
        //canopy
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
                progress_max = binding.seekBarWeight.max
                progress_min = binding.seekBarWeight.min
                progress     = binding.seekBarWeight.progress
                when (progress) {
                    progress_min -> {
                        binding.seekBarWeight.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_0, null)
                    }
                    progress_max -> {
                        binding.seekBarWeight.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_3, null)
                    }
                    else -> {
                        when(getPercent(progress - progress_min,progress_max - progress_min)) {
                            0->{binding.seekBarWeight.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_0, null)}
                            1->{binding.seekBarWeight.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_1, null)}
                            2->{binding.seekBarWeight.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_2, null)}
                            3->{binding.seekBarWeight.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_3, null)}
                            else ->{}
                        }
                    }
                }
            }
            1 -> {
                progress_max = binding.seekBarEquipment.max
                progress_min = binding.seekBarEquipment.min
                progress     = binding.seekBarEquipment.progress
                when (progress) {
                    progress_min -> {
                        binding.seekBarEquipment.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_equipment_alt2, null)
                    }
                    progress_max -> {
                        binding.seekBarEquipment.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_equipment_3, null)
                    }
                    else -> {
                        when(getPercent(progress - progress_min,progress_max - progress_min)) {
                            0->{binding.seekBarEquipment.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_equipment_0, null)}
                            1->{binding.seekBarEquipment.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_equipment_1, null)}
                            2->{binding.seekBarEquipment.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_equipment_2, null)}
                            3->{binding.seekBarEquipment.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_equipment_3, null)}
                            else ->{}
                        }
                    }
                }
            }
            2 -> {
                progress_max = binding.seekBarCanopy.max
                progress_min = binding.seekBarCanopy.min
                progress     = binding.seekBarCanopy.progress
                when (progress) {
                    progress_min -> {
                        binding.seekBarCanopy.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_canopee_alt, null)
                    }
                    progress_max -> {
                        binding.seekBarCanopy.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_canopee_3, null)
                    }
                    else -> {
                        when(getPercent(progress - progress_min,progress_max - progress_min)) {
                            0->{binding.seekBarCanopy.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_canopee_0, null)}
                            1->{binding.seekBarCanopy.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_canopee_1, null)}
                            2->{binding.seekBarCanopy.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_canopee_2, null)}
                            3->{binding.seekBarCanopy.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_canopee_3, null)}
                            else ->{}
                        }
                    }
                }
            }
            4 -> {
                progress_max = binding.seekBarWeightTandem.max
                progress_min = binding.seekBarWeightTandem.min
                progress     = binding.seekBarWeightTandem.progress
                when (progress) {
                    progress_min -> {
                        binding.seekBarWeightTandem.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_0, null)
                    }
                    progress_max -> {
                        binding.seekBarWeightTandem.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_3, null)
                    }
                    else -> {
                        when(getPercent(progress - progress_min,progress_max - progress_min)) {
                            0->{binding.seekBarWeightTandem.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_0, null)}
                            1->{binding.seekBarWeightTandem.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_1, null)}
                            2->{binding.seekBarWeightTandem.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_2, null)}
                            3->{binding.seekBarWeightTandem.thumb = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_seek_bar_thumb_weight_3, null)}
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
        val result : Int
        if(binding.checkBoxTandem.isChecked)
        {
            result = View.VISIBLE
        }
        else
        {
            result = View.GONE
        }
        binding.WeightTandemConstraintLayout.visibility = result
    }

//*Animation for tandem weigh appearance





    //
}
