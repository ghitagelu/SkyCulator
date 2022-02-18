package com.example.SkyDiver


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class Add_IncomeOrExpense : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__income_or_expense)

        val editTextTitle: EditText = findViewById(R.id.editText_title)
        val editTextAmount: EditText = findViewById(R.id.editText_amount)
        val editTextDate: EditText = findViewById(R.id.editText_date)
        //test this shit Oliver
        val editTextWeight: EditText= findViewById(R.id.editTextWeight)
        val editTextEquipment: EditText= findViewById(R.id.editTextEquipment)
        val editTextCanopySize: EditText= findViewById(R.id.editTextCanopySize)

        //Toolbar
//        val tabIncome : TabItem = findViewById(R.id.TabItem_Income)
//        val tabExpense : TabItem = findViewById(R.id.TabItem_Expense)

//        tabLayout.findViewById<TabItem>(R.id.TabItem_Expense).isSelected

        val buttonSave: Button = findViewById(R.id.button_save)

        buttonSave.setOnClickListener( View.OnClickListener {

            // Code here executes on main thread after user presses button



            //Send Data with intent==============================================
            val returnIntent:Intent= Intent()
            val tempAmount = editTextAmount.text.toString()
            val tempTitle =editTextTitle.text.toString()
            val tempDate =  editTextDate.text.toString()
            val weight= editTextWeight.text.toString()
            val equipment = editTextEquipment.text.toString()
            val canopy = editTextCanopySize.text.toString()
            returnIntent
                .putExtra("amount", tempAmount)
                .putExtra("title",tempTitle)
                .putExtra("date",tempDate)
                .putExtra("sometingExtra", 55)
                .putExtra("weight", weight)
                .putExtra("equipment", equipment)
                .putExtra("canopy",canopy)


//
//            if (tabLayout.findViewById<TabItem>(R.id.TabItem_Expense).isSelected)
//            {
//                returnIntent.putExtra("Type", "income")
//            }else if(tabLayout.findViewById<TabItem>(R.id.TabItem_Expense).isSelected)
//            {
//                returnIntent.putExtra("Type", "expense")
//            }


            setResult(Activity.RESULT_OK, returnIntent)
            finish();
        })


        val buttonCancel: Button = findViewById(R.id.button_cancel)

        buttonCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish();
        }
//        buttonSave.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                val amount = editTextAmmount.text.toString().toInt()
//                val action = Add_IncomeOrExpenseDirections.confirmationAction(amount)
//                v.findNavController().navigate(action)
//
//            }
//        })



    }















}
